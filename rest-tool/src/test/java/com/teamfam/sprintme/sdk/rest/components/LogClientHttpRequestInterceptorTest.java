package com.teamfam.sprintme.sdk.rest.components;

import java.io.ByteArrayInputStream;
import java.net.URI;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Unit tests for the logging interceptor.
 * 
 * @author jbustamante
 *
 */
@ExtendWith(MockitoExtension.class)
public class LogClientHttpRequestInterceptorTest {
	@InjectMocks
	private LogClientHttpRequestInterceptor logClientHttpRequestInterceptor;
	
	/**
	 * Verify that the request and response are properly logged.
	 */
    @DisplayName("Request and Response Logged")
	@Test
	public void requestAndResponseLogged() throws Exception{
		//ARRANGE
		HttpRequest request = Mockito.mock(HttpRequest.class,Mockito.RETURNS_DEEP_STUBS);
		byte[] body = new String("This is a test.").getBytes();
		ClientHttpResponse response = Mockito.mock(ClientHttpResponse.class,Mockito.RETURNS_DEEP_STUBS);
		ClientHttpRequestExecution execution = Mockito.mock(ClientHttpRequestExecution.class);
		Mockito.when(execution.execute(request, body))
		.thenReturn(response);
		Mockito.when(request.getURI())
		.thenReturn(new URI("http://localhost:8080"));
		Mockito.when(request.getMethod())
		.thenReturn(HttpMethod.GET);
		Mockito.when(response.getBody())
		.thenReturn(new ByteArrayInputStream(body));
		//ACT
		ClientHttpResponse actualResponse = logClientHttpRequestInterceptor.intercept(request, body, execution);
		//ASSERT
		Assertions.assertEquals(response, actualResponse,"The response is not as expected.");
		Mockito.verify(request,Mockito.times(1)).getURI();
		Mockito.verify(request,Mockito.times(1)).getHeaders();
		Mockito.verify(request,Mockito.times(1)).getMethod();
		Mockito.verify(response,Mockito.times(1)).getRawStatusCode();
		Mockito.verify(response,Mockito.times(1)).getStatusText();
		Mockito.verify(response,Mockito.times(1)).getHeaders();
		Mockito.verify(response,Mockito.times(1)).getBody();
	}
}