package com.baiduvolunteer.http;

import java.util.HashMap;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class GetSettingRequest extends BaseRequest {

	private String key;
	
	public GetSettingRequest setKey(String key) {
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
		return "sysset";
	}

	@Override
	protected void generateParams(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		if(key!=null&&!key.isEmpty())
			map.put("param", key);

	}

	@Override
	protected HttpMethod requestMethod() {
		// TODO Auto-generated method stub
		return HttpMethod.POST;
	}

}
