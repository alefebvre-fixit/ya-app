package com.ya.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ya.model.user.Following;

public interface FollowingRepository extends MongoRepository<Following, String> {

	public int countByFollowee(String username);

	public int countByFollower(String username);

	public List<Following> findByFollowee(String username);

	public List<Following> findByFollower(String username);

	public List<Following> findByFolloweeAndFollower(String followee, String follower);
	
}