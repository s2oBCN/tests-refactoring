package pl.refactoring.builder.original.book.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Created by madzia1 on 2017-06-12.
 */
public class TestRequestBuilder {
    private MockMvc mvc;
    private MockHttpServletRequestBuilder requestBuilder;

    public TestRequestBuilder(MockMvc mvc, String urlTemplate, HttpMethod httpMethod) {
        this.mvc = mvc;
        requestBuilder = MockMvcRequestBuilders.request(httpMethod, urlTemplate);
    }

    public TestRequestBuilder withContent(Object content) throws JsonProcessingException {
        requestBuilder.content(HttpMockControllerTest.OBJECT_MAPPER.writeValueAsString(content));
        return this;
    }

    public TestRequestBuilder withContentType(MediaType applicationJson) {
        requestBuilder.contentType(applicationJson);
        return this;
    }

    public TestRequestBuilder accepting(MediaType applicationJson) {
        requestBuilder.accept(applicationJson);
        return this;
    }

    public VerificationOptions andVerify() throws Exception {
        ResultActions resultActions = mvc.perform(requestBuilder);
        return new VerificationOptions(resultActions);
    }
}
