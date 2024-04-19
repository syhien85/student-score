package root.repository;

import root.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepo extends JpaRepository<Course, Integer> {

    @Query("SELECT d FROM Course d WHERE d.name LIKE :s")
    Page<Course> search(@Param("s") String name, Pageable pageable);

    boolean existsByName(String name);
}
