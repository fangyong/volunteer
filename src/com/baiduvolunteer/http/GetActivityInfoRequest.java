package com.baiduvolunteer.http;

import java.util.HashMap;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class GetActivityInfoRequest extends BaseRequest {

	private long activityId;

	public GetActivityInfoRequest setActivityId(long activityId) {
		this.activityId = activityId;
		return this;
	}

	@Override
	protected String url() {
		// TODO Auto-generated method stub
		return "activity";
	}

	@Override
	protected String method() {
		// TODO Auto-generated method stub
		return "getActivity";
	}

	@Override
	protected void generateParams(HashMap<String, String> map) {
		map.put("activityId", "" + activityId);

	}

	@Override
	protected HttpMethod requestMethod() {
		// TODO Auto-generated method stub
		return HttpMethod.POST;
	}

}
