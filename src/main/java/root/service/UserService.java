package root.service;

import root.dto.PageDTO;
import root.dto.SearchDTO;
import root.dto.UserDTO;
import root.entity.User;
import root.enums.RoleEnum;
import root.enums.SortOrderEnum;
import root.exception.DuplicateResourceException;
import root.exception.ResourceNotFoundException;
import root.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    /*private final StudentRepo studentRepo;*/

    @Transactional
    @CacheEvict(cacheNames = "user-search", allEntries = true)
    public void create(UserDTO userDTO) {

        User user = new ModelMapper().map(userDTO, User.class);

        // Encoder password
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        if(userRepo.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateResourceException("email [" + userDTO.getEmail() + "] already taken");
        }
        userRepo.save(user);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = "score-search", allEntries = true), //
        @CacheEvict(cacheNames = "score", allEntries = true), //

        @CacheEvict(cacheNames = "student-search", allEntries = true), //
        @CacheEvict(cacheNames = "student", allEntries = true), //

        @CacheEvict(cacheNames = "user-search", allEntries = true),
        @CacheEvict(cacheNames = "user", key = "#userDTO.id")
    })
    public void update(UserDTO userDTO) {

        User currentUser = userRepo.findById(userDTO.getId()).orElseThrow(
            () -> new ResourceNotFoundException("user with id [" + userDTO.getId() + "] not found")
        );

        currentUser.setName(userDTO.getName());
        currentUser.setAge(userDTO.getAge());
        currentUser.setUsername(userDTO.getUsername());
        currentUser.setHomeAddress(userDTO.getHomeAddress());
        currentUser.setBirthdate(userDTO.getBirthdate());
        currentUser.setAvatarUrl(userDTO.getAvatarUrl());
        currentUser.setRoles(null);

        String email = userDTO.getEmail();
        if (email != null && !email.equals(currentUser.getEmail())) {
            if(userRepo.existsByEmail(userDTO.getEmail())) {
                throw new DuplicateResourceException("email [" + userDTO.getEmail() + "] already taken");
            }
            currentUser.setEmail(userDTO.getEmail());
        }

        userRepo.save(currentUser);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = "score-search", allEntries = true), //
        @CacheEvict(cacheNames = "score", allEntries = true), //

        @CacheEvict(cacheNames = "student-search", allEntries = true), //
        @CacheEvict(cacheNames = "student", allEntries = true), //

        @CacheEvict(cacheNames = "user-search", allEntries = true),
        @CacheEvict(cacheNames = "user", key = "#userDTO.id")
    })
    public void updatePassword(UserDTO userDTO) {
        User currentUser = userRepo.findById(userDTO.getId()).orElseThrow(
            () -> new ResourceNotFoundException("user with id [" + userDTO.getId() + "] not found")
        );

        String passwordEncoder = new BCryptPasswordEncoder().encode(userDTO.getPassword());
        if (userDTO.getPassword() != null && !passwordEncoder.equals(currentUser.getPassword())) {
            currentUser.setPassword(passwordEncoder);
        }

        userRepo.save(currentUser);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = "user", key = "#id"),
        @CacheEvict(cacheNames = "user-search", allEntries = true)
    })
    public void delete(int id) {
        if (!userRepo.existsById(id)) {
            throw new ResourceNotFoundException("user with id [" + id + "] not found");
        }
        // Cannot delete user.id == student.user.id :: constrain error
        /*if (studentRepo.existsById(id)) {
            throw new DuplicateResourceException("Cannot delete user with id [" + id + "]");
        }*/
        userRepo.deleteById(id);
    }

    @Cacheable(cacheNames = "user", key = "#id", unless = "#result == null")
    public UserDTO getById(int id) {
        return userRepo.findById(id).map(this::convert).orElseThrow(
            () -> new ResourceNotFoundException("user with id [" + id + "] not found")
        );
    }

    @Cacheable(cacheNames = "user-search")
    public PageDTO<List<UserDTO>> searchByName(SearchDTO searchDTO) {

        PageRequest pageRequest = PageRequest.of(
            searchDTO.getCurrentPage(),
            searchDTO.getSize(),
            searchDTO.getSortOrder() == SortOrderEnum.ASC
                ? Sort.by(searchDTO.getSortBy()).ascending()
                : Sort.by(searchDTO.getSortBy()).descending()
        );

        Page<User> page = userRepo.searchByName("%" + searchDTO.getKeyword() + "%", pageRequest);

        return PageDTO.<List<UserDTO>>builder()
            .totalPage(page.getTotalPages())
            .totalElements(page.getTotalElements())
            .data(page.get().map(this::convert).toList())
            .build();
    }

    private UserDTO convert(User user) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(user, UserDTO.class);
    }

    // Security
    /*@Transactional*/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userEntity = userRepo.findByUsername(username);
        if (userEntity.isEmpty()) {
            throw new UsernameNotFoundException("Not Found Username");
        }
        // convert User -> UserDetails
        // chuyển role về quyền(authority) trong security
        List<SimpleGrantedAuthority> authorities = userEntity.get().getRoles()
            .stream()
            .map((RoleEnum role) -> new SimpleGrantedAuthority(role.name()))
            .toList();

        // User này là con của UserDetails
        return new org.springframework.security.core.userdetails.User(
            username,
            userEntity.get().getPassword(),
            authorities
        );
    }

}
