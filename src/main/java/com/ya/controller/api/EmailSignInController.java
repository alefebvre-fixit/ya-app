package com.ya.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ya.model.user.SignUp;
import com.ya.model.user.YaUser;
import com.ya.model.user.impl.EmailSignIn;
import com.ya.security.YaUserInfo;
import com.ya.util.Logger;

/**
 * This controller generates the token that must be present in subsequent REST
 * invocations.
 */
@RestController
public class EmailSignInController extends SignInController {

	private final AuthenticationManager authenticationManager;

	@Autowired
	public EmailSignInController(AuthenticationManager am) {
		this.authenticationManager = am;
	}

	@RequestMapping(value = "/api/signin/email", method = { RequestMethod.POST })
	public YaUserInfo signin(@RequestBody EmailSignIn authenticationRequest) {

		YaUserInfo result = null;

		String username = authenticationRequest.getUsername();
		String password = authenticationRequest.getPassword();

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				username, password);
		Authentication authentication = this.authenticationManager
				.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		if (authentication.isAuthenticated()) {

			YaUser user = getUserService().findOne(username);
			if (user != null) {
				result = createYaUserInfo(user);
			}
		}

		return result;
	}

	@RequestMapping(value = "/api/signup", method = RequestMethod.POST)
	public YaUserInfo signUp(@RequestBody SignUp signup) {
		Logger.debug("UserAPIController.signup()");

		YaUserInfo result = null;
		List<String> messages = validate(signup);

		if (messages.size() == 0) {
			getUserService().signup(signup);

			EmailSignIn signin = new EmailSignIn();
			signin.setUsername(signup.getUsername());
			signin.setPassword(signup.getPassword());
			result = signin(signin);

		}

		// TODO Handle validation and errors

		return result;
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