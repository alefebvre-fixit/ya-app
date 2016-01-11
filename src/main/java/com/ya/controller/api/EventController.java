package com.ya.controller.api;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ya.exception.EntityAuthorizationException;
import com.ya.exception.EntityNotFoundException;
import com.ya.model.event.Event;
import com.ya.model.event.EventComment;
import com.ya.model.event.EventFactory;
import com.ya.model.event.EventTimeline;
import com.ya.model.event.Participation;
import com.ya.model.event.ParticipationSummary;
import com.ya.model.user.YaUser;
import com.ya.util.Logger;

@RestController
public class EventController extends YaController {

	@RequestMapping("/api/events")
	public List<Event> events() {
		Logger.debug("EventAPIController.events()");
		return getEventService().findAll();
	}

	@RequestMapping("/api/events/timeline")
	public EventTimeline getEventTimeline() {
		Logger.debug("EventAPIController.getEventTimeline()");
		return getEventService().findEventTimeline();
	}

	@RequestMapping("/api/groups/{groupId}/events/timeline")
	public EventTimeline getEventTimelineByGroup(@PathVariable String groupId) {
		Logger.debug("EventAPIController.getEventTimelineByGroup(groupId) groupId="
				+ groupId);
		return getEventService().findEventTimeline(groupId);
	}

	@RequestMapping("/api/groups/{groupId}/events/new")
	public Event createNewEvent(@PathVariable String groupId) {
		Logger.debug("EventAPIController.createNewEvent() gor groupId"
				+ groupId);
		return EventFactory.createEvent(getGroupService().findOne(groupId),
				getUser());
	}

	@RequestMapping(value = "/api/events/{eventId}", method = RequestMethod.PUT)
	public Event update(@PathVariable String eventId, @RequestBody Event event) {
		Logger.debug("EventAPIController.save()");

		Event original = getEventService().findOne(eventId);

		if (original == null)
			throw new EntityNotFoundException(Event.class.getSimpleName(),
					eventId);

		if (!original.canUpdate(getUserName()))
			throw new EntityAuthorizationException(Event.class.getSimpleName(),
					eventId);

		event.setUser(getUser().getIdentifier());
		event.setId(eventId);

		return getEventService().save(event);
	}

	@RequestMapping(value = "/api/events", method = RequestMethod.POST)
	public Event create(@RequestBody Event event) {
		Logger.debug("EventAPIController.save()");

		event.setUser(getUser().getIdentifier());
		event.setId(null);

		return getEventService().save(event);
	}

	@RequestMapping("/api/events/{eventId}/sponsors")
	public List<YaUser> sponsors(@PathVariable String eventId) {
		Logger.debug("EventAPIController.sponsors eventId =" + eventId);
		return getEventService().findSponsors(eventId);
	}

	@RequestMapping("/api/users/{username}/events")
	public List<Event> getUserEvents(@PathVariable String username) {
		Logger.debug("EventAPIController.eventByOwner username =" + username);
		return getEventService().findUserEvents(username, -1, -1);
	}

	@RequestMapping("/api/groups/{groupId}/events/last")
	public List<Event> lastGroupEvents(@PathVariable String groupId) {
		Logger.debug("EventAPIController.lastGroupEvents groupId =" + groupId);
		return getEventService().findGroupEvents(groupId, 0, 5);
	}

	@RequestMapping("/api/groups/{groupId}/events")
	public List<Event> groupEvents(@PathVariable String groupId) {
		Logger.debug("EventAPIController.groupEvents groupId =" + groupId);
		return getEventService().findGroupEvents(groupId, -1, -1);
	}

	@RequestMapping("/api/groups/{groupId}/events/size")
	public int eventSizeByGroup(@PathVariable String groupId) {
		Logger.debug("EventAPIController.eventSizeByGroup groupId =" + groupId);
		return getEventService().countByGroup(groupId);
	}

	@RequestMapping("/api/events/{eventId}")
	public Event event(@PathVariable String eventId) {
		Logger.debug("EventAPIController.event eventId =" + eventId);
		return getEventService().findOne(eventId);
	}

	@RequestMapping(value = "/api/events/{eventId}", method = RequestMethod.DELETE)
	public void deleteEvent(@PathVariable String eventId) {
		Logger.debug("EventAPIController.deleteEvent eventId =" + eventId);

		Event original = getEventService().findOne(eventId);

		if (original == null)
			throw new EntityNotFoundException(Event.class.getSimpleName(),
					eventId);

		if (!original.canUpdate(getUserName()))
			throw new EntityAuthorizationException(Event.class.getSimpleName(),
					eventId);

		getEventService().delete(eventId);
	}

