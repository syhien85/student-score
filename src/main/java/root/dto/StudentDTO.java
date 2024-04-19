package root.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentDTO extends TimeAuditableDTO {
    @NotBlank
    private String studentCode;

    private UserDTO user;
}
