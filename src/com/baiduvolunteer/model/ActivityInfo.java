package com.baiduvolunteer.model;

import java.io.Serializable;
import java.util.Date;

import com.baidu.mapapi.model.LatLng;

public class ActivityInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String activityID;//事件id
	public String organizer;//发布者名字
	public String organizerID;//发布者id
	public LatLng geo;//坐标
	public String address;//地址
	public String field;//领域
	public String title;//名称
	public int totalCount;//总人数
	public int currentCount;//当前人数
	public boolean addedToFav;//是否加入收藏
	public boolean isLine;//下线
	public String description;//事件描述
	public Date createTime;//创建时间
	public Date startTime;//起始时间
	public Date endTime;//终止时间
	public String iconUrl;//事件图标地址
	public String contactPhone;//联系电话
	public String shareUrl;//可选
	public String publishType;//发布类型
	public String publisher;//发布者
	
}
