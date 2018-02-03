package view_tests;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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

import dbmanagement.UsersRepository;
import domain.User;
import main.Application;
import services.ParticipantsService;

@SpringBootTest(classes = { Application.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class ParticipantsDataControllerTest {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ParticipantsService service;

	// MockMvc --> Para realizar peticiones y comprobar resultado, usado para
	// respuestas con informacion json.
	private MockMvc mockMvc;

	@Autowired
	private UsersRepository repo;

	private MockHttpSession session;

	private User maria;
	private String plainPassword;

	@Before
	public void setUp() throws Exception {

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();

		session = new MockHttpSession();

		// Setting up maria
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 1990);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		plainPassword = "pass14753";
		maria = new User("Maria", "10N30E", "asd", plainPassword, cal.getTime(), "Hallo", "Core", "158", 1);
		repo.insert(maria);
		
	}

	@After
	public void tearDown() {
		repo.delete(maria);
	}

	@Test
	public void userInsertInformation() throws Exception {
		String payload = String.format("{\"login\":\"%s\", \"password\":\"%s\"}", maria.getEmail(), plainPassword);
		// We send a POST request to that URI (from http:localhost...)
		MockHttpServletRequestBuilder request = post("/user").session(session).contentType(MediaType.APPLICATION_JSON)
				.content(payload.getBytes());
		mockMvc.perform(request).andDo(print())// AndDoPrint it is very usefull to see the http response and see if
												// something went wrong.
				.andExpect(status().isOk()) // The state of the response must be OK. (200);
				.andExpect(jsonPath("$.name", is(maria.getName()))) // We can do jsonpaths in order to check																							// 1996
				.andExpect(jsonPath("$.id", is(maria.getUserId())))
				.andExpect(jsonPath("$.location", is(maria.getLocation())))
				.andExpect(jsonPath("$.email", is(maria.getEmail())))
				.andExpect(jsonPath("$.kind", is(maria.getKind())))
				.andExpect(jsonPath("$.kindCode", is(maria.getKindCode())));
	}

	@Test
	public void userInsertInformationXML() throws Exception {
		String payload = String.format("<data><login>%s</login><password>%s</password></data>", maria.getEmail(),
				plainPassword);
		// POST request with XML content
		MockHttpServletRequestBuilder request = post("/user").session(session)
				.contentType(MediaType.APPLICATION_XML_VALUE).content(payload.getBytes());
		mockMvc.perform(request).andDo(print())// AndDoPrint it is very usefull to see the http response and see if
												// something went wrong.
				.andExpect(status().isOk()) // The state of the response must be OK. (200);
				.andExpect(jsonPath("$.name", is(maria.getName()))) // We can do jsonpaths in order to check
				.andExpect(jsonPath("$.id", is(maria.getUserId())))
				.andExpect(jsonPath("$.location", is(maria.getLocation())))
				.andExpect(jsonPath("$.email", is(maria.getEmail())))
				.andExpect(jsonPath("$.kind", is(maria.getKind())))
				.andExpect(jsonPath("$.kindCode", is(maria.getKindCode())));
	}

	@Test
	public void userInterfaceInsertInfoCorect() throws Exception {
		MockHttpServletRequestBuilder request = post("/userForm").session(session).param("login", maria.getEmail())
				.param("password", plainPassword);
		mockMvc.perform(request).andExpect(status().isOk());
	}

	@Test
	public void testForNotFound() throws Exception {
		String payload = String.format("{\"login\":\"%s\", \"password\":\"%s\"}", "Nothing", "Not really");
		MockHttpServletRequestBuilder request = post("/user").session(session).contentType(MediaType.APPLICATION_JSON)
				.content(payload.getBytes());
		mockMvc.perform(request).andDo(print())// AndDoPrint it is very usefull to see the http response and see if
												// something went wrong.
				.andExpect(status().isNotFound()); // The state of the response must be OK. (200);
	}

	/**
	 * Should return a 404 as before
	 */
	@Test
	public void testForIncorrectPassword() throws Exception {
		String payload = String.format("{\"login\":\"%s\", \"password\":\"%s\"}", maria.getEmail(),
				"Not maria's password");
		MockHttpServletRequestBuilder request = post("/user").session(session).contentType(MediaType.APPLICATION_JSON)
				.content(payload.getBytes());
		mockMvc.perform(request).andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	public void testChangePassword() throws Exception {
		MockHttpSession session = new MockHttpSession();
		// We check we have the proper credentials
		MockHttpServletRequestBuilder request = post("/userForm").session(session).param("login", maria.getEmail())
				.param("password", plainPassword);
		mockMvc.perform(request).andExpect(status().isOk());
		// We change it
		request = post("/userChangePassword").session(session).param("password", plainPassword)
				.param("newPassword", "HOLA").param("newPasswordConfirm", "HOLA");
		mockMvc.perform(request).andExpect(status().isOk());

		String payload = String.format("{\"login\":\"%s\", \"password\":\"%s\"}", maria.getEmail(), "HOLA");
		// We check password has changed
		request = post("/user").session(session).contentType(MediaType.APPLICATION_JSON).content(payload.getBytes());
		mockMvc.perform(request).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(maria.getName())))
				.andExpect(jsonPath("$.location", is(maria.getLocation())))
				.andExpect(jsonPath("$.id", is(maria.getUserId())))
				.andExpect(jsonPath("$.location", is(maria.getLocation())))
				.andExpect(jsonPath("$.email", is(maria.getEmail())))
				.andExpect(jsonPath("$.kind", is(maria.getKind())))
				.andExpect(jsonPath("$.kindCode", is(maria.getKindCode())));
		
		
		
	}

}
