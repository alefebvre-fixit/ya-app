package com.ya.model.notification.impl;

import java.util.Date;

import com.ya.model.notification.Notification;
import com.ya.model.notification.NotificationFactory;
import com.ya.model.survey.Survey;
import com.ya.util.Logger;

public class SurveyNotificationFactory extends NotificationFactory {

	@Override
	public Notification createNotification(Object object) {
		if (object instanceof Survey) {
			Survey survey = (Survey) object;
			Notification notification = new Notification();

			notification.setType(Notification.TYPE_EVENT);
			notification.setActor(survey.getUsername());
			notification.setEventId(survey.getId());
			notification.setNotificationDate(new Date());

			notification.setStatus(survey.getStatus());

			return notification;
		} else {
			Logger.debug("Object " + object.getClass().getName()
					+ " is not an instance of " + Survey.class.getSimpleName());
			return null;
		}
	}

}
