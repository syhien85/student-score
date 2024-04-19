package root.dto;

/*@NoArgsConstructor
@AllArgsConstructor
@Data
public class AvgScoreByCourseDTO {
    private Integer courseId;
    private String courseName;
    private double avg;
}*/
public record AvgScoreByCourseDTO(
    Integer courseId,
    String courseName,
    double avgScore
) {}