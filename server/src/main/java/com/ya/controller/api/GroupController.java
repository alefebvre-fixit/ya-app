package com.ya.controller.api;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ya.exception.EntityAuthorizationException;
import com.ya.exception.EntityNotFoundException;
import com.ya.model.group.Group;
import com.ya.model.group.GroupFactory;
import com.ya.model.user.YaUser;
import com.ya.util.Logger;

@RestController
public class GroupController extends YaController {

	@RequestMapping("/api/groups")
	public List<Group> groups() {
		Logger.debug("debug GroupController.groups()");
		Logger.info("info GroupController.groups()");
		Logger.warn("warn GroupController.groups()");
		Logger.error("error GroupController.groups()");
		
		
		
		return getGroupService().findAll();
	}

	@RequestMapping("/api/groups/{groupId}")
	public Group group(@PathVariable String groupId) {
		Logger.debug("GroupController.group groupId =" + groupId);
		return getGroupService().findOne(groupId);
	}

	@RequestMapping(value = "/api/groups/{groupId}/follow", method = RequestMethod.POST)
	public List<String> follow(@PathVariable String groupId) {
		Logger.debug("GroupController.follow groupId =" + groupId);

		getGroupService().follow(getUserName(), groupId);

		return getGroupService().findFollowingIds(getUserName());
	}

	@RequestMapping(value = "/api/groups/{groupId}/unfollow", method = RequestMethod.POST)
	public List<String> unfollow(String groupId) {
		Logger.debug("GroupController.unfollow groupId =" + groupId);

		getGroupService().unfollow(getUserName(), groupId);

		return getGroupService().findFollowingIds(getUserName());
	}

	@RequestMapping(value = "/api/user/{username}/groups")
	public List<Group> getUserGroups(@PathVariable String username) {
		Logger.debug("GroupController.groupByOwner username =" + username);

		return getGroupService().findUserGroups(username, -1, -1);
	}

	@RequestMapping("/api/groups/{groupId}/followers/size")
	public int followerSize(@PathVariable String groupId) {
		Logger.debug("GroupController.followerSize groupId =" + groupId);

		return getGroupService().countFollowers(groupId);
	}

	@RequestMapping("/api/groups/{groupId}/followers")
	public List<YaUser> followers(@PathVariable String groupId) {
		Logger.debug("GroupController.followers groupId =" + groupId);

		return getGroupService().findFollowers(groupId);
	}

	@RequestMapping("/api/groups/{groupId}/sponsors")
	public List<YaUser> sponsors(@PathVariable String groupId) {
		Logger.debug("GroupController.sponsors groupId =" + groupId);
		return getGroupService().findSponsors(groupId);
	}

	@RequestMapping("/api/user/{username}/groups/followings/id")
	public List<String> followingIds(String username) {
		Logger.debug("GroupController.followingIds username =" + username);
		return getGroupService().findFollowingIds(username);
	}

	@RequestMapping("/api/user/{username}/groups/followings/size")
	public int followingSize(String username) {
		Logger.debug("GroupController.followingSize username =" + username);
		return getGroupService().countFollowingSize(username);
	}

	@RequestMapping("/api/user/{username}/groups/followings")
	public List<Group> following(String username) {
		Logger.debug("GroupController.following username =" + username);
		return getGroupService().findFollowingGroups(username);
	}

	@RequestMapping(value = "/api/groups/{groupId}", method = RequestMethod.DELETE)
	public void deleteGroup(String groupId) {

		Group original = getGroupService().findOne(groupId);

		if (original == null)
			throw new EntityNotFoundException(Group.class.getSimpleName(), groupId);

		if (!original.canUpdate(getUserName()))
			throw new EntityAuthorizationException(Group.class.getSimpleName(), groupId);


		Logger.debug("GroupController.deleteGroup groupId =" + groupId);
		getGroupService().delete(groupId);
	}

	public Group createNewGroup() {
		Logger.debug("GroupController.createNewGroup()");
		return GroupFactory.createGroup(getUser());
	}

	@RequestMapping(value = "/api/groups", method = RequestMethod.POST)
	public Group create(@RequestBody Group group) {
		Logger.debug("GroupController.create()");

		if (group != null) {
			group.username = getUserName();
			group = getGroupService().save(group);
		}

		return group;
	}

	@RequestMapping(value = "/api/groups/{groupId}", method = RequestMethod.PUT)
	public Group update(@PathVariable String groupId, @RequestBody Group group) {
		Logger.debug("GroupController.update()");

		Group original = getGroupService().findOne(groupId);

		if (original == null)
			throw new EntityNotFoundException(Group.class.getSimpleName(), groupId);

		if (!original.canUpdate(getUserName()))
			throw new EntityAuthorizationException(Group.class.getSimpleName(), groupId);

		group.setId(groupId);
		group.username = getUserName();

		return getGroupService().save(group);

	}

}
