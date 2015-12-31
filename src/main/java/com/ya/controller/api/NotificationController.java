package com.ya.controller.api;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ya.model.notification.Notification;
import com.ya.util.Logger;

@RestController
public class NotificationController extends YaController {

	@RequestMapping("/api/notifications")
	public List<Notification> notifications(Principal principal) {
		
		
		
		Logger.debug("NotificationAPIController.notifications");
		Logger.debug("Principal principal " + principal.getName());
		return getNotificationService().findUserNotifications(getUserName(), 0,
				0);
	}

	@RequestMapping(value = "/api/groups/{groupId}/notifications/acknowledge", method = RequestMethod.POST)
	public void acknowledgeGroupNotifications(@PathVariable String groupId) {
		Logger.debug("NotificationAPIController.acknowledgeGroupNotifications groupId"
				+ groupId);

		getNotificationService().acknowledgeGroupNotifications(groupId,
				getUserName());
	}

	@RequestMapping(value = "/api/events/{eventId}/notifications/acknowledge", method = RequestMethod.POST)
	public void acknowledgeEventNotifications(@PathVariable String eventId) {
		Logger.debug("NotificationAPIController.acknowledgeEventNotifications eventId"
				+ eventId);

		getNotificationService().acknowledgeEventNotifications(eventId,
				getUserName());

	}

	@RequestMapping(value = "/api/notifications/{notificationId}/acknowledge", method = RequestMethod.POST)
	public void acknowledgeNotification(@PathVariable String notificationId) {
		Logger.debug("NotificationAPIController.acknowledgeNotification notificationId"
				+ notificationId);

		getNotificationService().acknowledgeNotification(notificationId);
	}

	@RequestMapping(value = "/api/notifications/acknowledge", method = RequestMethod.POST)
	public void acknowledgeNotifications() {
		Logger.debug("NotificationAPIController.acknowledgeNotifications");

		getNotificationService().acknowledgeNotifications(getUserName());
	}

}
