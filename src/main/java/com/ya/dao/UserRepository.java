package com.ya.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ya.model.user.YaUser;

public interface UserRepository extends MongoRepository<YaUser, String> {

	List<YaUser> findByEmail(String email);

	List<YaUser> findByUsername(String username);

	List<YaUser> findByUsernameIn(List<String> usernames);

}