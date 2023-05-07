package by.bsuir.housing.controller.handler;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.StringJoiner;

@Component
public class BindingResultParser {
    public String getFieldErrMismatches(BindingResult result) {
        StringJoiner sj = new StringJoiner(", ");
        result.getFieldErrors().forEach((err) -> sj.add(err.getField() + ": " + err.getDefaultMessage()));
        return sj.toString();
    }
}
