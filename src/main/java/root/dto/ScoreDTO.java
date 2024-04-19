package root.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ScoreDTO extends TimeAuditableDTO {
    private Integer id;

    @Min(0)
    @Max(10)
    private Double score;

    private CourseDTO course;

    private StudentDTO student;
}
