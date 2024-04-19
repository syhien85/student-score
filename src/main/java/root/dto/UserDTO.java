package root.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import root.enums.RoleEnum;

import java.util.Date;
import java.util.List;

@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO extends TimeAuditableDTO {
    private Integer id;

    @NotBlank
    private String name;
    private int age;
    private String username;
    @NotBlank
    private String password;
    private String homeAddress;
    private String avatarUrl;
    @NotBlank
    private String email;

    @JsonIgnore // bỏ qua trường này (ko trả về) trong Json response
    private MultipartFile file;

    @DateTimeFormat(pattern = "dd/MM/yyyy") // form body
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "GMT+7") // json body
    private Date birthdate;

    private List<RoleEnum> roles;
}
