package root.controller;

import root.dto.CourseDTO;
import root.dto.PageDTO;
import root.dto.ResponseDTO;
import root.dto.SearchDTO;
import root.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/")
    public ResponseDTO<Void> create(@RequestBody @Valid CourseDTO courseDTO) {
        courseService.create(courseDTO);
        return ResponseDTO.<Void>builder().status(200).msg("OK").build();
    }

    @PutMapping("/")
    public ResponseDTO<Void> update(@RequestBody @Valid CourseDTO courseDTO) {
        courseService.update(courseDTO);
        return ResponseDTO.<Void>builder().status(200).msg("OK").build();
    }

    @DeleteMapping("/")
    public ResponseDTO<Void> delete(@RequestParam("id") int id) {
        courseService.delete(id);
        return ResponseDTO.<Void>builder().status(200).msg("OK").build();
    }

    @GetMapping("/")
    public ResponseDTO<CourseDTO> getById(@RequestParam("id") int id) {
        return ResponseDTO.<CourseDTO>builder().status(200).msg("OK").data(courseService.getById(id)).build();
    }

    @PostMapping("/search")
    public ResponseDTO<PageDTO<List<CourseDTO>>> search(@RequestBody @Valid SearchDTO searchDTO) {
        return ResponseDTO.<PageDTO<List<CourseDTO>>>builder().status(200).msg("OK")
            .data(courseService.searchName(searchDTO))
            .build();
    }
}
