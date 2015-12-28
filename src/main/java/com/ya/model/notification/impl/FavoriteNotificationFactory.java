package com.ya.model.notification.impl;

import java.util.Date;

import com.ya.model.Favorite;
import com.ya.model.notification.Notification;
import com.ya.model.notification.NotificationFactory;
import com.ya.util.Logger;

public class FavoriteNotificationFactory extends NotificationFactory {

	@Override
	public Notification createNotification(Object object) {
		if (object instanceof Favorite) {
			Favorite favorite = (Favorite) object;
			Notification notification = new Notification();

			notification.setType(Notification.TYPE_FAVORITE);
			notification.setActor(favorite.getUsername());
			notification.setGroupId(favorite.getGroupId());
			notification.setNotificationDate(new Date());

			return notification;
		} else {
			Logger.debug("Object " + object.getClass().getName()
					+ " is not an instance of "
					+ Favorite.class.getSimpleName());
			return null;
		}
	}

}
