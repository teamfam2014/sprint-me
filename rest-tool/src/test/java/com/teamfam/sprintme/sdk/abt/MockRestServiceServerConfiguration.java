package com.teamfam.sprintme.sdk.abt;

import com.teamfam.sprintme.sdk.rest.client.RestClientSdkImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.MockRestServiceServer.MockRestServiceServerBuilder;
import org.springframework.web.client.RestTemplate;

/**
 * Test Configuration that requires to setting up the mock service server
 * and giving us control of it for our test. This is to avoid the issue that 
 * we face where we cannot wrap the handler in a buffer.
 * @author jbustamante
 *
 */
@Configuration
@Profile("test")
public class MockRestServiceServerConfiguration {
	
	/**
	 * Wrap the Mock Rest client factory in the buffered one.  
	 * @param restClientSdk The rest client SDK containing the rest template to use.
	 * @return The mock rest service server to use.
	 */
	@Bean
	public MockRestServiceServer mockRestServiceServer(RestClientSdkImpl restClientSdkImpl) {
		RestTemplate restTemplate = restClientSdkImpl.getRestTemplate();
		MockRestServiceServerBuilder builder = MockRestServiceServer.bindTo(restTemplate);
		MockRestServiceServer server = builder.bufferContent().build();
		return server;
	}
}
