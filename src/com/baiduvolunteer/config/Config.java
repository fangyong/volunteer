package com.baiduvolunteer.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

import com.baiduvolunteer.task.LoadCitiesTask;
import com.baiduvolunteer.task.LoadCitiesTask.OnTaskFinishListener;

public class Config implements Serializable {
	public static final String baseURL = "http://115.28.0.232/VolunteerApp/";
	// public static final String baseURL =
	// "http://192.168.1.77:8080/VolunteerApp/";
	public static final String USER_AGENT = "android_client";
	public static final String APP_SECRET = "2d56df9a08100634d51940309237855d";
	public static final int[] scales = { 50, 100, 200, 500, 1000, 2000, 5000,
			10000, 20000, 25000, 50000, 100000, 200000, 500000, 1000000,
			2000000 };

	public static class CityInfo implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public String name;
		public int id;

		public ArrayList<CityInfo> subCityList;

	}

	public HashMap<String, CityInfo> provinceList = new HashMap<String, Config.CityInfo>();

	private Config() {

	}

	private boolean initializing = false;

	public void init() {
		if (initializing)
			return;
		initializing = true;
		LoadCitiesTask task = new LoadCitiesTask();
		task.setOnTaskFinishListener(new OnTaskFinishListener() {

			@Override
			public void onTaskFinish(ArrayList<CityInfo> nodeList) {
				// TODO Auto-generated method stub
				provinceList.clear();
				for (CityInfo info : nodeList) {
					provinceList.put("" + info.id, info);
				}
				Log.d("test", "loadCities completed: "
						+ provinceList.keySet().size());
				initializing = false;
			}
		});
		task.execute();
	}

	public boolean isInitializing() {
		return initializing;
	}

	private static Config instance;

	private static Object lock = new Object();

	public static Config sharedConfig() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null)
					instance = new Config();
			}
		}
		return instance;
	}
}
