package by.bsuir.housing.service;

import by.bsuir.housing.dto.DealDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface DealService {

    Page<DealDto> listPaged(Pageable pageable);

    Double calculatePrice(Byte peopleCount,
                          Byte days,
                          Integer estateId,
                          LocalDate arriving
    );

    DealDto addDeal(DealDto dto);

    DealDto editDeal(DealDto dto);

    void deleteDeal(Integer id);
}
