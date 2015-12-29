package com.ya.controller.api;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ya.model.user.Profile;
import com.ya.model.user.YaUser;
import com.ya.util.Logger;

@RestController
public class UserController extends YaController {

	@RequestMapping("/api/me")
	public YaUser me() {
		Logger.debug("UserAPIController.me");

		return getUserService().findOne(getUserName());

	}

	@RequestMapping("/api/users")
	public List<YaUser> users() {
		Logger.debug("UserAPIController.users()");
		return getUserService().findAll();
	}

	@RequestMapping("/api/users/discovery")
	public List<YaUser> userDiscovery() {
		Logger.debug("UserAPIController.accounts()");
		return getUserService().findAll();
	}

	@RequestMapping("/api/users/{username}")
	public YaUser user(@PathVariable String username) {
		Logger.debug("UserAPIController.user(username)");
		return getUserService().findOne(username);
	}

	@RequestMapping(value = "/api/profile", method = RequestMethod.POST)
	public YaUser updateProfile(@RequestBody Profile profile) {
		Logger.debug("UserAPIController.updateProfile()");

		YaUser user = getUser();
		user.setProfile(profile);
		getUserService().save(user);

		return user;

	}

	@RequestMapping(value = "/api/users/{followee}/follow", method = RequestMethod.POST)
	public void follow(@PathVariable String followee) {
		Logger.debug("UserAPIController.follow(followee)" + followee);

		if (!getUserName().equals(followee)) {
			getUserService().follow(getUserName(), followee);
		}

	}

	@RequestMapping(value = "/api/users/{followee}/unfollow", method = RequestMethod.POST)
	public void unFollow(@PathVariable String followee) {
		Logger.debug("UserAPIController.unFollow(followee)" + followee);

		if (!getUserName().equals(followee)) {
			getUserService().unFollow(getUserName(), followee);
		}
	}

	@RequestMapping("/api/users/{username}/followers")
	public List<YaUser> followers(@PathVariable String username) {
		Logger.debug("UserAPIController.followers(username)" + username);

		return getUserService().getFollowers(username);
	}

	@RequestMapping("/api/users/{username}/followers/size")
	public int followersSize(@PathVariable String username) {
		Logger.debug("UserAPIController.followerSize username =" + username);
		return getUserService().countFollowers(username);
	}

	@RequestMapping("/api/users/{username}/following")
	public List<YaUser> following(@PathVariable String username) {
		Logger.debug("UserAPIController.following username =" + username);
		return getUserService().findFollowing(username);
	}

	@RequestMapping("/api/users/{username}/following/names")
	public List<String> followingNames(@PathVariable String username) {
		Logger.debug("UserAPIController.followingNames username =" + username);
		return getUserService().findFollowingNames(username);
	}

	@RequestMapping("/api/users/{username}/following/size")
	public int followingSize(String username) {
		Logger.debug("UserAPIController.followingSize username =" + username);
		return getUserService().countFollowing(username);
	}

}
