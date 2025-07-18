package pl.edu.libraryapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.libraryapi.exception.InvalidFileInputException;

import java.util.List;

@Service
public class FileService {
    public void validateFiles(List<MultipartFile> files) {
        if (files == null || files.size() != 2) {
            throw new InvalidFileInputException("Please upload exactly two files");
        }

        boolean hasImage = false;
        boolean hasDocument = false;

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new InvalidFileInputException("One of the files is empty");
            }

            String contentType = file.getContentType();
            String filename = file.getOriginalFilename();

            if (isImage(contentType, filename)) {
                hasImage = true;
            } else if (isPdfOrTxt(contentType, filename)) {
                hasDocument = true;
            } else {
                throw new InvalidFileInputException("Unsupported file type: " + filename);
            }
        }

        if (!hasImage || !hasDocument) {
            throw new InvalidFileInputException("Please upload one image and one PDF or TXT file");
        }
    }

    private boolean isImage(String contentType, String filename) {
        return contentType != null && contentType.startsWith("image/")
                || filename != null && filename.matches("(?i).*\\.(jpg|jpeg|png|gif)$");
    }

    private boolean isPdfOrTxt(String contentType, String filename) {
        return contentType != null && (contentType.equals("application/pdf") || contentType.equals("text/plain"))
                || filename != null && filename.matches("(?i).*\\.(pdf|txt)$");
    }
}
