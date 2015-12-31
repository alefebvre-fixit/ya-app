package com.ya;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ya.model.user.YaUser;

public class YaUserDetails implements UserDetails {

	private static final long serialVersionUID = 3928328674928827064L;

	private String password;
	private boolean enabled = true;
	private YaUser user = null;
	private Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

	public YaUserDetails(YaUser user, String pw, String... extraRoles) {
		this.user = user;
		this.password = pw;

		// setup roles
		Set<String> roles = new HashSet<String>();
		roles.addAll(Arrays.<String> asList(null == extraRoles ? new String[0]
				: extraRoles));

		// export them as part of authorities
		for (String r : roles) {
			authorities.add(new SimpleGrantedAuthority(role(r)));
		}

	}

	public YaUser getUser() {
		return user;
	}

	public String toString() {
		return "{enabled:" + isEnabled() + ", username:'" + getUsername()
				+ "', password:'" + getPassword() + "'}";
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.enabled;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.enabled;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.enabled;
	}

	@Override
	public String getUsername() {
		return this.user.getUsername();
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	private String role(String i) {
		return "ROLE_" + i;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}
}