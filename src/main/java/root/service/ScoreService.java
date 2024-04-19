package root.service;

import root.dto.*;
import root.entity.Score;
import root.enums.SortOrderEnum;
import root.exception.DuplicateResourceException;
import root.exception.RequestValidationException;
import root.exception.ResourceNotFoundException;
import root.repository.CourseRepo;
import root.repository.ScoreRepo;
import root.repository.StudentRepo;
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

@RequiredArgsConstructor
@Service
public class ScoreService {

    private final ScoreRepo scoreRepo;
    private final StudentRepo studentRepo;
    private final CourseRepo courseRepo;

    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = "score-search", allEntries = true),
        @CacheEvict(cacheNames = "score-avg-by-course", allEntries = true),
        @CacheEvict(cacheNames = "score-avg-by-student", allEntries = true)
    })
    public void create(ScoreDTO scoreDTO) {

        if (scoreDTO.getScore() == null) {
            throw new RequestValidationException("score with score must not be blank");
        }

        if (!courseRepo.existsById(scoreDTO.getCourse().getId())) {
            throw new ResourceNotFoundException(
                "course with id [" + scoreDTO.getCourse().getId() + "] not found"
            );
        }

        if (!studentRepo.existsById(scoreDTO.getStudent().getUser().getId())) {
            throw new ResourceNotFoundException(
                "student with id [" + scoreDTO.getStudent().getUser().getId() + "] not found"
            );
        }

        if (
            scoreRepo.existsByCourseIdAndStudentUserId(
                scoreDTO.getCourse().getId(),
                scoreDTO.getStudent().getUser().getId()
            )
        ) {
            throw new DuplicateResourceException(
                "score of course with id [%s] and student with id [%s] already taken"
                    .formatted(
                        scoreDTO.getCourse().getId(),
                        scoreDTO.getStudent().getUser().getId()
                    )
            );
        }

        Score score = new ModelMapper().map(scoreDTO, Score.class);
        scoreRepo.save(score);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = "score-search", allEntries = true),
        @CacheEvict(cacheNames = "score-avg-by-course", allEntries = true),
        @CacheEvict(cacheNames = "score-avg-by-student", allEntries = true),
        @CacheEvict(cacheNames = "score", key = "#scoreDTO.id")
    })
    public void update(ScoreDTO scoreDTO) {
        Score currentScore = scoreRepo.findById(scoreDTO.getId()).orElseThrow(
            () -> new ResourceNotFoundException(
                "score with id [" + scoreDTO.getId() + "] not found"
            )
        );
        currentScore.setScore(scoreDTO.getScore());
        scoreRepo.save(currentScore);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = "score", key = "#id"),
        @CacheEvict(cacheNames = "score-search", allEntries = true),
    })
    public void delete(int id) {
        if (!scoreRepo.existsById(id)) {
            throw new ResourceNotFoundException("score with id [" + id + "] not found");
        }
        scoreRepo.deleteById(id);
    }

    @Cacheable(cacheNames = "score", key = "#id", unless = "#result == null")
    public ScoreDTO getById(int id) {
        return scoreRepo.findById(id).map(this::convert).orElseThrow(
            () -> new ResourceNotFoundException("score with id [" + id + "] not found")
        );
    }

    @Cacheable(cacheNames = "score-search")
    public PageDTO<List<ScoreDTO>> searchService(SearchScoreDTO searchDTO) {

        PageRequest pageRequest = PageRequest.of(
            searchDTO.getCurrentPage(),
            searchDTO.getSize(),
            searchDTO.getSortOrder() == SortOrderEnum.ASC
                ? Sort.by(searchDTO.getSortBy()).ascending()
                : Sort.by(searchDTO.getSortBy()).descending()
        );

        String key = "%" + searchDTO.getKeyword() + "%";
        Integer c1 = searchDTO.getStudentId();
        Integer c2 = searchDTO.getCourseId();

        Page<Score> page = null;

        if (c1 == null && c2 == null) {
            page = scoreRepo.searchByDefault(key, pageRequest);
        }
        if (c1 != null && c2 == null) {
            page = scoreRepo.searchByStudentId(key, c1, pageRequest);
        }
        if (c1 == null && c2 != null) {
            page = scoreRepo.searchByCourseId(key, c2, pageRequest);
        }
        if (c1 != null && c2 != null) {
            page = scoreRepo.searchByStudentIdAndCourseId(key, c1, c2, pageRequest);
        }

        return PageDTO.<List<ScoreDTO>>builder()
            .totalPage(page.getTotalPages())
            .totalElements(page.getTotalElements())
            .data(page.get().map(this::convert).toList())
            .build();
    }

    @Cacheable(cacheNames = "score-avg-by-course")
    public PageDTO<List<AvgScoreByCourseDTO>> avgScoreByCourseService() {

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("c.id").ascending());
        Page<AvgScoreByCourseDTO> page = scoreRepo.avgScoreByCourse(pageRequest);

        return PageDTO.<List<AvgScoreByCourseDTO>>builder()
            .totalPage(page.getTotalPages())
            .totalElements(page.getTotalElements())
            .data(page.getContent())
            .build();
    }

    @Cacheable(cacheNames = "score-avg-by-student")
    public PageDTO<List<AvgScoreByStudentDTO>> avgScoreByStudentService() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("avgScore").descending());
        /*Page<AvgScoreByStudentDTO> page = scoreRepo.avgScoreByStudent(pageRequest);*/
        Page<AvgScoreByStudentDTO> page = scoreRepo.avgScoreByStudentNativeQuery(pageRequest);

        return PageDTO.<List<AvgScoreByStudentDTO>>builder()
            .totalPage(page.getTotalPages())
            .totalElements(page.getTotalElements())
            .data(page.getContent())
            .build();
    }

    private ScoreDTO convert(Score score) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(score, ScoreDTO.class);
    }
}
