package pl.refactoring.builder.original.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Created by madzia1 on 2017-06-03.
 */
public class HttpMockControllerTest {
    //Required to Generate JSON content from Java objects
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    protected MockMvc mvc;

    public VerificationOptions doGet(String urlTemplate) throws Exception {
        ResultActions resultActions = mvc.perform(get(urlTemplate)
                .accept(MediaType.APPLICATION_JSON));

        return new VerificationOptions(resultActions);
    }

    public VerificationOptions doDelete(String urlTemplate) throws Exception {
        ResultActions resultActions = mvc.perform(delete(urlTemplate)
                .accept(MediaType.APPLICATION_JSON));

        return new VerificationOptions(resultActions);
    }

    public TestRequestBuilder doPost(String urlTemplate) {
        return new TestRequestBuilder(mvc, urlTemplate, HttpMethod.POST);
    }

}
