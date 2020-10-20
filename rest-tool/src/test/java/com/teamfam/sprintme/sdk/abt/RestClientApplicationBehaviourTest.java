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
		validateRestOperation(getRestClientDto(HttpScheme.HTTP,HttpMethod.GET,RestClientDtoType.SIMPLE),"/stubs/account.json","http://localhost:8080/account/{id}");
	}
	
	@DisplayName("Loaded Http Get")
	@Test
	public void testLoadedHttpGet() throws IOException{
		validateRestOperation(getRestClientDto(HttpScheme.HTTP,HttpMethod.GET,RestClientDtoType.ALL),"/stubs/account.json","http://localhost:8080/account/{id}?paid=true");
	}

	@DisplayName("Simple Http Post")
	@Test
	public void testSimpleHttpPost() throws IOException{
		validateRestOperation(getRestClientDto(HttpScheme.HTTP,HttpMethod.POST,RestClientDtoType.SIMPLE),"/stubs/account.json","http://localhost:8080/account/{id}");
	}

	@DisplayName("Loaded Http Post")
	@Test
	public void testLoadedHttpPost() throws IOException{
		validateRestOperation(getRestClientDto(HttpScheme.HTTP,HttpMethod.POST,RestClientDtoType.ALL),"/stubs/account.json","http://localhost:8080/account/{id}?paid=true");
	}	

	/**@DisplayName("Simple Http Put")
	@Test
	public void testSimpleHttpPut(){
		
	}

	@DisplayName("Loaded Http Put")
	@Test
	public void testLoadedHttpPut(){
		
	}

	@DisplayName("Simple Http Delete")
	@Test
	public void testSimpleHttpDelete(){
		
	}

	@DisplayName("Loaded Http Delete")
	@Test
	public void testLoadedHttpDelete(){
		
	}*/

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

	private RestClientDto<?> getRestClientDto(HttpScheme httpScheme, HttpMethod httpMethod, RestClientDtoType restClientDtoType) throws IOException{
		RestClientDto.RestClientDtoBuilder<Object> builder = restClientDtoType.getRestClientDto(httpScheme,httpMethod);
		switch(httpMethod){
			case GET:
				break;
			default:
				builder.payload(simplePayload());
				break;
		}
		return builder.build();
	}

	private String simplePayload() throws IOException{
		String payload = FileUtils.readFileToString(new File(classpath + "/" + "/stubs/account.json"), "UTF-8");
		return payload;
	}

	private MultiValueMap<String,String> simplePathParameters(){
		MultiValueMap<String,String> pathParameters = new LinkedMultiValueMap<String,String>();
		pathParameters.add("id", "1234567890");
		return pathParameters;
	}

	private enum RestClientDtoType{
		SIMPLE,
		ALL{
			@Override
			public RestClientDto.RestClientDtoBuilder<Object> getRestClientDto(HttpScheme httpScheme, HttpMethod httpMethod) throws IOException{
				RestClientDto.RestClientDtoBuilder<Object> fullRestClientDto = super.getRestClientDto(httpScheme,httpMethod);
				fullRestClientDto.queryParameters(simpleQueryParameters());
				return fullRestClientDto;
			}
		};

		public RestClientDto.RestClientDtoBuilder<Object> getRestClientDto(HttpScheme httpScheme, HttpMethod httpMethod) throws IOException{
			return RestClientDto.builder().headers(simpleHeaders())
			.host("localhost")
			.port(8080)
			.scheme(httpScheme)
			.httpMethod(httpMethod)
			.uri("/account/{id}")
			.pathParameters(simplePathParameters());
		}

		public MultiValueMap<String,String> simplePathParameters(){
			MultiValueMap<String,String> pathParameters = new LinkedMultiValueMap<String,String>();
			pathParameters.add("id", "1234567890");
			return pathParameters;
		}
	
		public MultiValueMap<String,String> simpleQueryParameters(){
			MultiValueMap<String,String> queryParameters = new LinkedMultiValueMap<String,String>();
			queryParameters.add("paid", Boolean.TRUE.toString());
			return queryParameters;
		}

		private Map<String,String> simpleHeaders(){
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("tid", UUID.randomUUID().toString());
			headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			return headers;
		}
	}
}
