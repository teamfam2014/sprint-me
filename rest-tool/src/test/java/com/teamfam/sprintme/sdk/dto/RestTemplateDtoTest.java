package com.teamfam.sprintme.sdk.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Map;

import com.teamfam.sprintme.sdk.rest.constants.ErrorConstants;
import com.teamfam.sprintme.sdk.rest.constants.HttpScheme;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

/**
 * Unit test the behaviour of the rest template dto builder.
 * @author jbustamante
 *
 */
public class RestTemplateDtoTest {
	
	/**
	 * For a simple http get, we need to ensure that the http entity doesn't contain
	 * a payload, and it has the appropriate headers loaded, and it is building the URL 
	 * accordingly. 
	 */
	@Test
	public void simpleHttpGet() {
		//ARRANGE
		RestClientDto<?> restClientDto = Mockito.mock(RestClientDto.class);
		mockSimpleHttpGet(restClientDto);
		//ACT
		RestTemplateDto restTemplateDto = new RestTemplateDto.Builder(restClientDto)
															 .build();
		//ASSERT
		assertNotNull(restTemplateDto);
		assertNotNull(restTemplateDto.getHttpEntity());
		assertNotNull(restTemplateDto.getCompletedUri());
		HttpEntity<?> httpEntity = restTemplateDto.getHttpEntity();
		assertNotNull(httpEntity.getHeaders());
		assertFalse(httpEntity.getHeaders().isEmpty());
		assertNull(httpEntity.getBody());
	}
	
	/**
	 * If the host is not present in the rest client, then it is invalid.
	 */
	@Test
	public void invalidHost() {
		//ARRANGE
		RestClientDto<?> restClientDto = Mockito.mock(RestClientDto.class);
		mockSimpleHttpGet(restClientDto);
		Mockito.when(restClientDto.getHost())
		.thenReturn(null);		
		//ACT and ASSERT
		assertValidation(restClientDto,ErrorConstants.INVALID_HOST);
	}

	/**
	 * If the scheme is not present in the rest client, then it is invalid.
	 */
	@Test
	public void invalidScheme() {
		//ARRANGE
		RestClientDto<?> restClientDto = Mockito.mock(RestClientDto.class);
		mockSimpleHttpGet(restClientDto);
		Mockito.when(restClientDto.getScheme())
		.thenReturn(null);
		//ACT and ASSERT
		assertValidation(restClientDto,ErrorConstants.INVALID_SCHEME);
	}	
	
	/**
	 * If the URI is not present in the rest client, then it is invalid.
	 */
	@Test
	public void invalidUri() {
		//ARRANGE
		RestClientDto<?> restClientDto = Mockito.mock(RestClientDto.class);
		mockSimpleHttpGet(restClientDto);
		Mockito.when(restClientDto.getUri())
		.thenReturn(null);
		//ACT and ASSERT
		assertValidation(restClientDto,ErrorConstants.INVALID_URI);
	}
	
	/**
	 * If the headers are null in the rest client, then it is invalid.
	 */
	@Test
	public void nullHeaders() {
		//ARRANGE
		RestClientDto<?> restClientDto = Mockito.mock(RestClientDto.class);
		mockSimpleHttpGet(restClientDto);
		Mockito.when(restClientDto.getHeaders())
		.thenReturn(null);
		//ACT and ASSERT
		assertValidation(restClientDto,ErrorConstants.NULL_HEADERS);
	}	
	
	/**
	 * If the headers are empty in the rest client, then it is invalid.
	 */
	@Test
	public void emptyHeaders() {
		//ARRANGE
		RestClientDto<?> restClientDto = Mockito.mock(RestClientDto.class);
		mockSimpleHttpGet(restClientDto);
		Mockito.when(restClientDto.getHeaders())
		.thenReturn(new HashMap<String,String>());
		//ACT and ASSERT
		assertValidation(restClientDto,ErrorConstants.EMPTY_HEADERS);
	}	
	
	/**
	 * If the http method is not present in the rest client, then it is invalid.
	 */
	@Test
	public void invalidHttpMethod() {
		//ARRANGE
		RestClientDto<?> restClientDto = Mockito.mock(RestClientDto.class);
		mockSimpleHttpGet(restClientDto);
		Mockito.when(restClientDto.getHttpMethod())
		.thenReturn(null);
		//ACT and ASSERT
		assertValidation(restClientDto,ErrorConstants.INVALID_HTTP_METHOD);
	}	
	
	/**
	 * For a loaded http POST, we need to ensure that the http entity contains a payload,
	 * appropriate headers, and it builds the URL accordingly with query parameters, and 
	 * path parameters.
	 */
	
	@SuppressWarnings("unchecked")
	private void mockCommonRestTemplateBehaviour(RestClientDto<?> restClientDto) {
		Mockito.when(restClientDto.getHost())
		.thenReturn("localhost");
		Mockito.when(restClientDto.getScheme())
		.thenReturn(HttpScheme.HTTP);
		Mockito.when(restClientDto.getUri())
		.thenReturn("/dummy/uri");
		Map<String,String> simpleMockHeaders = stubSimpleHeaders();
		Mockito.when(restClientDto.getHeaders())
		.thenReturn(simpleMockHeaders);
	}
	
	private Map<String,String> stubSimpleHeaders(){
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("intuit_tid", "1234");
		return headers;
	}
	
	private void mockSimpleHttpGet(RestClientDto<?> restClientDto) {
		mockCommonRestTemplateBehaviour(restClientDto);
		Mockito.when(restClientDto.getHttpMethod())
		.thenReturn(HttpMethod.GET);
	}
	
	private void assertValidation(RestClientDto<?> restClientDto, String expectedMsg) {
		try {
			RestTemplateDto restTemplateDto = new RestTemplateDto.Builder(restClientDto)
				 .build();
			fail("No Exception Thrown");
		}catch(IllegalArgumentException iae) {
			//ASSERT - illegal argument exception thrown
			String message = iae.getMessage();
			assertEquals(expectedMsg,message);
		}catch(Throwable t) {
			fail("The expected exception is not thrown.");
		}		
	}
}
