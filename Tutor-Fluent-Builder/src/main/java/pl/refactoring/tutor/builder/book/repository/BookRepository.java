package pl.refactoring.tutor.builder.book.repository;

import pl.refactoring.tutor.builder.book.model.Book;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class BookRepository {
    private Map<String, Book> byIsbn = new LinkedHashMap<>();

    public Book save(Book book) {
        byIsbn.put(book.getIsbn(), book);
        return book;
    }

    public void delete(String isbn) {
        byIsbn.remove(isbn);
    }

    public List<Book> findAll() {
        return byIsbn.values().stream()
                .collect(Collectors.toList());
    }

    public Book findByIsbn(String isbn) {
        return byIsbn.get(isbn);
    }

    public List<Book> findByAuthor(String author) {
        return byIsbn
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().getAuthor().contains(author))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public void clear() {
        byIsbn = new LinkedHashMap<>();
    }
}
