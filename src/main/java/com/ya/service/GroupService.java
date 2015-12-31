package com.ya.service;

import java.util.List;

import com.ya.model.group.Group;
import com.ya.model.user.YaUser;

public interface GroupService {

	public List<Group> findAll();

	public String create(Group group);

	public Group save(Group group);

	public void delete(String id);

	public Group findOne(String id);

	public int countByOwner(String username);

	public List<Group> findUserGroups(String username, int offset, int length);

	public void follow(String username, String groupId);

	public void unfollow(String username, String groupId);

	public List<String> findFollowingIds(String username);

	public List<Group> findFollowingGroups(String username);

	public int countFollowingSize(String username);

	public List<YaUser> findFollowers(String groupId);

	public List<YaUser> findSponsors(String groupId);

	public List<String> findFollowerNames(String groupId);

	public int countFollowers(String groupId);

	public String findGroupOwner(String groupId);

}
