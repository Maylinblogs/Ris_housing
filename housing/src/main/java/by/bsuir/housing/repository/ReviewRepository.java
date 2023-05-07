package by.bsuir.housing.repository;

import by.bsuir.housing.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Page<Review> findAllByEstateId(Integer estateId, Pageable pageable);

    @Query("SELECT AVG(r.grade) FROM Review r WHERE r.estate.id = ?1")
    Double findAvgGradeWithEstateId(Integer estateId);
}
