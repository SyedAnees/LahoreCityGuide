package com.example.lahorecityguide.holder;

import java.util.Vector;

import com.example.lahorecityguide.model.CityDetailsList;



public class AllCityDetails {
	public static Vector<CityDetailsList> allCityDetailsList = new Vector<CityDetailsList>();

	public static Vector<CityDetailsList> getAllCityDetails() {
		return AllCityDetails.allCityDetailsList;
	}

	public static void setAllCityDetails(Vector<CityDetailsList> allCityDetailsList) {
		AllCityDetails.allCityDetailsList = allCityDetailsList;
	}

	public static CityDetailsList getCityDetailsList(int pos) {
		return AllCityDetails.allCityDetailsList.elementAt(pos);
	}

	public static void setCityDetailsList(CityDetailsList CityDetailsList) {
		AllCityDetails.allCityDetailsList.addElement(CityDetailsList);
	}

	public static void removeAll() {
		AllCityDetails.allCityDetailsList.removeAllElements();
	}

}
