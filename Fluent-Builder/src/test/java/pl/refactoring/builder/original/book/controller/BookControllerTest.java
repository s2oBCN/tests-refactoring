package pl.refactoring.builder.original.book.controller;

import org.assertj.core.api.StrictAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.refactoring.builder.original.book.BookApplication;
import pl.refactoring.builder.original.book.model.Book;
import pl.refactoring.builder.original.book.repository.BookRepository;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 1. Initial CleanUp
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookApplication.class, webEnvironment = RANDOM_PORT)
public class BookControllerTest extends MockMvcControllerTest {

    public static final Book BOOK_1 = new Book("Book 1", "QWER1234", "Author 1", 200);
    public static final Book BOOK_2 = new Book("Book2", "ISBN2", "Author2", 200);
    @Autowired
    private WebApplicationContext ctx;

    @Before
    public void setUpUsingWebApplicationContext() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(ctx).build();
        bookRepository.clear();
    }

    //Required to delete the data added for tests.
    //Directly invoke the APIs interacting with the DB
    @Autowired
    private BookRepository bookRepository;

    @Test
    public void shouldCreateBook() throws Exception {
        // Given / When
        doPost("/book/")
                .withContent(BOOK_1)
                .andVerify()
                .hasStatusOK()
                .hasJson("$.message", "Book created successfully");

        // Then
        Book bookFromDb = bookRepository.findByIsbn(BOOK_1.getIsbn());
        StrictAssertions.assertThat(bookFromDb)
                .isEqualToComparingFieldByField(BOOK_1);
    }

    @Test
    public void shouldGetBookDetails() throws Exception {
        // Given
        bookRepository.save(BOOK_1);

        // When / Then
        doGet("/book/" + BOOK_1.getIsbn())
                .andVerify()
                .hasStatusOK()
                .hasJson("$.name", BOOK_1.getName())
                .hasJson("$.isbn", BOOK_1.getIsbn())
                .hasJson("$.author", BOOK_1.getAuthor())
                .hasJson("$.pages", BOOK_1.getPages());
    }

    @Test
    public void shouldUpdateBookDetails() throws Exception {
        // Given
        bookRepository.save(BOOK_1);

        //Now create Request body with the updated Book Data.
        Map<String, Object> requestBody = new HashMap<String, Object>();
        requestBody.put("name", "Book2");
        requestBody.put("isbn", BOOK_1.getIsbn());
        requestBody.put("author", "Author2");
        requestBody.put("pages", 327);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        mvc.perform(put("/book/" + BOOK_1.getIsbn())
                .content(OBJECT_MAPPER.writeValueAsString(requestBody))
                .headers(requestHeaders)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book Updated successfully"));

        //Fetching the Book details directly from the DB to verify the API succeeded in updating the book details
        Book bookFromDb = bookRepository.findByIsbn(BOOK_1.getIsbn());
        assertEquals(bookFromDb.getName(), requestBody.get("name"));
        assertEquals(bookFromDb.getIsbn(), requestBody.get("isbn"));
        assertEquals(bookFromDb.getAuthor(), requestBody.get("author"));
        assertTrue(bookFromDb.getPages() == Integer.parseInt(requestBody.get("pages").toString()));
    }

    @Test
    public void shouldDeleteBook() throws Exception {
        // Given
        bookRepository.save(BOOK_1);

        // When
        doDelete("/book/" + BOOK_1.getIsbn())
                .andVerify()
                .hasStatusOK();

        Book bookFromDb = bookRepository.findByIsbn(BOOK_1.getIsbn());

        // Then
        assertNull(bookFromDb);
    }

    @Test
    public void testGetAllBooksApi() throws Exception{
        // Given
        bookRepository.save(BOOK_1);
        bookRepository.save(BOOK_2);

        // When / Then
        doGet("/book/")
                .andVerify()
                .hasStatusOK()
                .hasJsonArray("$.books")

                .hasJson("$.books[0].name", BOOK_1.getName())
                .hasJson("$.books[0].isbn", BOOK_1.getIsbn())
                .hasJson("$.books[0].author", BOOK_1.getAuthor())
                .hasJson("$.books[0].pages", BOOK_1.getPages())

                .hasJson("$.books[1].name", BOOK_2.getName())
                .hasJson("$.books[1].isbn", BOOK_2.getIsbn())
                .hasJson("$.books[1].author", BOOK_2.getAuthor())
                .hasJson("$.books[1].pages", BOOK_2.getPages());

    }

}
