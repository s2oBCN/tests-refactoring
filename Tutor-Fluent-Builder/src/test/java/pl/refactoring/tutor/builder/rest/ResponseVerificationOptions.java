package pl.refactoring.tutor.builder.rest;

import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ResponseVerificationOptions {
    private static final Logger LOG = LoggerFactory.getLogger(ResponseVerificationOptions.class);

    private final ResultActions resultActions;

    public ResponseVerificationOptions(ResultActions resultActions) {
        this.resultActions = resultActions;
    }

    public ResponseVerificationOptions hasStatus(HttpStatus httpStatus) {
        return expect(status().is(httpStatus.value()));
    }

    public ResponseVerificationOptions isOk() {
        return hasStatus(HttpStatus.OK);
    }

    public ResponseVerificationOptions isNotFound(){
        hasStatus(HttpStatus.NOT_FOUND);
        return this;
    }

    public ResponseVerificationOptions isBadRequest() {
        hasStatus(HttpStatus.BAD_REQUEST);
        return this;
    }

    public ResponseVerificationOptions isAccessDenied() {
        hasStatus(HttpStatus.FORBIDDEN);
        return this;
    }

    public ResponseVerificationOptions isCreated() {
        hasStatus(HttpStatus.CREATED);
        hasHeader(HttpHeaders.LOCATION, not(isEmptyOrNullString()));
        return this;
    }

    public ResponseVerificationOptions hasHeader(String header, Matcher<String> matcher) {
        return expect(MockMvcResultMatchers.header().string(header, matcher));
    }

    public <T> ResponseVerificationOptions hasJson(String jsonPath, Matcher<T> matcher) {
        return expect(jsonPath(jsonPath, matcher));
    }

    public ResponseVerificationOptions hasBody(Matcher<String> bodyMatcher) {
        return expect(content().string(bodyMatcher));
    }

    // this method should stay private as it doesn't match this idea of fluent interface created here
    private ResponseVerificationOptions expect(ResultMatcher matcher) {
        try {
            resultActions.andExpect(matcher);
        } catch (Throwable error) {
            try {
                String msg = String.format("Response content was:\n%s", resultActions.andReturn().getResponse().getContentAsString());
                throw new AssertionError(msg, error);
            } catch (Exception e) {
                LOG.error("Error while notifying about response error:", e);
                throw new RuntimeException(error);
            }
        }
        return this;
    }

}
