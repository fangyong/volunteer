package com.baiduvolunteer.model;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONObject;

import com.baidu.mapapi.model.LatLng;

public class ActivityInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String activityID;// 事件id
	public String organizer;// 发布者名字
	public String organizerID;// 发布者id
	public String distance;// 距离
	public String address;// 地址
	public String field;// 领域
	public String title;// 名称
	public int totalCount;// 总人数
	public int currentCount;// 当前人数
	public boolean addedToFav;// 是否加入收藏
	public boolean isLine;// 下线
	public String description;// 事件描述
	public long createTime;// 创建时间
	public Date startTime;// 起始时间
	public Date endTime;// 终止时间
	public String iconUrl;// 事件图标地址
	public String contactPhone;// 联系电话
	public String shareUrl;// 可选
	public String publishType;// 发布类型
	public String publisher;// 发布者
	public boolean isAttend;// 是否报名
	public double latitude;// 纬度
	public double longitude;//经度

	public static ActivityInfo createFromJson(JSONObject activity) {
		ActivityInfo activityInfo = new ActivityInfo();
		activityInfo.loadFromJson(activity);
		return activityInfo;
	}

	public void loadFromJson(JSONObject activity) {
		try {
			this.activityID = activity.optString("activityId", this.activityID);
			this.title = activity.getString("actName");
			if (activity.optInt("isLine") == 1)
				this.isLine = true;
			else
				this.isLine = false;
			if (activity.optInt("collection") == 1)
				this.addedToFav = true;
			else
				this.addedToFav = false;
			this.latitude = activity.optDouble("latitude");
			this.longitude = activity.optDouble("longitude");
			this.publishType = activity.getString("publishType");
			this.contactPhone = activity.optString("contactPhone");
			this.publisher = activity.optString("publisher");
			this.organizerID = activity.optString("publishId");
			this.description = activity.optString("activityDes");
			this.iconUrl = activity.optString("logo");
			this.distance = activity.optString("distance");
			this.address = activity.optString("serviceAdress");
			this.currentCount = activity.optInt("apply");
			this.totalCount = activity.optInt("recruitment");
			this.description = activity.optString("activityDes");
			this.field = activity.optString("field");
			this.isAttend = activity.optInt("apply", 0) == 1;
			this.startTime = new Date(Long.parseLong(activity
					.getString("serviceOpenTime")));
			this.endTime = new Date(Long.parseLong(activity
					.getString("serviceOverTime")));

			this.createTime = Long.parseLong(activity.optString("createTime",
					"0"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
