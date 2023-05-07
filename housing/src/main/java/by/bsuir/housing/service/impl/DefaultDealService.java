package by.bsuir.housing.service.impl;

import by.bsuir.housing.dto.DealDto;
import by.bsuir.housing.exception.NoSuchRecordException;
import by.bsuir.housing.mapper.DealMapper;
import by.bsuir.housing.repository.DealRepository;
import by.bsuir.housing.repository.EstateRepository;
import by.bsuir.housing.repository.UserRepository;
import by.bsuir.housing.service.DealService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultDealService implements DealService {

    private final DealRepository dealRepository;
    private final UserRepository userRepository;
    private final EstateRepository estateRepository;

    private static final Double SERVICE_COMMISSION = 1.1;

    @Override
    public Page<DealDto> listPaged(Pageable pageable) {
        return dealRepository.findAll(pageable).map(DealMapper.INSTANCE::toDto);
    }

    @Override
    public Double calculatePrice(Byte peopleCount, Byte days, Integer estateId, LocalDate arriving) {
        final var estatePrice = estateRepository.findById(estateId)
                .orElseThrow(() -> new NoSuchRecordException(String.format("No estate for id %d", estateId)))
                .getPrice();
        final var leaving = arriving.plusDays(days + 1);
        final var numOfWeekends = arriving.datesUntil(leaving)
                .filter(day -> day.getDayOfWeek().equals(DayOfWeek.SATURDAY) || day.getDayOfWeek().equals(DayOfWeek.SUNDAY))
                .count();
        final var total = peopleCount * (
                (estatePrice * numOfWeekends * 1.1)
                +
                (estatePrice * (days-numOfWeekends))
        );
        return calculateWithServiceCommission(total);
    }

    private Double calculateWithServiceCommission(Double price) {
        return price * SERVICE_COMMISSION;
    }

    @Override
    public DealDto addDeal(DealDto dto) {
        final var deal = DealMapper.INSTANCE.fromDto(dto);
        deal.setUser(userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NoSuchRecordException(String.format("No user for id %d", dto.getUserId()))));
        deal.setEstate(estateRepository.findById(dto.getEstateId())
                .orElseThrow(() -> new NoSuchRecordException(String.format("No estate for id %d", dto.getEstateId()))));
        deal.setId(null);
        deal.setPrice((int)Math.round(calculatePrice(dto.getPeopleCount().byteValue(), dto.getDays().byteValue(), dto.getEstateId(), dto.getArriving())));
        dealRepository.save(deal);
        return DealMapper.INSTANCE.toDto(deal);
    }

    @Override
    public DealDto editDeal(DealDto dto) {
        final var deal = DealMapper.INSTANCE.fromDto(dto);
        if (dealRepository.existsById(deal.getId())) {
            deal.setUser(userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new NoSuchRecordException(String.format("No user for id %d", dto.getUserId()))));
            deal.setEstate(estateRepository.findById(dto.getEstateId())
                    .orElseThrow(() -> new NoSuchRecordException(String.format("No estate for id %d", dto.getEstateId()))));
            deal.setPrice((int)Math.round(calculatePrice(dto.getPeopleCount().byteValue(), dto.getDays().byteValue(), dto.getEstateId(), dto.getArriving())));
            dealRepository.save(deal);
            return DealMapper.INSTANCE.toDto(deal);
        } else {
            throw new NoSuchRecordException(String.format("No deal for id %d", deal.getId()));
        }
    }

    @Override
    public void deleteDeal(Integer id) {
        dealRepository.deleteById(id);
    }
}
