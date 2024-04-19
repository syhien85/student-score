package root.entity;

import jakarta.persistence.*;
import lombok.*;
import root.enums.RoleEnum;

import java.util.Date;
import java.util.List;

@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class User extends TimeAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private int age;
    private String username;
    private String password;
    private String homeAddress;
    @Temporal(TemporalType.DATE)
    private Date birthdate;
    private String avatarUrl;
    @Column(unique = true)
    private String email;


    /*@OneToMany(mappedBy = "user")
    private List<UserRole> roles;*/

    /**
     * cach 2: (xoa entity UserRole)
     * map voi bang trung gian o database, ko phai tao entity
     * chi ap dung voi bang co 2 cot, 3 cot tro len phai tao bang
     * joinColumns :: chinh la cot user_id trong bang user_role (foreign key)
     *
     * @ Column :: cot role trong bang user_role
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    @Enumerated(EnumType.ORDINAL)
    private List<RoleEnum> roles;
}
