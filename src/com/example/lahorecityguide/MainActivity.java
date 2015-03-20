package com.example.lahorecityguide;


import com.example.lahorecityguide.extra.AllConstants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {


	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in
	
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in
	

	protected LocationManager locationManager;

	private LinearLayout airport, atm, bakery, bank, 
			book_store, bus_station, doctor, food,
			furniture_store, gym, health, hospital,
			lawyer, movie_theater, museum, parking,
			pharmacy, post_office, restaurant, school,
			shopping_mall, travel_agency;
	private static Context con;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		con = this;
		iUI();
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MINIMUM_TIME_BETWEEN_UPDATES,
				MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
	}


	protected void alertbox(String title, String mymessage) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Your Device's GPS is Disable").setCancelable(false)
				.setTitle("Gps Status").setPositiveButton("Gps On",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								
								Intent myIntent = new Intent(
										Settings.ACTION_SECURITY_SETTINGS);
								startActivity(myIntent);
								dialog.cancel();
							}
						}).setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	protected void showCurrentLocation() {

		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location != null) {
			String message = String.format(
					"Current Location \n Longitude: %1$s \n Latitude: %2$s",
					location.getLongitude(), location.getLatitude());
			Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG)
					.show();

			Log.e("GeoData:", message);

			double lat = location.getLatitude();

			double lng = location.getLongitude();
			
			AllConstants.UPlat = String.valueOf(lat);
			AllConstants.UPlng = String.valueOf(lng);
		}

	}

	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			String message = String.format(
					"New Location \n Longitude: %1$s \n Latitude: %2$s",
					location.getLongitude(), location.getLatitude());

		}

		public void onStatusChanged(String s, int i, Bundle b) {

		}

		public void onProviderDisabled(String s) {
			alertbox("Gps Status!!", "Your GPS is: OFF");

		}

		public void onProviderEnabled(String s) {
			Toast.makeText(MainActivity.this,
					"GPS turned on",
					Toast.LENGTH_LONG).show();
		}

	}


	private void iUI() {

		airport = (LinearLayout) findViewById(R.id.airport);
		airport.setOnClickListener(this);

		atm = (LinearLayout) findViewById(R.id.atm);
		atm.setOnClickListener(this);

		bakery = (LinearLayout) findViewById(R.id.bakery);
		bakery.setOnClickListener(this);

		bank = (LinearLayout) findViewById(R.id.bank);
		bank.setOnClickListener(this);



		bus_station = (LinearLayout) findViewById(R.id.busstation);
		bus_station.setOnClickListener(this);

		doctor = (LinearLayout) findViewById(R.id.doctor);
		doctor.setOnClickListener(this);

		food = (LinearLayout) findViewById(R.id.food);
		food.setOnClickListener(this);

		furniture_store = (LinearLayout) findViewById(R.id.furniture_store);
		furniture_store.setOnClickListener(this);


		gym = (LinearLayout) findViewById(R.id.gym);
		gym.setOnClickListener(this);

		health = (LinearLayout) findViewById(R.id.health);
		health.setOnClickListener(this);

		hospital = (LinearLayout) findViewById(R.id.hospital);
		hospital.setOnClickListener(this);

		

		hospital = (LinearLayout) findViewById(R.id.hospital);
		hospital.setOnClickListener(this);


		lawyer = (LinearLayout) findViewById(R.id.lawyer);
		lawyer.setOnClickListener(this);

		movie_theater = (LinearLayout) findViewById(R.id.movie_theater);
		movie_theater.setOnClickListener(this);

		museum = (LinearLayout) findViewById(R.id.museum);
		museum.setOnClickListener(this);


		parking = (LinearLayout) findViewById(R.id.parking);
		parking.setOnClickListener(this);


		pharmacy = (LinearLayout) findViewById(R.id.pharmacy);
		pharmacy.setOnClickListener(this);

		post_office = (LinearLayout) findViewById(R.id.post_office);
		post_office.setOnClickListener(this);

		restaurant = (LinearLayout) findViewById(R.id.restaurant);
		restaurant.setOnClickListener(this);

		school = (LinearLayout) findViewById(R.id.school);
		school.setOnClickListener(this);

		shopping_mall = (LinearLayout) findViewById(R.id.shopping_mall);
		shopping_mall.setOnClickListener(this);

		travel_agency = (LinearLayout) findViewById(R.id.travel_agency);
		travel_agency.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		
		showCurrentLocation();
		
		
		switch (v.getId()) {

		case R.id.airport:
			AllConstants.query = "airport";
			final Intent re = new Intent(this, ListActivity.class);
			re.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(re);

			break;

		case R.id.atm:
			AllConstants.query = "atm";
			final Intent h = new Intent(this, ListActivity.class);
			h.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(h);

			break;

		case R.id.bakery:
			AllConstants.query = "bakery";
			final Intent b = new Intent(this, ListActivity.class);
			b.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(b);

			break;
		case R.id.bank:
			AllConstants.query = "bank";
			final Intent ba = new Intent(this, ListActivity.class);
			ba.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(ba);

			break;

		case R.id.busstation:
			AllConstants.query = "bus_station";
			final Intent bs = new Intent(this, ListActivity.class);
			bs.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(bs);

			break;

		case R.id.doctor:
			AllConstants.query = "doctor";
			final Intent doctor = new Intent(this, ListActivity.class);
			doctor.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(doctor);

			break;
		case R.id.food:
			AllConstants.query = "food";
			final Intent food = new Intent(this, ListActivity.class);
			food.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(food);

			break;

		case R.id.furniture_store:
			AllConstants.query = "furniture_store";
			final Intent furniture_store = new Intent(this, ListActivity.class);
			furniture_store.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(furniture_store);

			break;

		case R.id.gym:
			AllConstants.query = "gym";
			final Intent gym = new Intent(this, ListActivity.class);
			gym.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(gym);

			break;
		case R.id.health:
			AllConstants.query = "health";
			final Intent health = new Intent(this, ListActivity.class);
			health.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(health);

			break;
		case R.id.hospital:
			AllConstants.query = "hospital";
			final Intent hospital = new Intent(this, ListActivity.class);
			hospital.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(hospital);
			break;


		case R.id.lawyer:
			AllConstants.query = "lawyer";
			final Intent lawyer = new Intent(this, ListActivity.class);
			lawyer.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(lawyer);

			break;
		case R.id.movie_theater:
			AllConstants.query = "movie_theater";
			final Intent movie_theater = new Intent(this, ListActivity.class);
			movie_theater.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(movie_theater);

			break;
		case R.id.museum:
			AllConstants.query = "museum";
			final Intent museum = new Intent(this, ListActivity.class);
			museum.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(museum);

			break;

		case R.id.parking:
			AllConstants.query = "parking";
			final Intent parking = new Intent(this, ListActivity.class);
			parking.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(parking);

			break;

		case R.id.pharmacy:
			AllConstants.query = "pharmacy";
			final Intent pharmacy = new Intent(this, ListActivity.class);
			pharmacy.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(pharmacy);

			break;
		case R.id.post_office:
			AllConstants.query = "post_office";
			final Intent post_office = new Intent(this, ListActivity.class);
			post_office.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(post_office);

			break;
		case R.id.restaurant:
			AllConstants.query = "restaurant";
			final Intent restaurant = new Intent(this, ListActivity.class);
			restaurant.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(restaurant);

			break;
		case R.id.school:
			AllConstants.query = "school";
			final Intent school = new Intent(this, ListActivity.class);
			school.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(school);

			break;
		case R.id.shopping_mall:
			AllConstants.query = "shopping_mall";
			final Intent shopping_mall = new Intent(this, ListActivity.class);
			shopping_mall.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(shopping_mall);

			break;
		case R.id.travel_agency:
			AllConstants.query = "travel_agency";
			final Intent travel_agency = new Intent(this, ListActivity.class);
			travel_agency.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(travel_agency);

		}

	}

	public void btnFacebook(View v) {

		Intent next = new Intent(con, FacebookActivity.class);
		next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(next);
	}

	public void btnTwitter(View v) {

		Intent next = new Intent(con, TwitterActivity.class);
		next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(next);
	}
}
