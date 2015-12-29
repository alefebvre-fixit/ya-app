package com.ya.model.notification.impl;

import java.util.Date;

import com.ya.model.group.Group;
import com.ya.model.notification.Notification;
import com.ya.model.notification.NotificationFactory;
import com.ya.util.Logger;

public class GroupNotificationFactory extends NotificationFactory {

	@Override
	public Notification createNotification(Object object) {
		if (object instanceof Group) {
			Group group = (Group) object;
			Notification notification = new Notification();

			notification.setType(Notification.TYPE_GROUP);
			notification.setActor(group.getUsername());
			notification.setGroupId(group.getId());
			notification.setGroupName(group.getName());
			notification.setNotificationDate(new Date());

			notification.setStatus(group.getStatus());

			return notification;
		} else {
			Logger.debug("Object " + object.getClass().getName()
					+ " is not an instance of " + Group.class.getSimpleName());
			return null;
		}
	}

}
