package by.bsuir.housing.controller;

import by.bsuir.housing.entity.Estate;
import by.bsuir.housing.service.EstateService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/estate")
public class EstateController {

    private final EstateService estateService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Estate findOneById(@PathVariable Integer id) {
        return estateService.findOneById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Estate> listPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sort_dir,
            @RequestParam(defaultValue = "price") String sort_by,
            @RequestParam(defaultValue = "") String city
    ) {
        System.out.println("page = " + page + ", size = " + size);
        return estateService.listPaged(PageRequest.of(page, size)
                .withSort(Sort.Direction.fromString(sort_dir), sort_by),
                city
        ).toList();
    }

    @GetMapping(value = "/{id}/report")
    public ResponseEntity<InputStreamResource> generateReport(@PathVariable Integer id) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + "Report-" + System.currentTimeMillis() + ".xlsx");
        ByteArrayInputStream in = new ByteArrayInputStream(estateService.generateReport(id).toByteArray());
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Estate addEstate(@RequestBody Estate estate) {
        return estateService.addEstate(estate);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Estate editEstate(@RequestBody Estate estate) {
        return estateService.editEstate(estate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEstate(@PathVariable Integer id) {
        estateService.deleteEstate(id);
    }
}
