package com.baiduvolunteer.http;

import java.util.HashMap;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class BindBPushRequest extends BaseRequest {

	private String oauthId;
	protected String channelId;

	public BindBPushRequest setOauthId(String oauthId) {
		this.oauthId = oauthId;
		return this;
	}

	public BindBPushRequest setChannelId(String channelId) {
		this.channelId = channelId;
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
		return "device";
	}

	@Override
	protected void generateParams(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		map.put("oauthId", oauthId);
		map.put("channelId", channelId);
	}

	@Override
	protected HttpMethod requestMethod() {
		// TODO Auto-generated method stub
		return HttpMethod.POST;
	}

}
