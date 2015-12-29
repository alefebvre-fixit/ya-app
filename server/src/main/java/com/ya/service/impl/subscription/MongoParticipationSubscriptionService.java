package com.ya.service.impl.subscription;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.ya.model.notification.Notification;
import com.ya.service.SubscriptionService;
import com.ya.util.Logger;

@Component
public class MongoParticipationSubscriptionService extends
		MongoSubscriptionService implements SubscriptionService {

	@Override
	public Set<String> getSubscribers(Notification notification) {

		Logger.debug("MongoParticipationSubscriptionService.getSubscribers(Notification notification");

		Set<String> result = new HashSet<String>();

		if (notification.getEventId() != null) {

			// Owner of the event want to receive notifications
			String owner = getEventService().findEventOwner(
					notification.getEventId());

			Logger.debug("MongoParticipationSubscriptionService.getSubscribers(Notification notification) owner="
					+ owner);

			if (owner != null) {
				result.add(owner);
			}

		}

		return result;
	}

}
