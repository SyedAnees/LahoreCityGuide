package com.example.lahorecityguide.extra;

import com.example.lahorecityguide.extra.AllConstants;
import com.example.lahorecityguide.extra.BaseURL;


public class AllURL {

	public static String loginURL(String email, String password) {
		return BaseURL.HTTP + "login.php?EmailAddress=" + email + "&Password="
				+ password;
	}

	public static String nearByURL(String UPLat,String UPLng,String query,String apiKey) {
		return BaseURL.HTTP + "nearbysearch/json?location="+UPLat+","+UPLng+"&rankby=distance&types="+query+"&sensor=false&key="+apiKey;
	}

	
	public static String cityGuideURL(String query,String apiKey) {
		return BaseURL.HTTP + "textsearch/json?query="+query+"+in+"+AllConstants.cityName+"&sensor=true&key="+apiKey;
	}
	
	public static String cityGuideDetailsURL(String reference, String apiKey) {
		return BaseURL.HTTP + "details/json?reference="+reference+"&sensor=true&key="+apiKey;
	}
}