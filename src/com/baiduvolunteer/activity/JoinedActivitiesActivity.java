package com.baiduvolunteer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.baiduvolunteer.R;
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.view.ActivityListCellHolder;

public class JoinedActivitiesActivity extends BaseActivity {
	private Button backButton;
	private ListView eventsList;
	private ArrayAdapter<ActivityInfo> mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joinedevents);
		backButton = (Button) findViewById(R.id.backButton);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				JoinedActivitiesActivity.this.finish();
			}
		});
		
		eventsList = (ListView) findViewById(R.id.eventsList);
		
		mAdapter = new ArrayAdapter<ActivityInfo>(this, 0){
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 1;
			}
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView==null){
					ActivityListCellHolder holder = ActivityListCellHolder.create(getContext());
					convertView = holder.container;
					convertView.setTag(holder);
				}
				ActivityListCellHolder holder = (ActivityListCellHolder) convertView.getTag();
				holder.favIcon.setVisibility(View.INVISIBLE);
				return convertView;
			}
		};
		
		eventsList.setAdapter(mAdapter);
		
		eventsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(JoinedActivitiesActivity.this, ActivityInfoActivity.class);
				intent.putExtra("joined", true);
				startActivity(intent);
			}
		});
	}
}