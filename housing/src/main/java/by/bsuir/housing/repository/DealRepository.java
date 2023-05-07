package by.bsuir.housing.repository;

import by.bsuir.housing.entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DealRepository extends JpaRepository<Deal, Integer> {
    List<Deal> findByUser_Id(Integer id);
    List<Deal> findByEstate_Id(Integer id);
}
