package com.ya.model.group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ya.model.user.UserIdentifier;
import com.ya.util.YaUtil;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "Group")
public class Group {

	public static final String STATUS_NEW = "New";
	public static final String STATUS_PUBLISHED = "Published";
	public static final String STATUS_DRAFT = "Draft";

	@Id
	public String id;

	private Date creationDate;
	private Date modificationDate;

	public double version = 0;
	private String status = STATUS_NEW;
	private int eventSize = 0;

	private String type;
	private String name;
	private String description;
	private String city;
	private String country;
	private String location;

	private UserIdentifier user;
	private List<UserIdentifier> sponsors = new ArrayList<UserIdentifier>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getVersion() {
		return version;
	}

	public void incrementVersion() {
		this.version++;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getUsername() {
		return getUser().getUsername();
	}

	public void setVersion(double version) {
		this.version = version;
	}

	@JsonIgnore
	public void incrementEventSize() {
		eventSize++;
	}

	@JsonIgnore
	public void decrementEventSize() {
		eventSize--;
	}

	public int getEventSize() {
		return eventSize;
	}

	public void setEventSize(int eventSize) {
		this.eventSize = eventSize;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public UserIdentifier getUser() {
		return user;
	}

	public void setUser(UserIdentifier user) {
		this.user = user;
	}

	public List<UserIdentifier> getSponsors() {
		return sponsors;
	}

	public void setSponsors(List<UserIdentifier> sponsors) {
		this.sponsors = sponsors;
	}

	public boolean canUpdate(String actor) {

		if (actor != null) {
			if (actor.equals(getUsername())) {
				return true;
			}

			if (isSponsor(actor)) {
				return true;
			}
		}

		return false;
	}

	public boolean isSponsor(UserIdentifier identifier) {
		if (identifier != null) {
			return isSponsor(identifier.getUsername());
		}
		return false;
	}

	public boolean isSponsor(String username) {
		if (YaUtil.isNotEmpty(sponsors)) {
			for (UserIdentifier userIdentifier : sponsors) {
				if (userIdentifier.getUsername().equals(username)) {
					return true;
				}
			}
		}
		return false;
	}

}
