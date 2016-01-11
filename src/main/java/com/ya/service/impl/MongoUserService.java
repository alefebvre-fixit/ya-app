package com.ya.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ya.dao.CredentialRepository;
import com.ya.dao.FollowingRepository;
import com.ya.dao.UserRepository;
import com.ya.model.user.Credential;
import com.ya.model.user.Following;
import com.ya.model.user.SignIn;
import com.ya.model.user.SignUp;
import com.ya.model.user.UserIdentifier;
import com.ya.model.user.YaUser;
import com.ya.model.user.YaUserFactory;
import com.ya.model.user.impl.EmailSignIn;
import com.ya.model.user.impl.EmailSignUp;
import com.ya.model.user.impl.FacebookSignUp;
import com.ya.service.UserService;
import com.ya.util.Logger;
import com.ya.util.YaUtil;

@Component
public class MongoUserService implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FollowingRepository followingRepository;

	@Autowired
	private CredentialRepository credentialRepository;

	@Override
	public YaUser findOne(String username) {
		Logger.debug("MongoUserService.load(String userName) username="
				+ username);
		YaUser result = null;

		List<YaUser> users = userRepository.findByUsername(username);
		if (users != null && users.size() > 0) {
			result = users.get(0);
		}

		return result;
	}

	@Override
	public YaUser authenticateByUserName(String username, String password) {

		YaUser result = null;

		Credential credential = findCredential(username);
		if (credential != null) {

			if (credential.authenticate(password)) {
				result = findOne(username);
			} else {
				Logger.debug("Cannot find authenticate user " + username);
			}

		} else {
			Logger.debug("Cannot find credential for user " + username);
		}

		return result;
	}

	@Override
	public String create(YaUser user) {
		YaUser result = save(user);
		return result.getUsername();
	}

	@Override
	public YaUser save(YaUser user) {
		return userRepository.save(user);
	}

	@Override
	public List<YaUser> findAll() {
		return userRepository.findAll();
	}

	@Override
	public void delete(String id) {
		userRepository.delete(id);
		Logger.debug("MongoUserService.delete(String id) id=" + id);
	}

	@Override
	public YaUser signup(SignUp signup) {

		if (signup instanceof EmailSignUp) {
			return signup((EmailSignUp) signup);
		} else if (signup instanceof FacebookSignUp) {
			return signup((FacebookSignUp) signup);
		} else {
			Logger.debug("Invalid signup =" + signup);
			return null;
		}

	}

	private YaUser signup(EmailSignUp signup) {
		YaUser result = YaUserFactory.create(signup);
		Credential credential = new Credential(signup.getUsername(),
				signup.getPassword());
		save(credential);
		return save(result);
	}

	private YaUser signup(FacebookSignUp signup) {
		YaUser result = YaUserFactory.create(signup);
		return save(result);
	}

	@Override
	public YaUser authenticate(SignIn signin) {
		if (signin instanceof EmailSignIn) {
			EmailSignIn emailSignIn = (EmailSignIn) signin;
			return authenticateByUserName(emailSignIn.getUsername(),
					emailSignIn.getPassword());
		}
		Logger.error("MongoUserService.authenticate(SignIn signin) unknown signin method = "
				+ signin.getClass().getSimpleName());
		return null;
	}

	@Override
	public void follow(String follower, String followee) {
		Logger.debug("MongoUserService.follow(String follower, String followee) follower="
				+ follower + " follower= " + follower);

		List<Following> following = followingRepository
				.findByFolloweeAndFollower(followee, follower);
		if (YaUtil.isEmpty(following)) {
			followingRepository.save(Following.create(followee, follower));
		}
	}

	@Override
	public void unFollow(String follower, String followee) {
		Logger.debug("MongoUserService.unFollow(String follower, String followee) follower="
				+ follower + " follower= " + follower);
		List<Following> following = followingRepository
				.findByFolloweeAndFollower(followee, follower);
		if (YaUtil.isNotEmpty(following)) {
			followingRepository.delete(following);
		}

	}

	@Override
	public List<YaUser> getFollowers(String username) {

		Logger.debug("MongoUserService.getFollowers(String username) username="
				+ username);

		List<YaUser> result = null;

		List<String> names = findFollowerNames(username);

		if (YaUtil.isNotEmpty(names)) {
			result = userRepository.findByUsernameIn(names);
		}

		if (result == null) {
			result = new ArrayList<YaUser>();
		}

		Logger.debug("MongoUserService.getFollowers(String username) result="
				+ result.size());

		return result;
	}

	@Override
	public int countFollowers(String username) {
		Logger.debug("MongoUserService.countFollowers(String username) username="
				+ username);
		return followingRepository.countByFollowee(username);
	}

	@Override
	public List<YaUser> findFollowing(String username) {
		List<YaUser> result = null;

		Logger.debug("MongoUserService.getFollowing(String username) username="
				+ username);

		List<String> names = findFollowingNames(username);

		if (YaUtil.isNotEmpty(names)) {
			result = userRepository.findByUsernameIn(names);
		}

		if (result == null) {
			result = new ArrayList<YaUser>();
		}

		return result;
	}

	@Override
	public int countFollowing(String username) {
		Logger.debug("MongoUserService.countFollowing(String username) username="
				+ username);
		return followingRepository.countByFollower(username);
	}

	@Override
	public List<String> findFollowerNames(String username) {
		Logger.debug("MongoUserService.getFollowerNames(String username) username="
				+ username);

		List<String> result = new ArrayList<String>();

		List<Following> following = followingRepository
				.findByFollowee(username);
		if (YaUtil.isNotEmpty(following)) {
			for (Following f : following) {
				Logger.debug(f.toString());
				result.add(f.getFollower());
			}
		}

		return result;
	}

	@Override
	public List<String> findFollowingNames(String username) {
		Logger.debug("MongoUserService.getFollowingNames(String username) username="
				+ username);

		List<String> result = new ArrayList<String>();

		List<Following> following = followingRepository
				.findByFollower(username);
		if (following != null && following.size() > 0) {
			for (Following f : following) {
				Logger.debug(f.toString());
				result.add(f.getFollowee());
			}
		}

		return result;
	}

	@Override
	public List<YaUser> find(List<String> usernames) {
		List<YaUser> result = null;

		Logger.debug("MongoUserService.find(List<String> usernames) usernames="
				+ usernames);

		if (YaUtil.isNotEmpty(usernames)) {
			result = userRepository.findByUsernameIn(usernames);
		}

		if (result == null) {
			result = new ArrayList<YaUser>();
		}

		return result;
	}

	@Override
	public YaUser findOneByEmail(String email) {
		Logger.debug("MongoUserService.findOneByEmail(String email) email="
				+ email);

		YaUser result = null;

		if (YaUtil.isNotEmpty(email)) {
			List<YaUser> users = userRepository.findByEmail(email);

			if (YaUtil.isEmpty(users)) {
				Logger.debug("Cannot find user with email=" + email);
			} else if (users.size() == 1) {
				Logger.debug("Cannot find one user with email=" + email);
				result = users.get(0);
			} else {
				Logger.debug("Find " + users.size() + " user with email="
						+ email);
				for (YaUser yaUser : users) {
					if (YaUtil.isEmpty(yaUser.getUsername())) {
						delete(yaUser.getId());
					} else {
						result = yaUser;
					}
				}
			}
		}

		return result;
	}

	@Override
	public Credential findCredential(String username) {
		return credentialRepository.findOne(username);
	}

	@Override
	public Credential save(Credential credential) {
		return credentialRepository.save(credential);
	}

	@Override
	public List<YaUser> findByIdentifiers(List<UserIdentifier> identifiers) {

		List<String> usernames = new ArrayList<String>();

		if (YaUtil.isNotEmpty(identifiers)) {
			for (UserIdentifier id : identifiers) {
				usernames.add(id.getUsername());
			}
		}

		return find(usernames);
	}

}
