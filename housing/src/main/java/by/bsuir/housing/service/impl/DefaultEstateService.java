package by.bsuir.housing.service.impl;

import by.bsuir.housing.entity.Deal;
import by.bsuir.housing.entity.Estate;
import by.bsuir.housing.exception.NoSuchRecordException;
import by.bsuir.housing.repository.DealRepository;
import by.bsuir.housing.repository.EstateRepository;
import by.bsuir.housing.service.EstateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultEstateService implements EstateService {

    private final EstateRepository estateRepository;
    private final DealRepository dealRepository;

    @Override
    public Page<Estate> listPaged(Pageable pageable, String city) {
        return estateRepository.findAllByCityContaining(city, pageable);
    }

    @Override
    public Estate findOneById(Integer id) {
        return estateRepository.findById(id)
                .orElseThrow(() -> new NoSuchRecordException(String.format("No estate for id %d", id)));
    }

    @Override
    @SneakyThrows
    public ByteArrayOutputStream generateReport(Integer id) {
        // Create a workbook
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Report-" + System.currentTimeMillis());

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("№");
        headerRow.createCell(1).setCellValue("Заказчик");
        headerRow.createCell(2).setCellValue("Количество жильцов");
        headerRow.createCell(3).setCellValue("Длительность");
        headerRow.createCell(4).setCellValue("Дата заселения");
        headerRow.createCell(5).setCellValue("Цена");

        final var deals = dealRepository.findByEstate_Id(id);
        if (!estateRepository.existsById(id)) throw new NoSuchRecordException(String.format("No estate for id %d", id));
        int rowNum = 1;
        for (Deal deal : deals) {
            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(rowNum);
            row.createCell(1).setCellValue(deal.getUser().getPerson().getFirstName() + " " + deal.getUser().getPerson().getLastName() + " (" + deal.getUser().getEmail() + ")");
            row.createCell(2).setCellValue(deal.getPeopleCount());
            row.createCell(3).setCellValue(deal.getDays());
            row.createCell(4).setCellValue(deal.getArriving());
            row.createCell(5).setCellValue(deal.getPrice());
            rowNum++;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return outputStream;
    }

    @Override
    public Estate addEstate(Estate estate) {
        estate.setId(null);
        return estateRepository.save(estate);
    }

    @Override
    public void deleteEstate(Integer id) {
        estateRepository.deleteById(id);
    }

    @Override
    public Estate editEstate(Estate estate) {
        if (estateRepository.existsById(estate.getId())) {
            return estateRepository.save(estate);
        } else {
            throw new NoSuchRecordException(String.format("No estate for id %d", estate.getId()));
        }
    }
}
