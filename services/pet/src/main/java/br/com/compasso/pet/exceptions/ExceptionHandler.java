package br.com.compasso.pet.exceptions;

import br.com.compasso.pet.advices.GenericException;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleApiError(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        log.error("handleApiError() - Invalid API request: " + ex.getTarget().getClass().toString() +
                " - " + ex.getErrorCount() + " - fields: " + errorMap.toString());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @org.springframework.web.bind.annotation.ExceptionHandler(GenericException.class)
    public Map<String, String> handleGenericException(GenericException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorMessage", ex.getMessage());
        log.error("handleGenericException() - " + errorMap.toString());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(DateTimeParseException.class)
    public Map<String, String> handleDateTimeException(DateTimeParseException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorMessage", "Expected: dd/MM/yyyy HH:mm:ss, Received: " + ex.getParsedString());
        log.error("handleDateTimeParseException() - " + errorMap.toString() + "  " + ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(JsonMappingException.class)
    public Map<String, String> handleJsonMappingException(JsonMappingException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorMessage", "Invalid format");
        log.error("handleJsonMappingException() - " + ex.getMessage() + "\n" + errorMap.toString());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @org.springframework.web.bind.annotation.ExceptionHandler(MessageFeignException.class)
    public Map<String, String> handleFeignException(MessageFeignException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorMessage", ex.getMessage());
        log.error("handleFeignException() - " + ex.getMessage() + "\n" + errorMap.toString());
        return errorMap;
    }
}
