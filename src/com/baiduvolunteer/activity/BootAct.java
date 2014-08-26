package com.baiduvolunteer.activity;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.ClipboardManager;

import com.baidu.mapapi.SDKInitializer;
import com.baiduvolunteer.R;
import com.baiduvolunteer.config.Config;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.util.ViewUtils;

public class BootAct extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if(instance==null)
//			SDKInitializer.initialize(getApplication().getApplicationContext());
		ViewUtils.init(this.getApplicationContext());
		Config.sharedConfig().init();
		User.sharedUser().load();
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread thread, final Throwable ex) {
				// TODO Auto-generated method stub
				new Thread() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Looper.prepare();

						// 设置邮件文本内容
						try {
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
							PrintStream printStream = new PrintStream(bos, true);
							ex.printStackTrace(printStream);
							Intent intent = new Intent();
							// 设置对象动作
							intent.setAction(Intent.ACTION_SEND);
							// 设置对方邮件地址
							intent.putExtra(Intent.EXTRA_EMAIL,
									new String[] { "85395102@qq.com" });
							// 设置标题内容
							// Toast.makeText(instance, bos.toString(),
							// Toast.LENGTH_LONG).show();
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.putExtra(Intent.EXTRA_SUBJECT, "bug report");
							intent.putExtra(Intent.EXTRA_TEXT, bos.toString());
							// startActivity(intent);
							ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
							clipboardManager.setText(bos.toString());
							// new
							// AlertDialog.Builder(instance).setMessage(printStream.toString()).show();
							Looper.loop();

						} catch (Exception e) {
							e.printStackTrace();
						}

						//
						// ex.printStackTrace();
					}
				}.start();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(10);
				// System.exit(0);
			}

		});

		setContentView(R.layout.activity_start_page);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				startActivity(new Intent(BootAct.this, LoginAct.class));
				finish();
			}
		}, 2000);
	}
}
