package by.bsuir.housing.dto;

import by.bsuir.housing.entity.Estate;
import by.bsuir.housing.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DealDto {
    private Integer id;
    private Integer estateId;
    private Estate estate;
    private Integer userId;
    private UserDto user;
    private Integer price;
    private Integer days;
    private Integer peopleCount;
    private LocalDate arriving;
}
