package pl.refactoring.tutor.builder.rest;


import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UrlBuilder {
    private List<Object> pathParams = null;
    private LinkedMultiValueMap<String, String> queryParams = null;

    public void addPathParams(Object... params) {
        if (pathParams == null) {
            pathParams = new ArrayList<>();
        }
        Collections.addAll(pathParams, params);
    }

    public void addQueryParam(String name, Object valueObject) {
        if (queryParams == null) {
            queryParams = new LinkedMultiValueMap<>();
        }

        String value = Objects.toString(valueObject);
        queryParams.add(name, value);
    }

    public String generateTargetUrl(String urlTemplate) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(urlTemplate);

        if (queryParams != null && !queryParams.isEmpty()) {
            uriComponentsBuilder.queryParams(queryParams);
        }

        UriComponents uriComponents;
        if (pathParams != null && !pathParams.isEmpty()) {
            uriComponents = uriComponentsBuilder.buildAndExpand(pathParams.stream().toArray(Object[]::new));
        } else {
            uriComponents = uriComponentsBuilder.build();
        }

        return uriComponents.toUriString();
    }
}
