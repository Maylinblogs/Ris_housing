package by.bsuir.housing.dto;

import by.bsuir.housing.entity.Estate;
import by.bsuir.housing.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Integer id;
    private String text;
    private Byte grade;
    private Estate estate;
    private Integer estateId;
    private UserDto author;
    private Integer authorId;
}
