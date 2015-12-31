package com.ya.model.user.impl;

import org.springframework.beans.factory.annotation.Required;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ya.model.user.SignIn;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailSignIn extends SignIn {

	private String password;

	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "EmailSignIn [password=" + password + ", username=" + username
				+ "]";
	}

}
