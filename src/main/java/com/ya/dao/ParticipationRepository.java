package com.ya.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.ya.model.event.Event;
import com.ya.model.event.Participation;

public interface ParticipationRepository extends MongoRepository<Participation, String> {

	public int countByUsername(String username);

	public Page<Event> findByUsername(String username, Pageable pageable);
		
	public Page<Participation> findByEventId(String eventId, Pageable pageable);
	
	public List<Participation> findByEventIdAndUsername(String eventId, String username);

	public List<Participation> findByUsername(String username);
	
	public List<Participation> findByEventId(String eventId);

	public int countByEventIdAndStatus(String eventId, String status);

	public List<Participation> deleteByEventId(String eventId);
	
}