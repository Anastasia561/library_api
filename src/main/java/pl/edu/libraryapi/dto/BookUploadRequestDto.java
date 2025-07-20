package pl.edu.libraryapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.libraryapi.validation.annotation.Image;
import pl.edu.libraryapi.validation.annotation.PDF;
import pl.edu.libraryapi.validation.annotation.UniqueIsbn;

public class BookUploadRequestDto {
    @Image
    MultipartFile coverImage;
    @PDF
    MultipartFile book;
    @ISBN
    @UniqueIsbn
    @NotBlank(message = "ISBN is required")
    private String isbn;
    @NotBlank(message = "Title is required")
    private String title;
    @Min(1400)
    @NotNull(message = "Publication year is required")
    private Integer publicationYear;
    @Min(0)
    private Integer pages;
    @NotBlank(message = "Author full name is required")
    private String author;
    @NotBlank(message = "Publisher name is required")
    private String publisher;
    @NotBlank(message = "Genre name is required")
    private String genre;

    public BookUploadRequestDto() {
    }

    public MultipartFile getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(MultipartFile coverImage) {
        this.coverImage = coverImage;
    }

    public MultipartFile getBook() {
        return book;
    }

    public void setBook(MultipartFile book) {
        this.book = book;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
