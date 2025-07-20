package pl.edu.libraryapi.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pl.edu.libraryapi.validation.validator.UniqueIsbnValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueIsbnValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueIsbn {
    String message() default "Isbn must be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
