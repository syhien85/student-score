package root.repository;

import root.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {

    Page<User> findByName(String keyword, Pageable pageable);

    Optional<User> findByUsername(String username);

    @Query(
        "SELECT u " +
            "FROM User u " +
            "WHERE u.name LIKE :name"
    )
    Page<User> searchByName(@Param("name") String s, Pageable pageable);

    @Query(
        "SELECT u " +
            "FROM User u " +
            "WHERE u.name LIKE :s OR u.username LIKE :s"
    )
    List<User> searchByNameOrUsername(@Param("s") String s);

    // câu lệnh thêm sửa xoá phải có @Modifying
    @Modifying
    @Query(
        "DELETE " +
            "FROM User u " +
            "WHERE u.username = :x"
    )
    int deleteUsername(@Param("x") String s);

    // tự generate lệnh xoá
    void deleteByUsername(@Param("x") String s);

    // for: jobschedule.JobSchedule
    @Query(
        "SELECT u " +
            "FROM User u " +
            "WHERE DAY(u.birthdate) = :date AND MONTH(u.birthdate) = :month"
    )
    List<User> searchByBirthdate(@Param("date") int date, @Param("month") int month);

    boolean existsByEmail(String email);

    boolean existsByName(String admin);
}
