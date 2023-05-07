package by.bsuir.housing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    private String email;
    private String phoneNumber;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    private String passIdentificationNumber;
}
