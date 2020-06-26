package com.teamfam.sprintme.sdk.dto;

import java.util.Map;

import com.teamfam.sprintme.sdk.constants.HttpScheme;

import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

/**
 * Rest Client data transfer object required to encapsulate the data required to send through Http.
 */
public class RestClientDto<T> {
    private String host;
    //This is the default no port value for the URI components builder.
    private int port = 1;
    private HttpScheme scheme;
    private String uri;
    private Map<String,String>headers;
    private Map<String,String> pathParameters;
    private MultiValueMap<String,String> queryParameters;
    private T payload;
    private HttpMethod httpMethod;
}