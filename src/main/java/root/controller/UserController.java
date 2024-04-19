package root.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import root.dto.PageDTO;
import root.dto.ResponseDTO;
import root.dto.SearchDTO;
import root.dto.UserDTO;
import root.service.EmailService;
import root.service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    @Value("${upload.folder}")
    private String UPLOAD_FOLDER;

    @PostMapping("/")
    public ResponseDTO<Void> create(@ModelAttribute("user") @Valid UserDTO userDTO) {

        String uploadFilePath = uploadFilePath(userDTO.getFile());
        userDTO.setAvatarUrl(uploadFilePath);

        userService.create(userDTO);

        // Send email after create user
        new Thread(() -> {
            emailService.sendRegisterEmail(userDTO.getEmail(), userDTO.getName());
        }).start();

        return ResponseDTO.<Void>builder().status(200).msg("OK").build();
    }

    @PutMapping("/")
    public ResponseDTO<Void> update(@ModelAttribute("user") UserDTO userDTO) {

        String uploadFilePath = uploadFilePath(userDTO.getFile());
        userDTO.setAvatarUrl(uploadFilePath);

        userService.update(userDTO);

        return ResponseDTO.<Void>builder().status(200).msg("OK").build();
    }

    @PutMapping("/update-password")
    public ResponseDTO<Void> updatePassword(@RequestBody UserDTO userDTO) {

        userService.updatePassword(userDTO);

        return ResponseDTO.<Void>builder().status(200).msg("OK").build();
    }

    @DeleteMapping("/")
    public ResponseDTO<Void> delete(@RequestParam("id") int id) {
        userService.delete(id);
        return ResponseDTO.<Void>builder().status(200).msg("OK").build();
    }

    // chua xy ly phan nay
    @GetMapping("/download")
    public void download(@RequestParam("filename") String filename, HttpServletResponse resp)
        throws IOException {
        File file = new File(UPLOAD_FOLDER + filename);
        Files.copy(file.toPath(), resp.getOutputStream());
    }

    @GetMapping("/")
    public ResponseDTO<UserDTO> getById(int id) {
        return ResponseDTO.<UserDTO>builder()
            .status(200).msg("OK")
            .data(userService.getById(id))
            .build();
    }

    @PostMapping("/search")
    public ResponseDTO<PageDTO<List<UserDTO>>> search(@RequestBody @Valid SearchDTO searchDTO) {
        return ResponseDTO.<PageDTO<List<UserDTO>>>builder()
            .status(200).msg("OK")
            .data(userService.searchByName(searchDTO))
            .build();
    }

    private String uploadFilePath(MultipartFile fileUpload) {
        String newFilename = null;

        if (fileUpload != null && !fileUpload.isEmpty()) {
            if (new File(UPLOAD_FOLDER).exists()) {
                new File(UPLOAD_FOLDER).mkdir();
            }

            String filename = fileUpload.getOriginalFilename();

            if (filename != null) {
                String extension = filename.substring((filename.lastIndexOf(".")));
                newFilename = UUID.randomUUID() + extension;
                File newFile = new File(UPLOAD_FOLDER + newFilename);
                try {
                    fileUpload.transferTo(newFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return newFilename;
    }
}
