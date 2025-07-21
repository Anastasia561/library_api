package pl.edu.libraryapi.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.edu.libraryapi.exception.EntityNotFoundException;
import pl.edu.libraryapi.exception.FolderNotFoundException;
import pl.edu.libraryapi.exception.InvalidFileInputException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class, InvalidFileInputException.class, FolderNotFoundException.class})
    public ResponseEntity<String> handleException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handle(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> handleConstraintViolationException(ConstraintViolationException ex) {
        return ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> {
                            return violation.getPropertyPath().toString();
                        },
                        violation -> violation.getMessage()
                ));
    }
}
