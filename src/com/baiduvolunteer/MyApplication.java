package com.baiduvolunteer;

import com.baidu.mapapi.SDKInitializer;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SDKInitializer.initialize(this);
		Log.d("test", "app create");
	}
}
