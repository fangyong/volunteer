package com.baiduvolunteer.http;

import java.util.HashMap;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class GetActivitiesListRequest extends BaseRequest {

	private String vUid;
	private long startTimes;
	private long endTimes;

	public GetActivitiesListRequest setvUid(String vUid) {
		this.vUid = vUid;
		return this;
	}

	public GetActivitiesListRequest setStartTimes(long startTimes) {
		this.startTimes = startTimes;
		return this;
	}

	public GetActivitiesListRequest setEndTimes(long endTimes) {
		this.endTimes = endTimes;
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
		return "getActivities";
	}

	@Override
	protected void generateParams(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		if (vUid != null) {
			map.put("vuid", vUid);
		}
		if (startTimes > 0)
			map.put("starttimes", "" + startTimes);
		if (endTimes > 0)
			map.put("endtimes", "" + endTimes);
	}

	@Override
	protected HttpMethod requestMethod() {
		// TODO Auto-generated method stub
		return HttpMethod.POST;
	}

}
