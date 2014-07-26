package com.baiduvolunteer.http;

import java.util.HashMap;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class getJoinedActivityListRequest extends BaseRequest {

	private String vUid;
	private long startTimes;
	private long endTimes;
	private int size;
	private long end;

	public getJoinedActivityListRequest setvUid(String vUid) {
		this.vUid = vUid;
		return this;
	}

	public getJoinedActivityListRequest setStartTimes(long startTimes) {
		this.startTimes = startTimes;
		return this;
	}

	public getJoinedActivityListRequest setEndTimes(long endTimes) {
		this.endTimes = endTimes;
		return this;
	}
	
	public getJoinedActivityListRequest setEnd(long end) {
		this.endTimes = end;
		return this;
	}
	
	public getJoinedActivityListRequest setSize(int size) {
		this.size = size;
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
		return "getJoinActivities";
	}

	@Override
	protected void generateParams(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		map.put("vuid", vUid);
		if (end > 0) {
			map.put("end", end + "");
		}
		if (size > 0) {
			map.put("size", size + "");
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
