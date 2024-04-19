package root.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchScoreDTO extends SearchDTO {
//    @Min(1)
    private Integer studentId;
//    @Min(1)
    private Integer courseId;
}
