package pl.edu.libraryapi.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.libraryapi.dto.BookDownloadResponseDto;
import pl.edu.libraryapi.dto.BookLibrarianResponseDto;
import pl.edu.libraryapi.dto.BookUploadRequestDto;
import pl.edu.libraryapi.dto.BookUserResponseDto;
import pl.edu.libraryapi.service.BookService;
import pl.edu.libraryapi.service.FileService;
import pl.edu.libraryapi.service.StorageService;
import pl.edu.libraryapi.validator.BookUploadValidator;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final FileService fileService;
    private final BookUploadValidator bookUploadValidator;
    private final StorageService storageService;

    public BookController(BookService bookService, FileService fileService,
                          BookUploadValidator bookUploadValidator, StorageService storageService) {
        this.bookService = bookService;
        this.fileService = fileService;
        this.bookUploadValidator = bookUploadValidator;
        this.storageService = storageService;
    }

    @GetMapping
    public List<BookUserResponseDto> getBooks() {
        return bookService.getAllBooksForUser();
    }

    @GetMapping("/{isbn}")
    public BookUserResponseDto getBookByIsbn(@PathVariable String isbn) {
        return bookService.getBookByIsbn(isbn);
    }

    @GetMapping("/{isbn}/download/full")
    public BookDownloadResponseDto getFullDownloadUrl(@PathVariable String isbn) {
        //full pdf url
        return new BookDownloadResponseDto();
    }

    @GetMapping("/{isbn}/download/preview")
    public BookDownloadResponseDto getPreviewDownloadUrl(@PathVariable String isbn) {
        //preview pdf url
        return new BookDownloadResponseDto();
    }

    @GetMapping("/info")
    public List<BookLibrarianResponseDto> getBooksInfo() {
        return bookService.getAllBooksForLibrarian();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadBook(@RequestPart("file") List<MultipartFile> files,
                                             @RequestPart("metadata") BookUploadRequestDto dto) {
        try {

            bookUploadValidator.validate(dto);
            fileService.validateFiles(files);
            storageService.uploadFiles(files, dto.getIsbn());
            bookService.saveBook(dto);
            return ResponseEntity.ok().build();

        } catch (Exception e) {

            storageService.deleteFolder(dto.getIsbn());
            throw new RuntimeException("Book upload failed", e);
        }
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<String> deleteBook(@PathVariable String isbn) {
        try {
            bookService.deleteBookByIsbn(isbn);
            storageService.deleteFolder(isbn);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new RuntimeException("Book delete failed", e);
        }

    }
}
