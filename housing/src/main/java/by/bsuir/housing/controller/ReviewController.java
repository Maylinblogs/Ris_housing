package by.bsuir.housing.controller;

import by.bsuir.housing.dto.ReviewDto;
import by.bsuir.housing.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/estate/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewDto> listPagedForOneEstate(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Integer id
    ) {
        return reviewService.listPagedForOneEstate(PageRequest.of(page, size), id).toList();
    }

    @GetMapping("/avg/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Double averageGradeForEstate(@PathVariable Integer id) {
        return reviewService.averageGradeForEstate(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto addReview(@RequestBody ReviewDto reviewDto) {
        return reviewService.addReview(reviewDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ReviewDto editReview(@RequestBody ReviewDto reviewDto) {
        return reviewService.editReview(reviewDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable Integer id) {
        reviewService.deleteReview(id);
    }
}
