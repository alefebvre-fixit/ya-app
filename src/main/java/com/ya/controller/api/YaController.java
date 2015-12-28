package com.ya.controller.api;

import org.springframework.beans.factory.annotation.Autowired;

import com.ya.model.user.YaUser;
import com.ya.service.EventService;
import com.ya.service.GroupService;
import com.ya.service.NotificationService;
import com.ya.service.SurveyService;
import com.ya.service.UserService;

public class YaController {

	public static final String SESSION_ATTRIBUTE_USERNAME = "username";
	public static final String SESSION_ATTRIBUTE_ACCESS_TOKEN = "access_token";

	@Autowired
	private GroupService groupService;

	@Autowired
	private UserService userService;

	@Autowired
	private EventService eventService;

	@Autowired
	private SurveyService surveyService;

	@Autowired
	private NotificationService notificationService;

	protected GroupService getGroupService() {
		return groupService;
	}

	protected NotificationService getNotificationService() {
		return notificationService;
	}

	protected UserService getUserService() {
		return userService;
	}

	protected EventService getEventService() {
		return eventService;
	}

	protected YaUser getUser() {
		//TODO to be changed - no comment
		return userService.findOne(getUserName());
	}

	protected SurveyService getSurveyService() {
		return surveyService;
	}

	protected static String getUserName() {
		return "antoinelefebvre";
	}
	
}
