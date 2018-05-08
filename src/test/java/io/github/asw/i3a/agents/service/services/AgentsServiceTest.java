package io.github.asw.i3a.agents.service.services;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import TestKit.IntegrationTest;
import io.github.asw.i3a.agents.service.Service;
import io.github.asw.i3a.agents.service.repositories.AgentsRepository;
import io.github.asw.i3a.agents.service.services.AgentsService;
import io.github.asw.i3a.agents.service.types.Agent;

@SpringBootTest(classes = { Service.class })
@RunWith(SpringJUnit4ClassRunner.class)
@Category(IntegrationTest.class)
public class AgentsServiceTest {

	@Autowired
	private AgentsService service;

	@Autowired
	private AgentsRepository repository;

	private Agent mockAgent;

	@Before
	public void setUp() {
		mockAgent = new Agent( "Pepe", "10N10W", "pepe@pepe.pepe", "1234", "pepe", 2 );
		mockAgent.setKindCode( 2 );
		repository.save( mockAgent );
	}

	@After
	public void tearDown() {
		repository.delete( mockAgent );
	}

	@Test
	public void getAllAgentsTest() {
		assertNotNull( service.getAllAgents() );
		assertTrue( service.getAllAgents().contains( mockAgent ) );
	}

	@Test
	public void getByIdTest() {
		assertNotNull( mockAgent.getAgentId() );
		assertNotNull( service.getById( mockAgent.getAgentId() ) );
		assertEquals( mockAgent, service.getById( mockAgent.getAgentId() ) );
		Agent mockAgent2 = new Agent( "Pepe", "10N10W", "pepe@pepe.pepe", "1234", "pepe", 2 );
		mockAgent2.setKindCode( 2 );
		repository.save( mockAgent2 );
		repository.delete( mockAgent2 );
		assertNull( service.getById( mockAgent2.getAgentId() ) );
	}

	@Test
	public void findByKindCodeTest() {
		assertNotNull( service.findByKindCode( mockAgent.getKindCode() ) );
		assertTrue( service.findByKindCode( mockAgent.getKindCode() ).contains( mockAgent ) );
	}

}
