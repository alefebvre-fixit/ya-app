package com.ya.service.impl.subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ya.model.notification.Notification;
import com.ya.service.SubscriptionService;
import com.ya.util.Logger;

@Component
public class SubscriptionServiceFactory {

	@Autowired
	private MongoFollowersSubscriptionService followersSubscriptionService;

	@Autowired
	private MongoParticipationSubscriptionService participationSubscriptionService;

	@Autowired
	private MongoGroupSubscriptionService groupSubscriptionService;

	@Autowired
	private MongoEventSubscriptionService eventSubscriptionService;

	public final SubscriptionService getInstance(Notification notification) {
		SubscriptionService result = null;

		if (Notification.TYPE_FOLLOWERS.equals(notification.getType())) {
			result = followersSubscriptionService;
		} else if (Notification.TYPE_GROUP.equals(notification.getType())) {
			result = groupSubscriptionService;
		} else if (Notification.TYPE_EVENT.equals(notification.getType())) {
			result = eventSubscriptionService;
		} else if (Notification.TYPE_PARTICIPATION.equals(notification
				.getType())) {
			result = participationSubscriptionService;
		}

		if (result != null) {
			Logger.debug("SubscriptionServiceFactory.getInstance(Object object) result="
					+ result.getClass().getSimpleName());
		} else {
			Logger.debug("SubscriptionServiceFactory.getInstance(Object object) result = notfound");
		}

		return result;
	}

}
