package com.baiduvolunteer.http;

import java.util.HashMap;

import android.util.Log;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class JointActivityRequest extends BaseRequest {

	private String vuid;
	private String activityId;

	@Override
	protected String url() {
		// TODO Auto-generated method stub
		return "activity";
	}

	@Override
	protected String method() {
		// TODO Auto-generated method stub
		return "joinActivity";
	}

	public JointActivityRequest setVuid(String vuid) {
		this.vuid = vuid;
		return this;
	}

	public JointActivityRequest setActivityId(String activityId) {
		this.activityId = activityId;
		return this;
	}

	@Override
	protected void generateParams(HashMap<String, String> map) {
		map.put("vuid", vuid);
		map.put("activityId", activityId);
	}

	@Override
	protected HttpMethod requestMethod() {
		return HttpMethod.POST;
	}

}
