package pl.refactoring.builder.original.book.model;


public class Book {
    private String name;
    private String isbn;
    private String author;
    private int pages;

    // Needed to map object into Json structure
    public Book() {
    }

    public Book(String name, String isbn, String author, int pages){
        this.name = name;
        this.isbn = isbn;
        this.author = author;
        this.pages = pages;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public int getPages() {
        return pages;
    }
    public void setPages(int pages) {
        this.pages = pages;
    }

}
