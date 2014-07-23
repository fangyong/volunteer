package com.baiduvolunteer.view;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.api.AsyncBaiduRunner;
import com.baidu.api.AsyncBaiduRunner.RequestListener;
import com.baidu.api.Baidu;
import com.baidu.api.BaiduException;
import com.baiduvolunteer.MyApplication;
import com.baiduvolunteer.R;
import com.baiduvolunteer.activity.FavoritesActivity;
import com.baiduvolunteer.activity.JoinedActivitiesActivity;
import com.baiduvolunteer.activity.ModifyUserInfoAct;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.GetUserInfoRequest;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.util.ViewUtils;

public class UserCenterView extends LinearLayout {

	private View favCell;
	private View joinedCell;

	private TextView userNameLabel;
	private ImageView userIcon;
	private TextView joinedCount;
	private TextView favCount;
	private TextView regDate;

	public UserCenterView(Context context) {
		super(context);
	}

	public UserCenterView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void onPause() {
		// TODO Auto-generated method stub

	}

	public void onResume() {
		this.updateInfo();
	}

	private void updateInfo() {
		if (userNameLabel != null) {
			userNameLabel.setText(User.sharedUser().uname);
		}

		new GetUserInfoRequest().setHandler(new ResponseHandler() {

			@Override
			public void handleResponse(BaseRequest request, int statusCode,
					String errorMsg, final String response) {
				// TODO Auto-generated method stub
				Log.d("test", "getInfo request status " + statusCode
						+ " response " + response);
				ViewUtils.runInMainThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (response == null)
							return;
						try {
							JSONObject obj = new JSONObject(response);
							if (obj != null)
								obj = obj.optJSONObject("result");
							if (obj != null) {
								if (favCount != null)
									favCount.setText(""
											+ obj.optInt("collection_num"));
								if (joinedCount != null)
									joinedCount.setText(""
											+ obj.optInt("activity_num"));
								if (regDate != null)
									regDate.setText("已注册 "
											+ obj.optInt("regDay") + " 天");
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
		Baidu baidu = MyApplication.getApplication().getBaidu();
		if (baidu != null) {
			baidu.init(getContext());
			// String accessToken = atm.getAccessToken();
			AsyncBaiduRunner runner = new AsyncBaiduRunner(baidu);
			runner.request(
					"https://openapi.baidu.com/rest/2.0/passport/users/getInfo",
					null, "POST", new RequestListener() {

						@Override
						public void onIOException(IOException arg0) {
							// TODO Auto-generated method stub
							Log.d("test", "ioexception");
						}

						@Override
						public void onComplete(String arg0) {
							// TODO Auto-generated method stub
							Log.d("test", "response:" + arg0);
							try {
								JSONObject obj = new JSONObject(arg0);
								final String portrait = obj
										.optString("portrait");
								if (portrait != null)
									ViewUtils.runInMainThread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											ViewUtils.bmUtils.display(userIcon,
													"http://himg.bdimg.com/sys/portrait/item/"
															+ portrait);
										}
									});

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						@Override
						public void onBaiduException(BaiduException arg0) {
							// TODO Auto-generated method stub
							Log.d("test",
									"baidu exception:" + arg0.getMessage());
						}
					});
		}
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		favCell = this.findViewById(R.id.favCell);
		favCell.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getContext(),
						FavoritesActivity.class);
				getContext().startActivity(intent);
			}
		});
		joinedCell = this.findViewById(R.id.joinedCell);
		joinedCell.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getContext(),
						JoinedActivitiesActivity.class);
				getContext().startActivity(intent);
			}
		});
		this.userNameLabel = (TextView) findViewById(R.id.userNameLabel);
		userIcon = (ImageView) findViewById(R.id.userIcon);
		joinedCount = (TextView) findViewById(R.id.joinedCount);
		favCount = (TextView) findViewById(R.id.favCount);
		regDate = (TextView) findViewById(R.id.regDate);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		updateInfo();
		findViewById(R.id.userinfo_ll).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(getContext(),
								ModifyUserInfoAct.class);
						getContext().startActivity(intent);
					}
				});
	}

}
