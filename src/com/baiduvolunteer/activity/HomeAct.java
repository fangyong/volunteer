package com.baiduvolunteer.activity;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

import com.baidu.api.AccessTokenManager;
import com.baidu.api.AsyncBaiduRunner;
import com.baidu.api.AsyncBaiduRunner.RequestListener;
import com.baidu.api.Baidu;
import com.baidu.api.BaiduException;
import com.baiduvolunteer.R;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.LoginRequest;
import com.baiduvolunteer.view.IndexView;

//import com.baiduvolunteer.http.LoginRequest;

public class HomeAct extends Activity {

	private TabWidget tabWidget;

	private TabHost tabHost;

	private Baidu baidu = null;

	private Handler getUserInfoHandler;

	private String uid;

	private IndexView indexView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getUserInfoHandler = new Handler();

		Intent intent = getIntent();
		baidu = intent.getParcelableExtra("baidu");
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
								try {
									JSONObject userinfo = new JSONObject(arg0);
									String uid = userinfo.optString("uid");
									LoginRequest loginRequest = new LoginRequest(
											uid);
									loginRequest
											.setHandler(new ResponseHandler() {

												@Override
												public void handleResponse(
														BaseRequest request,
														int statusCode,
														String errorMsg,
														String response) {
													Log.d("test", "response:"
															+ response);

												}
											});
									loginRequest.start();
								} catch (JSONException e) {
									e.printStackTrace();
								}

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
		indexView = (IndexView) findViewById(R.id.tab_index);
		initTabs();
	}

	void initTabs() {
		View lb1 = getLayoutInflater().inflate(R.layout.label_tabhost, null);
		TextView tx1 = (TextView) lb1.findViewById(R.id.text);
		tx1.setText("首页");
		ImageView iv1 = (ImageView) lb1.findViewById(R.id.img);
		iv1.setImageResource(R.drawable.icon_tab_home);
		View lb2 = getLayoutInflater().inflate(R.layout.label_tabhost, null);
		TextView tx2 = (TextView) lb2.findViewById(R.id.text);
		tx2.setText("活动");
		ImageView iv2 = (ImageView) lb2.findViewById(R.id.img);
		iv2.setImageResource(R.drawable.icon_tab_activity);
		View lb3 = getLayoutInflater().inflate(R.layout.label_tabhost, null);
		TextView tx3 = (TextView) lb3.findViewById(R.id.text);
		tx3.setText("我的");
		ImageView iv3 = (ImageView) lb3.findViewById(R.id.img);
		iv3.setImageResource(R.drawable.icon_tab_me);
		View lb4 = getLayoutInflater().inflate(R.layout.label_tabhost, null);
		TextView tx4 = (TextView) lb4.findViewById(R.id.text);
		tx4.setText("更多");
		ImageView iv4 = (ImageView) lb4.findViewById(R.id.img);
		iv4.setImageResource(R.drawable.icon_tab_more);
		tabHost.addTab(tabHost.newTabSpec("tab_index").setIndicator(lb1)
				.setContent(R.id.tab_index));
		tabHost.addTab(tabHost.newTabSpec("tab_activities").setIndicator(lb2)
				.setContent(R.id.tab_activities));
		tabHost.addTab(tabHost.newTabSpec("tab_usercenter").setIndicator(lb3)
				.setContent(R.id.tab_usercenter));
		tabHost.addTab(tabHost.newTabSpec("tab_more").setIndicator(lb4)
				.setContent(R.id.tab_more));
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				if ("tab_index".equals(tabId)) {
					indexView.onResume();
				} else {
					indexView.onPause();
				}
			}
		});
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (indexView != null) {
			indexView.onResume();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (indexView != null) {
			indexView.onPause();
		}
	}
}
