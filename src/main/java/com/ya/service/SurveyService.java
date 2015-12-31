package com.ya.service;

import java.util.List;

import com.ya.model.survey.Survey;

public interface SurveyService {

	public List<Survey> getAll();

	public Survey save(Survey survey);

	public void delete(String id);

	public Survey getSurvey(String id);

}
