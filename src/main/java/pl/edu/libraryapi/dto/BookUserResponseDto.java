package pl.edu.libraryapi.dto;

import java.net.URL;

public class BookUserResponseDto {
    private String isbn;
    private String title;
    private int year;
    private String pages;
    private String author;
    private String authorPenName;
    private String publisher;
    private String genre;
    private URL coverImage;

    public URL getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(URL coverImage) {
        this.coverImage = coverImage;
    }

    public String getAuthorPenName() {
        return authorPenName;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setAuthorPenName(String authorPenName) {
        this.authorPenName = authorPenName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
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
