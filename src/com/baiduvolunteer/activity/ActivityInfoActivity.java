package com.baiduvolunteer.activity;

import com.baiduvolunteer.R;
import com.baiduvolunteer.view.ListViewCell;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ActivityInfoActivity extends BaseActivity implements
		OnClickListener {

	private ListViewCell locationCell;
	private ListViewCell organizerCell;
	private ListViewCell contactCell;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		locationCell = (ListViewCell) findViewById(R.id.locationCell);
		organizerCell = (ListViewCell) findViewById(R.id.organizerCell);
		contactCell = (ListViewCell) findViewById(R.id.contactCell);
		contactCell.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == contactCell) {
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:" + "15652650348"));
			startActivity(intent);
		}
	}
}
