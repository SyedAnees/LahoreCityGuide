package com.example.lahorecityguide;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.lahorecityguide.ListActivity.RestaurantAdapter;
import com.example.lahorecityguide.extra.*;
import com.example.lahorecityguide.holder.AllCityDetails;
import com.example.lahorecityguide.model.CityDetailsList;
import com.example.lahorecityguide.parser.CityDetailsParser;

public class ListDetailsActivity extends Activity {

	private Context con;
	private String pos = "";
	private TextView cName, cAdd, cCell, cWeb;
	private CacheImageDownloader downloader;
	private Bitmap defaultBit;
	private ProgressDialog pDialog;
	private CityDetailsList CD;
	private RatingBar detailsRat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.detailslayout);
		con = this;

		initUI();
	}

	private void initUI() {
		// TODO Auto-generated method stub

		cName = (TextView) findViewById(R.id.cName);
		cAdd = (TextView) findViewById(R.id.cAddress);
		cCell = (TextView) findViewById(R.id.cPhone);
		cWeb = (TextView) findViewById(R.id.cWeb);
		downloader = new CacheImageDownloader();
		defaultBit = BitmapFactory.decodeResource(getResources(),
				R.drawable.noimage);

		updateUI();

	}


	private void updateUI() {
		if (!SharedPreferencesHelper.isOnline(con)) {
			AlertMessage.showMessage(con, "Error", "No internet connection");
			return;
		}

		pDialog = ProgressDialog.show(this, "", "Loading..", false, false);

		final Thread d = new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub

				try {
					if (CityDetailsParser.connect(con, AllURL
							.cityGuideDetailsURL(AllConstants.referrence,
									AllConstants.apiKey))) {
					}

				} catch (final Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				runOnUiThread(new Runnable() {

					public void run() {
						// TODO Auto-generated method stub
						if (pDialog != null) {
							pDialog.cancel();
						}
						Typeface title = Typeface.createFromAsset(getAssets(),
								"fonts/ROBOTO-REGULAR.TTF");
						Typeface add = Typeface.createFromAsset(getAssets(),
								"fonts/ROBOTO-LIGHT.TTF");
						try {

							CD = AllCityDetails.getAllCityDetails()
									.elementAt(0);

							cName.setText(CD.getName().trim());
							try {

								cName.setText(CD.getName().trim());
								cName.setTypeface(title);
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							try {

								cAdd.setText(CD.getFormatted_address().trim());
								cAdd.setTypeface(add);
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							try {

								cCell.setText(CD.getFormatted_phone_number()
										.trim());
								cCell.setTypeface(add);
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							try {

								cWeb.setText(CD.getWebsite().trim());
								cWeb.setTypeface(add);
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							try {

								AllConstants.lat = CD.getLat().trim();

								AllConstants.lng = CD.getLng().trim();
								PrintLog.myLog("GEO", AllConstants.lat);
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}

							// ------Rating ---

							String rating = CD.getRating();

							try {

								Float count = Float.parseFloat(rating);
								
							} catch (Exception e) {

								PrintLog.myLog("error at rating", e.toString());
							}

							// ---Photo---
							try {

								String imgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
										+ AllConstants.photoReferrence
										+ "&sensor=true&key="
										+ AllConstants.apiKey;

								final ImageView lImage = (ImageView) findViewById(R.id.imageViewL);

								if (AllConstants.photoReferrence.length() != 0) {

									downloader.download(imgUrl.trim(), lImage);

								}

								else {
									lImage.setImageBitmap(defaultBit);
									
								}

							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
				});

			}
		});
		d.start();
	}

	public void mapViewBtn(View v) {

		Intent next = new Intent(con, MapViewActivity.class);
		next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(next);

	}

	public void btnHome(View v) {

		Intent next = new Intent(con, MainActivity.class);
		next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(next);

	}

	public void btnBack(View v) {
		finish();

	}
}