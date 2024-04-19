package root.repository;

import root.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepo extends JpaRepository<Student, Integer> {
    @Query(
        "SELECT s " +
            "FROM Student s " +
            "WHERE s.studentCode LIKE :s"
    )
    Page<Student> searchByName(@Param("s") String keyword, Pageable pageable);

    boolean existsByStudentCode(String studentCode);
}
