package com.baiduvolunteer.http;

import java.util.HashMap;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class GetPublisherInfoRequest extends BaseRequest {

	private String publisherId;
	
	public GetPublisherInfoRequest setPublisherId(String publisherId) {
		this.publisherId = publisherId;
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
		return "getPublisher";
	}

	@Override
	protected void generateParams(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		map.put("publishId", publisherId);
	}

	@Override
	protected HttpMethod requestMethod() {
		// TODO Auto-generated method stub
		return HttpMethod.POST;
	}

}
