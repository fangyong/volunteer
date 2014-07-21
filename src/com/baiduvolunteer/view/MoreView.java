package com.baiduvolunteer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baiduvolunteer.R;

public class MoreView extends LinearLayout implements OnClickListener {

	private SettingCell pushSettings;
	private SettingCell feedBack;
	private SettingCell about;
	private SettingCell checkUpdate;
	private LinearLayout logoutButton;

	public MoreView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public MoreView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public MoreView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		pushSettings = (SettingCell) findViewById(R.id.pushSettings);
		feedBack = (SettingCell) findViewById(R.id.feedBack);
		about = (SettingCell) findViewById(R.id.about);
		checkUpdate = (SettingCell) findViewById(R.id.checkUpdate);
		pushSettings.iconView.setImageResource(R.drawable.icon_setting_push);
		feedBack.iconView.setImageResource(R.drawable.icon_setting_feedback);
		about.iconView.setImageResource(R.drawable.icon_setting_about);
		checkUpdate.iconView.setImageResource(R.drawable.icon_setting_update);
		pushSettings.textLabel.setText("推送设置");
		feedBack.textLabel.setText("意见反馈");
		about.textLabel.setText("关于");
		checkUpdate.textLabel.setText("检查更新");
		pushSettings.setOnClickListener(this);
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
		if (v == pushSettings) {

		} else if (v == feedBack) {

		} else if (v == about) {

		} else if (v == checkUpdate) {

		}
	}

	public void onPause() {
		// TODO Auto-generated method stub

	}

	public void onResume() {
		// TODO Auto-generated method stub

	}
}
