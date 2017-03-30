package pl.refactoring.tutor.builder.rest;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.util.function.Consumer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

// TODO Move urlTemplate from TestRequestOptions in UrlBuilder (left on purpose)\
// TODO Get rid of 'requestBody = null' and make use of Optionals
public class TestRequestOptions {
    private final MockMvc mockMvc;

    private final HttpMethod httpMethod;
    private final String urlTemplate;
    private final UrlBuilder urlBuilder = new UrlBuilder();

    private String requestBody = null;
    private MediaType contentType = MediaType.APPLICATION_JSON;

    public TestRequestOptions(MockMvc mockMvc, HttpMethod httpMethod, String urlTemplate) {
        this.mockMvc = mockMvc;
        this.httpMethod = httpMethod;
        this.urlTemplate = urlTemplate;
    }

    public TestRequestOptions withUrlParams(Object... params) {
        urlBuilder.addPathParams(params);
        return this;
    }

    public TestRequestOptions withQueryParam(String name, Object value) {
        urlBuilder.addQueryParam(name, value);
        return this;
    }

    public TestRequestOptions withRequestBody(Consumer<JsonObjectBuilder> builderConsumer) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builderConsumer.accept(builder);

        String requestBody = builder.build().toString();
        contentType = MediaType.APPLICATION_JSON;

        return withRequestBody(requestBody);
    }

    public TestRequestOptions withRequestBody(String requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public ResponseVerificationOptions andVerify() {
        MockHttpServletRequestBuilder request = request(httpMethod, urlBuilder.generateTargetUrl(urlTemplate));

        if (requestBody != null) {
            request.content(requestBody);
            request.contentType(contentType);
        }

        try {
            return new ResponseVerificationOptions(mockMvc.perform(request));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseVerificationOptions andVerifyOk() {
        return andVerify().isOk();
    }
}
