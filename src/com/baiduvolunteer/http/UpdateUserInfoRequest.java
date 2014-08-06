package com.baiduvolunteer.http;

import java.util.HashMap;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class UpdateUserInfoRequest extends BaseRequest {

	private String vUid;
	private String nickName;
	private int sex;
	private String province;
	private String city;
	private String phone;
	private String email;

	public UpdateUserInfoRequest setvUid(String vUid) {
		this.vUid = vUid;
		return this;
	}

	public UpdateUserInfoRequest setNickName(String nickName) {
		this.nickName = nickName;
		return this;
	}

	public UpdateUserInfoRequest setSex(int sex) {
		this.sex = sex;
		return this;
	}

	public UpdateUserInfoRequest setProvince(String province) {
		this.province = province;
		return this;
	}

	public UpdateUserInfoRequest setCity(String city) {
		this.city = city;
		return this;
	}

	public UpdateUserInfoRequest setPhone(String phone) {
		this.phone = phone;
		return this;
	}
	
	public UpdateUserInfoRequest setEmail(String email) {
		this.email = email;
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
		return "modifyinfo";
	}

	@Override
	protected void generateParams(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		map.put("vuid", this.vUid);
		map.put("nickname",this.nickName);
		map.put("sex", ""+this.sex);
		map.put("province", province);
		map.put("city", city);
		map.put("phone", phone);
		map.put("email", email);
	}

	@Override
	protected HttpMethod requestMethod() {
		// TODO Auto-generated method stub
		return HttpMethod.POST;
	}

}
