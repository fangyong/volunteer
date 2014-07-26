package com.baiduvolunteer.activity;

import com.baiduvolunteer.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PushSettingsActivity extends Activity implements OnClickListener {

	private ImageView checkBox1;
	private ImageView checkBox2;
	private View backButton;

	private boolean push;
	private boolean sms;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pushsettings);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		push = prefs.getBoolean("push", true);
		sms = prefs.getBoolean("sms", true);

		checkBox1 = (ImageView) findViewById(R.id.setting_push);
		checkBox2 = (ImageView) findViewById(R.id.setting_sms);
		checkBox1.setImageResource(push ? R.drawable.checkimg_on
				: R.drawable.checkimg_off);
		checkBox2.setImageResource(sms ? R.drawable.checkimg_on
				: R.drawable.checkimg_off);
		checkBox1.setOnClickListener(this);
		checkBox2.setOnClickListener(this);
		backButton = findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == checkBox1) {
			push = !push;
			checkBox1.setImageResource(push ? R.drawable.checkimg_on
					: R.drawable.checkimg_off);
		} else if (v == checkBox2) {
			sms = !sms;
			checkBox2.setImageResource(sms ? R.drawable.checkimg_on
					: R.drawable.checkimg_off);
		} else if (backButton == v) {
			PreferenceManager.getDefaultSharedPreferences(this).edit()
					.putBoolean("push", push).putBoolean("sms", sms).commit();
			finish();
		}
	}
}
