package pl.refactoring.builder.original.book.controller;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by madzia1 on 2017-06-03.
 */
public class VerificationOptions {

    private ResultActions resultActions;

    public VerificationOptions(ResultActions resultActions) {

        this.resultActions = resultActions;
    }

    public VerificationOptions andExpect(ResultMatcher matcher) throws Exception {
        resultActions.andExpect(matcher);
        return this;
    }

    VerificationOptions hasStatusOK() throws Exception {
        return andExpect(status().isOk());
    }

    VerificationOptions hasJson(String jsonKey, Object jsonValue) throws Exception {
        return andExpect(jsonPath(jsonKey).value(jsonValue));
    }

    VerificationOptions hasArray(String jsonKey) throws Exception {
        return andExpect(jsonPath(jsonKey).isArray());
    }
}
