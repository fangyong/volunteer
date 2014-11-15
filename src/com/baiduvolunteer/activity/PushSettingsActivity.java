package com.baiduvolunteer.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.baiduvolunteer.R;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.SaveSettingRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.GetSettingRequest;
import com.baiduvolunteer.util.ViewUtils;

public class PushSettingsActivity extends Activity implements OnClickListener {

	public static final String key_sms = "key_setting_sms";
	public static final String key_push = "key_setting_push";

	private View checkBox1;
	private View checkBox2;
	private View backButton;

	private ImageView pushOn;
	private ImageView pushOff;
	private ImageView smsOn;
	private ImageView smsOff;

	private boolean push;
	private boolean sms;

	private ProgressDialog mpd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pushsettings);
		// SharedPreferences prefs = PreferenceManager
		// .getDefaultSharedPreferences(this);
		// push = prefs.getBoolean("push", true);
		// sms = prefs.getBoolean("sms", true);

		mpd = new ProgressDialog(this);
		mpd.setIndeterminate(true);
		mpd.setCancelable(false);

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
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		mpd.show();
		new GetSettingRequest().setHandler(new ResponseHandler() {

			@Override
			public void handleResponse(BaseRequest request, int statusCode,
					String errorMsg, String response) {
				// TODO Auto-generated method stub
				mpd.dismiss();
				Log.d("test", "settings:" + response);
				try {
					JSONObject result = new JSONObject(response);
					result = result.optJSONObject("result");
					if (result == null)
						return;
					JSONArray kvs = result.optJSONArray("params");
					if (kvs == null)
						return;
					for (int i = 0; i < kvs.length(); i++) {
						JSONObject kv = kvs.optJSONObject(i);
						if (key_push.equals(kv.optString("param", null))) {
							String s = kv.optString("value", "off");
							push = "on".equals(s);
						}
						if (key_sms.equals(kv.optString("param", null))) {
							String s = kv.optString("value", "off");
							sms = "on".equals(s);
						}
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ViewUtils.runInMainThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						pushOn.setVisibility(push ? View.VISIBLE
								: View.INVISIBLE);
						pushOff.setVisibility(push ? View.INVISIBLE
								: View.VISIBLE);
						smsOn.setVisibility(sms ? View.VISIBLE : View.INVISIBLE);
						smsOff.setVisibility(sms ? View.INVISIBLE
								: View.VISIBLE);
					}
				});
			}

			@Override
			public void handleError(BaseRequest request, int statusCode,
					String errorMsg) {
				// TODO Auto-generated method stub
				super.handleError(request, statusCode, errorMsg);
				mpd.dismiss();
			}
		}).start();
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
			// PreferenceManager.getDefaultSharedPreferences(this).edit()
			// .putBoolean("push", push).putBoolean("sms", sms).commit();
			new SaveSettingRequest().setKey(key_push)
					.setValue(push ? "on" : "off")
					.setHandler(new ResponseHandler() {

						@Override
						public void handleResponse(BaseRequest request,
								int statusCode, String errorMsg, String response) {
							// TODO Auto-generated method stub

						}
					}).start();

			new SaveSettingRequest().setKey(key_sms)
					.setValue(sms ? "on" : "off")
					.setHandler(new ResponseHandler() {

						@Override
						public void handleResponse(BaseRequest request,
								int statusCode, String errorMsg, String response) {
							// TODO Auto-generated method stub
						}

						@Override
						public void handleError(BaseRequest request,
								int statusCode, String errorMsg) {
							// TODO Auto-generated method stub
							super.handleError(request, statusCode, errorMsg);
						}
					}).start();

			finish();
		}
	}
}
