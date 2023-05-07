package by.bsuir.housing.controller;

import lombok.Data;

import java.time.LocalDate;

@Data
public
class CalculationRequest {
    public Byte peopleCount;
    public Byte days;
    public Integer estateId;
    public LocalDate arriving;
}
