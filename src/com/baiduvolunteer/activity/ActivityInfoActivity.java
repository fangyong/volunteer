package com.baiduvolunteer.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baiduvolunteer.R;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.GetActivityInfoRequest;
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.util.ViewUtils;
import com.baiduvolunteer.view.ListViewCell;

public class ActivityInfoActivity extends BaseActivity implements
		OnClickListener {
	// private ListViewCell infoCell;
	private ListViewCell locationCell;
	private ListViewCell organizerCell;
	private ListViewCell contactCell;

	private TextView activityTitle;
	private ImageView activityPic;
	private TextView activityTime;
	private TextView activityCategory;
	private TextView activityEnrollNumber;
	private TextView activityIntro;

	private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm");

	private Button backButton;
	private Button attendButton;

	private ActivityInfo activityInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		boolean joined = getIntent().getBooleanExtra("joined", false);
		setContentView(R.layout.activity_info);
		activityInfo = (ActivityInfo) getIntent().getSerializableExtra(
				"activity");
		locationCell = (ListViewCell) findViewById(R.id.locationCell);
		organizerCell = (ListViewCell) findViewById(R.id.organizerCell);
		// infoCell = (ListViewCell) findViewById(R.id.infoContainer);
		locationCell.iconView.setImageResource(R.drawable.icon_info_location);
		organizerCell.textLabel.setText("北大青年志愿者协会");
		organizerCell.iconView
				.setImageResource(R.drawable.icon_info_organization);
		contactCell = (ListViewCell) findViewById(R.id.contactCell);
		contactCell.iconView.setImageResource(R.drawable.icon_info_call);
		contactCell.textLabel.setText("18201506318");

		contactCell.setOnClickListener(this);
		organizerCell.setOnClickListener(this);
		backButton = (Button) findViewById(R.id.backBtn);
		backButton.setOnClickListener(this);
		attendButton = (Button) findViewById(R.id.joinButton);
		attendButton.setOnClickListener(this);
		activityTitle = (TextView) findViewById(R.id.activityTitle);
		activityPic = (ImageView) findViewById(R.id.activityPic);
		activityTime = (TextView) findViewById(R.id.activityTime);
		activityCategory = (TextView) findViewById(R.id.activityCategory);
		activityEnrollNumber = (TextView) findViewById(R.id.activityEnrollNumber);
		activityIntro = (TextView) findViewById(R.id.activityIntro);
		if (joined) {
			attendButton.setVisibility(View.GONE);
		}
		new GetActivityInfoRequest().setActivityId(activityInfo.activityID)
				.setHandler(new ResponseHandler() {

					@Override
					public void handleResponse(BaseRequest request,
							int statusCode, String errorMsg,
							final String response) {
						Log.d("activityinfo test", response);
						ViewUtils.runInMainThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									JSONObject activity = new JSONObject(
											response);
									activity = activity.optJSONObject("result");
									activity = activity
											.optJSONObject("activity");
									activityInfo.activityID = activity
											.optString("activityId",
													activityInfo.activityID);
									activityInfo.title = activity
											.getString("actName");
									if (activity.optInt("isLine") == 1)
										activityInfo.isLine = true;
									else
										activityInfo.isLine = false;
									if (activity.optInt("collection") == 1)
										activityInfo.addedToFav = true;
									else
										activityInfo.addedToFav = false;
									activityInfo.publishType = activity
											.getString("publishType");
									activityInfo.contactPhone = activity
											.optString("contactPhone");
									activityInfo.startTime = new Date(
											Long.parseLong(activity
													.getString("serviceOpenTime")));
									activityInfo.endTime = new Date(
											Long.parseLong(activity
													.getString("serviceOverTime")));
									activityInfo.publisher = activity
											.getString("publisher");
									activityInfo.description = activity
											.optString("activityDes");
									activityInfo.iconUrl = activity
											.getString("logo");
									activityInfo.distance = activity
											.optString("distance");
									activityInfo.address = activity
											.optString("serviceAdress");
									activityInfo.currentCount = activity
											.optInt("apply");
									activityInfo.totalCount = activity
											.optInt("recruitment");
									activityInfo.description = activity
											.optString("activityDes");
									activityInfo.field = activity
											.optString("field");
									locationCell.textLabel
											.setText(activityInfo.address);
									activityTime.setText(sdf
											.format(activityInfo.startTime)
											+ "-"
											+ sdf.format(activityInfo.endTime));
									activityTitle.setText(activityInfo.title);
									activityEnrollNumber.setText(String.format(
											"报名人数 %d/%d",
											activityInfo.currentCount,
											activityInfo.totalCount));
									activityCategory
											.setText(activityInfo.field);
									organizerCell.textLabel
											.setText(activityInfo.publisher);
									contactCell.textLabel
											.setText(activityInfo.contactPhone);
									activityIntro
											.setText(activityInfo.description);
									if (activityInfo.iconUrl != null)
										ViewUtils.bmUtils.display(activityPic,
												activityInfo.iconUrl);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
					}
				}).start();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == contactCell) {

			new AlertDialog.Builder(this)
					.setMessage(
							"确认拨打电话"
									+ contactCell.textLabel.getText()
											.toString() + "?")
					.setPositiveButton("拨打",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent(
											Intent.ACTION_CALL);
									intent.setData(Uri.parse("tel:"
											+ contactCell.textLabel.getText()));
									startActivity(intent);
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							}).show();
		} else if (v == organizerCell) {
			Intent intent = new Intent(this, PublisherAct.class);
			intent.putExtra("publisherId", activityInfo.activityID);
			this.startActivity(intent);
		} else if (v == backButton) {
			this.finish();
		} else if (v == attendButton) {
			if (User.sharedUser().phoneNumber == null
					|| User.sharedUser().phoneNumber.isEmpty()) {
				new AlertDialog.Builder(this)
						.setMessage("请先补完个人资料")
						.setPositiveButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								})
						.setNeutralButton("设置",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										Intent intent = new Intent(
												ActivityInfoActivity.this,
												ModifyUserInfoAct.class);
										startActivity(intent);
									}
								}).show();
			} else {

				Toast.makeText(this, "已提交报名请求", Toast.LENGTH_LONG).show();
				attendButton.setEnabled(false);
			}

		}
	}
}
