package com.ya.model.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.codec.Hex;

import com.ya.model.user.impl.FacebookSignUp;

public class YaUserFactory {

	public static YaUser create(SignUp signup) {

		YaUser result = new YaUser();

		result.setUsername(signup.getUsername());
		result.setEmail(signup.getEmail());
		result.setProfile(signup.getProfile());

		if (signup instanceof FacebookSignUp) {
			FacebookSignUp facebook = (FacebookSignUp) signup;
			result.setFacebookId(facebook.getFaceBookId());
		}

		result.setGravatarId(computeGravatarId(result));

		return result;
	}

	public static String computeGravatarId(YaUser user) {
		// TODO Do not instantiate every time

		String result = null;

		if (user != null && user.getEmail() != null) {
			MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new IllegalStateException("No MD5 algorithm available!");
			}
			result = new String(Hex.encode(digest.digest(user.getEmail()
					.getBytes())));
		}

		return result;
	}

}
