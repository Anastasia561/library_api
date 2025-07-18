package pl.edu.libraryapi.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import pl.edu.libraryapi.dto.BookUploadRequestDto;

import java.util.Set;

@Component
public class BookUploadValidator {
    private final Validator validator;

    public BookUploadValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(BookUploadRequestDto dto) {
        Set<ConstraintViolation<BookUploadRequestDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
