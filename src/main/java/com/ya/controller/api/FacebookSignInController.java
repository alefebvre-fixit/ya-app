package com.ya.controller.api;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.User;
import com.ya.exception.YaAuthenticationException;
import com.ya.model.user.SignUp;
import com.ya.model.user.YaUser;
import com.ya.model.user.impl.FacebookSignIn;
import com.ya.model.user.impl.FacebookSignUp;
import com.ya.security.YaUserInfo;
import com.ya.util.Logger;
import com.ya.util.YaUtil;

@RestController
public class FacebookSignInController extends SignInController {

	String APP_SECRET = "07682c1ea374c2ca6eaa6fcff5ebb589";

	@RequestMapping(value = "/api/signin/facebook", method = RequestMethod.POST)
	public YaUserInfo signIn(@RequestBody FacebookSignIn signin) {
		Logger.debug("UserAPIController.facebookSignIn()");

		YaUser user = null;
		Logger.debug("UserAPIController.facebookSignIn(String token) token="
				+ signin.getToken());
		FacebookClient facebookClient = new DefaultFacebookClient(
				signin.getToken(), APP_SECRET, Version.VERSION_2_5);

		User facebook = facebookClient.fetchObject("me", User.class,
				Parameter.with("fields", "id,name,email,bio,website"));

		if (facebook != null) {
			Logger.debug("UserAPIController.facebookSignIn() retrieved user from facebook="
					+ facebook);

			user = getUserService().findOneByEmail(facebook.getEmail());

			// We do not know this user, this is actually a sign-up
			if (user == null) {
				Logger.debug("UserAPIController.facebookSignIn() unknown user from facebook="
						+ facebook);
				SignUp signup = new FacebookSignUp(facebook);
				user = getUserService().signup(signup);
			} else {
				if (YaUtil.isEmpty(user.getFacebookId())) {
					// merge with existing account
					user.setFacebookId(facebook.getId());
					user = getUserService().save(user);
				}
			}
		}

		if (user == null) {
			Logger.debug("UserAPIController.facebookSignIn() cannot identify user=");
			throw new YaAuthenticationException(
					"UserAPIController.facebookSignIn() cannot identify user");
		}
		// TODO To be implemented
		// ession().clear();
		// session(SESSION_ATTRIBUTE_USERNAME, user.getUsername());
		// session(SESSION_ATTRIBUTE_ACCESS_TOKEN, signin.getToken());

		return createYaUserInfo(user);
	}

}
