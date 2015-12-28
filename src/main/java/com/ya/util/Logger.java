package com.ya.util;

import org.slf4j.LoggerFactory;

public class Logger {

	private static final org.slf4j.Logger log = LoggerFactory
			.getLogger(Logger.class.getClass());

	public static void debug(String msg) {
		log.debug(msg);
	}

	public static void error(String msg) {
		log.error(msg);
	}

}
