package pl.refactoring.tutor.builder.book.controller;

import pl.refactoring.tutor.builder.book.model.Book;
import pl.refactoring.tutor.builder.book.model.BookBuilder;
import pl.refactoring.tutor.builder.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> createBook(@RequestBody Book book) {
        Book newBook = bookRepository.save(book);

        Map<String, Object> response = new LinkedHashMap<String, Object>();
        response.put("message", "Book created successfully");
        response.put("book", newBook);
        return response;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{bookId}")
    public Book getBookDetails(@PathVariable("bookId") String bookId) {
        return bookRepository.findByIsbn(bookId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{bookId}")
    public Map<String, Object> editBook(@RequestBody Map<String, Object> bookMap) {
        Book book = new BookBuilder().withName(bookMap.get("name").toString()).withIsbn(bookMap.get("isbn").toString()).withAuthor(bookMap.get("author").toString()).withPages(Integer.parseInt(bookMap.get("pages").toString())).createBook();

        Book editedBook = bookRepository.save(book);

        Map<String, Object> response = new LinkedHashMap<String, Object>();
        response.put("message", "Book Updated successfully");
        response.put("book", editedBook);
        return response;
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/{bookId}")
    public Map<String, String> deleteBook(@PathVariable("bookId") String bookId) {
        bookRepository.delete(bookId);
        Map<String, String> response = new HashMap<String, String>();
        response.put("message", "Book deleted successfully");

        return response;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> getSpecifiedBooks(@RequestParam(value = "author", required = false) String author) {
        List<Book> booksByAuthor = author != null ? bookRepository.findByAuthor(author) : bookRepository.findAll();

        Map<String, Object> response = new LinkedHashMap<String, Object>();
        response.put("totalBooks", booksByAuthor.size());
        response.put("books", booksByAuthor);

        return response;
    }

}

