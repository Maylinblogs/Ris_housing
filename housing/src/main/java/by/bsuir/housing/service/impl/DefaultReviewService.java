package by.bsuir.housing.service.impl;

import by.bsuir.housing.dto.ReviewDto;
import by.bsuir.housing.exception.NoSuchRecordException;
import by.bsuir.housing.mapper.ReviewMapper;
import by.bsuir.housing.repository.EstateRepository;
import by.bsuir.housing.repository.ReviewRepository;
import by.bsuir.housing.repository.UserRepository;
import by.bsuir.housing.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultReviewService implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final EstateRepository estateRepository;

    @Override
    public Page<ReviewDto> listPagedForOneEstate(Pageable pageable, Integer id) {
        return reviewRepository.findAllByEstateId(id, pageable).map(ReviewMapper.INSTANCE:: toDto);
    }

    @Override
    public Double averageGradeForEstate(Integer id) {
        return reviewRepository.findAvgGradeWithEstateId(id);
    }

    @Override
    public ReviewDto addReview(ReviewDto reviewDto) {
        final var review = ReviewMapper.INSTANCE.fromDto(reviewDto);
        review.setAuthor(userRepository.findById(reviewDto.getAuthorId())
                .orElseThrow(() -> new NoSuchRecordException(String.format("No user for id %d", reviewDto.getAuthorId()))));
        review.setEstate(estateRepository.findById(reviewDto.getEstateId())
                .orElseThrow(() -> new NoSuchRecordException(String.format("No estate for id %d", reviewDto.getEstateId()))));
        review.setId(null);
        reviewRepository.save(review);
        return ReviewMapper.INSTANCE.toDto(review);
    }

    @Override
    public ReviewDto editReview(ReviewDto reviewDto) {
        if (reviewRepository.existsById(reviewDto.getId())) {
            final var review = ReviewMapper.INSTANCE.fromDto(reviewDto);
            review.setAuthor(userRepository.findById(reviewDto.getAuthorId())
                    .orElseThrow(() -> new NoSuchRecordException(String.format("No user for id %d", reviewDto.getAuthorId()))));
            review.setEstate(estateRepository.findById(reviewDto.getEstateId())
                    .orElseThrow(() -> new NoSuchRecordException(String.format("No estate for id %d", reviewDto.getEstateId()))));
            reviewRepository.save(review);
            return ReviewMapper.INSTANCE.toDto(review);
        } else {
            throw new NoSuchRecordException(String.format("No review for id %d", reviewDto.getId()));
        }
    }

    @Override
    public void deleteReview(Integer id) {
        reviewRepository.deleteById(id);
    }
}
