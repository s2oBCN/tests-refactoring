package pl.refactoring.builder.original.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by madzia1 on 2017-06-03.
 */
public class HttpMockControllerTest {
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
