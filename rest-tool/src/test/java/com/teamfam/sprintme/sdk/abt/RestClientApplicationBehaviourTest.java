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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
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
	
	@DisplayName("Simple Http Get")
	@Test
	public void testPlainHttpGet() throws IOException{
		validateRestOperation(simpleHttpGet(),"/stubs/account.json","http://localhost:8080/account/{id}");
	}
	
	@DisplayName("Loaded Http Get")
	@Test
	public void testLoadedHttpGet() throws IOException{
		validateRestOperation(loadedHttpGet(),"/stubs/account.json","http://localhost:8080/account/{id}?paid=true");
	}

	@DisplayName("Simple Http Post")
	@Test
	public void testSimpleHttpPost() throws IOException{
		validateRestOperation(simpleHttpPost(),"/stubs/account.json","http://localhost:8080/account/{id}");
	}

	@DisplayName("Loaded Http Post")
	@Test
	public void testLoadedHttpPost() throws IOException{
		validateRestOperation(loadedHttpPost(),"/stubs/account.json","http://localhost:8080/account/{id}?paid=true");
	}	

	

	private void validateRestOperation(RestClientDto<?> request,String fileName,String httpUrlString) throws IOException{
		//ARRANGE
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpUrlString).encode();
		String url = null;
		if (request.getPathParameters() != null && !request.getPathParameters().isEmpty()){
			url = builder.buildAndExpand(simplePathParameters())
						 .toUriString();
		}else{
			url = builder.build()
						 .toUriString();
		}
		String simpleResponseStub = FileUtils.readFileToString(new File(classpath + "/" + fileName), "UTF-8");
		if (request.getPayload() != null){
			mockRestServiceServer.expect(once(), requestTo(url))
								.andExpect(MockRestRequestMatchers.content().string(request.getPayload().toString()))
								.andRespond(withSuccess(simpleResponseStub,MediaType.APPLICATION_JSON));
		}else{
			mockRestServiceServer.expect(once(), requestTo(url))
								.andRespond(withSuccess(simpleResponseStub,MediaType.APPLICATION_JSON));
		}
		//ACT
		Account account = restClientSdk.send(request, Account.class);
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
	
	private RestClientDto<?> loadedHttpGet(){
		return RestClientDto.builder().headers(simpleHeaders())
											.host("localhost")
											.port(8080)
											.scheme(HttpScheme.HTTP)
											.httpMethod(HttpMethod.GET)
											.uri("/account/{id}")
											.pathParameters(simplePathParameters())
											.queryParameters(simpleQueryParameters())
											.build();
	}

	private RestClientDto<?> simpleHttpPost() throws IOException{
		return RestClientDto.builder().headers(simpleHeaders())
											.host("localhost")
											.port(8080)
											.scheme(HttpScheme.HTTP)
											.httpMethod(HttpMethod.POST)
											.uri("/account/{id}")
											.pathParameters(simplePathParameters())
											.payload(simplePayload())
											.build();
	}
	
	private RestClientDto<?> loadedHttpPost() throws IOException{
		return RestClientDto.builder().headers(simpleHeaders())
											.host("localhost")
											.port(8080)
											.scheme(HttpScheme.HTTP)
											.httpMethod(HttpMethod.POST)
											.uri("/account/{id}")
											.pathParameters(simplePathParameters())
											.queryParameters(simpleQueryParameters())
											.payload(simplePayload())
											.build();
	}

	private String simplePayload() throws IOException{
		String payload = FileUtils.readFileToString(new File(classpath + "/" + "/stubs/account.json"), "UTF-8");
		return payload;
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

	private MultiValueMap<String,String> simpleQueryParameters(){
		MultiValueMap<String,String> queryParameters = new LinkedMultiValueMap<String,String>();
		queryParameters.add("paid", Boolean.TRUE.toString());
		return queryParameters;
	}
}
