package by.bsuir.housing.service;

import by.bsuir.housing.entity.Estate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface EstateService {

    Page<Estate> listPaged(Pageable pageable, String city);

    Estate findOneById(Integer id);

    ByteArrayOutputStream generateReport(Integer id);

    Estate addEstate(Estate estate);

    void deleteEstate(Integer id);

    Estate editEstate(Estate estate);
}
