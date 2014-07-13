package com.baiduvolunteer.http;

import java.util.HashMap;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class TestRequest extends BaseRequest {

	@Override
	protected String url() {
		// TODO Auto-generated method stub
		return "test";
	}

	@Override
	protected void generateParams(HashMap<String, String> map) {
		map.put("test", "sfs");
	}
		
	@Override
	protected HttpMethod getMethod() {
		// TODO Auto-generated method stub
		return HttpMethod.GET;
	}

}
