package pl.refactoring.builder.original.book.repository;

import org.springframework.stereotype.Repository;
import pl.refactoring.builder.original.book.model.Book;

import java.util.*;

/**
 * 1. CleanUp to Java 8 streams where applicable
 */
// TODO Mogłoby być jakieś Bean-Primary dla Testing Scope albo coś inneg dla Produkcji :-)
@Repository
public class BookRepository {
    private Map<String, Book> byIsbn = new HashMap<>();

    public Book save(Book book) {
        byIsbn.put(book.getIsbn(), book);

        return book;
    }

    public void delete(String isbn) {
        byIsbn.remove(isbn);
    }

    public List<Book> findAll() {
        Collection<Book> values = byIsbn.values();

        ArrayList<Book> result = new ArrayList<>();
        result.addAll(values);
        return result;
    }

    public Book findByIsbn(String isbn) {
        return byIsbn.get(isbn);
    }

    // TODO Optional
    public List<Book> findByTitle(String bookId) {
        return null;
    }

    public List<Book> findByAuthor(String author) {
        return null;
    }

    public void clear() {
        byIsbn = new HashMap<>();
    }
}
