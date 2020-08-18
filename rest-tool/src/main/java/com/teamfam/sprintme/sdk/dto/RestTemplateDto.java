package com.teamfam.sprintme.sdk.dto;

import java.net.URI;
import java.util.Map;

import com.teamfam.sprintme.sdk.rest.constants.ErrorConstants;
import com.teamfam.sprintme.sdk.rest.constants.HttpScheme;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * A Data transfer object designed to prepare the http request data
 * for transfer via the Spring Rest template.
 *  
 * @author teamfam
 *
 */
public class RestTemplateDto {
	private HttpEntity<?> httpEntity;
	private URI completedUri;
	
	private RestTemplateDto(HttpEntity<?> httpEntity, URI completedUri) {
		this.httpEntity = httpEntity;
		this.completedUri = completedUri;
	}

	public HttpEntity<?> getHttpEntity() {
		return httpEntity;
	}

	public URI getCompletedUri() {
		return completedUri;
	}

	/**
	 * Rest Template builder DTO that will construct the rest template dto with
	 * the appropriate http entity populated with the HttpHeaders as well as the URI
	 * populated with the appropriate query and path parameters.
	 * 
	 * @author teamfam
	 *
	 */
	public static class Builder<T>{
		private RestClientDto<T> restClientDto;
		
		public Builder(RestClientDto<T> restClientDto) {
			this.restClientDto = restClientDto;
		}
		
		public RestTemplateDto build() {
			validateMinimalRestClientDto();
			HttpEntity<?> httpEntity = buildHttpEntity(restClientDto.getPayload(),restClientDto.getHeaders());
			URI completedUri = completeUri(restClientDto.getHost(),restClientDto.getPort(),restClientDto.getScheme(),restClientDto.getUri(),restClientDto.getPathParameters(),restClientDto.getQueryParameters());
			RestTemplateDto restTemplateDto = new RestTemplateDto(httpEntity,completedUri);
			return restTemplateDto;
		}

		private URI completeUri(String host, int port, HttpScheme scheme, String path, MultiValueMap<String, String> pathParameters, MultiValueMap<String, String> queryParameters) {
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().host(host)
																			.port(port)
																			.scheme(scheme.toString().toLowerCase())
																			.path(path)
																			.queryParams(queryParameters);
			if (pathParameters != null && !pathParameters.isEmpty()){
				return builder.buildAndExpand(pathParameters)
							  .toUri();
			}else{
				return builder.build()
							  .toUri();
			}
		}

		private HttpEntity<?> buildHttpEntity(T payload, Map<String, String> headers) {
			HttpHeaders httpHeaders = mapHttpHeaders(headers);
			//If the payload is null then the response type is unknown
			if (payload == null) {
				HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders); 
				return httpEntity;
			}else {
				HttpEntity<T> httpEntity = new HttpEntity<T>(payload,httpHeaders); 
				return httpEntity;
			}
		}

		private HttpHeaders mapHttpHeaders(Map<String, String> headers) {
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setAll(headers);
			return httpHeaders;
		}
		
		//Validate that the minimmal rest client attributes are populated.
		private void validateMinimalRestClientDto() {
			Assert.isTrue(StringUtils.isNotBlank(restClientDto.getHost()),ErrorConstants.INVALID_HOST);
			Assert.notNull(restClientDto.getScheme(),ErrorConstants.INVALID_SCHEME);
			Assert.isTrue(StringUtils.isNotBlank(restClientDto.getUri()),ErrorConstants.INVALID_URI);			
			Assert.notNull(restClientDto.getHeaders(),ErrorConstants.NULL_HEADERS);
			Assert.isTrue(!restClientDto.getHeaders().isEmpty(),ErrorConstants.EMPTY_HEADERS);
			Assert.notNull(restClientDto.getHttpMethod(),ErrorConstants.INVALID_HTTP_METHOD);
		}
	}
}