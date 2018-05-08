package io.github.asw.i3a.agents.service.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import TestKit.IntegrationTest;
import io.github.asw.i3a.agents.service.Service;
import io.github.asw.i3a.agents.service.services.AgentsService;
import io.github.asw.i3a.agents.service.types.Agent;

@SpringBootTest(classes = { Service.class })
@RunWith(SpringJUnit4ClassRunner.class)
@Category(IntegrationTest.class)
public class RegisterTest {

	// {"name":"colunga91","location":"10N99W", "email":"colunga91@gmail.com",
	// "password":"123456","username":"47170929X","kindCode":1}

	@Autowired
	private WebApplicationContext context;
	private static final String QUERY_STRING = "{\"name\":\"%s\", \"location\":\"%s\", \"email\":\"%s\", \"password\":\"%s\", \"username\":\"%s\", \"kindCode\":%s}";
	private static final String NO_NAME_QUERY_STRING = "{\"location\":\"%s\", \"email\":\"%s\", \"password\":\"%s\", \"username\":\"%s\", \"kindCode\":%s}";
	private static final String NO_LOCATION_QUERY_STRING = "{\"name\":\"%s\", \"email\":\"%s\", \"password\":\"%s\", \"username\":\"%s\", \"kindCode\":%s}";
	private static final String NO_EMAIL_QUERY_STRING = "{\"name\":\"%s\", \"location\":\"%s\", \"password\":\"%s\", \"username\":\"%s\", \"kindCode\":%s}";
	private static final String NO_PASSWORD_QUERY_STRING = "{\"name\":\"%s\", \"location\":\"%s\", \"email\":\"%s\", \"username\":\"%s\", \"kindCode\":%s}";
	private static final String NO_USERNAME_QUERY_STRING = "{\"name\":\"%s\", \"location\":\"%s\", \"email\":\"%s\", \"password\":\"%s\", \"kindCode\":%s}";
	private static final String NO_KINDCODE_QUERY_STRING = "{\"name\":\"%s\", \"location\":\"%s\", \"email\":\"%s\", \"password\":\"%s\", \"username\":\"%s\"}";
	private MockMvc mockMvc;

	@Autowired
	private AgentsService service;
	private MockHttpSession session;
	private Agent maria;
	private String plainPassword;

	@Before
	public void setUp() throws Exception {

		this.mockMvc = MockMvcBuilders.webAppContextSetup( this.context ).build();

		session = new MockHttpSession();

		// Setting up maria
		plainPassword = "pass14753";
		maria = new Agent( "Maria", "10N30E", "asd", plainPassword, "158", 1 );
	}

	@Test
	public void successfulInsertTest() throws Exception {
		String payload = String.format( QUERY_STRING, maria.getName(), maria.getLocation(),
				maria.getEmail(), plainPassword, maria.getId(), maria.getKindCode() );

		// We send a POST request to that URI (from http:localhost...)
		MockHttpServletRequestBuilder request = post( "/register" ).session( session )
				.contentType( MediaType.APPLICATION_JSON ).content( payload.getBytes() );

		mockMvc.perform( request ).andDo( print() )
				// The state of the response must be CREATED. (201);
				.andExpect( status().isCreated() );

		maria = service.getAgent( maria.getId(), plainPassword, maria.getKindCode() );
		service.delete( maria );
	}

	@Test
	public void noNameUnSuccessfulInsertTest() throws Exception {
		String payload = String.format( NO_NAME_QUERY_STRING, maria.getLocation(),
				maria.getEmail(), plainPassword, maria.getId(), maria.getKindCode() );

		// We send a POST request to that URI (from http:localhost...)
		MockHttpServletRequestBuilder request = post( "/register" ).session( session )
				.contentType( MediaType.APPLICATION_JSON ).content( payload.getBytes() );

		mockMvc.perform( request ).andDo( print() )
				// The state of the response must be CREATED. (201);
				.andExpect( status().isBadRequest() );
	}

	@Test
	public void noLocationUnSuccessfulInsertTest() throws Exception {
		String payload = String.format( NO_LOCATION_QUERY_STRING, maria.getName(),
				maria.getEmail(), plainPassword, maria.getId(), maria.getKindCode() );

		// We send a POST request to that URI (from http:localhost...)
		MockHttpServletRequestBuilder request = post( "/register" ).session( session )
				.contentType( MediaType.APPLICATION_JSON ).content( payload.getBytes() );

		mockMvc.perform( request ).andDo( print() )
				// The state of the response must be CREATED. (201);
				.andExpect( status().isBadRequest() );

	}
	
	@Test
	public void noEmailUnSuccessfulInsertTest() throws Exception {
		String payload = String.format( NO_EMAIL_QUERY_STRING, maria.getName(), maria.getLocation(),
				plainPassword, maria.getId(), maria.getKindCode() );

		// We send a POST request to that URI (from http:localhost...)
		MockHttpServletRequestBuilder request = post( "/register" ).session( session )
				.contentType( MediaType.APPLICATION_JSON ).content( payload.getBytes() );

		mockMvc.perform( request ).andDo( print() )
				// The state of the response must be CREATED. (201);
				.andExpect( status().isBadRequest() );
	}
	
	@Test
	public void noPasswordUnSuccessfulInsertTest() throws Exception {
		String payload = String.format( NO_PASSWORD_QUERY_STRING, maria.getName(), maria.getLocation(),
				maria.getEmail(),
				plainPassword, maria.getId(), maria.getKindCode() );

		// We send a POST request to that URI (from http:localhost...)
		MockHttpServletRequestBuilder request = post( "/register" ).session( session )
				.contentType( MediaType.APPLICATION_JSON ).content( payload.getBytes() );

		mockMvc.perform( request ).andDo( print() )
				// The state of the response must be CREATED. (201);
				.andExpect( status().isBadRequest() );
	}
	
	@Test
	public void noUsernameUnSuccessfulInsertTest() throws Exception {
		String payload = String.format( NO_USERNAME_QUERY_STRING, maria.getName(), maria.getLocation(),
				maria.getEmail(),
				plainPassword, maria.getId(), maria.getKindCode() );

		// We send a POST request to that URI (from http:localhost...)
		MockHttpServletRequestBuilder request = post( "/register" ).session( session )
				.contentType( MediaType.APPLICATION_JSON ).content( payload.getBytes() );

		mockMvc.perform( request ).andDo( print() )
				// The state of the response must be CREATED. (201);
				.andExpect( status().isBadRequest() );
	}
	
	@Test
	public void noKindCodeUnSuccessfulInsertTest() throws Exception {
		String payload = String.format( NO_KINDCODE_QUERY_STRING, maria.getName(), maria.getLocation(),
				maria.getEmail(),
				plainPassword, maria.getId(), maria.getKindCode() );

		// We send a POST request to that URI (from http:localhost...)
		MockHttpServletRequestBuilder request = post( "/register" ).session( session )
				.contentType( MediaType.APPLICATION_JSON ).content( payload.getBytes() );

		mockMvc.perform( request ).andDo( print() )
				// The state of the response must be CREATED. (201);
				.andExpect( status().isBadRequest() );
	}
}
