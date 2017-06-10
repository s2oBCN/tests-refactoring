package pl.refactoring.builder.original.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.refactoring.builder.original.book.model.Book;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by madzia1 on 2017-06-03.
 */
public class HttpMockControllerTest {
    //Required to Generate JSON content from Java objects
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    protected MockMvc mvc;

    protected VerificationOptions doGet(String urlTemplate) throws Exception {
        ResultActions resultActions = mvc.perform(get(urlTemplate)
                .accept(MediaType.APPLICATION_JSON));

        return new VerificationOptions(resultActions);
    }

    protected VerificationOptions doDelete(String urlTemplate) throws Exception {
        ResultActions resultActions = mvc.perform(delete(urlTemplate)
                .accept(MediaType.APPLICATION_JSON));

        return new VerificationOptions(resultActions);
    }

    protected VerificationOptions doPost(String urlTemplate, Book content) throws Exception {
        ResultActions resultActions = mvc.perform(post(urlTemplate)
                .content(OBJECT_MAPPER.writeValueAsString(content))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        return new VerificationOptions(resultActions);
    }
}
