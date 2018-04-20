package org.uniovi.i3a.agents_service.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.uniovi.i3a.agents_service.services.AgentsService;
import org.uniovi.i3a.agents_service.types.Agent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class GetAgentsController {

    @Autowired
    private AgentsService service;

    @RequestMapping("/agents")
    public List<Agent> getAllAgents(@Nullable @RequestParam("kindCode") String kindCode) {
	List<Agent> result;

	if (kindCode == null) {
	    log.info("Geting all agents agents");
	    result = service.getAllAgents();
	} else {
	    log.info("Geting all agents agents filtered by kindCode: " + kindCode);
	    result = service.findByKindCode(Integer.parseInt(kindCode));
	}

	return result;
    }

    @RequestMapping(value = "/agents/{id}")
    public Agent getIncident(@PathVariable("id") String id) {
	log.info("Geting agent from controller with id: " + id);
	return service.getById(id);
    }

}
