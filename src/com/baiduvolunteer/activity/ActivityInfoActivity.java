package com.baiduvolunteer.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.baiduvolunteer.R;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.view.ListViewCell;

public class ActivityInfoActivity extends BaseActivity implements
		OnClickListener {

	private ListViewCell locationCell;
	private ListViewCell organizerCell;
	private ListViewCell contactCell;
	private Button backButton;
	private Button attendButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		boolean joined = getIntent().getBooleanExtra("joined", false);
		setContentView(R.layout.activity_info);
		locationCell = (ListViewCell) findViewById(R.id.locationCell);
		locationCell.iconView.setImageResource(R.drawable.icon_info_location);
		locationCell.textLabel.setText("北京大学");
		organizerCell = (ListViewCell) findViewById(R.id.organizerCell);
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
		if (joined) {
			attendButton.setVisibility(View.GONE);
		}
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
