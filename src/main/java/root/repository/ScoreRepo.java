package root.repository;

import root.dto.AvgScoreByCourseDTO;
import root.dto.AvgScoreByStudentDTO;
import root.entity.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScoreRepo extends JpaRepository<Score, Integer> {
    Optional<Score> findByCourseIdAndStudentUserId(Integer courseId, Integer studentUserId);

    boolean existsByCourseIdAndStudentUserId(Integer courseId, Integer studentUserId);

    String queryKeyword = "(s.student.studentCode LIKE :s OR s.student.user.name LIKE :s OR s.course.name LIKE :s)";

    @Query("SELECT s FROM Score s " +
        "WHERE " + queryKeyword)
    Page<Score> searchByDefault(@Param("s") String s, Pageable pageable);

    @Query("SELECT s FROM Score s " +
        "WHERE s.student.user.id = :c1 AND " + queryKeyword)
    Page<Score> searchByStudentId(@Param("s") String s, @Param("c1") Integer c1, Pageable pageable);

    @Query("SELECT s FROM Score s " +
        "WHERE s.course.id = :c2 AND " + queryKeyword)
    Page<Score> searchByCourseId(@Param("s") String s, @Param("c2") Integer c2, Pageable pageable);

    @Query("SELECT s FROM Score s " +
        "WHERE s.student.user.id = :c1 AND s.course.id = :c2 AND " + queryKeyword)
    Page<Score> searchByStudentIdAndCourseId(@Param("s") String s, @Param("c1") Integer c1, @Param("c2") Integer c2, Pageable pageable);

    // ave score theo course
    // Cau lenh tuy bien JPQL
    // ORDER BY AVG(s.score) DESC // sort by avgScore DESC
    @Query(
        "SELECT new root.dto.AvgScoreByCourseDTO(c.id, c.name, AVG(s.score)) " +
            "FROM Score s " +
            "JOIN s.course c " +
            "GROUP BY c.id, c.name"
    )
     Page<AvgScoreByCourseDTO> avgScoreByCourse(Pageable pageable);

    @Query(
        nativeQuery = true,
        value =
            "SELECT u.id as userId, u.name as userName, AVG(s.score) as avgScore " +
                "FROM student st " +
                "JOIN score s " +
                    "ON st.user_id = s.student_user_id " +
                "JOIN user u " +
                    "ON st.user_id = u.id " +
                "GROUP BY u.id",
        countQuery =
            "SELECT count(1) " +
                "FROM student st " +
                "JOIN score s " +
                    "ON st.user_id = s.student_user_id " +
                "JOIN user u " +
                    "ON st.user_id = u.id " +
                "GROUP BY u.id"
    )
    Page<AvgScoreByStudentDTO> avgScoreByStudentNativeQuery(Pageable pageable);

    /*@Query(
        "SELECT new root.dto.AvgScoreByStudentDTO(st.userId, st.studentCode, AVG(s.score)) " +
            "FROM Score s " +
            "JOIN s.student st " +
            "GROUP BY st.userId, st.studentCode"
    )
    Page<AvgScoreByStudentDTO> avgScoreByStudent(Pageable pageable);*/
}
