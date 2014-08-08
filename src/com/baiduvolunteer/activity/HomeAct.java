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

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.api.AccessTokenManager;
import com.baidu.api.AsyncBaiduRunner;
import com.baidu.api.AsyncBaiduRunner.RequestListener;
import com.baidu.api.Baidu;
import com.baidu.api.BaiduException;
import com.baidu.push.Utils;
import com.baiduvolunteer.MyApplication;
import com.baiduvolunteer.R;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.UnbindBPushRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.LoginRequest;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.util.ViewUtils;
import com.baiduvolunteer.view.ActivitiesView;
import com.baiduvolunteer.view.IndexView;
import com.baiduvolunteer.view.MoreView;
import com.baiduvolunteer.view.UserCenterView;
import com.umeng.update.UmengUpdateAgent;

//import com.baiduvolunteer.http.LoginRequest;

public class HomeAct extends Activity {

	private TabWidget tabWidget;

	private TabHost tabHost;

	private Handler getUserInfoHandler;

	private IndexView indexView;
	private MoreView moreView;
	private ActivitiesView activitiesView;
	private UserCenterView userCenterView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);

		getUserInfoHandler = new Handler();

		Baidu baidu = MyApplication.getApplication().getBaidu();
		if (baidu != null) {
			baidu.init(this);
			AccessTokenManager atm = baidu.getAccessTokenManager();
			// String accessToken = atm.getAccessToken();
			AsyncBaiduRunner runner = new AsyncBaiduRunner(baidu);
			// runner.request(
			// "https://openapi.baidu.com/rest/2.0/passport/users/getInfo",
			// null, "POST", new RequestListener() {
			//
			// @Override
			// public void onIOException(IOException arg0) {
			// // TODO Auto-generated method stub
			// Log.d("test", "ioexception");
			// }
			//
			// @Override
			// public void onComplete(String arg0) {
			// // TODO Auto-generated method stub
			// Log.d("test", "response:" + arg0);
			// }
			//
			// @Override
			// public void onBaiduException(BaiduException arg0) {
			// // TODO Auto-generated method stub
			// Log.d("test",
			// "baidu exception:" + arg0.getMessage());
			// }
			// });
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
										// Toast.makeText(HomeAct.this, arg0,
										// Toast.LENGTH_LONG).show();
										// ;
										JSONObject userinfo = new JSONObject(
												arg0);
										String uid = userinfo.optString("uid");
										// XXX fix later
										// User.sharedUser().vuid = uid;
										String uname = userinfo
												.optString("uname");
										if (User.sharedUser().uname == null) {
											User.sharedUser().uname = uname;
										}
										User.sharedUser().save();

										Log.d("test",
												"user.name "
														+ User.sharedUser().uname);

										Log.i(">>>>>>>>>>>uid", uid);
										LoginRequest loginRequest = new LoginRequest(
												uid, User.sharedUser().uname);
										loginRequest
												.setHandler(new ResponseHandler() {

													@Override
													public void handleResponse(
															BaseRequest request,
															int statusCode,
															String errorMsg,
															String response) {
														Log.d("test",
																"getallinfo response "
																		+ response);
														if (statusCode == 200) {
															try {
																JSONObject object = new JSONObject(
																		response);
																JSONObject result = object
																		.optJSONObject("result");
																if (result != null) {
																	String uid = result
																			.optString("vuid");
																	if (uid != null) {
																		User.sharedUser().vuid = uid;
																		User.sharedUser()
																				.save();
																		User.sharedUser()
																				.syncWithServer();
																		if (!Utils
																				.hasBind(getApplicationContext()))
																			PushManager
																					.startWork(
																							getApplicationContext(),
																							PushConstants.LOGIN_TYPE_API_KEY,
																							Utils.getMetaValue(
																									HomeAct.this,
																									"api_key"));
																	}
																}
															} catch (JSONException e) {
																// TODO
																// Auto-generated
																// catch block
																e.printStackTrace();
															}
														}

														Log.d("test",
																"response:"
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
		}

		tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		tabWidget.setBackgroundColor(0xffffffff);

		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		indexView = (IndexView) findViewById(R.id.tab_index);
		activitiesView = (ActivitiesView) findViewById(R.id.tab_activities);
		userCenterView = (UserCenterView) findViewById(R.id.tab_usercenter);
		moreView = (MoreView) findViewById(R.id.tab_more);
		initTabs();
	}

	void initTabs() {
		View lb1 = getLayoutInflater().inflate(R.layout.label_tabhost, null);
		TextView tx1 = (TextView) lb1.findViewById(R.id.text);
		tx1.setText("首页");
		ImageView iv1 = (ImageView) lb1.findViewById(R.id.img);
		iv1.setImageResource(R.drawable.home_tab_icon);
		View lb2 = getLayoutInflater().inflate(R.layout.label_tabhost, null);
		TextView tx2 = (TextView) lb2.findViewById(R.id.text);
		tx2.setText("活动");
		ImageView iv2 = (ImageView) lb2.findViewById(R.id.img);
		iv2.setImageResource(R.drawable.activity_tab_icon);
		View lb3 = getLayoutInflater().inflate(R.layout.label_tabhost, null);
		TextView tx3 = (TextView) lb3.findViewById(R.id.text);
		tx3.setText("我的");
		ImageView iv3 = (ImageView) lb3.findViewById(R.id.img);
		iv3.setImageResource(R.drawable.mypf_tab_icon);
		View lb4 = getLayoutInflater().inflate(R.layout.label_tabhost, null);
		TextView tx4 = (TextView) lb4.findViewById(R.id.text);
		tx4.setText("更多");
		ImageView iv4 = (ImageView) lb4.findViewById(R.id.img);
		iv4.setImageResource(R.drawable.more_tab_icon);
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
				Log.d("test", "tabindex " + tabId);
				if ("tab_index".equals(tabId)) {
					indexView.onResume();
					moreView.onPause();
					userCenterView.onPause();
					activitiesView.onPause();
				} else if ("tab_activities".equals(tabId)) {
					indexView.onPause();
					moreView.onPause();
					userCenterView.onPause();
					activitiesView.onResume();
				} else if ("tab_usercenter".equals(tabId)) {
					indexView.onPause();
					moreView.onPause();
					userCenterView.onResume();
					activitiesView.onPause();
				} else if ("tab_more".equals(tabId)) {
					indexView.onPause();
					moreView.onResume();
					userCenterView.onPause();
					activitiesView.onPause();
				}
			}
		});
	}

	public void logout(View v) {
		Baidu baidu = MyApplication.getApplication().getBaidu();
		new UnbindBPushRequest().setChannelId(User.sharedUser().channelId)
				.setHandler(new ResponseHandler() {

					@Override
					public void handleResponse(BaseRequest request,
							int statusCode, String errorMsg, String response) {
						// TODO Auto-generated method stub
						Log.d("test", "unbind result " + response);
					}
				}).start();
		if (baidu != null)
			baidu.clearAccessToken();
		User.sharedUser().clear();
		User.sharedUser().bPushUid = null;

		Intent intent = new Intent(HomeAct.this, LoginAct.class);
		intent.putExtra("forceLogin", true);
		startActivity(intent);
		Log.d("test", "ttttttt");
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String tabId = tabHost.getCurrentTabTag();
		if ("tab_index".equals(tabId) && indexView != null) {
			indexView.onResume();
		}
		if ("tab_more".equals(tabId) && moreView != null) {
			moreView.onResume();
		}
		if ("tab_usercenter".equals(tabId) && userCenterView != null) {
			userCenterView.onResume();
		}
		if ("tab_activities".equals(tabId) && activitiesView != null) {
			activitiesView.onResume();
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		String tabId = tabHost.getCurrentTabTag();
		if ("tab_index".equals(tabId) && indexView != null) {
			indexView.onPause();
		}
		if ("tab_more".equals(tabId) && moreView != null) {
			moreView.onPause();
		}
		if ("tab_usercenter".equals(tabId) && userCenterView != null) {
			userCenterView.onPause();
		}
		if ("tab_activities".equals(tabId) && activitiesView != null) {
			activitiesView.onPause();
		}
	}
}
