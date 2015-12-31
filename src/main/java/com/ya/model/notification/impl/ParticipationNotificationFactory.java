package com.ya.model.notification.impl;

import java.util.Date;

import com.ya.model.event.Participation;
import com.ya.model.notification.Notification;
import com.ya.model.notification.NotificationFactory;
import com.ya.util.Logger;

public class ParticipationNotificationFactory extends NotificationFactory {

	@Override
	public Notification createNotification(Object object) {
		if (object instanceof Participation) {
			Participation participation = (Participation) object;
			Notification notification = new Notification();

			notification.setType(Notification.TYPE_PARTICIPATION);
			notification.setActor(participation.getUsername());
			notification.setEventId(participation.getEventId());
			notification.setEventName(participation.getEventName());
			notification.setNotificationDate(new Date());
			notification.setStatus(participation.getStatus());

			return notification;
		} else {
			Logger.debug("Object " + object.getClass().getName()
					+ " is not an instance of "
					+ Participation.class.getSimpleName());
			return null;
		}
	}

}
