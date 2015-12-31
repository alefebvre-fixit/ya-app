package com.ya.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.ya.model.event.Event;

public interface EventRepository extends MongoRepository<Event, String> {

	public int countByUsername(String username);

	public Page<Event> findByUsername(String username, Pageable pageable);
	
	public List<Event> findByUsername(String username);
	
	public int countByGroupId(String groupId);
	
	public Page<Event> findByGroupId(String groupId, Pageable pageable);

	public List<Event> findByGroupId(String groupId);

	
}