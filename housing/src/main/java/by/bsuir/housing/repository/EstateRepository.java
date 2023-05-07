package by.bsuir.housing.repository;

import by.bsuir.housing.entity.Estate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EstateRepository extends JpaRepository<Estate, Integer> {
    Page<Estate> findAllByCityContaining(String city, Pageable pageable);

    Page<Estate> findByFavouritesId(Integer id, Pageable pageable);
}
