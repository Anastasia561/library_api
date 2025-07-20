package pl.edu.libraryapi.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.libraryapi.validation.annotation.Image;

public class ImageValidator implements ConstraintValidator<Image, MultipartFile> {
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        if (file == null || file.isEmpty()) return false;

        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();

        return contentType != null && contentType.startsWith("image/")
                || filename != null && filename.matches("(?i).*\\.(jpg|jpeg|png|gif)$");
    }
}
