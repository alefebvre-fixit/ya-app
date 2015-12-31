package com.ya;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;

import com.ya.xauth.XAuthTokenConfigurer;

@EnableWebSecurity(debug = true)
@Configuration
@Order
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private YaUserDetailsService userDetailService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(
				SessionCreationPolicy.STATELESS);

		/*
		 * String[] restEndpointsToSecure = { "groups" }; for (String endpoint :
		 * restEndpointsToSecure) {
		 * http.authorizeRequests().antMatchers("/api/groups")
		 * .hasRole(CustomUserDetailsService.ROLE_USER); }
		 */

		/*
		 * http.authorizeRequests() .antMatchers("/resources/**", "/signup",
		 * "/about", "/api/authenticate").permitAll()
		 * .anyRequest().authenticated();
		 */
		http.authorizeRequests().anyRequest().permitAll();

		SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> securityConfigurerAdapter = new XAuthTokenConfigurer(
				userDetailsServiceBean());
		http.apply(securityConfigurerAdapter);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder authManagerBuilder)
			throws Exception {
		authManagerBuilder.userDetailsService(userDetailService);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
