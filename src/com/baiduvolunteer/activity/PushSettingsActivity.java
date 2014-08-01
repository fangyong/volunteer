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

	private View checkBox1;
	private View checkBox2;
	private View backButton;

	private ImageView pushOn;
	private ImageView pushOff;
	private ImageView smsOn;
	private ImageView smsOff;

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

		checkBox1 = findViewById(R.id.setting_push);
		checkBox2 = findViewById(R.id.setting_sms);
		pushOn = (ImageView) findViewById(R.id.setting_pushon);
		pushOff = (ImageView) findViewById(R.id.setting_pushoff);
		smsOn = (ImageView) findViewById(R.id.setting_smson);
		smsOff = (ImageView) findViewById(R.id.setting_smsoff);
		pushOn.setVisibility(push ? View.VISIBLE : View.INVISIBLE);
		pushOff.setVisibility(push ? View.INVISIBLE : View.VISIBLE);
		smsOn.setVisibility(sms ? View.VISIBLE : View.INVISIBLE);
		smsOff.setVisibility(sms ? View.INVISIBLE : View.VISIBLE);
		// checkBox2.setImageResource(sms ? R.drawable.checkimg_on
		// : R.drawable.checkimg_off);
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
			pushOn.setVisibility(push ? View.VISIBLE : View.INVISIBLE);
			pushOff.setVisibility(push ? View.INVISIBLE : View.VISIBLE);
		} else if (v == checkBox2) {
			sms = !sms;
			smsOn.setVisibility(sms ? View.VISIBLE : View.INVISIBLE);
			smsOff.setVisibility(sms ? View.INVISIBLE : View.VISIBLE);
		} else if (backButton == v) {
			PreferenceManager.getDefaultSharedPreferences(this).edit()
					.putBoolean("push", push).putBoolean("sms", sms).commit();
			finish();
		}
	}
}
