package pl.edu.libraryapi.mapper;

import org.springframework.stereotype.Component;
import pl.edu.libraryapi.dto.BookLibrarianResponseDto;
import pl.edu.libraryapi.dto.BookUploadRequestDto;
import pl.edu.libraryapi.dto.BookUserResponseDto;
import pl.edu.libraryapi.entity.*;
import pl.edu.libraryapi.exception.EntityNotFoundException;
import pl.edu.libraryapi.repository.AuthorRepository;
import pl.edu.libraryapi.repository.GenreRepository;
import pl.edu.libraryapi.repository.PublisherRepository;

@Component
public class BookMapper {
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;

    public BookMapper(GenreRepository genreRepository, PublisherRepository publisherRepository,
                      AuthorRepository authorRepository) {
        this.genreRepository = genreRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
    }

    public BookUserResponseDto toBookUserResponseDto(Book book) {
        BookUserResponseDto dto = new BookUserResponseDto();
        dto.setTitle(book.getTitle());
        dto.setYear(book.getPublicationYear());
        dto.setPages(book.getPages() != null ? book.getPages() + "" : "-");
        dto.setAuthor(book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName());
        dto.setPublisher(book.getPublisher().getName());
        dto.setGenre(book.getGenre().getName());
        dto.setIsbn(book.getIsbn());
        dto.setAuthorPenName(book.getAuthor().getPenName() == null ? "-" : book.getAuthor().getPenName());
        return dto;
    }

    public BookLibrarianResponseDto toBookLibrarianResponseDto(Book book) {
        BookLibrarianResponseDto dto = new BookLibrarianResponseDto();
        dto.setTitle(book.getTitle());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setPages(book.getPages() != null ? book.getPages() + "" : "-");
        dto.setAuthor(book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName());
        dto.setPublisher(book.getPublisher().getName());
        dto.setGenre(book.getGenre().getName());
        dto.setIsbn(book.getIsbn());
        dto.setAuthorPenName(book.getAuthor().getPenName() == null ? "-" : book.getAuthor().getPenName());
        return dto;
    }

    public Book toBook(BookUploadRequestDto dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setPublicationYear(dto.getPublicationYear());
        book.setPages(dto.getPages());
        book.setIsbn(dto.getIsbn());
        Author author = authorRepository.findByFullName(dto.getAuthor().split(" ")[0], dto.getAuthor().split(" ")[1])
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        Publisher publisher = publisherRepository.findByName(dto.getPublisher())
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found"));
        Genre genre = genreRepository.findByName(dto.getGenre())
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setGenre(genre);
        return book;
    }
}
