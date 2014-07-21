package com.baiduvolunteer.model;

import java.io.Serializable;
import java.util.Date;

import com.baiduvolunteer.util.FileCacheUtil;
import com.baiduvolunteer.util.ViewUtils;

public class User implements Serializable {

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

	public void clear(){
		this.city = 0;
		this.gender = 0;
		this.uname = null;
		this.vuid = null;
		this.save();
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

}
