package com.ya;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ya.model.user.Credential;
import com.ya.service.UserService;

@Component
public class YaUserDetailsService implements UserDetailsService {

	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_USER = "USER";

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		UserDetails result = null;

		Credential credential = userService.findCredential(username);

		if (credential != null) {
			result = new YaUserDetails(credential.getUsername(),
					credential.getPassword(), ROLE_USER);
		} else {
			throw new UsernameNotFoundException("Cannot find username "
					+ username);
		}

		return result;
	}

}
