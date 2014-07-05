package com.baiduvolunteer.activity;

import com.baiduvolunteer.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;

public class HomeAct extends Activity {

	private TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();

		tabHost.addTab(tabHost.newTabSpec("tab_index").setIndicator("首頁")
				.setContent(R.id.tab_index));
		tabHost.addTab(tabHost.newTabSpec("tab_activities").setIndicator("活动")
				.setContent(R.id.tab_activities));
		tabHost.addTab(tabHost.newTabSpec("tab_usercenter").setIndicator("我的")
				.setContent(R.id.tab_usercenter));
		tabHost.addTab(tabHost.newTabSpec("tab_more").setIndicator("更多")
				.setContent(R.id.tab_more));
	}
}
