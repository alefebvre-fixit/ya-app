package com.ya.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ya.model.survey.Survey;

public interface SurveyRepository extends MongoRepository<Survey, String> {

}