package com.teamfam.sprintme.sdk.abt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.teamfam.sprintme.sdk.abt.bean.Account;
import com.teamfam.sprintme.sdk.dto.RestClientDto;
import com.teamfam.sprintme.sdk.rest.client.RestClientSdk;
import com.teamfam.sprintme.sdk.rest.constants.HttpScheme;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;


/**
 * Application behaviour test of the rest client SDK.
 * @author jbustamante
 *
 */
@SpringBootTest(classes=RestClientApp.class)
@ActiveProfiles("test")
public class RestClientApplicationBehaviourTest{

	private String classpath = RestClientApplicationBehaviourTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();

	@Autowired
	private RestClientSdk restClientSdk;
	
	@Autowired
	private MockRestServiceServer mockRestServiceServer;
	
	/**
	 * A simple Http Get that retrieves a JSON document from a rest server and 
	 * produces a plain old java object as a response.
	 */	
	@Test
	public void testPlainHttpGet() throws IOException{
		//ARRANGE
		RestClientDto<?> simpleGetRequest = simpleHttpGet();
		String fileName = "/stubs/account.json";
		String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/account/{id}").encode()
																						   .buildAndExpand(simplePathParameters())
																						   .toUriString();
		String simpleGetResponseStub = FileUtils.readFileToString(new File(classpath + "/" + fileName), "UTF-8");
		mockRestServiceServer.expect(once(), requestTo(url))
								   .andRespond(withSuccess(simpleGetResponseStub,MediaType.APPLICATION_JSON));
		//ACT
		Account account = restClientSdk.send(simpleGetRequest, Account.class);
		//ASSERT
		mockRestServiceServer.verify();		
		assertNotNull(account);
		assertNotNull(account.getAccountId());
		assertNotNull(account.getFirstName());
		assertNotNull(account.getLastName());
	}
	
	private RestClientDto<?> simpleHttpGet(){
		return RestClientDto.builder().headers(simpleHeaders())
											.host("localhost")
											.port(8080)
											.scheme(HttpScheme.HTTP)
											.httpMethod(HttpMethod.GET)
											.uri("/account/{id}")
											.pathParameters(simplePathParameters())
											.build();
	}
	
	private Map<String,String> simpleHeaders(){
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("tid", UUID.randomUUID().toString());
		headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}
	
	private MultiValueMap<String,String> simplePathParameters(){
		MultiValueMap<String,String> pathParameters = new LinkedMultiValueMap<String,String>();
		pathParameters.add("id", "1234567890");
		return pathParameters;
	}
}
