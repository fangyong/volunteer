package com.baiduvolunteer.http;

import java.util.HashMap;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class GetActivityCollectionListRequest extends BaseRequest {

	private String vUid;

	private int size = 0;

	private int page = 0;

	public GetActivityCollectionListRequest setvUid(String vUid) {
		this.vUid = vUid;
		return this;
	}

	public GetActivityCollectionListRequest setSize(int size) {
		this.size = size;
		return this;
	}

	public GetActivityCollectionListRequest setPage(int page) {
		this.page = page;
		return this;
	}

	// public GetActivityCollectionListRequest setStartTimes(long startTimes) {
	// this.startTimes = startTimes;
	// return this;
	// }

	// public GetActivityCollectionListRequest setEndTimes(long endTimes) {
	// this.endTimes = endTimes;
	// return this;
	// }

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
		if (page > 0)
			map.put("page", "" + page);
		if (size > 0)
			map.put("size", "" + size);
	}

	@Override
	protected HttpMethod requestMethod() {
		// TODO Auto-generated method stub
		return HttpMethod.POST;
	}

}
