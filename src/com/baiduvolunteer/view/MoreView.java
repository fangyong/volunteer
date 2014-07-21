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

	private TextView pushSettings;
	private TextView feedBack;
	private TextView about;
	private TextView checkUpdate;
	private Button logoutButton;

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
		pushSettings = (TextView) findViewById(R.id.pushSettings);
		feedBack = (TextView) findViewById(R.id.feedBack);
		about = (TextView) findViewById(R.id.about);
		checkUpdate = (TextView) findViewById(R.id.checkUpdate);
		pushSettings.setOnClickListener(this);
		feedBack.setOnClickListener(this);
		about.setOnClickListener(this);
		checkUpdate.setOnClickListener(this);
//		logoutButton = (Button) findViewById(R.id.logoutBtn);
//		logoutButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Baidu baidu = MyApplication.getApplication().getBaidu();
//				if (baidu != null) {
//					baidu.clearAccessToken();
//				}
//				Intent intent = new Intent(getContext(), LoginAct.class);
//				intent.putExtra("forceLogin", true);
//				getContext().startActivity(intent);
//			}
//		});
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
