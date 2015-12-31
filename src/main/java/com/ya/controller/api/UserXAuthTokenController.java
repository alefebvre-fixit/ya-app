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
import com.ya.model.user.YaUser;
import com.ya.xauth.AuthenticationRequest;
import com.ya.xauth.TokenUtils;

/**
 * This controller generates the token that must be present in subsequent REST
 * invocations.
 */
@RestController
public class UserXAuthTokenController {

	private final TokenUtils tokenUtils = new TokenUtils();
	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;

	@Autowired
	public UserXAuthTokenController(AuthenticationManager am,
			UserDetailsService userDetailsService) {
		this.authenticationManager = am;
		this.userDetailsService = userDetailsService;
	}

	@RequestMapping(value = "/api/authenticate", method = { RequestMethod.POST })
	public UserTransfer authorize(
			@RequestBody AuthenticationRequest authenticationRequest) {
		String username = authenticationRequest.getUsername();
		String password = authenticationRequest.getPassword();

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				username, password);
		Authentication authentication = this.authenticationManager
				.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		YaUserDetails details = (YaUserDetails) this.userDetailsService
				.loadUserByUsername(username);

		return new UserTransfer(details.getUser(),
				tokenUtils.createToken(details));
	}

	public static class UserTransfer {

		private final YaUser user;
		private final String token;

		public UserTransfer(YaUser user, String token) {
			this.token = token;
			this.user = user;
		}

		public YaUser getUser() {
			return user;
		}

		public String getToken() {
			return this.token;
		}
	}
}