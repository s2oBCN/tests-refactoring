package pl.refactoring.builder.original.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.refactoring.builder.original.book.BookApplication;
import pl.refactoring.builder.original.book.model.Book;
import pl.refactoring.builder.original.book.repository.BookRepository;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 1. Initial CleanUp
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookApplication.class, webEnvironment = RANDOM_PORT)
public class BookControllerTest extends HttpMockControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    public static final Book BOOK_1 = new Book("Book 1", "QWER1234", "Author 1", 200);
    public static final Book BOOK_2 = new Book("Book2", "ISBN2", "Author2", 200);
    public static final Book BOOK_1_UPDATED = new Book("Book2", BOOK_1.getIsbn(), "Author2", 200);

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
        // Given
        mvc.perform(post("/book/")
                .content(OBJECT_MAPPER.writeValueAsString(BOOK_1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book created successfully"));

        // When
        Book bookFromDb = bookRepository.findByIsbn(BOOK_1.getIsbn());

        // Then
        assertThat(bookFromDb).isEqualToComparingFieldByField(BOOK_1);
    }

    @Test
    public void shouldGetBookDetails() throws Exception {
        // Given
        bookRepository.save(BOOK_1);

        // When / Then
        doGet("/book/" + BOOK_1.getIsbn())
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

        mvc.perform(put("/book/" + BOOK_1.getIsbn())
                .content(OBJECT_MAPPER.writeValueAsString(BOOK_1_UPDATED))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book Updated successfully"));

        //Fetching the Book details directly from the DB to verify the API succeeded in updating the book details
        Book bookFromDb = bookRepository.findByIsbn(BOOK_1.getIsbn());
        assertThat(bookFromDb).isEqualToComparingFieldByField(BOOK_1_UPDATED);
    }

    @Test
    public void shouldDeleteBook() throws Exception {
        // Given
        bookRepository.save(BOOK_1);

        // When
        doDelete("/book/" + BOOK_1.getIsbn())
                .hasStatusOK();

        // Then
        Book bookFromDb = bookRepository.findByIsbn(BOOK_1.getIsbn());

        // Then
        assertThat(bookFromDb)
                .isNull();
    }

    @Test
    public void testGetAllBooksApi() throws Exception{
        // Given
        bookRepository.save(BOOK_1);
        bookRepository.save(BOOK_2);

        // When / Then
        doGet("/book/")
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
