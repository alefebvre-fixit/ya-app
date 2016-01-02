package com.ya.controller.api;

import com.ya.model.user.YaUser;
import com.ya.security.TokenUtils;
import com.ya.security.YaUserInfo;

public abstract class SignInController extends YaController {

	protected YaUserInfo createYaUserInfo(YaUser user) {
		YaUserInfo result = new YaUserInfo(user, TokenUtils.createToken(user));

		result.setFollowingUsers(getUserService().findFollowingNames(
				user.getUsername()));
		result.setFollowingGroups(getGroupService().findFollowingIds(
				user.getUsername()));

		return result;
	}

}