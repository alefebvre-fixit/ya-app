package com.ya.model.notification.impl;

import java.util.Date;

import com.ya.model.event.Event;
import com.ya.model.notification.Notification;
import com.ya.model.notification.NotificationFactory;
import com.ya.util.Logger;

public class EventNotificationFactory extends NotificationFactory {
	
	@Override
	public Notification createNotification(Object object) {
		if (object instanceof Event) {
			Event event = (Event) object;
			Notification notification = new Notification();

			notification.setType(Notification.TYPE_EVENT);
			notification.setActor(event.getUsername());
			notification.setGroupId(event.getGroupId());
			notification.setEventName(event.getName());
			notification.setGroupName(event.getGroupName());
			notification.setEventId(event.getId());
			notification.setNotificationDate(new Date());

			notification.setStatus(event.getStatus());

			return notification;
		} else {
			Logger.debug("Object " + object.getClass().getName()
					+ " is not an instance of " + Event.class.getSimpleName());
			return null;
		}
	}

}
