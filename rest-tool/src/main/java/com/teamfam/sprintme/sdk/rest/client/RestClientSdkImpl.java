package com.teamfam.sprintme.sdk.rest.client;

import com.teamfam.sprintme.sdk.dto.RestClientDto;
import com.teamfam.sprintme.sdk.dto.RestTemplateDto;
import com.teamfam.sprintme.sdk.rest.constants.ErrorConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;

@Component
@Getter
public class RestClientSdkImpl implements RestClientSdk {
	
	private RestTemplate restTemplate;
	
	@Autowired
	public RestClientSdkImpl(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}
	
	@Override
	public <U, T> U send(RestClientDto<T> restClientDto, Class<U> returnType) {
		return send(restClientDto, returnType, restTemplate);
	}
	
	@Override
	public <U, T> U send(RestClientDto<T> restClientDto, Class<U> returnType, RestTemplate restTemplate) {
		Assert.notNull(restTemplate, ErrorConstants.REST_TEMPLATE_EMPTY);
		Assert.notNull(restClientDto, ErrorConstants.REST_CLIENT_DTO_EMPTY);
		RestTemplateDto restTemplateDto = new RestTemplateDto.Builder<T>(restClientDto).build();
		ResponseEntity<U> responseEntity = restTemplate.exchange(restTemplateDto.getCompletedUri(), restClientDto.getHttpMethod(),
                restTemplateDto.getHttpEntity(), returnType);
		return responseEntity.getBody();
	}
}