package root.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseDTO extends TimeAuditableDTO {
    private Integer id;

    @NotBlank
    private String name;
}
