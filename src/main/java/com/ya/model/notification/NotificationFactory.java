package com.ya.model.notification;

import com.ya.model.Favorite;
import com.ya.model.event.Event;
import com.ya.model.event.Participation;
import com.ya.model.group.Group;
import com.ya.model.notification.impl.EventNotificationFactory;
import com.ya.model.notification.impl.FavoriteNotificationFactory;
import com.ya.model.notification.impl.GroupNotificationFactory;
import com.ya.model.notification.impl.ParticipationNotificationFactory;
import com.ya.model.notification.impl.SurveyNotificationFactory;
import com.ya.model.survey.Survey;
import com.ya.util.Logger;

public abstract class NotificationFactory {

	public abstract Notification createNotification(Object object);

	// Fixit
	private final static NotificationFactory favoriteFactory = new FavoriteNotificationFactory();

	// Ya
	private final static NotificationFactory participationFactory = new ParticipationNotificationFactory();
	private final static NotificationFactory groupFactory = new GroupNotificationFactory();
	private final static NotificationFactory eventFactory = new EventNotificationFactory();
	private final static NotificationFactory surveyFactory = new SurveyNotificationFactory();

	public static NotificationFactory getInstance(Object object) {
		NotificationFactory result = null;

		if (object instanceof Group) {
			result = groupFactory;
		} else if (object instanceof Event) {
			result = eventFactory;
		} else if (object instanceof Survey) {
			result = surveyFactory;
		} else if (object instanceof Participation) {
			result = participationFactory;
		} else if (object instanceof Favorite) {
			result = favoriteFactory;
		}

		if (result != null) {
			Logger.debug("NotificationFactory.getInstance(Object object) result="
					+ result.getClass().getSimpleName());
		} else {
			Logger.debug("NotificationFactory.getInstance(Object object) result = notfound");
		}

		return result;
	}

}
