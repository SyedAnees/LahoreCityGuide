package com.example.lahorecityguide.extra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class PskHttpRequest {

	public static String currentUserName = "";
	public static String currentPass = "";
	public static String currentVersion = "";
	public static String currentURL = "";

	public static void resetAllUserData() {

		PskHttpRequest.currentUserName = "";
		PskHttpRequest.currentPass = "";
		PskHttpRequest.currentVersion = "";
		PskHttpRequest.currentURL = "";

	}

	public static String doLogin(String url, final String username,
			final String pass) {

		PskHttpRequest.currentUserName = username;
		PskHttpRequest.currentPass = pass;

		final HttpPost httpost = new HttpPost(url);

		Log.d("Log in URL is ", url);
		PskHttpRequest.currentURL = url;

		final List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("email", username));
		nvps.add(new BasicNameValuePair("password", pass));
		
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

			return PskHttpRequest.getData(httpost);

		} catch (final UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";

	}

	public static String getData(final HttpPost httpost) throws IOException,
			URISyntaxException {

		String inputLine = "Error";
		final StringBuffer buf = new StringBuffer();

		{

			InputStream ins = null;

			ins = PskHttpRequest.getUrlData(httpost);

			final InputStreamReader isr = new InputStreamReader(ins);
			final BufferedReader in = new BufferedReader(isr);

			while ((inputLine = in.readLine()) != null) {
				buf.append(inputLine);
			}

			in.close();

		}

		return buf.toString();

	}

	public static InputStream getUrlData(final HttpPost httpost)
			throws URISyntaxException, ClientProtocolException, IOException {

		final HttpClient client = PskHttpRequest.getClient();

	
		final HttpResponse res = client.execute(httpost);

		System.out.println("post response for  register: "
				+ res.getStatusLine());

		return res.getEntity().getContent();
	}

	public static DefaultHttpClient getClient() {
		DefaultHttpClient ret = null;
		
		final HttpParams params = new BasicHttpParams();

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "utf-8");
		params.setBooleanParameter("http.protocol.expect-continue", false);
		
		final SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		final SSLSocketFactory sslSocketFactory = SSLSocketFactory
				.getSocketFactory();
		sslSocketFactory
				.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		registry.register(new Scheme("https", sslSocketFactory, 443));
		final ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(
				params, registry);
		ret = new DefaultHttpClient(manager, params);
		return ret;

	}


	public static InputStream getInputStreamForGetRequest(final String url)
			throws URISyntaxException, ClientProtocolException, IOException {

		Log.w("URL is ", url);
		final DefaultHttpClient httpClient = PskHttpRequest.getClient();
		URI uri;
		InputStream data = null;

		uri = new URI(url);
		final HttpGet method = new HttpGet(uri);

		final HttpResponse res = httpClient.execute(method);

		System.out.println("Login form get: " + res.getStatusLine());

		System.out.println("get login cookies:");
		httpClient.getCookieStore().getCookies();

		final String code = res.getStatusLine().toString();
		Log.w("status line ", code);
		data = res.getEntity().getContent();

		return data;
	}

	public static String getText(final InputStream in) throws IOException {
		String text = "";
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				in));
		final StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			text = sb.toString();

		} finally {

			in.close();

		}
		return text;
	}

	public static String doRegister(String url, final String firstName,
			String lastName, String email, final String pass) {

		final HttpPost httpost = new HttpPost(url);

		Log.d("Log in URL is ", url);
		PskHttpRequest.currentURL = url;

		final List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("email", email));
		nvps.add(new BasicNameValuePair("password", pass));
		nvps.add(new BasicNameValuePair("firstname", firstName));
		nvps.add(new BasicNameValuePair("lastname", lastName));
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

			return PskHttpRequest.getData(httpost);

		} catch (final UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "failed";

	}

}