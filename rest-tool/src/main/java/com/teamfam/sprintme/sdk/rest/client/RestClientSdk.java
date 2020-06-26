package com.teamfam.sprintme.sdk.rest.client;

import com.teamfam.sprintme.sdk.dto.RestClientDto;

import org.springframework.web.client.RestTemplate;

/**
 * Rest Client that will send the request, the headers, url and query
 * parameters, and http method that we will be sending across the wire.
 * 
 * @author teamfam
 *
 */
public interface RestClientSdk {
	
	/**
	 * Send a payload to a destination via an HttpMethod (GET, POST etc.) provided
	 * in the rest client DTO.  
	 * @param restClientDto The rest client DTO containing the necessary restful metadata
	 * to send the http request.
	 * @param returnType The type to return. It can be Void.class if there is no return type 
	 * required. 
	 * @return A response based on the operation that is being performed. If the user is performing
	 * a put operation, then the generic Void type can be returned to indicate that the operation doesn't
	 * expect a response.
	 */
	public <U,T> U send(RestClientDto<T> restClientDto, Class<U> returnType);
	
	/**
	 * Send a payload to a destination via an HttpMethod (GET, POST etc.) provided
	 * in the rest client DTO.  
	 * @param restClientDto The rest client DTO containing the necessary restful metadata
	 * to send the http request.
	 * @param returnType The type to return. It can be Void.class if there is no return type 
	 * required.
	 * @param restTemplate The rest template to execute the rest operation. 
	 * @return A response based on the operation that is being performed. If the user is performing
	 * a put operation, then the generic Void type can be returned to indicate that the operation doesn't
	 * expect a response.
	 */
	public <U,T> U send(RestClientDto<T> restClientDto, Class<U> returnType, RestTemplate restTemplate);
}