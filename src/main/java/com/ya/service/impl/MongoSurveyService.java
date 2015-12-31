package com.ya.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ya.dao.SurveyRepository;
import com.ya.model.survey.Survey;
import com.ya.service.NotificationService;
import com.ya.service.SurveyService;
import com.ya.util.Logger;

@Component
public class MongoSurveyService implements SurveyService {

	public static final String USER_NAME = "username";
	public static final String SURVEY_ID = "surveyId";
	public static final String EVENT_ID = "eventId";

	@Autowired
	SurveyRepository surveyRepository;

	@Autowired
	private NotificationService notificationService;

	@Override
	public void delete(String id) {
		Logger.debug("MongoEventService.delete(String id) id=" + id);
		surveyRepository.delete(id);
	}

	@Override
	public Survey save(Survey survey) {
		notificationService.publishNotification(survey);

		Survey result = surveyRepository.save(survey);
		return result;
	}

	@Override
	public Survey getSurvey(String id) {
		return surveyRepository.findOne(id);
	}

	@Override
	public List<Survey> getAll() {
		return surveyRepository.findAll();
	}

}
