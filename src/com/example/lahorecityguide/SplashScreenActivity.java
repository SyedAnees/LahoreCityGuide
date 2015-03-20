package com.example.lahorecityguide;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class SplashScreenActivity extends Activity {
	
	private final Handler mHandler = new Handler();
	private static final int duration = 3000;
	
	
	

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splashscreen);

		mHandler.postDelayed(mPendingLauncherRunnable,
				SplashScreenActivity.duration);
}

	@Override
	protected void onPause() {
		super.onPause();
		mHandler.removeCallbacks(mPendingLauncherRunnable);
	}

	private final Runnable mPendingLauncherRunnable = new Runnable() {

		public void run() {
			final Intent i = new Intent(SplashScreenActivity.this,
					MainActivity.class);
			
			startActivity(i);
			finish();
		}
	};
}