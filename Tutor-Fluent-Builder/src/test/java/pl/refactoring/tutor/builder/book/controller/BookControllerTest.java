package pl.refactoring.tutor.builder.book.controller;

import pl.refactoring.tutor.builder.book.BookApplication;
import pl.refactoring.tutor.builder.book.model.Book;
import pl.refactoring.tutor.builder.book.model.BookBuilder;
import pl.refactoring.tutor.builder.book.repository.BookRepository;
import pl.refactoring.tutor.builder.rest.MockMvcRequestTest;
import pl.refactoring.tutor.builder.rest.ResponseVerificationOptions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

//TODO BookRepository with BookRepositoryRule

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BookApplication.class, webEnvironment = DEFINED_PORT)
public class BookControllerTest extends MockMvcRequestTest {

    private static final String AUTHOR_1 = "Author 1";
    private static final String AUTHOR_2 = "Author 2";

    private static final Book BOOK_1 = new BookBuilder()
            .withName("Book 1")
            .withIsbn("ISBN_1")
            .withAuthor(AUTHOR_1)
            .withPages(628)
            .createBook();
    private static final Book BOOK_2 = new BookBuilder()
            .withName("Book 2")
            .withIsbn("ISBN_2")
            .withAuthor(AUTHOR_2)
            .withPages(482)
            .createBook();
    private static final Book BOOK_3 = new BookBuilder()
            .withName("Book 3")
            .withIsbn("ISBN_3")
            .withAuthor(AUTHOR_1)
            .withPages(533)
            .createBook();

    //TODO Replace with BookRepositoryRule
    @Autowired
    private BookRepository bookRepository;

    @Before
    public void clearRepository() throws Exception {
        bookRepository.clear();
    }

    @Test
    public void shouldCreateBook() throws Exception {
        ResponseVerificationOptions responseVerificationOptions = doPost("/book/")
                .withRequestBody(builder ->
                {
                    builder.add("name", BOOK_1.getName())
                            .add("isbn", BOOK_1.getIsbn())
                            .add("author", BOOK_1.getAuthor())
                            .add("pages", BOOK_1.getPages());
                })
                .andVerifyOk();

        Book bookFromDb = bookRepository.findByIsbn(BOOK_1.getIsbn());

        responseVerificationOptions
                .hasJson("$.message", equalTo("Book created successfully"))
                .hasJson("$.book.name", equalTo(bookFromDb.getName()))
                .hasJson("$.book.isbn", equalTo(bookFromDb.getIsbn()))
                .hasJson("$.book.author", equalTo(bookFromDb.getAuthor()))
                .hasJson("$.book.pages", equalTo(bookFromDb.getPages()));
    }

    @Test
    public void shouldGetBookDetails() throws Exception {
        //Create a new book using the BookRepository API
        bookRepository.save(BOOK_1);

        doGet("/book/{isbn}")
                .withUrlParams(BOOK_1.getIsbn())
                .andVerifyOk()
                .hasJson("$.name", equalTo(BOOK_1.getName()))
                .hasJson("$.isbn", equalTo(BOOK_1.getIsbn()))
                .hasJson("$.author", equalTo(BOOK_1.getAuthor()))
                .hasJson("$.pages", equalTo(BOOK_1.getPages()));
    }

    @Test
    public void shouldUpdateBookDetails() throws Exception {
        //Create a new book using the BookRepository API
        bookRepository.save(BOOK_1);

        //Now create Request body with the updated Book Data.
        ResponseVerificationOptions responseVerificationOptions = doPut("/book/{isbn}")
                .withUrlParams(BOOK_1.getIsbn())
                .withRequestBody(builder ->
                {
                    builder.add("name", BOOK_2.getName())
                            .add("isbn", BOOK_1.getIsbn())
                            .add("author", BOOK_2.getAuthor())
                            .add("pages", BOOK_2.getPages());
                })
                .andVerifyOk();

        Book book2FromDb = bookRepository.findByIsbn(BOOK_1.getIsbn());
        assertThat(book2FromDb)
                .isEqualToComparingOnlyGivenFields(BOOK_2, "name", "author", "pages");
    }

    @Test
    public void shouldDeleteBook() throws Exception {
        //Create a new book using the BookRepository API
        bookRepository.save(BOOK_1);

        doDelete("/book/{isbn}")
                .withUrlParams(BOOK_1.getIsbn())
                .andVerifyOk();

        //When try to fetch from the DB directly
        Book bookFromDb = bookRepository.findByIsbn(BOOK_1.getIsbn());
        // Then it is not found any longer
        assertNull(bookFromDb);
    }

    @Test
    public void testGetAllBooksDetails() throws Exception {
        //Add some test data for the API
        bookRepository.save(BOOK_1);
        bookRepository.save(BOOK_2);

        doGet("/book/")
                .andVerifyOk()
                .hasJson("$.books", hasSize(2))
                .hasJson("$.books[0].name", equalTo(BOOK_1.getName()))
                .hasJson("$.books[0].isbn", equalTo(BOOK_1.getIsbn()))
                .hasJson("$.books[0].author", equalTo(BOOK_1.getAuthor()))
                .hasJson("$.books[0].pages", equalTo(BOOK_1.getPages()))
                .hasJson("$.books[1].name", equalTo(BOOK_2.getName()))
                .hasJson("$.books[1].isbn", equalTo(BOOK_2.getIsbn()))
                .hasJson("$.books[1].author", equalTo(BOOK_2.getAuthor()))
                .hasJson("$.books[1].pages", equalTo(BOOK_2.getPages()));
    }

    @Test
    public void testGetBooksByQuery() throws Exception {
        //Add some test data for the API
        bookRepository.save(BOOK_1);
        bookRepository.save(BOOK_2);
        bookRepository.save(BOOK_3);

        doGet("/book/")
                .withQueryParam("author", AUTHOR_2)
                .andVerifyOk()
                .hasJson("$.books", hasSize(1))
                .hasJson("$.books[0].name", equalTo(BOOK_2.getName()))
                .hasJson("$.books[0].isbn", equalTo(BOOK_2.getIsbn()))
                .hasJson("$.books[0].author", equalTo(BOOK_2.getAuthor()))
                .hasJson("$.books[0].pages", equalTo(BOOK_2.getPages()));
    }
}
