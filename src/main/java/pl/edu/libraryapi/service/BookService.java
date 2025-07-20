package pl.edu.libraryapi.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.edu.libraryapi.dto.BookLibrarianResponseDto;
import pl.edu.libraryapi.dto.BookUploadRequestDto;
import pl.edu.libraryapi.dto.BookUserResponseDto;
import pl.edu.libraryapi.entity.Book;
import pl.edu.libraryapi.exception.EntityNotFoundException;
import pl.edu.libraryapi.mapper.BookMapper;
import pl.edu.libraryapi.repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public List<BookUserResponseDto> getAllBooksForUser() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookUserResponseDto).collect(Collectors.toList());
    }

    public BookUserResponseDto getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn).map(bookMapper::toBookUserResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("Book with isbn - " + isbn + " not found"));
    }

    public List<BookLibrarianResponseDto> getAllBooksForLibrarian() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookLibrarianResponseDto).collect(Collectors.toList());
    }

    public String getBookTitleByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .map(Book::getTitle)
                .orElseThrow(() -> new EntityNotFoundException("Book with isbn - " + isbn + " not found"));
    }

    @Transactional
    public void deleteBookByIsbn(String isbn) {
        bookRepository.findByIsbn(isbn)
                .ifPresentOrElse(bookRepository::delete, () -> {
                    throw new EntityNotFoundException("Book with isbn - " + isbn + " not found");
                });
    }

    @Transactional
    public void saveBook(BookUploadRequestDto dto) {
        bookRepository.save(bookMapper.toBook(dto));
    }
}
