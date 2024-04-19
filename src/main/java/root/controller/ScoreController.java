package root.controller;

import root.dto.*;
import root.service.ScoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/score")
public class ScoreController {

    private final ScoreService scoreService;

    @PostMapping("/")
    public ResponseDTO<Void> create(@RequestBody @Valid ScoreDTO scoreDTO) {
        scoreService.create(scoreDTO);
        return ResponseDTO.<Void>builder().status(200).msg("OK").build();
    }

    @PutMapping("/")
    public ResponseDTO<Void> update(@RequestBody ScoreDTO scoreDTO) {
        scoreService.update(scoreDTO);
        return ResponseDTO.<Void>builder().status(200).msg("OK").build();
    }

    @DeleteMapping("/")
    public ResponseDTO<Void> delete(@RequestParam("id") int id) {
        scoreService.delete(id);
        return ResponseDTO.<Void>builder().status(200).msg("OK").build();
    }

    @GetMapping("/")
    public ResponseDTO<ScoreDTO> getById(@RequestParam("id") int id) {
        return ResponseDTO.<ScoreDTO>builder().status(200).msg("OK")
            .data(scoreService.getById(id))
            .build();
    }

    @PostMapping("/search")
    public ResponseDTO<PageDTO<List<ScoreDTO>>> search(@RequestBody @Valid SearchScoreDTO searchDTO) {
        return ResponseDTO.<PageDTO<List<ScoreDTO>>>builder().status(200).msg("OK")
            .data(scoreService.searchService(searchDTO))
            .build();
    }

    @PostMapping("/avg-by-course")
    public ResponseDTO<PageDTO<List<AvgScoreByCourseDTO>>> avgScoreByCourse() {
        return ResponseDTO.<PageDTO<List<AvgScoreByCourseDTO>>>builder().status(200).msg("OK")
            .data(scoreService.avgScoreByCourseService())
            .build();
    }

    @PostMapping("/avg-by-student")
    public ResponseDTO<PageDTO<List<AvgScoreByStudentDTO>>> avgScoreByStudent() {
        return ResponseDTO.<PageDTO<List<AvgScoreByStudentDTO>>>builder().status(200).msg("OK")
            .data(scoreService.avgScoreByStudentService())
            .build();
    }
}
