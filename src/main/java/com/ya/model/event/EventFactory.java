package com.ya.model.event;

import java.util.Date;

import com.ya.model.group.Group;
import com.ya.model.user.YaUser;

public class EventFactory {

	public static final Event createEvent(Group group, YaUser user) {
		Event result = new Event();

		result.setUser(user.getIdentifier());
		result.setCreationDate(new Date());
		result.setModificationDate(result.getCreationDate());
		result.setLocation(group.getLocation());
		result.setCity(group.getCity());
		result.setCountry(group.getCountry());
		result.setGroupId(group.getId());
		result.setGroupName(group.getName());
		result.setType(group.getType());

		result.setSponsors(group.getSponsors());

		if (!result.isSponsor(group.getUsername())) {
			result.isSponsor(group.getUsername());
		}
		if (result.isSponsor(user.getUsername())) {
			result.removeFromSponsors(user.getUsername());
		}

		return result;
	}

}
