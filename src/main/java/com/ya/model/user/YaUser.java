package com.ya.model.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.restfb.types.User;

@Document(collection = "User")
public class YaUser {

	@Id
	private String id;
	private String username;
	private String email;
	private String facebookId;
	private String gravatarId;

	private Profile profile = new Profile();

	public YaUser() {
	}

	public YaUser(String email) {
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", email=" + email
				+ "]";
	}
	
	public UserIdentifier getIdentifier(){
		UserIdentifier result = new UserIdentifier();
		
		result.setFacebookId(facebookId);
		result.setGravatarId(gravatarId);
		result.setUsername(username);
		
		return result;
	}
	

}
