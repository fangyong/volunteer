package com.baiduvolunteer.http;

import java.util.HashMap;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class LoginRequest extends BaseRequest {

	private String uid;

	public LoginRequest(String uid) {
		this.uid = uid;
	}

	@Override
	protected String url() {
		return "app";
	}

	@Override
	protected void generateParams(HashMap<String, String> map) {
		map.put("uid",uid);
//		map.put("signature",SignatureTool.getSignature(map));
		
	}

	@Override
	protected HttpMethod requestMethod() {
		return HttpMethod.POST;
	}
	
	@Override
	protected String method() {
		// TODO Auto-generated method stub
		return "login";
	}

}