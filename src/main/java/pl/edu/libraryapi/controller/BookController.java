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
import pl.edu.libraryapi.validator.BookUploadValidator;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final FileService fileService;
    private final BookUploadValidator bookUploadValidator;

    public BookController(BookService bookService, FileService fileService, BookUploadValidator bookUploadValidator) {
        this.bookService = bookService;
        this.fileService = fileService;
        this.bookUploadValidator = bookUploadValidator;
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
        bookUploadValidator.validate(dto);
        fileService.validateFiles(files);
        bookService.saveBook(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<String> deleteBook(@PathVariable String isbn) {
        bookService.deleteBookByIsbn(isbn);
        return ResponseEntity.noContent().build();
    }
}
