package com.baiduvolunteer.http;

import java.util.HashMap;

import android.util.Log;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class GetActivitiesListRequest extends BaseRequest {

	private String vUid;
	private long startTimes;
	private long endTimes;
	private long end;
	private int page;
	private int size = 10;
	private double lat = 361;
	private double lng = 361;

	public GetActivitiesListRequest setLat(double lat) {
		this.lat = lat;
		return this;
	}

	public GetActivitiesListRequest setLng(double lng) {
		this.lng = lng;
		return this;
	}

	public GetActivitiesListRequest setPage(int page) {
		this.page = page;
		return this;
	}

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

	public GetActivitiesListRequest setSize(int size) {
		this.size = size;
		return this;
	}

	public GetActivitiesListRequest setEnd(long end) {
		this.end = end;
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
		Log.i("test activities", map.toString());
		map.put("page", page + "");
		map.put("size", size + "");
		// map.put("end", end + "");
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
