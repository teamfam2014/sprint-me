package com.teamfam.sprintme.sdk.rest.components;

import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

/**
 * Intercept the request and response, and log the out going and incoming
 * payloads.
 * 
 * @author teamfam
 *
 */
@Component
public class LogClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
	
	private static final Logger LOG = LoggerFactory.getLogger(LogClientHttpRequestInterceptor.class);
	
	public static final String CHAR_ENCODING = "UTF-8";
	
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		String requestLog = buildRequestMessage(request,body);
		LOG.info(requestLog);
		ClientHttpResponse response = execution.execute(request, body);
		String responseLog = buildResponseMessage(response);
		LOG.info(responseLog);
		return response;
	}

	/**
	 * Build the request message from the HttpRequest and the body.
	 * @param request The request containing the HTTP Method, URI, and headers.
	 * @param body The outgoing payload. 
	 * @return A string containing the information required for logging prior to sending the 
	 * message out.
	 */
	private String buildRequestMessage(HttpRequest request, byte[] body) throws IOException{
		StringBuilder requestLog = new StringBuilder("Request Sent. uri=");
		requestLog.append(request.getURI().toString());
		requestLog.append(" | headers=");
		requestLog.append(request.getHeaders());
		requestLog.append(" | HttpMethod=");
		requestLog.append(request.getMethod().toString());
		requestLog.append(" | payload=");
		requestLog.append(new String(body,CHAR_ENCODING));
		return requestLog.toString();
	}	
	
	/**
	 * Build the response message to log 
	 * @param response The response containing the status and payload to lod
	 * @return The log message containing the string to log
	 * @throws IOException When there is an issue fetching the response.
	 */
	private String buildResponseMessage(ClientHttpResponse response) throws IOException{
		StringBuilder responseLog = new StringBuilder("Response Received. StatusCode=");
		responseLog.append(response.getRawStatusCode());
		responseLog.append(" | StatusText=");
		responseLog.append(response.getStatusText());
		responseLog.append(" | Headers=");
		responseLog.append(response.getHeaders());
		responseLog.append(" | responseBody=");
		responseLog.append(StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
		return responseLog.toString();
	}
}