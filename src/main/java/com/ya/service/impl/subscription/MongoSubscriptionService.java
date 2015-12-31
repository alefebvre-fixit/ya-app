package com.ya.service.impl.subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ya.service.EventService;
import com.ya.service.GroupService;

@Component
public abstract class MongoSubscriptionService {

	@Autowired
	private EventService eventService;

	protected EventService getEventService() {
		return eventService;
	}

	@Autowired
	private GroupService groupService;

	protected GroupService getGroupService() {
		return groupService;
	}

}
