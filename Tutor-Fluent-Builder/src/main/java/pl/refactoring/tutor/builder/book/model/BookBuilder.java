package pl.refactoring.tutor.builder.book.model;

public class BookBuilder {
    private String name;
    private String isbn;
    private String author;
    private int pages;

    public BookBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public BookBuilder withIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public BookBuilder withAuthor(String author) {
        this.author = author;
        return this;
    }

    public BookBuilder withPages(int pages) {
        this.pages = pages;
        return this;
    }

    public Book createBook() {
        return new Book(name, isbn, author, pages);
    }
}