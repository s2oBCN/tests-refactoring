package pl.refactoring.builder.original.book.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Created by w.krakowski on 6/21/2017.
 */
public class TestRequestBuilder {
    private MockMvc mvc;
    private MockHttpServletRequestBuilder requestBuilder;

    public TestRequestBuilder(MockMvc mvc, String urlTemplate, HttpMethod httpMethod) {
        this.mvc = mvc;
        requestBuilder = MockMvcRequestBuilders
                .request(httpMethod, urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    public TestRequestBuilder accepting(MediaType acceptingType) {
        requestBuilder.accept(acceptingType);

        return this;
    }

    public TestRequestBuilder withContentType(MediaType contentType) {
        requestBuilder.contentType(contentType);

        return this;
    }

    public TestRequestBuilder withContent(Object content) throws JsonProcessingException {
        requestBuilder.content(MockMvcControllerTest.OBJECT_MAPPER.writeValueAsString(content));

        return this;
    }

    public VerificationOptions andVerify() throws Exception {
        ResultActions resultActions = mvc.perform(requestBuilder);

        return new VerificationOptions(resultActions);
    }
}
