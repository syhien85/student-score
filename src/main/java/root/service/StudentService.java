package root.service;

import root.dto.*;
import root.entity.Student;
import root.entity.User;
import root.enums.SortOrderEnum;
import root.exception.DuplicateResourceException;
import root.exception.RequestValidationException;
import root.exception.ResourceNotFoundException;
import root.repository.StudentRepo;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StudentService {

    private final StudentRepo studentRepo;
    private final UserRepo userRepo;

    @Transactional
    @CacheEvict(cacheNames = "student-search", allEntries = true)
    public void create(StudentDTO studentDTO) {

        // user.email not blank && user.email not duplicate
        String userEmail = studentDTO.getUser().getEmail();
        if (userEmail == null || userEmail.isEmpty()) {
            throw new RequestValidationException("user with email must not be blank");
        }
        if(userRepo.existsByEmail(userEmail)) {
            throw new DuplicateResourceException("email [" + userEmail + "] already taken");
        }

        // user.name not blank
        String userName = studentDTO.getUser().getName();
        if (userName == null || userName.isEmpty()) {
            throw new RequestValidationException("user with name must not be blank");
        }

        // student.studentCode not duplicate
        if(studentRepo.existsByStudentCode(studentDTO.getStudentCode())) {
            throw new DuplicateResourceException(
                "student code [" + studentDTO.getStudentCode() + "] already taken"
            );
        }

        Student student = new ModelMapper().map(studentDTO, Student.class);

        User user = student.getUser();
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        student.setUser(user);

        studentRepo.save(student);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = "score-search", allEntries = true), //
        @CacheEvict(cacheNames = "score-avg-by-student", allEntries = true), //
        @CacheEvict(cacheNames = "score", allEntries = true), //

        @CacheEvict(cacheNames = "student-search", allEntries = true),
        @CacheEvict(cacheNames = "student", key = "#studentDTO.user.id")
    })
    public void update(StudentDTO studentDTO) {

        Student currentStudent = studentRepo.findById(studentDTO.getUser().getId()).orElseThrow(
            () -> new ResourceNotFoundException(
                "student with id [" + studentDTO.getUser().getId() + "] not found"
            )
        );

        // student name not duplicate
        String studentCodeDTO = studentDTO.getStudentCode();
        if (studentCodeDTO != null && !studentCodeDTO.equals(currentStudent.getStudentCode())) {
            if(studentRepo.existsByStudentCode(studentCodeDTO)) {
                throw new DuplicateResourceException(
                    "student with studentCode [" + studentCodeDTO + "] already taken"
                );
            }
            currentStudent.setStudentCode(studentDTO.getStudentCode());
        }

        User currentUser = currentStudent.getUser();

        currentUser.setName(studentDTO.getUser().getName());
        currentUser.setAge(studentDTO.getUser().getAge());
        currentUser.setUsername(studentDTO.getUser().getUsername());
        currentUser.setHomeAddress(studentDTO.getUser().getHomeAddress());
        currentUser.setBirthdate(studentDTO.getUser().getBirthdate());
        currentUser.setRoles(studentDTO.getUser().getRoles());

        if(userRepo.existsByEmail(studentDTO.getUser().getEmail())) {
            throw new DuplicateResourceException(
                "email [" + studentDTO.getUser().getEmail() + "] already taken"
            );
        }
        currentUser.setEmail(studentDTO.getUser().getEmail());

        currentStudent.setUser(currentUser);
        studentRepo.save(currentStudent);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = "score-search", allEntries = true), //
        @CacheEvict(cacheNames = "score-avg-by-student", allEntries = true), //
        @CacheEvict(cacheNames = "score", allEntries = true), //

        @CacheEvict(cacheNames = "student-search", allEntries = true),
        @CacheEvict(cacheNames = "student", key = "#studentDTO.user.id")
    })
    public void updatePassword(StudentDTO studentDTO) {
        Student currentStudent = studentRepo.findById(studentDTO.getUser().getId()).orElseThrow(
            () -> new ResourceNotFoundException(
                "student with id [" + studentDTO.getUser().getId() + "] not found"
            )
        );

        if (studentDTO.getUser().getPassword() != null) {
            String passwordEncoder =
                new BCryptPasswordEncoder().encode(studentDTO.getUser().getPassword());
            currentStudent.getUser().setPassword(passwordEncoder);
        }

        currentStudent.setUser(currentStudent.getUser());
        studentRepo.save(currentStudent);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = "student", key = "#id"),
        @CacheEvict(cacheNames = "student-search", allEntries = true)
    })
    public void delete(int id) {
        if (!studentRepo.existsById(id)) {
            throw new ResourceNotFoundException("student with id [" + id + "] not found");
        }
        studentRepo.deleteById(id);
    }

    @Cacheable(cacheNames = "student", key = "#id", unless = "#result == null")
    public StudentDTO getById(int id) {
        return studentRepo.findById(id).map(this::convert).orElseThrow(
            () -> new ResourceNotFoundException("student with id [" + id + "] not found")
        );
    }

    @Cacheable(cacheNames = "student-search")
    public PageDTO<List<StudentDTO>> searchName(SearchDTO searchDTO) {

        PageRequest pageRequest = PageRequest.of(
            searchDTO.getCurrentPage(),
            searchDTO.getSize(),
            searchDTO.getSortOrder() == SortOrderEnum.ASC
                ? Sort.by(searchDTO.getSortBy()).ascending()
                : Sort.by(searchDTO.getSortBy()).descending()
        );

        Page<Student> page = studentRepo.searchByName(
            "%" + searchDTO.getKeyword() + "%",
            pageRequest
        );

        return PageDTO.<List<StudentDTO>>builder()
            .totalPage(page.getTotalPages())
            .totalElements(page.getTotalElements())
            .data(page.get().map(this::convert).toList())
            .build();
    }

    private StudentDTO convert(Student student) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(student, StudentDTO.class);
    }
}
