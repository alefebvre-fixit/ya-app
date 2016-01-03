package com.ya.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ya.model.user.Credential;

public interface CredentialRepository extends
		MongoRepository<Credential, String> {

}