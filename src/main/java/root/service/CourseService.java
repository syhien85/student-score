package root.service;

import root.dto.CourseDTO;
import root.dto.PageDTO;
import root.dto.SearchDTO;
import root.entity.Course;
import root.enums.SortOrderEnum;
import root.exception.DuplicateResourceException;
import root.exception.RequestValidationException;
import root.exception.ResourceNotFoundException;
import root.repository.CourseRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    void create(CourseDTO courseDTO);

    void update(CourseDTO courseDTO);

    void delete(int id);

    CourseDTO getById(int id);

    PageDTO<List<CourseDTO>> searchName(SearchDTO searchDTO);
}

@RequiredArgsConstructor
@Service
class CourseServiceImpl implements CourseService {

    private final CourseRepo courseRepo;

    @Transactional
    @CacheEvict(cacheNames = "course-search", allEntries = true)
    public void create(CourseDTO courseDTO) {

        if (courseRepo.existsByName(courseDTO.getName())) {
            throw new DuplicateResourceException(
                "course name [%s] already taken".formatted(courseDTO.getName())
            );
        }

        Course course = new ModelMapper().map(courseDTO, Course.class);
        courseRepo.save(course);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = "score-search", allEntries = true), //
        @CacheEvict(cacheNames = "score-avg-by-course", allEntries = true), //
        @CacheEvict(cacheNames = "score", allEntries = true), //

        @CacheEvict(cacheNames = "course-search", allEntries = true),
        @CacheEvict(cacheNames = "course", key = "#courseDTO.id")
    })
    public void update(CourseDTO courseDTO) {

        Course currentCourse = findById(courseDTO.getId());

        boolean changes = false;

        if (
            courseDTO.getName() != null && !courseDTO.getName().equals(currentCourse.getName())
        ) {
            if (courseRepo.existsByName(courseDTO.getName())) {
                throw new DuplicateResourceException(
                    "course name [%s] already taken".formatted(courseDTO.getName())
                );
            }

            currentCourse.setName(courseDTO.getName());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes found");
        }

        courseRepo.save(currentCourse);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = "course", key = "#id"),
        @CacheEvict(cacheNames = "course-search", allEntries = true)
    })
    public void delete(int id) {
        if (!courseRepo.existsById(id)) {
            throw new ResourceNotFoundException("course with id [" + id + "] not found");
        }
        courseRepo.deleteById(id);
    }

    @Cacheable(cacheNames = "course", key = "#id", unless = "#result == null")
    public CourseDTO getById(int id) {
        return Optional.of(findById(id)).map(this::convert).get();
    }

    @Cacheable(cacheNames = "course-search")
    public PageDTO<List<CourseDTO>> searchName(SearchDTO searchDTO) {

        PageRequest pageRequest = PageRequest.of(
            searchDTO.getCurrentPage(),
            searchDTO.getSize(),
            searchDTO.getSortOrder() == SortOrderEnum.ASC
                ? Sort.by(searchDTO.getSortBy()).ascending()
                : Sort.by(searchDTO.getSortBy()).descending()
        );

        Page<Course> page = courseRepo.search(
            "%" + searchDTO.getKeyword() + "%",
            pageRequest
        );

        return PageDTO.<List<CourseDTO>>builder()
            .totalPage(page.getTotalPages())
            .totalElements(page.getTotalElements())
            .data(page.get().map(this::convert).toList())
            .build();
    }

    private Course findById(Integer id) {
        return courseRepo.findById(id).orElseThrow(
            () -> new ResourceNotFoundException(
                "course with id [" + id + "] not found"
            )
        );
    }

    private CourseDTO convert(Course course) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(course, CourseDTO.class);
    }
}
