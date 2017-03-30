package pl.refactoring.tutor.builder.rest;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class MockMvcRequestTest {

    protected MockMvc mvc;

    @Autowired
    protected WebApplicationContext ctx;

    @Before
    public void setUpUsingWebApplicationContext() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    public TestRequestOptions doGet(String url) {
        return doRequest(HttpMethod.GET, url);
    }

    public TestRequestOptions doPost(String url) {
        return doRequest(HttpMethod.POST, url);
    }

    public TestRequestOptions doPut(String url) {
        return doRequest(HttpMethod.PUT, url);
    }

    public TestRequestOptions doDelete(String url) {
        return doRequest(HttpMethod.DELETE, url);
    }

    public TestRequestOptions doRequest(HttpMethod httpMethod, String url) {
        return new TestRequestOptions(mvc, httpMethod, url);
    }
}
