package com.example.lahorecityguide;
import com.example.lahorecityguide.extra.AllConstants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class TwitterActivity extends Activity {
	
	
	private WebView twitterweBview;
	Context con;

	String url = AllConstants.twitterFanPageUrl;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.twitterview);
		con = this;
		 Toast.makeText(TwitterActivity.this, "Loading..",
				 Toast.LENGTH_LONG).show();
		try {
			updateWebView(url);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private class HelloWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && twitterweBview.canGoBack()) {
			twitterweBview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

	private void updateWebView(String url) {
		// TODO Auto-generated method stub

		twitterweBview = (WebView) findViewById(R.id.mapViewtwi);
		twitterweBview.getSettings().setJavaScriptEnabled(true);
		twitterweBview.getSettings().setDomStorageEnabled(true);
		twitterweBview.loadUrl(url);

		twitterweBview.setWebViewClient(new HelloWebViewClient());

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