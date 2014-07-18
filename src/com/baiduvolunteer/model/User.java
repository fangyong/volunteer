package com.baiduvolunteer.model;

import java.util.Date;

public class User {
	private User() {

	}

	private static Object createLock;

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

	public int vuid;// id
	public int uname;// 用户名
	public int gender;// 性别
	public int province;// 省份
	public int city;// 城市
	public Date registerTime;// 注册时间
	public String phoneNumber;// 手机号

}
