package com.baiduvolunteer.activity;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import com.baidu.api.AccessTokenManager;
import com.baidu.api.AsyncBaiduRunner;
import com.baidu.api.AsyncBaiduRunner.RequestListener;
import com.baidu.api.Baidu;
import com.baidu.api.BaiduException;
import com.baiduvolunteer.R;

public class HomeAct extends Activity {

	private TabWidget tabWidget;

	private TabHost tabHost;

	private Baidu baidu = null;

	private Handler getUserInfoHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getUserInfoHandler = new Handler();

		Intent intent = getIntent();
		baidu = intent.getParcelableExtra("baidu");
		// 初始化当前的环境
		if (baidu == null) {
			return;
		}
		baidu.init(this);
		AccessTokenManager atm = baidu.getAccessTokenManager();
		// String accessToken = atm.getAccessToken();
		AsyncBaiduRunner runner = new AsyncBaiduRunner(baidu);
		runner.request(Baidu.LoggedInUser_URL, null, "POST",
				new RequestListener() {

					@Override
					public void onIOException(IOException arg0) {

					}

					@Override
					public void onComplete(final String arg0) {
						getUserInfoHandler.post(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(HomeAct.this, arg0,
										Toast.LENGTH_LONG).show();

							}
						});
					}

					@Override
					public void onBaiduException(BaiduException arg0) {

					}
				});
		tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		tabWidget.setBackgroundColor(0xffffffff);

		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();

		tabHost.addTab(tabHost
				.newTabSpec("tab_index")
				.setIndicator("首頁",
						getResources().getDrawable(R.drawable.ic_launcher))
				.setContent(R.id.tab_index));
		tabHost.addTab(tabHost
				.newTabSpec("tab_activities")
				.setIndicator("活动",
						getResources().getDrawable(R.drawable.ic_launcher))
				.setContent(R.id.tab_activities));
		tabHost.addTab(tabHost
				.newTabSpec("tab_usercenter")
				.setIndicator("我的",
						getResources().getDrawable(R.drawable.ic_launcher))
				.setContent(R.id.tab_usercenter));
		tabHost.addTab(tabHost
				.newTabSpec("tab_more")
				.setIndicator("更多",
						getResources().getDrawable(R.drawable.ic_launcher))
				.setContent(R.id.tab_more));

		// View v = tabWidget.getChildTabViewAt(1);
		//
		// v.setBackgroundColor(0xffffffff);
	}

}
