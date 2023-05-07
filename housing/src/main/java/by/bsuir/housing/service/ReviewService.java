package by.bsuir.housing.service;

import by.bsuir.housing.dto.ReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    Page<ReviewDto> listPagedForOneEstate(Pageable pageable, Integer id);

    Double averageGradeForEstate(Integer id);

    ReviewDto addReview(ReviewDto reviewDto);

    ReviewDto editReview(ReviewDto reviewDto);

    void deleteReview(Integer id);
}
