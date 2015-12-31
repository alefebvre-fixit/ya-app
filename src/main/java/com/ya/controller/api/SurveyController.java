package com.ya.controller.api;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ya.exception.EntityAuthorizationException;
import com.ya.exception.EntityNotFoundException;
import com.ya.model.survey.Survey;
import com.ya.util.Logger;

@RestController
public class SurveyController extends YaController {

	@RequestMapping(value = "/api/surveys/{surveyId}", method = RequestMethod.PUT)
	public Survey update(@PathVariable String surveyId,
			@RequestBody Survey survey) {
		Logger.debug("SurveyAPIController.update()");

		Survey original = getSurveyService().getSurvey(surveyId);

		if (original == null)
			throw new EntityNotFoundException(Survey.class.getSimpleName(), surveyId);

		if (!original.canUpdate(getUserName()))
			throw new EntityAuthorizationException(Survey.class.getSimpleName(), surveyId);

		survey.setUsername(getUserName());
		survey.setId(surveyId);
		return getSurveyService().save(survey);

	}

	@RequestMapping(value = "/api/survey", method = RequestMethod.POST)
	public Survey create(@RequestBody Survey survey) {
		Logger.debug("SurveyAPIController.save()");

		survey.setUsername(getUserName());
		survey.setId(null);

		return getSurveyService().save(survey);

	}

	@RequestMapping("/api/surveys/{surveyId}")
	public Survey survey(@PathVariable String surveyId) {
		Logger.debug("SurveyAPIController.survey surveyId =" + surveyId);
		return getSurveyService().getSurvey(surveyId);
	}

	@RequestMapping(value = "/api/surveys/{surveyId}", method = RequestMethod.DELETE)
	public void deleteSurvey(String surveyId) {

		Survey original = getSurveyService().getSurvey(surveyId);
		
		if (original == null)
			throw new EntityNotFoundException(Survey.class.getSimpleName(), surveyId);

		if (!original.canUpdate(getUserName()))
			throw new EntityAuthorizationException(Survey.class.getSimpleName(), surveyId);

		getSurveyService().delete(surveyId);
	}

	@RequestMapping("/api/surveys")
	public List<Survey> surveys() {
		Logger.debug("SurveyAPIController.surveys()");

		return getSurveyService().getAll();
	}
	
	
}
