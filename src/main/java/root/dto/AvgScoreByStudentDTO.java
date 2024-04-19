package root.dto;

/*@NoArgsConstructor
@AllArgsConstructor
@Data
public class AvgScoreByStudentDTO {
    private Integer userId;
    private String studentCode;
    private double avg;
}*/
/*public record AvgScoreByStudentDTO(
    Integer userId,
    String studentCode,
    double avg
) {}*/

// if use nativeQuery
public interface AvgScoreByStudentDTO {
    Integer getUserId();
    String getUserName();
    double getAvgScore();
}
