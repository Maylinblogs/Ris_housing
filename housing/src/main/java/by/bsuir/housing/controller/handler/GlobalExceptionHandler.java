package by.bsuir.housing.controller.handler;

import by.bsuir.housing.exception.NoSuchRecordException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final BindingResultParser bindingResultParser;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        String message = "Fields of request dto has errors: " + bindingResultParser.getFieldErrMismatches(bindingResult);
        ExceptionInfo info = ExceptionInfo.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .errorCode(40001)
                .errorMessage(message)
                .build();
        return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchRecordException.class)
    public ResponseEntity<ExceptionInfo> handleNoSuchElementException(NoSuchRecordException ex) {
        ExceptionInfo info = new ExceptionInfo(HttpStatus.NOT_FOUND, 40401, ex.getMessage());
        return new ResponseEntity<>(info, HttpStatus.NOT_FOUND);
    }
}