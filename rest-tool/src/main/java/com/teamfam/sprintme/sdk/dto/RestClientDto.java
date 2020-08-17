package com.teamfam.sprintme.sdk.dto;

import java.util.Map;

import com.teamfam.sprintme.sdk.rest.constants.HttpScheme;

import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Rest Client data transfer object required to encapsulate the data required to send through Http.
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RestClientDto<T> {
    private String host;
    //This is the default no port value for the URI components builder.
    @Builder.Default
    private int port = 1;
    private HttpScheme scheme;
    private String uri;
    private Map<String,String> headers;
    private MultiValueMap<String,String> pathParameters;
    private MultiValueMap<String,String> queryParameters;
    private T payload;
    private HttpMethod httpMethod;
}