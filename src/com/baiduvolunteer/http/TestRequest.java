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
	protected String method() {
		// TODO Auto-generated method stub
		return "test";
	}

	@Override
	protected HttpMethod requestMethod() {
		// TODO Auto-generated method stub
		return HttpMethod.GET;
	}

	public static void main(String[] args) {
		TestRequest testRequest = new TestRequest();
		testRequest.start();
		testRequest.setHandler(new ResponseHandler() {

			@Override
			public void handleResponse(BaseRequest request, int statusCode,
					String errorMsg, String response) {
				// TODO Auto-generated method stub

			}
		});

	}

}
