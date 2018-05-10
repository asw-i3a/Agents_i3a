/*
 * This source file is part of the Agents_i3a open source project.
 *
 * Copyright (c) 2017 Agents_i3a project authors.
 * Licensed under MIT License.
 *
 * See /LICENSE for license information.
 * 
 * This class is based on the AlbUtil project.
 * 
 */
package io.github.asw.i3a.agents.service.services;

import java.io.IOException;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import TestKit.IntegrationTest;
import io.github.asw.i3a.agents.service.Service;
import io.github.asw.i3a.agents.service.services.AgentsService;
import io.github.asw.i3a.agents.service.types.Agent;

/**
 * Created by Nicolás on 15/02/2017.
 * 
 * Adapter by Víctor on 02/02/2018
 */
@SpringBootTest(classes = { Service.class })
@RunWith(SpringJUnit4ClassRunner.class)
@Category(IntegrationTest.class)
public class DatabaseTest {

	@Autowired
	private AgentsService service;

	// User to use as reference for test
	private Agent testedUser;
	private Agent testedUser2;

	/*
	 * Para este test se necesita el siguiente documento en la base de datos: {
	 * "_id" : ObjectId("5893a06ace8c8e1b79d8a9a9"), "_class" : "Model.User",
	 * "firstName" : "Maria", "location" : "10N30E", "password" :
	 * "9gvHm9TI57Z9ZW8/tTu9Nk10NDZayLIgKcFT8WdCVXPeY5gF57AFjS/l4nKNY1Jq",
	 * "dateOfBirth" : ISODate("1982-12-27T23:00:00.000Z"), "address" : "Hallo",
	 * "nationality" : "Core", "userId" : "321", "email" : "asd", "kind":
	 * "Person", kindCode:1 } Also a resources/master.csv file is needed with
	 * the following rows: 1; Person 2; Entity
	 */
	@Before
	public void setUp() throws IOException {
		testedUser = new Agent( "Luis", "48.2S35E", "LGracia@gmail.com", "Luis123", "147", 1 );
		service.save( testedUser );

		testedUser2 = new Agent( "Maria", "10N30E", "asd", "pass14753", "158", 1 );
		service.save( testedUser2 );
	}

	@After
	public void tearDown() {
		service.delete( testedUser );
		service.delete( testedUser2 );
	}

	@Test
	public void getAgentTest() {
		Agent agent = service.getAgent( testedUser.getId(), "Luis123", testedUser.getKindCode() );
		assertNotNull( agent );
		assertEquals( testedUser.getName(), agent.getName() );
	}

	@Test
	public void updatePasswordTest() {
		// It should be previously encoded if the DB is given so this may be
		// changed.
		Agent user = service.getAgent( testedUser.getId(), "Luis123", testedUser.getKindCode() );
		user.setPassword( "confidencial" );
		service.save( user );
		Agent userAfter = service.getAgent( testedUser.getId(), "confidencial",
				testedUser.getKindCode() );
		assertTrue( new StrongPasswordEncryptor().checkPassword( "confidencial",
				userAfter.getPassword() ) ); // They
		// should
		// be
		// the
		// same
		// when
		// we
		// introduce
		// the
		// password.
		Assert.assertEquals( user, userAfter ); // They should be the same user
												// by the equals.

	}

	@Test
	public void updateAgentDataTest() throws IOException {
		Agent user = service.getAgent( testedUser2.getId(), "pass14753", 1 );

		assertEquals( "Maria", user.getName() );
		assertEquals( "10N30E", user.getLocation() );
		assertEquals( "158", user.getId() );
		assertEquals( "asd", user.getEmail() );
		assertEquals( "Person", user.getKind() );
		assertEquals( 1, user.getKindCode() );

		assertEquals( user.getName(), user.getName() );
		assertEquals( user.getLocation(), user.getLocation() );
		assertEquals( user.getEmail(), user.getEmail() );
		assertEquals( user.getId(), user.getId() );
		assertEquals( "Person", user.getKind() );
		assertEquals( 1, user.getKindCode() );

		user.setName( "Pepa" );
		user.setLocation( "45N35.5W" );
		user.setKindCode( 2 );

		service.save( user );
		Agent updatedUser = service.getAgent( testedUser2.getId(), "pass14753", 2 );
		assertEquals( "Pepa", updatedUser.getName() );
		assertEquals( "45N35.5W", updatedUser.getLocation() );
		assertEquals( "158", updatedUser.getId() );
		assertEquals( "asd", updatedUser.getEmail() );
		assertEquals( "Entity", updatedUser.getKind() );
		assertEquals( 2, updatedUser.getKindCode() );

	}

}
