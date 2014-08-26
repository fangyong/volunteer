package com.baiduvolunteer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.text.ClipboardManager;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.api.Baidu;
import com.baidu.frontia.FrontiaApplication;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.push.Utils;
import com.baiduvolunteer.config.Config;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.util.ViewUtils;

public class MyApplication extends FrontiaApplication {
	private static MyApplication instance;
	private Baidu baidu;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if(instance==null)
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
