package pl.edu.libraryapi.controller;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.libraryapi.dto.BookLibrarianResponseDto;
import pl.edu.libraryapi.dto.BookUploadRequestDto;
import pl.edu.libraryapi.dto.BookUserResponseDto;
import pl.edu.libraryapi.service.BookService;
import pl.edu.libraryapi.service.StorageService;

import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final StorageService storageService;

    public BookController(BookService bookService, StorageService storageService) {
        this.bookService = bookService;
        this.storageService = storageService;
    }

    @GetMapping
    public List<BookUserResponseDto> getBooks() {
        return bookService.getAllBooksForUser().stream()
                .peek(b -> b.setCoverImage(storageService.generateBookCoverURL(b.getIsbn())))
                .toList();
    }

    @GetMapping("/{isbn}")
    public BookUserResponseDto getBookByIsbn(@PathVariable String isbn) {
        BookUserResponseDto book = bookService.getBookByIsbn(isbn);
        book.setCoverImage(storageService.generateBookCoverURL(book.getIsbn()));
        return book;
    }

    @GetMapping("/{isbn}/download/full")
    public URL getFullDownloadUrl(@PathVariable String isbn) {
        return storageService.generateBookDownloadURL(isbn, bookService.getBookTitleByIsbn(isbn), true);
    }

    @GetMapping("/{isbn}/download/preview")
    public URL getPreviewDownloadUrl(@PathVariable String isbn) {
        return storageService.generateBookDownloadURL(isbn, bookService.getBookTitleByIsbn(isbn), false);
    }

    @GetMapping("/info")
    public List<BookLibrarianResponseDto> getBooksInfo() {

        return bookService.getAllBooksForLibrarian().stream()
                .peek(b -> b.setCoverImage(storageService.generateBookCoverURL(b.getIsbn())))
                .toList();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadBook(@ModelAttribute @Valid BookUploadRequestDto dto) {
        try {
            storageService.uploadFiles(List.of(dto.getBook(), dto.getCoverImage()), dto.getIsbn());
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
