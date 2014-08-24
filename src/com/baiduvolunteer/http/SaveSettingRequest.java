package com.baiduvolunteer.http;

import java.util.HashMap;

import android.text.TextUtils;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class SaveSettingRequest extends BaseRequest {

	private String key;
	private String value;

	public SaveSettingRequest setKey(String key) {
		this.key = key;
		return this;
	}

	public SaveSettingRequest setValue(String value) {
		this.value = value;
		return this;
	}

	@Override
	protected String url() {
		// TODO Auto-generated method stub
		return "app";
	}

	@Override
	protected String method() {
		// TODO Auto-generated method stub
		return "sysset";
	}

	@Override
	protected void generateParams(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		if (key != null && !TextUtils.isEmpty(key) && value != null
				&& !TextUtils.isEmpty(value)) {
			map.put("param", key);
			map.put("value", value);
		}
	}

	@Override
	protected HttpMethod requestMethod() {
		// TODO Auto-generated method stub
		return HttpMethod.POST;
	}

}
