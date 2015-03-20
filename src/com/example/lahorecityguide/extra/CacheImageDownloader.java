package com.example.lahorecityguide.extra;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class CacheImageDownloader {
	private static final String LOG_TAG = "CacheImageDownloader";

	private static final int HARD_CACHE_CAPACITY = 40;
	private static final int DELAY_BEFORE_PURGE = 30 * 1000; // in milliseconds

	private final HashMap<String, Bitmap> sHardBitmapCache = new LinkedHashMap<String, Bitmap>(
			CacheImageDownloader.HARD_CACHE_CAPACITY / 2, 0.75f, true) {

		private static final long serialVersionUID = 1L;

		
		@Override
		protected boolean removeEldestEntry(
				final LinkedHashMap.Entry<String, Bitmap> eldest) {
			if (size() > CacheImageDownloader.HARD_CACHE_CAPACITY) {
				
				CacheImageDownloader.sSoftBitmapCache.put(eldest.getKey(),
						new SoftReference<Bitmap>(eldest.getValue()));
				return true;
			} else {
				return false;
			}
		}
	};

	
	private final static ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
			CacheImageDownloader.HARD_CACHE_CAPACITY / 2);

	private final Handler purgeHandler = new Handler();

	private final Runnable purger = new Runnable() {
		
		public void run() {
			clearCache();
		}
	};

	public void download(final String url, final ImageView imageView) {

		download(url, imageView, null);
	}

	public void download(final String url, final ImageView imageView,
			final String cookie) {
		resetPurgeTimer();
		final Bitmap bitmap = getBitmapFromCache(url);

		if (bitmap == null) {
			forceDownload(url, imageView, cookie);
		} else {
			CacheImageDownloader.cancelPotentialDownload(url, imageView);
			imageView.setImageBitmap(bitmap);
			imageView.setVisibility(View.VISIBLE);

		}
	}


	private void forceDownload(final String url, final ImageView imageView,
			final String cookie) {
		
		if (url == null) {
			imageView.setImageDrawable(null);
			imageView.setVisibility(View.GONE);
			return;
		}

		if (CacheImageDownloader.cancelPotentialDownload(url, imageView)) {
			final BitmapDownloaderTask task = new BitmapDownloaderTask(
					imageView);
			final DownloadedDrawable downloadedDrawable = new DownloadedDrawable(
					task);
			imageView.setImageDrawable(downloadedDrawable);
			task.execute(url, cookie);
		}
	}

	public void clearCache() {
		sHardBitmapCache.clear();
		CacheImageDownloader.sSoftBitmapCache.clear();
	}

	private void resetPurgeTimer() {
		purgeHandler.removeCallbacks(purger);
		purgeHandler.postDelayed(purger,
				CacheImageDownloader.DELAY_BEFORE_PURGE);
	}

	private static boolean cancelPotentialDownload(final String url,
			final ImageView imageView) {
		final BitmapDownloaderTask bitmapDownloaderTask = CacheImageDownloader
				.getBitmapDownloaderTask(imageView);

		if (bitmapDownloaderTask != null) {
			final String bitmapUrl = bitmapDownloaderTask.url;
			if (bitmapUrl == null || !bitmapUrl.equals(url)) {
				bitmapDownloaderTask.cancel(true);
			} else {
				// The same URL is already being downloaded.
				return false;
			}
		}
		return true;
	}

	private static BitmapDownloaderTask getBitmapDownloaderTask(
			final ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof DownloadedDrawable) {
				final DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	private Bitmap getBitmapFromCache(final String url) {
		// First try the hard reference cache
		synchronized (sHardBitmapCache) {
			final Bitmap bitmap = sHardBitmapCache.get(url);
			if (bitmap != null) {
				
				sHardBitmapCache.remove(url);
				sHardBitmapCache.put(url, bitmap);
				return bitmap;
			}
		}

		// Then try the soft reference cache
		final SoftReference<Bitmap> bitmapReference = CacheImageDownloader.sSoftBitmapCache
				.get(url);
		if (bitmapReference != null) {
			final Bitmap bitmap = bitmapReference.get();
			if (bitmap != null) {
				
				return bitmap;
			} else {
				
				CacheImageDownloader.sSoftBitmapCache.remove(url);
			}
		}

		return null;
	}

	class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		private static final int IO_BUFFER_SIZE = 4 * 1024;
		private String url;
		private final WeakReference<ImageView> imageViewReference;

		public BitmapDownloaderTask(final ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		@Override
		protected Bitmap doInBackground(final String... params) {
			final HttpClient client = new DefaultHttpClient();
			url = params[0];
			final HttpGet getRequest = new HttpGet(url);
			final String cookie = params[1];
			if (cookie != null) {
				getRequest.setHeader("cookie", cookie);
			}

			try {
				final HttpResponse response = client.execute(getRequest);
				final int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					Log.w("CacheImageDownloader", "Error " + statusCode
							+ " while retrieving bitmap from " + url);
					return null;
				}

				final HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream inputStream = null;
					OutputStream outputStream = null;
					try {
						inputStream = entity.getContent();
						final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
						outputStream = new BufferedOutputStream(dataStream,
								BitmapDownloaderTask.IO_BUFFER_SIZE);
						copy(inputStream, outputStream);
						outputStream.flush();

						final byte[] data = dataStream.toByteArray();
						final Bitmap bitmap = BitmapFactory.decodeByteArray(
								data, 0, data.length);
						return bitmap;

					} finally {
						if (inputStream != null) {
							inputStream.close();
						}
						if (outputStream != null) {
							outputStream.close();
						}
						entity.consumeContent();
					}
				}
			} catch (final IOException e) {
				getRequest.abort();
				Log.w(CacheImageDownloader.LOG_TAG,
						"I/O error while retrieving bitmap from " + url, e);
			} catch (final IllegalStateException e) {
				getRequest.abort();
				Log.w(CacheImageDownloader.LOG_TAG, "Incorrect URL: " + url);
			} catch (final Exception e) {
				getRequest.abort();
				Log.w(CacheImageDownloader.LOG_TAG,
						"Error while retrieving bitmap from " + url, e);
			} finally {
				if (client != null) {
					// client.close();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}

			// Add bitmap to cache
			if (bitmap != null) {
				synchronized (sHardBitmapCache) {
					sHardBitmapCache.put(url, bitmap);
				}
			}

			if (imageViewReference != null) {
				final ImageView imageView = imageViewReference.get();
				final BitmapDownloaderTask bitmapDownloaderTask = CacheImageDownloader
						.getBitmapDownloaderTask(imageView);
				
				if (this == bitmapDownloaderTask) {
					imageView.setImageBitmap(bitmap);
					imageView.setVisibility(View.VISIBLE);

				}
			}
		}

		public void copy(final InputStream in, final OutputStream out)
				throws IOException {
			final byte[] b = new byte[BitmapDownloaderTask.IO_BUFFER_SIZE];
			int read;
			while ((read = in.read(b)) != -1) {
				out.write(b, 0, read);
			}
		}
	}

	static class DownloadedDrawable extends ColorDrawable {
		private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

		public DownloadedDrawable(
				final BitmapDownloaderTask bitmapDownloaderTask) {
			super(Color.BLACK);
			bitmapDownloaderTaskReference = new WeakReference<BitmapDownloaderTask>(
					bitmapDownloaderTask);
		}

		public BitmapDownloaderTask getBitmapDownloaderTask() {
			return bitmapDownloaderTaskReference.get();
		}
	}
}
