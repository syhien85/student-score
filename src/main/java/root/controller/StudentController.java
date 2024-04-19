package root.controller;

import root.dto.PageDTO;
import root.dto.ResponseDTO;
import root.dto.SearchDTO;
import root.dto.StudentDTO;
import root.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/")
    public ResponseDTO<Void> create(@RequestBody @Valid StudentDTO studentDTO) {
        studentService.create(studentDTO);
        return ResponseDTO.<Void>builder().status(200).msg("OK").build();
    }

    @PutMapping("/")
    public ResponseDTO<Void> update(@RequestBody StudentDTO studentDTO) {
        studentService.update(studentDTO);
        return ResponseDTO.<Void>builder().status(200).msg("OK").build();
    }

    @PutMapping("/update-password")
    public ResponseDTO<Void> updatePassword(@RequestBody StudentDTO studentDTO) {
        studentService.updatePassword(studentDTO);
        return ResponseDTO.<Void>builder().status(200).msg("OK").build();
    }

    @DeleteMapping("/")
    public ResponseDTO<Void> delete(@RequestParam("id") int id) {
        studentService.delete(id);
        return ResponseDTO.<Void>builder().status(200).msg("OK").build();
    }

    @GetMapping("/")
    public ResponseDTO<StudentDTO> getById(@RequestParam("id") int id) {
        return ResponseDTO.<StudentDTO>builder().status(200).msg("OK")
            .data(studentService.getById(id))
            .build();
    }

    @PostMapping("/search")
    public ResponseDTO<PageDTO<List<StudentDTO>>> search(@RequestBody @Valid SearchDTO searchDTO) {
        return ResponseDTO.<PageDTO<List<StudentDTO>>>builder().status(200).msg("OK")
            .data(studentService.searchName(searchDTO))
            .build();
    }
}
