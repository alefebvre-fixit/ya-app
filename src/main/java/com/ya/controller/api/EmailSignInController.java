package com.ya.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ya.YaUserDetails;
import com.ya.model.user.impl.EmailSignIn;
import com.ya.security.TokenUtils;
import com.ya.security.YaUserInfo;

/**
 * This controller generates the token that must be present in subsequent REST
 * invocations.
 */
@RestController
public class EmailSignInController extends YaController {

	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;

	@Autowired
	public EmailSignInController(AuthenticationManager am,
			UserDetailsService userDetailsService) {
		this.authenticationManager = am;
		this.userDetailsService = userDetailsService;
	}

	@RequestMapping(value = "/api/signin/email", method = { RequestMethod.POST })
	public YaUserInfo authorize(@RequestBody EmailSignIn authenticationRequest) {
		String username = authenticationRequest.getUsername();
		String password = authenticationRequest.getPassword();

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				username, password);
		Authentication authentication = this.authenticationManager
				.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		YaUserDetails details = (YaUserDetails) this.userDetailsService
				.loadUserByUsername(username);

		return createYaUserInfo(details);
	}

	private YaUserInfo createYaUserInfo(YaUserDetails details) {
		YaUserInfo result = new YaUserInfo(details.getUser(),
				TokenUtils.createToken(details));

		result.setFollowingUsers(getUserService().findFollowingNames(
				details.getUsername()));
		result.setFollowingGroups(getGroupService().findFollowingIds(
				details.getUsername()));

		return result;
	}

}