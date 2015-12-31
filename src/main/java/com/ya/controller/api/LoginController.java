package com.ya.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.User;
import com.ya.model.user.SignUp;
import com.ya.model.user.YaUser;
import com.ya.model.user.impl.EmailSignIn;
import com.ya.model.user.impl.FacebookSignIn;
import com.ya.model.user.impl.FacebookSignUp;
import com.ya.util.Logger;
import com.ya.util.YaUtil;

@RestController
public class LoginController extends YaController {

	@RequestMapping(value = "/api/signup", method = RequestMethod.POST)
	public YaUser signUp(@RequestBody SignUp signup) {
		Logger.debug("UserAPIController.signup()");

		YaUser user = null;
		List<String> messages = validate(signup);

		if (messages.size() == 0) {
			// session().clear();
			user = getUserService().signup(signup);
			// session(SESSION_ATTRIBUTE_USERNAME, user.getUsername());
		}

		return user;
	}

	@RequestMapping(value = "/api/signin/email", method = RequestMethod.POST)
	public YaUser signIn(@RequestBody EmailSignIn signin) {

		Logger.debug("UserAPIController.signIn()");
		YaUser user = getUserService().authenticate(signin);

		// if (user == null) {
		// return forbidden("Invalid password");
		// }

		// session().clear();
		// session(SESSION_ATTRIBUTE_USERNAME, user.getUsername());

		return user;
	}

	@RequestMapping(value = "/api/signin/google", method = RequestMethod.POST)
	public YaUser googleSignIn() {

		Logger.debug("UserAPIController.googleSignIn()");

		// SignIn signin = Json.fromJson(body.asJson(), SignIn.class);
		/*
		 * User user = getUserService().authenticate(signin);
		 * 
		 * if (user == null) { return forbidden("Invalid password"); }
		 * session().clear(); session(SESSION_ATTRIBUTE_USERNAME,
		 * user.getUsername());
		 */

		return null;

	}

	String APP_SECRET = "07682c1ea374c2ca6eaa6fcff5ebb589";

	@RequestMapping(value = "/api/signin/facebook", method = RequestMethod.POST)
	public YaUser facebookSignIn(@RequestBody FacebookSignIn signin) {
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
			// session().clear();
			// return forbidden("Invalid password");
		}
		// TODO To be implemented
		// ession().clear();
		// session(SESSION_ATTRIBUTE_USERNAME, user.getUsername());
		// session(SESSION_ATTRIBUTE_ACCESS_TOKEN, signin.getToken());

		return user;
	}

	// TODO use bean validation
	public List<String> validate(SignUp signup) {
		List<String> result = new ArrayList<String>();

		YaUser user = getUserService().findOne(signup.getUsername());
		if (user != null) {
			Logger.debug("UserAPIController.validate() : User already exist");
			result.add("User already exist");
		}

		return result;
	}

}
