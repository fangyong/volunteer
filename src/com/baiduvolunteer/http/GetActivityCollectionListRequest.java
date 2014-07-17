package com.baiduvolunteer.http;

import java.util.HashMap;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class GetActivityCollectionListRequest extends BaseRequest {

	private String vUid;
	private long startTimes;
	private long endTimes;

	public GetActivityCollectionListRequest setvUid(String vUid) {
		this.vUid = vUid;
		return this;
	}

	public GetActivityCollectionListRequest setStartTimes(long startTimes) {
		this.startTimes = startTimes;
		return this;
	}

	public GetActivityCollectionListRequest setEndTimes(long endTimes) {
		this.endTimes = endTimes;
		return this;
	}

	@Override
	protected String url() {
		// TODO Auto-generated method stub
		return "user";
	}

	@Override
	protected String method() {
		// TODO Auto-generated method stub
		return "getActivityCollections";
	}

	@Override
	protected void generateParams(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		map.put("vuid", vUid);
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
