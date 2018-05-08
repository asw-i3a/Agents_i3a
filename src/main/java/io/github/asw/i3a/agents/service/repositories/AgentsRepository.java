package io.github.asw.i3a.agents.service.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import io.github.asw.i3a.agents.service.types.Agent;

public interface AgentsRepository extends MongoRepository<Agent, ObjectId> {

	/**
	 * Find a user by its email address.
	 * 
	 * @param id address of the user to look for.
	 * @return the user if exists, null otherwise.
	 */
	Agent findById( String id );

	List<Agent> findByKindCode( int kindCode );

}
