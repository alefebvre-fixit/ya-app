package com.ya.security;

import java.util.ArrayList;
import java.util.List;

import com.ya.model.user.YaUser;

public class YaUserInfo {
	
	private final YaUser user;
	private final String token;

	private List<String> followingGroups = new ArrayList<String>();
	private List<String> followingUsers = new ArrayList<String>();
	
	public YaUserInfo(YaUser user, String token) {
		this.token = token;
		this.user = user;
	}

	public YaUser getUser() {
		return user;
	}

	public String getToken() {
		return this.token;
	}

	public List<String> getFollowingGroups() {
		return followingGroups;
	}

	public void setFollowingGroups(List<String> followingGroups) {
		this.followingGroups = followingGroups;
	}

	public List<String> getFollowingUsers() {
		return followingUsers;
	}

	public void setFollowingUsers(List<String> followingUsers) {
		this.followingUsers = followingUsers;
	}
	
	
	
}
