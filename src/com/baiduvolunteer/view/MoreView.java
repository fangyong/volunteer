package com.baiduvolunteer.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baiduvolunteer.R;
import com.baiduvolunteer.activity.AboutActivity;
import com.baiduvolunteer.activity.FeedbackActivity;
import com.baiduvolunteer.activity.PushSettingsActivity;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

public class MoreView extends LinearLayout implements OnClickListener {

	// private SettingCell pushSettings;
	private SettingCell feedBack;
	private SettingCell about;
	private SettingCell checkUpdate;
	private LinearLayout logoutButton;

	public MoreView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public MoreView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	// public MoreView(Context context, AttributeSet attrs, int defStyle) {
	// super(context, attrs, defStyle);
	// // TODO Auto-generated constructor stub
	// }

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// pushSettings = (SettingCell) findViewById(R.id.pushSettings);
		// pushSettings.setVisibility(View.GONE);
		feedBack = (SettingCell) findViewById(R.id.feedBack);
		about = (SettingCell) findViewById(R.id.about);
		checkUpdate = (SettingCell) findViewById(R.id.checkUpdate);
		// pushSettings.iconView.setImageResource(R.drawable.icon_setting_push);
		feedBack.iconView.setImageResource(R.drawable.icon_setting_feedback);
		about.iconView.setImageResource(R.drawable.icon_setting_about);
		checkUpdate.iconView.setImageResource(R.drawable.icon_setting_update);
		// pushSettings.textLabel.setText("推送设置");
		feedBack.textLabel.setText("意见反馈");
		about.textLabel.setText("关于");
		checkUpdate.textLabel.setText("检查更新");
		// pushSettings.setOnClickListener(this);
		feedBack.setOnClickListener(this);
		about.setOnClickListener(this);
		checkUpdate.setOnClickListener(this);
		// logoutButton = (Button) findViewById(R.id.logoutBtn);
		// logoutButton.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Baidu baidu = MyApplication.getApplication().getBaidu();
		// if (baidu != null) {
		// baidu.clearAccessToken();
		// }
		// Intent intent = new Intent(getContext(), LoginAct.class);
		// intent.putExtra("forceLogin", true);
		// getContext().startActivity(intent);
		// }
		// });
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// if (v == pushSettings) {
		// Intent intent = new Intent(getContext(), PushSettingsActivity.class);
		// getContext().startActivity(intent);
		//
		// } else
		if (v == feedBack) {
			Intent intent = new Intent(getContext(), FeedbackActivity.class);
			getContext().startActivity(intent);

		} else if (v == about) {
			Intent intent = new Intent(getContext(), AboutActivity.class);
			getContext().startActivity(intent);
		} else if (v == checkUpdate) {
			UmengUpdateAgent.setDefault();
			UmengUpdateAgent.update(getContext());
			UmengUpdateAgent.setUpdateOnlyWifi(false);
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

				@Override
				public void onUpdateReturned(int updateStatus,
						UpdateResponse updateInfo) {
					// if (arg1 == null || !arg1.hasUpdate)
					// Toast.makeText(getContext(), "未检测到更新版本",
					// Toast.LENGTH_LONG).show();
					switch (updateStatus) {
					case 0: // 有更新
						UmengUpdateAgent.showUpdateDialog(getContext(),
								updateInfo);
						break;
					case 1: // 无更新
						Toast.makeText(getContext(), "当前已是最新版",
								Toast.LENGTH_LONG).show();
						break;
					case 2: // 如果设置为wifi下更新且wifi无法打开时调用
						Toast.makeText(getContext(), "无法连接网络",
								Toast.LENGTH_LONG).show();
						break;
					case 3: // 连接超时
						Toast.makeText(getContext(), "连接超时，请稍候重试",
								Toast.LENGTH_LONG).show();
						break;
					}
				}

			});
		}
	}

	public void onPause() {
		// TODO Auto-generated method stub

	}

	public void onResume() {
		// TODO Auto-generated method stub

	}
}
