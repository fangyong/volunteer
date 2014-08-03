package com.baiduvolunteer.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.baiduvolunteer.R;

public class AboutActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		findViewById(R.id.backButton).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
