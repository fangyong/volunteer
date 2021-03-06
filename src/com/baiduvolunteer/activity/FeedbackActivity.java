package com.baiduvolunteer.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baiduvolunteer.R;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.GetUserInfoRequest;
import com.baiduvolunteer.http.SendFeedBackRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.util.ViewUtils;

public class FeedbackActivity extends BaseActivity {
	private EditText feedbackInput;
	private EditText phoneInput;
	private TextView nameLabel;
	private TextView textLimit;
	private Button submitButton;
	private ProgressDialog mpd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		mpd = new ProgressDialog(this);
		mpd.setCancelable(false);
		mpd.setIndeterminate(true);
		findViewById(R.id.backButton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		textLimit = (TextView) findViewById(R.id.textLimit);
		feedbackInput = (EditText) findViewById(R.id.feedBackInput);
		feedbackInput
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						100) });
		feedbackInput.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.length() > 70) {
					textLimit.setText(String.format("%d/100", s.length()));
				} else {
					textLimit.setText(null);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		submitButton = (Button) findViewById(R.id.feedBackButton);
		phoneInput = (EditText) findViewById(R.id.phoneInput);
		nameLabel = (TextView) findViewById(R.id.namelabel);
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(feedbackInput.getText())) {
					Toast.makeText(FeedbackActivity.this, "请输入反馈信息",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					mpd.show();
					new SendFeedBackRequest()
							.setPhone(phoneInput.getText().toString())
							.setContent(feedbackInput.getText().toString())
							.setHandler(new ResponseHandler() {

								@Override
								public void handleResponse(BaseRequest request,
										int statusCode, String errorMsg,
										String response) {
									Log.i("feed back response", response);
									ViewUtils.runInMainThread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											mpd.dismiss();
										}
									});
									// TODO Auto-generated method stub
									try {
										JSONObject result = new JSONObject(
												response);
										boolean success = false;
										JSONObject responseHead = result
												.optJSONObject("responseHead");
										success = responseHead != null
												&& "success".equals(responseHead
														.optString("success",
																null));
										if (success) {
											Toast.makeText(
													FeedbackActivity.this,
													"反馈成功！", Toast.LENGTH_LONG)
													.show();
											finish();
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}

								public void handleError(BaseRequest request,
										int statusCode, String errorMsg) {
									mpd.dismiss();
									super.handleError(request, statusCode,
											errorMsg);
								};
							}).start();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		nameLabel.setText(User.sharedUser().uname);
		if (TextUtils.isEmpty(phoneInput.getText())) {
			phoneInput.setText(User.sharedUser().phoneNumber);
		}

	}

	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		super.onPostResume();
		InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		manager.hideSoftInputFromInputMethod(feedbackInput.getWindowToken(), 0);
		feedbackInput.clearFocus();
		phoneInput.clearFocus();
		submitButton.requestFocus();
	}

}
