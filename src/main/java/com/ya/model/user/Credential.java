package com.ya.model.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Credential")
public class Credential {

	public Credential() {
	}

	public Credential(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public String toString() {
		return "Credential [username=" + username + ", password=" + password
				+ "]";
	}

	@Id
	private String username;

	public String password;

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public boolean authenticate(String password) {
		return this.password.equals(password);
	}

}
