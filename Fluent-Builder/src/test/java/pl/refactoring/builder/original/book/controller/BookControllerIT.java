package pl.refactoring.builder.original.book.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.refactoring.builder.original.book.BookApplication;
import pl.refactoring.builder.original.book.model.Book;
import pl.refactoring.builder.original.book.repository.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BookApplication.class, webEnvironment = RANDOM_PORT)
public class BookControllerIT {

    //Required to Generate JSON content from Java objects
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    //Required to delete the data added for tests.
    //Directly invoke the APIs interacting with the DB
    @Autowired
    private BookRepository bookRepository;

    //Test RestTemplate to invoke the APIs.
    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Before
    public void clearRepository() {
        bookRepository.clear();
    }

    @Test
    public void shouldCreateBook() throws JsonProcessingException{
        String isbn = "QWER1234";

        //Building the Request body data
        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("name", "Book 1");
        requestBodyMap.put("isbn", isbn);
        requestBodyMap.put("author", "Author 1");
        requestBodyMap.put("pages", 200);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        //Creating http entity object with request body and headers
        HttpEntity httpEntity =
                new HttpEntity(OBJECT_MAPPER.writeValueAsString(requestBodyMap), requestHeaders);

        //Invoking the API
        Map<String, Object> responseMap =
                restTemplate.postForObject("http://localhost:8888/book", httpEntity, Map.class, Collections.emptyMap());

        assertNotNull(responseMap);

        //Asserting the response of the API.
        String message = responseMap.get("message").toString();
        assertEquals("Book created successfully", message);

        //Fetching the Book details directly from the DB to verify the API succeeded
        Book bookFromDb = bookRepository.findByIsbn(isbn);
        assertEquals("Book 1", bookFromDb.getName());
        assertEquals(isbn, bookFromDb.getIsbn());
        assertEquals("Author 1", bookFromDb.getAuthor());
        assertTrue(200 == bookFromDb.getPages());
    }

    @Test
    public void shouldGetBookDetails(){
        //Create a new book using the BookRepository API
        String isbn = "ISBN1";
        Book book = new Book("Book1", isbn, "Author1", 200);
        bookRepository.save(book);

        //Now make a call to the API to get details of the book
        Book responseMap = restTemplate.getForObject("http://localhost:8888/book/"+ isbn, Book.class);

        //Verify that the data from the API and data saved in the DB are same
        assertNotNull(responseMap);
        assertEquals(book.getName(), responseMap.getName());
        assertEquals(book.getIsbn(), responseMap.getIsbn());
        assertEquals(book.getAuthor(), responseMap.getAuthor());
        assertTrue(book.getPages() == responseMap.getPages());
    }

    // TODO Wprowadzić użycie parametru dla PUT
    @Test
    public void shouldUpdateBookDetails() throws JsonProcessingException{
        //Create a new book using the BookRepository API
        String isbn = "ISBN1";
        Book book = new Book("Book1", isbn, "Author1", 200);
        bookRepository.save(book);

        //Now create Request body with the updated Book Data.
        Map<String, Object> requestBodyMap = new HashMap<String, Object>();
        requestBodyMap.put("name", "Book2");
        requestBodyMap.put("isbn", isbn);
        requestBodyMap.put("author", "Author2");
        requestBodyMap.put("pages", 200);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        //Creating http entity object with request body and headers
        HttpEntity<String> httpEntity =
                new HttpEntity<>(OBJECT_MAPPER.writeValueAsString(requestBodyMap), requestHeaders);

        //Invoking the API
        Map responseMap = (Map) restTemplate.exchange("http://localhost:8888/book/" + isbn,
                HttpMethod.PUT, httpEntity, Map.class, Collections.EMPTY_MAP).getBody();

        assertNotNull(responseMap);
        assertTrue(!responseMap.isEmpty());

        //Asserting the response of the API.
//        String message = apiResponse.get("message").toString();
//        assertEquals("Book Updated successfully", message);

        //Fetching the Book details directly from the DB to verify the API succeeded in updating the book details
        Book bookFromDb = bookRepository.findByIsbn(isbn);
        assertEquals(requestBodyMap.get("name"), bookFromDb.getName());
        assertEquals(requestBodyMap.get("isbn"), bookFromDb.getIsbn());
        assertEquals(requestBodyMap.get("author"), bookFromDb.getAuthor());
        assertTrue(Integer.parseInt(requestBodyMap.get("pages").toString()) == bookFromDb.getPages());
    }

    @Test
    public void shouldDeleteBook(){
        //Create a new book using the BookRepository API
        String isbn1 = "ISBN1";
        Book book = new Book("Book1", isbn1, "Author1", 200);
        bookRepository.save(book);

        //Now Invoke the API to delete the book
        restTemplate.delete("http://localhost:8888/book/"+ isbn1, Collections.EMPTY_MAP);

        //Try to fetch from the DB directly
        Book bookFromDb = bookRepository.findByIsbn(isbn1);
        //and assert that there is no data found
        assertNull(bookFromDb);
    }


    @Test
    public void testGetAllBooksApi(){
        //Add some test data for the API
        Book book1 = new Book("Book1", "ISBN1", "Author1", 200);
        bookRepository.save(book1);

        Book book2 = new Book("Book2", "ISBN2", "Author2", 200);
        bookRepository.save(book2);

        //Invoke the API
        Map<String, Object> apiResponse = restTemplate.getForObject("http://localhost:8888/book", Map.class);

        //Assert the response from the API
        int totalBooks = Integer.parseInt(apiResponse.get("totalBooks").toString());
        assertTrue(totalBooks == 2);

        List<Map<String, Object>> booksList = (List<Map<String, Object>>) apiResponse.get("books");
        assertTrue(booksList.size() == 2);

        //Delete the test data created
        bookRepository.delete(book1.getIsbn());
        bookRepository.delete(book2.getIsbn());
    }
}
