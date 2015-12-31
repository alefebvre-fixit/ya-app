package com.ya.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.ya.model.group.Group;

public interface GroupRepository extends MongoRepository<Group, String> {

	public int countByUsername(String username);

	public Page<Group> findByUsername(String username, Pageable pageable);
	
	public List<Group> findByUsername(String username);

}