package com.baiduvolunteer.http;

import java.util.HashMap;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**app?method=feedback&vuid=XX&phone=XX
 * 
 * @author zhujun
 *
 */
public class SendFeedBackRequest extends BaseRequest {

	@Override
	protected String url() {
		// TODO Auto-generated method stub
		return "app";
	}
	
	private String phone;
	
	private String content;
	
	public SendFeedBackRequest setContent(String content) {
		this.content = content;
		return this;
	}
	
	public SendFeedBackRequest setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	@Override
	protected String method() {
		// TODO Auto-generated method stub
		return "feedback";
	}

	@Override
	protected void generateParams(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		if(this.phone!=null&&!this.phone.isEmpty()){
			map.put("phone", phone);
		}
		map.put("content", content);
	}

	@Override
	protected HttpMethod requestMethod() {
		// TODO Auto-generated method stub
		return HttpMethod.POST;
	}

}
