package com.ya.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.ya.dao.EventCommentRepository;
import com.ya.dao.EventRepository;
import com.ya.dao.ParticipationRepository;
import com.ya.model.event.Event;
import com.ya.model.event.EventComment;
import com.ya.model.event.EventTimeline;
import com.ya.model.event.Participation;
import com.ya.model.event.ParticipationSummary;
import com.ya.model.group.Group;
import com.ya.model.user.YaUser;
import com.ya.service.EventService;
import com.ya.service.GroupService;
import com.ya.service.NotificationService;
import com.ya.service.UserService;
import com.ya.util.Logger;

@Component
public class MongoEventService implements EventService {

	public static final String USER_NAME = "username";
	public static final String GROUP_ID = "groupId";
	public static final String EVENT_ID = "eventId";
	public static final String STATUS = "status";

	@Autowired
	EventRepository eventRepository;

	@Autowired
	ParticipationRepository participationRepository;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	EventCommentRepository commentRepository;

	@Autowired
	private GroupService groupService;

	@Autowired
	private UserService userService;

	@Override
	public List<Event> findAll() {
		Logger.debug("MongoEventService.findAll()");
		return eventRepository.findAll(new Sort(Sort.Direction.DESC, "date"));
	}

	@Override
	public void delete(String id) {
		Logger.debug("MongoEventService.delete(String id) id=" + id);
		Event event = findOne(id);
		if (event != null && event.getGroupId() != null) {
			Group group = groupService.findOne(event.getGroupId());
			if (group != null) {
				group.decrementEventSize();
				groupService.save(group);
			}
		}
		eventRepository.delete(id);
	}

	@Override
	public Event save(Event event) {
		notificationService.publishNotification(event);

		if (event.getGroupId() != null && event.getId() == null) {
			Group group = groupService.findOne(event.getGroupId());
			if (group != null) {
				group.incrementEventSize();
				Logger.debug("MongoEventService.create(Event id) incrementEventSize="
						+ group.getEventSize());
				groupService.save(group);
			}
		}

		Event result = eventRepository.save(event);
		return result;
	}

	@Override
	public Event findOne(String id) {
		return eventRepository.findOne(id);
	}

	@Override
	public int countByOwner(String username) {
		return eventRepository.countByUsername(username);
	}

	@Override
	public List<Event> findUserEvents(String username, int offset, int length) {
		List<Event> result = null;

		if (length > 0) {
			Page<Event> pages = eventRepository.findByUsername(username,
					new PageRequest(offset, length));
			result = pages.getContent();
		} else {
			result = eventRepository.findByUsername(username);
		}

		return result;
	}

	@Override
	public List<Event> findGroupEvents(String groupId, int offset, int length) {
		List<Event> result = null;

		if (length > 0) {
			Page<Event> pages = eventRepository.findByGroupId(groupId,
					new PageRequest(offset, length));
			result = pages.getContent();
		} else {
			result = eventRepository.findByGroupId(groupId);
		}

		return result;
	}

	@Override
	public int countByGroup(String groupId) {
		return eventRepository.countByGroupId(groupId);
	}

	@Override
	public String findEventOwner(String eventId) {
		// TODO Improve implementation by loading only the username

		String result = null;

		Event event = findOne(eventId);
		if (event != null) {
			result = event.getUsername();
		}

		return result;
	}

	@Override
	public List<Participation> findParticipations(String eventId, int offset,
			int length) {
		List<Participation> result = null;
		Logger.debug("MongoEventService.getParticipations(String eventId = "
				+ eventId + ", int offset = " + offset + ", int length = "
				+ length + " )");
		if (length > 0) {
			Page<Participation> pages = participationRepository.findByEventId(
					eventId, new PageRequest(offset, length));
			result = pages.getContent();
		} else {
			result = participationRepository.findByEventId(eventId);
		}

		return result;
	}

