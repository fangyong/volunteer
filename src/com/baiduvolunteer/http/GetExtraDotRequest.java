package com.baiduvolunteer.http;

import java.util.HashMap;

import android.text.TextUtils;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class GetExtraDotRequest extends BaseRequest {

	private double latMin;
	private double latMax;
	private double lngMin;
	private double lngMax;
	private int size = 0;
	private String key;
	private int page = 0;// start from 1

	public GetExtraDotRequest setLatMax(double latMax) {
		this.latMax = latMax;
		return this;
	}

	public GetExtraDotRequest setKey(String key) {
		this.key = key;
		return this;
	}

	public GetExtraDotRequest setLngMax(double lngMax) {
		this.lngMax = lngMax;
		return this;
	}

	public GetExtraDotRequest setLatMin(double latMin) {
		this.latMin = latMin;
		return this;
	}

	public GetExtraDotRequest setLngMin(double lngMin) {
		this.lngMin = lngMin;
		return this;
	}

	public GetExtraDotRequest setPage(int page) {
		this.page = page;
		return this;
	}

	public GetExtraDotRequest setSize(int size) {
		this.size = size;
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
		return "md";
	}

	@Override
	protected void generateParams(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		map.put("latmin", "" + latMin);
		map.put("latmax", "" + latMax);
		map.put("lngmin", "" + lngMin);
		map.put("lngmax", "" + lngMax);
		if (!TextUtils.isEmpty(key)) {
			map.put("key", key);
		}
		if (size > 0)
			map.put("size", size + "");
		if (page > 0)
			map.put("page", "" + page);

	}

	@Override
	protected HttpMethod requestMethod() {
		// TODO Auto-generated method stub
		return HttpMethod.POST;
	}

}
