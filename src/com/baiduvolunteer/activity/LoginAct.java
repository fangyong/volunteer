package com.baiduvolunteer.activity;

import com.baidu.api.Baidu;
import com.baidu.api.BaiduDialogError;
import com.baidu.api.BaiduException;
import com.baidu.api.BaiduDialog.BaiduDialogListener;
import com.baiduvolunteer.MyApplication;
import com.baiduvolunteer.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class LoginAct extends Activity {

	private String clientId = "KFBF1tbHUoT8qDgQ9ODPUkAs";

	private Baidu baidu = null;

	private boolean isForceLogin = false;

	private boolean isConfirmLogin = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_login);
		isForceLogin = getIntent().getBooleanExtra("forceLogin", false);
		baidu = new Baidu(clientId, LoginAct.this);
		MyApplication.getApplication().setBaidu(baidu);
		baidu.authorize(LoginAct.this, isForceLogin, isConfirmLogin,
				new BaiduDialogListener() {

					@Override
					public void onComplete(Bundle values) {
						Intent intent = new Intent(LoginAct.this, HomeAct.class);
						intent.putExtra("baidu", baidu);
						startActivity(intent);
						finish();
					}

					@Override
					public void onBaiduException(BaiduException e) {

					}

					@Override
					public void onError(BaiduDialogError e) {

					}

					@Override
					public void onCancel() {
						Intent intent = new Intent(LoginAct.this, HomeAct.class);
						startActivity(intent);
						finish();
					}
				});

	}
}
