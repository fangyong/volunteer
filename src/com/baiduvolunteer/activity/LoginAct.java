package com.baiduvolunteer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.api.Baidu;
import com.baidu.api.BaiduDialog.BaiduDialogListener;
import com.baidu.api.BaiduDialogError;
import com.baidu.api.BaiduException;
import com.baiduvolunteer.MyApplication;

public class LoginAct extends Activity {

	private String clientId = "GzILwHA3pcIrFxjHgeIPgmL6";//KFBF1tbHUoT8qDgQ9ODPUkAs";

	private Baidu baidu = null;

	private boolean isForceLogin = true;

	private boolean isConfirmLogin = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_login);
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
						// Intent intent = new Intent(LoginAct.this,
						// HomeAct.class);
						// startActivity(intent);
						// finish();
					}
				});

	}

	@Override
	public void onBackPressed() {

	}
	
	class MyBaidu extends Baidu{

		public MyBaidu(String clientId, Context context) {
			super(clientId, context);
			// TODO Auto-generated constructor stub
		}
		
		
	}
}
