package pl.edu.libraryapi.controller;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
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

    public BookController(BookService bookService, StorageService storageService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookUserResponseDto> getBooks() {
        return bookService.getAllBooksForUser().stream().toList();
    }

    @GetMapping("/{isbn}")
    public BookUserResponseDto getBookByIsbn(@PathVariable String isbn) {
        return bookService.getBookByIsbn(isbn);
    }

    @GetMapping("/{isbn}/download/full")
    public URL getFullDownloadUrl(@PathVariable String isbn) {
        return bookService.getBookDownloadUrl(isbn, true);
    }

    @GetMapping("/{isbn}/download/preview")
    public URL getPreviewDownloadUrl(@PathVariable String isbn) {
        return bookService.getBookDownloadUrl(isbn, false);
    }

    @GetMapping("/info")
    public List<BookLibrarianResponseDto> getBooksInfo() {
        return bookService.getAllBooksForLibrarian().stream().toList();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadBook(@ModelAttribute @Valid BookUploadRequestDto dto) {
        bookService.saveBook(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<String> deleteBook(@PathVariable String isbn) {
        bookService.deleteBookByIsbn(isbn);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{isbn}/update")
    public ResponseEntity<BookLibrarianResponseDto> mergePatchBook(@PathVariable String isbn,
                                                                   @RequestBody JsonMergePatch patch) {
        BookLibrarianResponseDto patchedBook = bookService.applyMergePatchToBook(isbn, patch);
        return ResponseEntity.ok(patchedBook);
    }
}
