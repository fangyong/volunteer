package com.baiduvolunteer.http;

import java.util.HashMap;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class GetPublisherCollectionsRequest extends BaseRequest {

	private String vUid;
	private int size;
	private int page;

	public GetPublisherCollectionsRequest setSize(int size) {
		this.size = size;
		return this;
	}
	
	public GetPublisherCollectionsRequest setPage(int page) {
		this.page = page;
		return this;
	}
	
	public void setvUid(String vUid) {
		this.vUid = vUid;
	}

	@Override
	protected String url() {
		// TODO Auto-generated method stub
		return "user";
	}

	@Override
	protected String method() {
		// TODO Auto-generated method stub
		return "getPublishCollections";
	}

	@Override
	protected void generateParams(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		map.put("vuid", vUid);
		if(page>0)map.put("page", ""+page);
		if(size>0)map.put("size", ""+size);
	}

	@Override
	protected HttpMethod requestMethod() {
		// TODO Auto-generated method stub
		return HttpMethod.POST;
	}

}
