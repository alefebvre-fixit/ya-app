package com.ya.model.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ya.model.user.impl.EmailSignUp;
import com.ya.model.user.impl.FacebookSignUp;

@Document(collection = "User")
public class YaUser {

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", email=" + email
				+ "]";
	}

	@Id
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String username;

	private String email;

	private String facebookId;

	private String gravatarId;

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

	private Profile profile = new Profile();

	public YaUser() {
	}



	public YaUser(String email) {
		this.email = email;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getGravatarId() {
		return gravatarId;
	}

	public void setGravatarId(String gravatarId) {
		this.gravatarId = gravatarId;
	}

}
