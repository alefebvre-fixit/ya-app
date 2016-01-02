package com.ya.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.ya.YaUserDetails;
import com.ya.exception.YaAuthenticationException;

/**
 * Sifts through all incoming requests and installs a Spring Security principal
 * if a header corresponding to a valid user is found.
 */
public class XAuthTokenFilter extends GenericFilterBean {

	private final UserDetailsService detailsService;
	private static final String X_AUTH_THOKEN_HEADER_NAME = "x-auth-token";

	public XAuthTokenFilter(UserDetailsService userDetailsService) {
		this.detailsService = userDetailsService;
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain filterChain) throws IOException, ServletException {
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest) arg0;
			String authToken = httpServletRequest
					.getHeader(X_AUTH_THOKEN_HEADER_NAME);

			if (StringUtils.hasText(authToken)) {
				String username = TokenUtils.getUserNameFromToken(authToken);

				YaUserDetails details = (YaUserDetails) this.detailsService
						.loadUserByUsername(username);

				if (TokenUtils.validateToken(authToken, details.getUser())) {
					UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
							details, details.getPassword(),
							details.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(token);
				}
			}
			filterChain.doFilter(arg0, arg1);
		} catch (Exception ex) {
			throw new YaAuthenticationException("Cannot authenticate user", ex);
		}
	}

}