package com.baiduvolunteer.model;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.GetAllUserInfoRequest;
import com.baiduvolunteer.util.FileCacheUtil;
import com.baiduvolunteer.util.ViewUtils;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User() {

	}

	private static Object createLock = new Object();

	private static User instance;

	public static User sharedUser() {
		if (instance == null) {
			synchronized (createLock) {
				if (instance == null)
					instance = new User();
			}
		}
		return instance;
	}

	public void clear() {
		this.city = 0;
		this.gender = 0;
		this.uname = null;
		this.vuid = null;
		this.phoneNumber = null;
		this.province = 0;
		this.registerTime = null;
		this.save();
	}

	public void syncWithServer() {
		if (this.vuid == null)
			return;
		new GetAllUserInfoRequest().setVUid(this.vuid)
				.setHandler(new ResponseHandler() {

					@Override
					public void handleResponse(BaseRequest request,
							int statusCode, String errorMsg, String response) {
						// TODO Auto-generated method stub
						Log.d("test", "all info:" + response);
						try {
							JSONObject resultObj = new JSONObject(response);
							if (resultObj != null)
								resultObj = resultObj.optJSONObject("result");
							city = Integer.valueOf(resultObj.optString("city"));
							uname = resultObj.optString("nickname");
							phoneNumber = resultObj.optString("phone");
							province = Integer.valueOf(resultObj
									.optString("province"));
							try {
								gender = Integer.valueOf(resultObj
										.optString("sex"));
							} catch (Exception e) {
								//do something
							}
							save();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}).start();
	}

	public void save() {
		FileCacheUtil
				.writeObject(ViewUtils.getContext(), instance, "savedUser");
	}

	public void load() {
		User user = (User) FileCacheUtil.readObject(ViewUtils.getContext(),
				"savedUser");
		if (user != null) {
			instance.uname = user.uname;
			instance.vuid = user.vuid;
			instance.gender = user.gender;
			instance.phoneNumber = user.phoneNumber;
			instance.province = user.province;
			instance.city = user.city;
		}
	}

	public String vuid;// id
	public String uname;// 用户名
	public int gender;// 性别
	public int province;// 省份
	public int city;// 城市
	public Date registerTime;// 注册时间
	public String phoneNumber;// 手机号

	public String buid;// 百度用户id

}
