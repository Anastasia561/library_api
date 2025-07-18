package pl.edu.libraryapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BookUploadRequestDto {
    @NotBlank(message = "ISBN is required")
    @Size(min = 13, max = 13, message = "ISBN must be exactly 13 characters")
    private String isbn;
    @NotBlank(message = "Title is required")
    private String title;
    @Min(1400)
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

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
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
