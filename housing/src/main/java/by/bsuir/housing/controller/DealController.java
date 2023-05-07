package by.bsuir.housing.controller;

import by.bsuir.housing.dto.DealDto;
import by.bsuir.housing.service.DealService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/deal")
public class DealController {

    private final DealService dealService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DealDto> listPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        System.out.println("page = " + page + ", size = " + size);
        return dealService.listPaged(PageRequest.of(page, size)).toList();
    }

    @PostMapping("/calculate-price")
    @ResponseStatus(HttpStatus.OK)
    public Double calculatePrice(@RequestBody CalculationRequest calculationRequest) {
        return dealService.calculatePrice(calculationRequest.peopleCount,
                calculationRequest.days,
                calculationRequest.estateId,
                calculationRequest.arriving);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DealDto addDeal(@RequestBody DealDto deal) {
        return dealService.addDeal(deal);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public DealDto editDeal(@RequestBody DealDto deal) {
        return dealService.editDeal(deal);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeal(@PathVariable Integer id) {
        dealService.deleteDeal(id);
    }
}

