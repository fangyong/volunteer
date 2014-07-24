package com.baiduvolunteer;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlertDialog;
import android.app.Application;
import android.widget.Toast;

import com.baidu.api.Baidu;
import com.baidu.mapapi.SDKInitializer;
import com.baiduvolunteer.config.Config;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.util.ViewUtils;

public class MyApplication extends Application {
	private static MyApplication instance;
	private Baidu baidu;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SDKInitializer.initialize(this);
		ViewUtils.init(this);
		instance = this;
		Config.sharedConfig().init();
		User.sharedUser().load();
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				// TODO Auto-generated method stub
				Toast.makeText(instance, ex.getMessage(), Toast.LENGTH_LONG)
						.show();

			}
		});

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