	@Override
	public Participation save(Participation participation) {

		Participation result = null;
		participation.incrementVersion();
		Logger.debug("MongoEventService.save(Participation participation)");
		if (participation.getId() == null) {

			Logger.debug("Looking for an existing participation with params eventId = "
					+ participation.getEventId()
					+ " participation "
					+ participation.getUsername());

			Participation existing = findOneParticipation(
					participation.getEventId(), participation.getUsername());
			if (existing != null) {
				Logger.debug("MongoEventService.save.reconcile with id="
						+ existing.id);
				participation.reconcile(existing);
				participation.setModificationDate(new Date());
				result = participationRepository.save(participation);
			} else {
				Logger.debug("Cannot find existing participation");

				participation.setCreationDate(new Date());
				participation.setModificationDate(participation
						.getCreationDate());
				result = participationRepository.save(participation);
			}
		} else {
			Logger.debug("MongoEventService.save.updateById(String id) id="
					+ participation.id);
			participation.setModificationDate(new Date());
			result = participationRepository.save(participation);
		}

		notificationService.publishNotification(participation);

		return result;
	}

	@Override
	public Participation findOneParticipation(String participationId) {
		return participationRepository.findOne(participationId);
	}

	@Override
	public Participation findOneParticipation(String eventId, String username) {
		Participation result = null;

		List<Participation> pages = participationRepository
				.findByEventIdAndUsername(eventId, username);

		if (pages != null && pages.size() > 0) {
			result = pages.get(0);
		}

		return result;
	}

	@Override
	public List<Participation> findUserParticipations(String username) {
		return participationRepository.findByUsername(username);
	}

	@Override
	public List<Participation> findParticipations(String eventId) {
		return participationRepository.findByEventId(eventId);
	}

	@Override
	public int countParticipations(String eventId) {
		return participationRepository.countByEventIdAndStatus(eventId,
				Participation.STATUS_IN);
	}

	@Override
	public EventComment findOneComment(String commentId) {
		return commentRepository.findOne(commentId);
	}

	@Override
	public EventComment saveComments(EventComment comment) {
		return commentRepository.save(comment);
	}

	@Override
	public void deleteComment(String commentId) {
		commentRepository.delete(commentId);
	}

	@Override
	public List<EventComment> findComments(String eventId, int offset,
			int length) {

		List<EventComment> result = null;
		Logger.debug("MongoEventService.getComments(String eventId = "
				+ eventId + ", int offset = " + offset + ", int length = "
				+ length + " )");
		if (length > 0) {
			Page<EventComment> pages = commentRepository
					.findByEventIdOrderByCommentDateDesc(eventId,
							new PageRequest(offset, length));
			result = pages.getContent();
		} else {
			result = commentRepository
					.findByEventIdOrderByCommentDateDesc(eventId);
		}
		Logger.debug("MongoEventService.getCommentSize(String eventId = "
				+ eventId + " ) found=" + result.size());

		return result;

	}

	@Override
	public int countComments(String eventId) {
		Logger.debug("MongoEventService.getCommentSize(String eventId = "
				+ eventId + " )");
		return commentRepository.countByEventId(eventId);
	}

	@Override
	public void deleteParticipation(String id) {
		Logger.debug("MongoEventService.deleteParticipation(String id = " + id
				+ " )");
		participationRepository.delete(id);
	}

	@Override
	public void deleteEventParticipations(String eventId) {
		Logger.debug("MongoEventService.deleteEventParticipations(String eventId = "
				+ eventId + " )");
		participationRepository.deleteByEventId(eventId);
	}

	@Override
	public ParticipationSummary findParticipationSummary(String eventId) {
		Logger.debug("MongoEventService.getParticipationSummary(String eventId = "
				+ eventId + " )");
		return new ParticipationSummary(findParticipations(eventId));
	}

	@Override
	public EventTimeline findEventTimeline() {

		EventTimeline result = new EventTimeline();
		result.add(findAll());

		return result;
	}

	@Override
	public EventTimeline findEventTimeline(String groupId) {

		EventTimeline result = new EventTimeline();
		result.add(findGroupEvents(groupId, 0, -1));

		return result;
	}

	@Override
	public List<YaUser> findSponsors(String eventId) {

		Logger.debug("MongoEventService.eventSponsors(String eventId) eventId="
				+ eventId);

		List<YaUser> result = new ArrayList<YaUser>();
		Event event = findOne(eventId);
		if (event != null) {
			result = userService.find(event.getSponsors());
		}

		if (result == null) {
			result = new ArrayList<YaUser>();
		}

		return result;
	}

}
