package com.baiduvolunteer.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baiduvolunteer.R;

public class FeedbackActivity extends BaseActivity {
	private EditText feedbackInput;
	private Button submitButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		findViewById(R.id.backButton).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		feedbackInput = (EditText) findViewById(R.id.feedBackInput);
		submitButton = (Button) findViewById(R.id.feedBackButton);
		submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(feedbackInput.getText().toString().isEmpty()){
					Toast.makeText(FeedbackActivity.this, "请输入反馈信息", Toast.LENGTH_SHORT).show();
					return;
				}else{
					//TODO add feedback logic
				}
			}
		});
	}
}
