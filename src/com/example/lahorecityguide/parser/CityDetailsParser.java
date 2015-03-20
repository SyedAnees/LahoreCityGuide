package com.example.lahorecityguide.parser;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import com.example.lahorecityguide.extra.PrintLog;
import com.example.lahorecityguide.extra.PskHttpRequest;
import com.example.lahorecityguide.holder.AllCityDetails;
import com.example.lahorecityguide.model.CityDetailsList;

public class CityDetailsParser {
	public static boolean connect(Context con, String url)
			throws JSONException, IOException {


		String result = "";
		try {
			result = PskHttpRequest.getText(PskHttpRequest
					.getInputStreamForGetRequest(url));
		} catch (final URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (result.length() < 1) {
			return false;
		
		}

		AllCityDetails.removeAll();

		final JSONObject detailsObject = new JSONObject(result);

		CityDetailsList detailsData;

		final JSONObject resultObject = detailsObject.getJSONObject("result");


		detailsData = new CityDetailsList();

		try {
			detailsData.setName(resultObject.getString("name"));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			detailsData.setRating(resultObject.getString("rating"));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			detailsData.setIcon(resultObject.getString("icon"));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			detailsData.setFormatted_address(resultObject
					.getString("formatted_address"));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		try {
			detailsData.setFormatted_phone_number(resultObject
					.getString("formatted_phone_number"));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			detailsData.setWebsite(resultObject.getString("website"));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		final JSONObject resultGeo = resultObject.getJSONObject("geometry");

		final JSONObject location = resultGeo.getJSONObject("location");

		try {
			detailsData.setLat(location.getString("lat"));
			PrintLog.myLog("lat : ", detailsData.getLat() + "");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		try {
			detailsData.setLng(location.getString("lng"));
			PrintLog.myLog("lng : ", detailsData.getLng() + "");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		
		
		AllCityDetails.setCityDetailsList(detailsData);
		detailsData = null;

		return true;
	}

}
