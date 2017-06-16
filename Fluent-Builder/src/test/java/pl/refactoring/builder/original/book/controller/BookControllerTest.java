package pl.refactoring.builder.original.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.junit4.SpringRunner;
import pl.refactoring.builder.original.book.BookApplication;
import pl.refactoring.builder.original.book.model.Book;
import pl.refactoring.builder.original.book.repository.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 1. Initial CleanUp
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookApplication.class, webEnvironment = RANDOM_PORT)
public class BookControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @Before
    public void setUpUsingWebApplicationContext() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(ctx).build();
        bookRepository.clear();
    }

    //Required to Generate JSON content from Java objects
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    //Required to delete the data added for tests.
    //Directly invoke the APIs interacting with the DB
    @Autowired
    private BookRepository bookRepository;

    @Test
    public void shouldCreateBook() throws Exception {
        String isbn = "QWER1234";

        //Building the Request body data
        Map<String, Object> requestBody = new HashMap<String, Object>();
        requestBody.put("name", "Book 1");
        requestBody.put("isbn", isbn);
        requestBody.put("author", "Author 1");
        requestBody.put("pages", 200);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        mvc.perform(post("/book/")
                .content(OBJECT_MAPPER.writeValueAsString(requestBody))
                .headers(requestHeaders)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book created successfully"));

        //Fetching the Book details directly from the DB to verify the API succeeded
        Book bookFromDb = bookRepository.findByIsbn(isbn);
        assertEquals("Book 1", bookFromDb.getName());
        assertEquals(isbn, bookFromDb.getIsbn());
        assertEquals("Author 1", bookFromDb.getAuthor());
        assertTrue(200 == bookFromDb.getPages());
    }

    @Test
    public void shouldGetBookDetails() throws Exception {
        //Create a new book using the BookRepository API
        String isbn = "ISBN1";
        Book book = new Book("Book1", isbn, "Author1", 200);
        bookRepository.save(book);

        //Now make a call to the API to get details of the book
        mvc.perform(get("/book/" + isbn).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(book.getName()))
                .andExpect(jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(jsonPath("$.author").value(book.getAuthor()))
                .andExpect(jsonPath("$.pages").value(book.getPages()));
    }

    @Test
    public void shouldUpdateBookDetails() throws Exception {
        //Create a new book using the BookRepository API
        String isbn = "ISBN1";
        Book book = new Book("Book1", isbn, "Author1", 200);
        bookRepository.save(book);

        //Now create Request body with the updated Book Data.
        Map<String, Object> requestBody = new HashMap<String, Object>();
        requestBody.put("name", "Book2");
        requestBody.put("isbn", isbn);
        requestBody.put("author", "Author2");
        requestBody.put("pages", 327);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        mvc.perform(put("/book/" + isbn)
                .content(OBJECT_MAPPER.writeValueAsString(requestBody))
                .headers(requestHeaders)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book Updated successfully"));

        //Fetching the Book details directly from the DB to verify the API succeeded in updating the book details
        Book bookFromDb = bookRepository.findByIsbn(isbn);
        assertEquals(bookFromDb.getName(), requestBody.get("name"));
        assertEquals(bookFromDb.getIsbn(), requestBody.get("isbn"));
        assertEquals(bookFromDb.getAuthor(), requestBody.get("author"));
        assertTrue(bookFromDb.getPages() == Integer.parseInt(requestBody.get("pages").toString()));
    }

    @Test
    public void shouldDeleteBook() throws Exception {
        //Create a new book using the BookRepository API
        String isbn1 = "ISBN1";
        Book book = new Book("Book1", isbn1, "Author1", 200);
        bookRepository.save(book);

        //Now Invoke the API to delete the book
        mvc.perform(delete("/book/" + isbn1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //Try to fetch from the DB directly
        Book bookFromDb = bookRepository.findByIsbn(isbn1);
        //and assert that there is no data found
        assertNull(bookFromDb);
    }

    @Test
    public void testGetAllBooksApi() throws Exception{
        //Add some test data for the API
        Book book1 = new Book("Book1", "ISBN1", "Author1", 200);
        bookRepository.save(book1);

        Book book2 = new Book("Book2", "ISBN2", "Author2", 200);
        bookRepository.save(book2);

        mvc.perform(get("/book/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books").isArray())
                .andExpect(jsonPath("$.books[0].name").value(book1.getName()))
                .andExpect(jsonPath("$.books[0].isbn").value(book1.getIsbn()))
                .andExpect(jsonPath("$.books[0].author").value(book1.getAuthor()))
                .andExpect(jsonPath("$.books[0].pages").value(book1.getPages()))
                .andExpect(jsonPath("$.books[1].name").value(book2.getName()))
                .andExpect(jsonPath("$.books[1].isbn").value(book2.getIsbn()))
                .andExpect(jsonPath("$.books[1].author").value(book2.getAuthor()))
                .andExpect(jsonPath("$.books[1].pages").value(book2.getPages()));

    }
}