	@RequestMapping(value = "/api/events/{eventId}/participations", method = RequestMethod.POST)
	public Participation participate(@PathVariable String eventId,
			@RequestBody Participation participation) {
		Logger.debug("EventAPIController.participate");

		Event event = getEventService().findOne(eventId);

		if (event == null)
			throw new EntityNotFoundException(Event.class.getSimpleName(),
					eventId);

		if (event.accept(participation)) {
			participation.setEventName(event.getName());
			participation.setEventId(event.getId());
			participation = getEventService().save(participation);
		}

		return participation;
	}

	@RequestMapping("/api/events/{eventId}/participations/generate")
	public void generateParticipation(@PathVariable String eventId) {
		Logger.debug("EventAPIController.generateParticipation() eventId = "
				+ eventId);
		Event event = getEventService().findOne(eventId);
		if (event != null) {
			getEventService().deleteEventParticipations(eventId);

			List<YaUser> users = getUserService().findAll();
			if (users != null) {
				for (YaUser user : users) {
					Logger.debug("User username=|" + user.getUsername() + "|");
					if (user.getUsername() != null
							&& !user.getUsername().equals("")) {
						Participation participation = new Participation(event,
								user);
						int i = ThreadLocalRandom.current().nextInt(1, 6);
						if (i == 1 || i == 2) {
							participation.setStatus(Participation.STATUS_IN);
						} else if (i == 2 || i == 3 || i == 4) {
							participation.setStatus(Participation.STATUS_OUT);
						} else {
							participation.setStatus(Participation.STATUS_RSVP);
						}
						getEventService().save(participation);
					} else {
						getUserService().delete(user.getId());
					}
				}
			}
		}
	}

	@RequestMapping("/api/events/{eventId}/participations")
	public List<Participation> participations(@PathVariable String eventId) {
		Logger.debug("EventAPIController.participations()" + eventId);
		return getEventService().findParticipations(eventId, 0, -1);
	}

	@RequestMapping("/api/events/{eventId}/participations/summary")
	public ParticipationSummary participationSummary(
			@PathVariable String eventId) {
		Logger.debug("EventAPIController.participationSummary()" + eventId);
		return getEventService().findParticipationSummary(eventId);
	}

	@RequestMapping("/api/events/{eventId}/participations/size")
	public int countParticipations(@PathVariable String eventId) {
		Logger.debug("EventAPIController.participations()" + eventId);
		return getEventService().countParticipations(eventId);
	}

	@RequestMapping("/api/events/{eventId}/participations/last")
	public List<Participation> lastParticipations(@PathVariable String eventId) {
		Logger.debug("EventAPIController.participations()" + eventId);
		return getEventService().findParticipations(eventId, 0, 5);
	}

	@RequestMapping("/api/users/{username}/participations")
	public List<Participation> userParticipations(@PathVariable String username) {
		Logger.debug("EventAPIController.userParticipations()" + username);
		return getEventService().findUserParticipations(username);
	}

	@RequestMapping("/api/users/{username}/events/{eventId}/participation")
	public Participation userParticipation(@PathVariable String username,
			@PathVariable String eventId) {
		Logger.debug("EventAPIController.userParticipation()" + eventId);

		Participation participation = getEventService().findOneParticipation(
				eventId, username);
		if (participation == null) {
			participation = new Participation();
			participation.setEventId(eventId);
			participation.setStatus(Participation.STATUS_RSVP);
			participation.setUsername(username);
		}

		return participation;
	}

	@RequestMapping("/api/events/{eventId}/comments")
	public List<EventComment> comments(@PathVariable String eventId) {
		Logger.debug("EventAPIController.comments for eventId" + eventId);

		return getEventService().findComments(eventId, 0, 0);

	}

	@RequestMapping("/api/events/{eventId}/comments/size")
	public int commentSize(@PathVariable String eventId) {
		Logger.debug("EventAPIController.commentSize for eventId" + eventId);

		return getEventService().countComments(eventId);
	}

	@RequestMapping(value = "/api/events/{eventId}/comments/content", method = RequestMethod.POST)
	public EventComment post(@PathVariable String eventId,
			@PathVariable String content) {
		Logger.debug("EventAPIController.post");
		EventComment comment = null;

		if (content != null && !"".equals(content)) {
			comment = new EventComment();
			comment.setEventId(eventId);

			comment.setUsername(getUserName());
			comment.setCommentDate(new Date());
			comment.setContent(content);

			comment = getEventService().saveComments(comment);
		}

		return comment;
	}

}
