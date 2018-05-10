package io.github.asw.i3a.agents.service.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.bson.types.ObjectId;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.asw.i3a.agents.service.repositories.AgentsRepository;
import io.github.asw.i3a.agents.service.types.Agent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AgentsServiceImpl implements AgentsService {

	@Autowired
	private AgentsRepository repository;

	@Override
	public Agent getAgent( String identifier, String password, int kindOfAgent ) {
		Agent agent = repository.findById( identifier );

		if (agent != null
				&& new StrongPasswordEncryptor().checkPassword( password, agent.getPassword() )
				&& agent.getKindCode() == kindOfAgent) {
			log.info( "Returning information of: " + agent.toString() );
			return agent;
		}

		log.warn(
				"No information found for: " + identifier + ", " + password + ", " + kindOfAgent );
		return null;
	}

	@Override
	public void save( Agent agent ) {
		repository.save( agent );
	}

	@Override
	public void delete( Agent agent ) {
		repository.delete( agent );
	}

	@Override
	public List<Agent> getAllAgents() {
		return repository.findAll();
	}

	@Override
	public Agent getById( String id ) {
		try {
			return repository.findById( new ObjectId( id ) ).get();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	@Override
	public List<Agent> findByKindCode( int kindCode ) {
		return repository.findByKindCode( kindCode );
	}

}
