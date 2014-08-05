package com.baiduvolunteer.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baiduvolunteer.R;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.GetUserInfoRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.util.ViewUtils;

public class FeedbackActivity extends BaseActivity {
	private EditText feedbackInput;
	private EditText phoneInput;
	private TextView nameLabel;
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
		phoneInput = (EditText) findViewById(R.id.phoneInput);
		nameLabel = (TextView) findViewById(R.id.namelabel);
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (feedbackInput.getText().toString().isEmpty()) {
					Toast.makeText(FeedbackActivity.this, "请输入反馈信息",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					// TODO add feedback logic
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		nameLabel.setText(User.sharedUser().uname);
		if (phoneInput.getText().toString().isEmpty()) {
			phoneInput.setText(User.sharedUser().phoneNumber);
		}
	}

}
