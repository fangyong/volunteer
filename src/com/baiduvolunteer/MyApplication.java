package com.baiduvolunteer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.text.ClipboardManager;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.api.Baidu;
import com.baidu.frontia.FrontiaApplication;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.push.Utils;
import com.baiduvolunteer.config.Config;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.util.ViewUtils;

public class MyApplication extends FrontiaApplication {
	private static MyApplication instance;
	private Baidu baidu;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SDKInitializer.initialize(this);
		ViewUtils.init(this);
		instance = this;
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
						try{
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
//							Toast.makeText(instance, bos.toString(), Toast.LENGTH_LONG).show();
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.putExtra(Intent.EXTRA_SUBJECT, "bug report");
							intent.putExtra(Intent.EXTRA_TEXT, bos.toString());
//							startActivity(intent);
							ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);  
							clipboardManager.setText(bos.toString()); 
//							new AlertDialog.Builder(instance).setMessage(printStream.toString()).show();
							Looper.loop();
							
						}catch(Exception e){
							e.printStackTrace();
						}
						
//						
//						ex.printStackTrace();
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
//				System.exit(0);
			}
			
		});
		
	}

	public void setBaidu(Baidu baidu) {
		this.baidu = baidu;
	}

	public Baidu getBaidu() {
		return baidu;
	}

	public static MyApplication getApplication() {
		return instance;
	}
}
