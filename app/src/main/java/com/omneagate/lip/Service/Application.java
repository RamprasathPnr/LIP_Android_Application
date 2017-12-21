/*@author Sathish kumar.R
 * Blaze dream technologies
 * */
package com.omneagate.lip.Service;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.user.lip.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

public class Application extends android.app.Application {
	private static Context mContext;
	private static Application mInstance = null;
	public static final String TAG = Application.class.getSimpleName();
	private static final String PROPERTY_ID = "UA-80361549-1";
	private static Context contextValue;

	public static synchronized Application getInstance() {
		contextValue = mInstance;
		return mInstance;
	}

	private RequestQueue mRequestQueue;

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public enum TrackerName {
		APP_TRACKER,
		GLOBAL_TRACKER,
		ECOMMERCE_TRACKER,
	}

	HashMap<TrackerName, Tracker> mTrackers = new HashMap<>();

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}
		return mRequestQueue;
	}

	@Override
	public void onCreate() {
		mInstance = this;
		mContext = getApplicationContext();
		// The following line triggers the initialization of CrashHandler
		//		Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(mContext));
//		Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(mContext));

		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable e) {
				handleUncaughtException(thread, e);
			}
		});
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		mInstance = null;
		super.onTerminate();
	}

	public void setGoogleTracker(String className) {
		try {
			Tracker t = getTracker(TrackerName.APP_TRACKER);
			t.setScreenName(className);
			t.send(new HitBuilders.AppViewBuilder().build());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized Tracker getTracker(TrackerName trackerId) {
		if (!mTrackers.containsKey(trackerId)) {

			GoogleAnalytics analytics = GoogleAnalytics.getInstance(contextValue);
			Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(R.xml.global_tracker)
					: (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(PROPERTY_ID)
					: analytics.newTracker(R.xml.ecommerce_tracker);
			mTrackers.put(trackerId, t);

		}
		return mTrackers.get(trackerId);
	}

	public void handleUncaughtException(Thread thread, Throwable e) {
		e.printStackTrace(); // not all Android versions will print the stack trace automatically
		Log.e("Error", e.toString(), e);
		Intent intent = new Intent();
		intent.setAction("com.omneagate.SEND_LOG"); // see step 5.
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
		startActivity(intent);
		System.exit(1); // kill off the crashed app
	}
}
