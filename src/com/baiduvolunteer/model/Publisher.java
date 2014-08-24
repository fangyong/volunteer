package com.baiduvolunteer.model;

import java.io.Serializable;

import org.json.JSONObject;

import android.text.TextUtils;

import com.baiduvolunteer.config.Config;

public class Publisher implements Serializable {
	public static enum PublisherType {
		PublisherTypeAPP, PublisherTypeGYX
	}

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

	public String linkEmail;

	public String field;//

	public String pid;// id

	public int numberOfActivities;

	public int memberNumber;

	public String address;

	public String email;

	public int city;

	public int province;

	public double latitude;

	public double longitude;

	public boolean isCollection;

	public int activityNum;// 发布活动数
	public int activityJoinNum;// 活动参加人数

	public void loadFromJson(JSONObject obj) {
		address = obj.optString("adress");
		if (address == null || TextUtils.isEmpty(address)) {
			address = obj.optString("address");
		}
		try {
			city = Integer.valueOf(obj.optString("city"));
		} catch (Exception e) {
			// TODO: handle exception
			city = 0;
		}

		String typeString = obj.optString("publishType");
		if ("gongyixiang".equals(typeString)) {
			publisherType = PublisherType.PublisherTypeGYX;
		} else {
			publisherType = PublisherType.PublisherTypeAPP;
		}

		publishName = obj.optString("institutionsName");
		if (publishName == null || TextUtils.isEmpty(publishName))
			publishName = obj.optString("publisherName");
		pid = obj.optString("publisherId");
		if (pid == null || TextUtils.isEmpty(pid))
			pid = obj.optString("institutionsId");
		logoUrl = obj.optString("logourl");
		if (logoUrl == null || TextUtils.isEmpty(logoUrl)) {
			logoUrl = obj.optString("avatar");
		}
		if (logoUrl != null && !TextUtils.isEmpty(logoUrl)
				&& !logoUrl.startsWith("http")) {
			logoUrl = Config.baseURL + logoUrl;
		}
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

		linkEmail = obj.optString("email", null);
		linkPhone = obj.optString("phone", null);

		latitude = obj.optDouble("latitude", 0);
		longitude = obj.optDouble("longitude", 0);
		size = obj.optString("size");
		if (linkPhone == null)
			linkPhone = obj.optString("contactPhone");
		linkUser = obj.optString("publisherName");
		isCollection = obj.optInt("collection", 0) != 0;
		activityNum = obj.optInt("publishActivityNum", 0);
		activityJoinNum = obj.optInt("publishActivityJoinNum", 0);
		email = obj.optString("email", null);
	}

	public static Publisher createFromJson(JSONObject obj) {
		Publisher publisher = new Publisher();
		publisher.loadFromJson(obj);
		return publisher;
	}
}
