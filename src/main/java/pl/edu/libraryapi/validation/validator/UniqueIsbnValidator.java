package pl.edu.libraryapi.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.edu.libraryapi.repository.BookRepository;
import pl.edu.libraryapi.validation.annotation.UniqueIsbn;

public class UniqueIsbnValidator implements ConstraintValidator<UniqueIsbn, String> {
    private final BookRepository bookRepository;

    public UniqueIsbnValidator(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext constraintValidatorContext) {
        return !bookRepository.existsByIsbn(isbn);
    }
}
