package pl.refactoring.builder.original.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by w.krakowski on 6/21/2017.
 */
public class MockMvcControllerTest {
    //Required to Generate JSON content from Java objects
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    protected MockMvc mvc;

    public TestRequestBuilder doGet(String urlTemplate) {
        return new TestRequestBuilder(mvc, urlTemplate, HttpMethod.GET);
    }

    public TestRequestBuilder doDelete(String urlTemplate) {
        return new TestRequestBuilder(mvc, urlTemplate, HttpMethod.DELETE);
    }

    public TestRequestBuilder doPost(String urlTemplate) {
        return new TestRequestBuilder(mvc, urlTemplate, HttpMethod.POST);
    }

}
