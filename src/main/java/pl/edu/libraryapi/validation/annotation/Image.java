package pl.edu.libraryapi.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pl.edu.libraryapi.validation.validator.ImageValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ImageValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Image {
    String message() default "File must be an image file (jpg|jpeg|png|gif)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
