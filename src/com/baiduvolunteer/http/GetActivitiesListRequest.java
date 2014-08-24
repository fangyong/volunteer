package com.baiduvolunteer.http;

import java.util.HashMap;

import android.util.Log;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class GetActivitiesListRequest extends BaseRequest {

	private String vUid;
	private int page;
	private int size = 10;
	private double lat = 0;
	private double lng = 0;

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

	public GetActivitiesListRequest setSize(int size) {
		this.size = size;
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
		map.put("page", page + "");
		map.put("size", size + "");
		if(lat>0)map.put("lat", ""+lat);
		if(lng>0)map.put("lng", ""+lng);
		// map.put("end", end + "");
	}

	@Override
	protected HttpMethod requestMethod() {
		// TODO Auto-generated method stub
		return HttpMethod.POST;
	}

}
