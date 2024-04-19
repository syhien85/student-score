package root.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UniqueCourseAndStudent",
            columnNames = {"course_id", "student_user_id"}
        )
    }
) // mỗi student chỉ có 1 score cho 1 course
public class Score extends TimeAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double score;

    /**
     * bang score la bang trung gian join voi 2 bang course va student
     * course.id = score.course.id || student.id = score.student.id
     */
    @ManyToOne
    private Course course;

    @ManyToOne
    private Student student;
}
