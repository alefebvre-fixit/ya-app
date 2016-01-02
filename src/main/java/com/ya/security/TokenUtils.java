package com.ya.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.codec.Hex;

import com.ya.model.user.YaUser;

public class TokenUtils {

	public static final String MAGIC_KEY = "obfuscate";

	public static String createToken(YaUser user) {
		long expires = System.currentTimeMillis() + 1000L * 60 * 60;
		return user.getUsername() + ":" + expires + ":"
				+ computeSignature(user, expires);
	}

	public static String computeSignature(YaUser user, long expires) {
		StringBuilder signatureBuilder = new StringBuilder();
		signatureBuilder.append(user.getUsername()).append(":");
		signatureBuilder.append(expires).append(":");
		signatureBuilder.append(user.getPassword()).append(":");
		signatureBuilder.append(TokenUtils.MAGIC_KEY);

		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("No MD5 algorithm available!");
		}
		return new String(Hex.encode(digest.digest(signatureBuilder.toString()
				.getBytes())));
	}

	public static String getUserNameFromToken(String authToken) {
		if (null == authToken) {
			return null;
		}
		String[] parts = authToken.split(":");
		return parts[0];
	}

	public static boolean validateToken(String authToken, YaUser user) {
		String[] parts = authToken.split(":");
		long expires = Long.parseLong(parts[1]);
		String signature = parts[2];
		String signatureToMatch = computeSignature(user, expires);
		return expires >= System.currentTimeMillis()
				&& signature.equals(signatureToMatch);
	}
	
}