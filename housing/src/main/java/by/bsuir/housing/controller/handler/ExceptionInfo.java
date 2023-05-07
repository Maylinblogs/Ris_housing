package by.bsuir.housing.controller.handler;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ExceptionInfo {
    HttpStatus httpStatus;
    int errorCode;
    String errorMessage;
}