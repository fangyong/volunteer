package com.baiduvolunteer.http;

import java.util.HashMap;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class SearchRequest extends BaseRequest {
	public static enum SearchType {
		SearchTypeActivity, SearchTypePublisher
	};

	private SearchType searchType;
	private String key;
	private long end;
	private int size;

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
		if (size > 0)
			map.put("size", size + "");
		if (end > 0)
			map.put("end", "" + end);
		map.put("key", key);
	}

	@Override
	protected HttpMethod requestMethod() {
		// TODO Auto-generated method stub
		return HttpMethod.POST;
	}

}
