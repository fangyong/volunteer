package com.baiduvolunteer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baiduvolunteer.R;

public class PublisherAct extends Activity implements OnClickListener{

	private Button backButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publisher);
		backButton = (Button) findViewById(R.id.button1);
		backButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(backButton == v){
			this.finish();
		}
	}
}
