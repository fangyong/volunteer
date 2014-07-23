package com.baiduvolunteer.model;

import java.io.Serializable;

import org.json.JSONObject;

public class Publisher implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	public static Publisher createFromJson(JSONObject obj) {
		return new Publisher();
	}
}
