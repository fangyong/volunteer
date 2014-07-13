package com.baiduvolunteer.http;

import com.baiduvolunteer.config.Config;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class HttpUtilsWrapper {
	private static HttpUtilsWrapper sharedInstance;
	private static Object lock = new Object();

	public static HttpUtilsWrapper sharedInstance() {
		if (sharedInstance == null) {
			synchronized (lock) {
				if (sharedInstance == null)
					sharedInstance = new HttpUtilsWrapper();
			}
		}
		return sharedInstance;
	}

	private HttpUtils utils;

	private HttpUtilsWrapper() {
		super();
		utils = new HttpUtils(Config.USER_AGENT);
		utils.configHttpCacheSize(1000000000);
		utils.configDefaultHttpCacheExpiry(1000000000);
		HttpUtils.sHttpCache.setEnabled(HttpMethod.GET, false);
	}

	public void send(HttpMethod method, String url,
			RequestParams requestParams, RequestCallBack<String> callback) {
		utils.send(method, url, requestParams, callback);
	}
}
