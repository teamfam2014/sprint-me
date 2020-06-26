package com.teamfam.sprintme.sdk.rest.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Customize a standard Rest template that adds to the pre-existing spring rest
 * template within the spring context.
 * 
 * @author teamfam
 *
 */
@Component
public class DefaultRestTemplateCustomizer implements RestTemplateCustomizer {
	
	private LogClientHttpRequestInterceptor logClientHttpRequestInterceptor;
	
	@Autowired
	public DefaultRestTemplateCustomizer(LogClientHttpRequestInterceptor logClientHttpRequestInterceptor) {
		this.logClientHttpRequestInterceptor = logClientHttpRequestInterceptor;
	}
	
	@Override
	public void customize(RestTemplate restTemplate) {
		restTemplate.getInterceptors().add(logClientHttpRequestInterceptor);
		restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
	}

}