package com.ya.service;

import java.util.Set;

import com.ya.model.notification.Notification;

public interface SubscriptionService {

	public abstract Set<String> getSubscribers(Notification notification);

}
