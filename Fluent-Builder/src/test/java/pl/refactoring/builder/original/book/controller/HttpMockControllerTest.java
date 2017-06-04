package pl.refactoring.builder.original.book.controller;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Created by madzia1 on 2017-06-03.
 */
public class HttpMockControllerTest {
    protected MockMvc mvc;

    protected VerificationOptions doGet(String urlTemplate) throws Exception {
        ResultActions resultActions = mvc.perform(get(urlTemplate)
                .accept(MediaType.APPLICATION_JSON));

        return new VerificationOptions(resultActions);
    }
}
