package com.baiduvolunteer.model;

import java.io.Serializable;

import org.json.JSONObject;

import com.baiduvolunteer.http.AddFavRequest.PublisherType;

import android.util.Log;

public class Publisher implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PublisherType publisherType;

	public String publishName;// String,活动发布者

	public String logoUrl;// ——String,头像

	public String mission;// 使命

	public String setUpTime;// 建造时间

	public String size;// 公会规模

	public String linkUser;// 联系人

	public String linkPhone;// 联系方式

	public String field;//

	public String pid;// id

	public int numberOfActivities;

	public int memberNumber;

	public String address;

	public int city;

	public int province;

	public double latitude;

	public double longtitude;

	public void loadFromJson(JSONObject obj) {
		address = obj.optString("adress");
		try {
			city = Integer.valueOf(obj.optString("city"));
		} catch (Exception e) {
			// TODO: handle exception
			city = 0;
		}
		publishName = obj.optString("institutionsName");
		if (publishName == null || publishName.isEmpty())
			publishName = obj.optString("publisherName");
		Log.d("test", "publisherName:" + publishName);
		pid = obj.optString("publisherId");
		if(pid==null||pid.isEmpty())
			pid = obj.optString("institutionsId");
		logoUrl = "http://www.gongyixiang.com"
				+ obj.optString("logourl", "/dend");
		field = obj.optString("filed");
		try {
			province = Integer.valueOf(obj.optString("province"));
		} catch (Exception e) {
			province = 0;
		}
		mission = obj.optString("mission");
		setUpTime = obj.optString("setUpTime");
		try {
			memberNumber = Integer.valueOf(obj.optString("size"));
		} catch (Exception e) {
			memberNumber = 0;
		}

		latitude = obj.optDouble("latitude", 0);
		longtitude = obj.optDouble("longitude", 0);
		size = obj.optString("size");
		linkPhone = obj.optString("contactPhone");
		linkUser = obj.optString("publisherName");
	}

	public static Publisher createFromJson(JSONObject obj) {
		Publisher publisher = new Publisher();
		publisher.loadFromJson(obj);
		return publisher;
	}
}
