package root.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Student extends TimeAuditable {
    @Id
    private Integer userId; // user_id

    /**
     * @ PrimaryKeyJoinColumn : dung cot khoa chinh dung lam cot join (user_id)
     * cot id (PK) cua bang user chinh la cot user_id (FK) cua bang student
     * OneToOne: cot khoa chinh va khoa phu la giong nhau
     * CascadeType.ALL: neu Student thay doi, User tuong ung se bi thay doi, co the update user qua student
     * Neu ko dung CascadeType.ALL, phai save User truoc
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    @MapsId
    private User user;

    private String studentCode;

    @OneToMany(mappedBy = "student")
    private List<Score> scores;

    /**
     * VD quan he ManyToMany:
     * gia su neu bang score ko co cot score,
     * co the dung manyToMany tu Student voi Course nhu sau:
     * join bang student voi score
     * cot join: student_id = course_id
     * => ko can tao entity score (bo)
     */
    /*@ManyToMany
    @JoinTable(
        name = "score",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;*/
}
