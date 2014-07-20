package com.baiduvolunteer;

import android.app.Application;

import com.baidu.api.Baidu;
import com.baidu.mapapi.SDKInitializer;

public class MyApplication extends Application {
	private static MyApplication instance;
	private Baidu baidu;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SDKInitializer.initialize(this);
		instance = this;
	}

	public void setBaidu(Baidu baidu) {
		this.baidu = baidu;
	}

	public Baidu getBaidu() {
		return baidu;
	}

	public static MyApplication getApplication() {
		return instance;
	}
}
