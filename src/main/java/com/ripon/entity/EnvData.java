package com.ripon.entity;

import org.springframework.stereotype.Component;

@Component
public class EnvData {
	
	public static String image_path;
	public static String host;
	
	public static String getImage_path() {
		return image_path;
	}
	public static void setImage_path(String image_path) {
		EnvData.image_path = image_path;
	}
	public static String getHost() {
		return host;
	}
	public static void setHost(String host) {
		EnvData.host = host;
	}
	
	
}
