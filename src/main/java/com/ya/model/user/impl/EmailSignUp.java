package com.ya.model.user.impl;

import com.ya.model.user.SignUp;

public class EmailSignUp extends SignUp {

	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "EmailSignUp [password=" + password + "]";
	}

}
