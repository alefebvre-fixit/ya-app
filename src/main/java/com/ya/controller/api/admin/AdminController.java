package com.ya.controller.api.admin;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ya.controller.api.YaController;
import com.ya.model.user.Credential;
import com.ya.model.user.YaUser;
import com.ya.util.Logger;
import com.ya.util.YaUtil;

@RestController
public class AdminController extends YaController {

	@RequestMapping("/api/admin/user/sanitize")
	public void sanitize() {

		Logger.debug("Start sanitize");
		sanitizeCredential();
		sanitizeProfilePicture();
	}

	private void sanitizeProfilePicture() {
		Logger.debug("Start sanitize profile picture");

		List<YaUser> users = getUserService().findAll();
		for (YaUser user : users) {
			if (YaUtil.isEmpty(user.getFacebookId())) {
				user.getProfile().getPicture()
						.setUrl("img/users/" + user.getUsername() + ".jpg");
			} else {
				user.getProfile()
						.getPicture()
						.setUrl("https://graph.facebook.com/"
								+ user.getFacebookId() + "/picture");
			}
			Logger.debug("Sanitize user " + user.getUsername()
					+ " with following picture:"
					+ user.getProfile().getPicture().getUrl());
			getUserService().save(user);
		}
	}

	
	private void sanitizeCredential() {
		Logger.debug("Start sanitize credentials");

		/*
		List<YaUser> users = getUserService().findAll();
		for (YaUser user : users) {

			Credential credential = getUserService().findCredential(
					user.getUsername());
			if (credential == null) {
				credential = new Credential(user.getUsername(),
						user.getPassword());
				getUserService().save(credential);
				Logger.debug("Sanitize user " + user.getUsername()
						+ " with following credentials:" + credential);
			} else {
				Logger.debug("User user " + user.getUsername()
						+ " already has proper credentials:" + credential);
			}

		}
		*/

	}

}
