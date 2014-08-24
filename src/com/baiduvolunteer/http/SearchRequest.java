package com.baiduvolunteer.http;

import java.util.HashMap;

import android.text.TextUtils;
import android.util.Log;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class SearchRequest extends BaseRequest {
	public static enum SearchType {
		SearchTypeActivity, SearchTypePublisher
	};

	private SearchType searchType;
	private String key;
	private long end;
	private int page;
	private int size = 10;
	private double lat = 361;
	private double lng = 361;

	public SearchRequest setLat(double lat) {
		this.lat = lat;
		return this;
	}

	public SearchRequest setLng(double lng) {
		this.lng = lng;
		return this;
	}

	public SearchRequest setPage(int page) {
		this.page = page;
		return this;
	}

	public SearchRequest setSize(int size) {
		this.size = size;
		return this;
	}

	public SearchRequest setSearchType(SearchType searchType) {
		this.searchType = searchType;
		return this;
	}

	public SearchRequest setEnd(long end) {
		this.end = end;
		return this;
	}

	public SearchRequest setKey(String key) {
		this.key = key;
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
		return "search";
	}

	@Override
	protected void generateParams(HashMap<String, String> map) {
		map.put("type",
				searchType == SearchType.SearchTypeActivity ? "activity"
						: "publisher");
		if (page > 0)
			map.put("page", page + "");
		if (size > 0)
			map.put("size", size + "");
		if (end > 0)
			map.put("end", "" + end);
		if (key != null && !TextUtils.isEmpty(key))
			map.put("key", key);
		if (lat != 361)
			map.put("lat", "" + lat);
		if (lng != 361)
			map.put("lng", "" + lng);
	}

	@Override
	protected HttpMethod requestMethod() {
		// TODO Auto-generated method stub
		return HttpMethod.POST;
	}

}
