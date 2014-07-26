package com.baiduvolunteer.activity;

import java.text.SimpleDateFormat;

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
import com.baiduvolunteer.http.JointActivityRequest;
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

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.M.d hh:mm");

	private View backButton;
	private View attendButton;
	private TextView attendBtnText;

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
		// organizerCell.textLabel.setText("北大青年志愿者协会");
		organizerCell.iconView
				.setImageResource(R.drawable.icon_info_organization);
		contactCell = (ListViewCell) findViewById(R.id.contactCell);
		contactCell.iconView.setImageResource(R.drawable.icon_info_call);
		// contactCell.textLabel.setText("18201506318");

		contactCell.setOnClickListener(this);
		organizerCell.setOnClickListener(this);
		backButton = findViewById(R.id.backBtn);
		backButton.setOnClickListener(this);
		attendButton = findViewById(R.id.joinButton);
		attendButton.setOnClickListener(this);
		activityTitle = (TextView) findViewById(R.id.activityTitle);
		activityPic = (ImageView) findViewById(R.id.activityPic);
		activityTime = (TextView) findViewById(R.id.activityTime);
		activityCategory = (TextView) findViewById(R.id.activityCategory);
		activityEnrollNumber = (TextView) findViewById(R.id.activityEnrollNumber);
		activityIntro = (TextView) findViewById(R.id.activityIntro);
		organizerCell.detailIconView
				.setImageResource(R.drawable.icon_cell_detail);
		contactCell.detailIconView
				.setImageResource(R.drawable.icon_cell_detail);
		locationCell.detailIconView
				.setImageResource(R.drawable.icon_cell_detail);
		attendBtnText = (TextView) findViewById(R.id.btnText);
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
									activityInfo.loadFromJson(activity);
									Log.d("test", "isattend"
											+ activityInfo.isAttend);
									locationCell.textLabel
											.setText(activityInfo.address);
									activityTime.setText(sdf
											.format(activityInfo.startTime)
											+ "-"
											+ sdf.format(activityInfo.endTime));
									activityTitle.setText(activityInfo.title);
									activityEnrollNumber.setText(String.format(
											"报名人数： %d/%d",
											activityInfo.currentCount,
											activityInfo.totalCount));
									activityCategory.setText("领域："
											+ activityInfo.field);
									organizerCell.textLabel
											.setText(activityInfo.publisher);
									contactCell.textLabel
											.setText(activityInfo.contactPhone);
									activityIntro
											.setText(activityInfo.description);
									attendButton.setEnabled(true);
									if (activityInfo.iconUrl != null)
										ViewUtils.bmUtils.display(activityPic,
												activityInfo.iconUrl);
									if (activityInfo.isAttend) {
										attendButton
												.setBackgroundColor(0xffe7e7e7);
										attendBtnText.setText("  已报名");
									} else {
										attendButton
												.setBackgroundColor(0xff107cfd);
										attendBtnText.setText("  我要报名");
									}

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
			intent.putExtra("publisherId", activityInfo.organizerID);
			this.startActivity(intent);
		} else if (v == backButton) {
			this.finish();
		} else if (v == attendButton) {
			if (activityInfo.isAttend)
				return;
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
				attendButton.setEnabled(false);
				new JointActivityRequest().setVuid(User.sharedUser().vuid)
						.setActivityId(activityInfo.activityID)
						.setHandler(new ResponseHandler() {

							@Override
							public void handleResponse(BaseRequest request,
									int statusCode, String errorMsg,
									String response) {
								Log.d("test", "join response" + response);
								// Toast.makeText(ActivityInfoActivity.this,
								// response, Toast.LENGTH_LONG).show();
								attendButton.setBackgroundColor(0xffe7e7e7);
								activityInfo.isAttend = true;
								Toast.makeText(ActivityInfoActivity.this,
										"已提交报名请求", Toast.LENGTH_LONG).show();
							}
						}).start();
				;
				// Toast.makeText(this, "已提交报名请求",
				// Toast.LENGTH_LONG).show();

			}

		}
	}
}
