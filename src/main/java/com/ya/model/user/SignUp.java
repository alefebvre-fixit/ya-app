package com.ya.model.user;

import org.springframework.beans.factory.annotation.Required;

public abstract class SignUp {

	private String username;

	private String email;

	private Profile profile = new Profile();

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
