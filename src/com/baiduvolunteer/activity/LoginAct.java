package com.baiduvolunteer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.baidu.api.Baidu;
import com.baidu.api.BaiduDialog.BaiduDialogListener;
import com.baidu.api.BaiduDialogError;
import com.baidu.api.BaiduException;
import com.baiduvolunteer.MyApplication;

public class LoginAct extends Activity {

	private String clientId = "GzILwHA3pcIrFxjHgeIPgmL6";// KFBF1tbHUoT8qDgQ9ODPUkAs";

	private Baidu baidu = null;

	private boolean isForceLogin = true;

	private boolean isConfirmLogin = true;
	
	private boolean loggedIn = false;

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
						Log.d("test", "complete");
						if(loggedIn){
							return;
						}
						loggedIn = true;
						Intent intent = new Intent(LoginAct.this, HomeAct.class);
						intent.putExtra("baidu", baidu);
						startActivity(intent);
						finish();
					}

					@Override
					public void onBaiduException(BaiduException e) {
						Toast.makeText(LoginAct.this,
								"exception : " + e.getMessage(),
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onError(BaiduDialogError e) {
						new AlertDialog.Builder(LoginAct.this)
								.setPositiveButton("重试",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												Intent intent = new Intent(
														LoginAct.this,
														LoginAct.class);
												startActivity(intent);
												finish();

											}
										})
								.setNegativeButton("退出",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												finish();

											}
										}).setTitle("网络连接失败，请检查网络后重试！")
								.create().show();
					}

					@Override
					public void onCancel() {
						// TODO Auto-generated method stub
						finish();
					}
				});

	}

	@Override
	public void onBackPressed() {

	}

	class MyBaidu extends Baidu {

		public MyBaidu(String clientId, Context context) {
			super(clientId, context);
			// TODO Auto-generated constructor stub
		}

	}
}
