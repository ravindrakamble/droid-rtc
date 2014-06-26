package com.droidrtc.util;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

	private static Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
	}

	public static Context getContext(){
		return mContext;
	}

	public static Context getmContext() {
		return mContext;
	}
}
