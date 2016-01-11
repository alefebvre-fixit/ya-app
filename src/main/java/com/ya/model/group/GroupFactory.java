package com.ya.model.group;

import java.util.Date;

import com.ya.model.user.YaUser;

public class GroupFactory {

	public static final Group createGroup(YaUser user) {
		Group result = new Group();

		result.setCreationDate(new Date());
		result.setModificationDate(result.getCreationDate());
		result.setUser(user.getIdentifier());
		result.setCity(user.getProfile().getCity());
		result.setCountry(user.getProfile().getCountry());

		return result;
	}

}
