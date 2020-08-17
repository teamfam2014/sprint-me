package com.teamfam.sprintme.sdk.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import com.teamfam.sprintme.sdk.rest.constants.HttpScheme;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

/**
 * Unit test the rest client DTO builder to ensure that we are properly building 
 * the data. 
 * @author jbustamante
 *
 */
public class RestClientDtoTest {
	
	/**
	 * When a rest template for a plain GET is passed in, then we ensure that 
	 * the http entity is populated without a Payload, but with headers. We will 
	 * also ensure that the url is populated as expected.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void plainHttpGet() {
		//ARRANGE
		Map<String,String> headers = Mockito.mock(Map.class);
		String uri = "/some/uri";
		String host = "localhost:8080";
		HttpScheme scheme = HttpScheme.HTTP;
		//ACT
		RestClientDto<?> plainGetDto = RestClientDto.builder().httpMethod(HttpMethod.GET)
				  .scheme(scheme)
				  .host(host)
				  .uri(uri)
				  .headers(headers)
				  .build();		
		//ASSERT
		assertNotNull(plainGetDto);
		assertEquals(headers, plainGetDto.getHeaders());
		assertEquals(uri, plainGetDto.getUri());
		assertEquals(host, plainGetDto.getHost());
		assertEquals(scheme, plainGetDto.getScheme());
	}
	
	/**
	 * When a rest template for a GET with path params and query params, then 
	 * ensure that the http entity is populated without a payload, and 
	 * a full set of headers.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void loadedHttpGet() {
		//ARRANGE
		Map<String,String> headers = Mockito.mock(Map.class);
		MultiValueMap<String,String> queryParams = Mockito.mock(MultiValueMap.class);
		MultiValueMap<String,String> pathParams = Mockito.mock(MultiValueMap.class);
		String uri = "/some/uri";
		String host = "localhost:8080";
		HttpScheme scheme = HttpScheme.HTTP;
		//ACT
		RestClientDto<?> plainGetDto = RestClientDto.builder().httpMethod(HttpMethod.GET)
				  .scheme(scheme)
				  .host(host)
				  .uri(uri)
				  .headers(headers)
				  .queryParameters(queryParams)
				  .pathParameters(pathParams)
				  .build();		
		//ASSERT
		assertNotNull(plainGetDto);
		assertEquals(headers, plainGetDto.getHeaders());
		assertEquals(uri, plainGetDto.getUri());
		assertEquals(host, plainGetDto.getHost());
		assertEquals(scheme, plainGetDto.getScheme());
		assertEquals(queryParams, plainGetDto.getQueryParameters());
		assertEquals(pathParams, plainGetDto.getPathParameters());
		assertEquals(HttpMethod.GET, plainGetDto.getHttpMethod());
	}	
	
	/**
	 * When a rest template for a plain POST, then we ensure that the http
	 * entity is populated with a payload, but with a full set of headers. We 
	 * will also expect the URI to be populated as expected.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void plainHttpPost() {
		//ARRANGE
		Map<String,String> headers = Mockito.mock(Map.class);
		String uri = "/some/uri";
		String host = "localhost:8080";
		HttpScheme scheme = HttpScheme.HTTP;
		Object payload = Mockito.mock(Object.class);
		//ACT
		RestClientDto<Object> postDto = RestClientDto.builder().httpMethod(HttpMethod.POST)
				  .scheme(scheme)
				  .host(host)
				  .uri(uri)
				  .headers(headers)
				  .payload(payload)
				  .build();		
		//ASSERT
		assertNotNull(postDto);
		assertEquals(headers, postDto.getHeaders());
		assertEquals(uri, postDto.getUri());
		assertEquals(host, postDto.getHost());
		assertEquals(scheme, postDto.getScheme());
		assertEquals(payload, postDto.getPayload());
		assertEquals(HttpMethod.POST, postDto.getHttpMethod());
	}	
}
