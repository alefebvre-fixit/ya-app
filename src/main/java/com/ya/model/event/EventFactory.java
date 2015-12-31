package com.ya.model.event;

import java.util.Date;

import com.ya.model.group.Group;
import com.ya.model.user.YaUser;

public class EventFactory {

	public static final Event createEvent(Group group, YaUser user) {
		Event result = new Event();

		result.setUsername(user.getUsername());
		result.setCreationDate(new Date());
		result.setModificationDate(result.getCreationDate());
		result.setLocation(group.getLocation());
		result.setCity(group.getCity());
		result.setCountry(group.getCountry());
		result.setGroupId(group.getId());
		result.setGroupName(group.getName());
		result.setType(group.getType());

		result.setSponsors(group.getSponsors());

		if (!result.getSponsors().contains(group.getUsername())) {
			result.getSponsors().add(group.getUsername());
		}
		if (result.getSponsors().contains(user.getUsername())) {
			result.getSponsors().remove(user.getUsername());
		}

		return result;
	}

}
