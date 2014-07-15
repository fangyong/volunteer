package com.baiduvolunteer.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.baiduvolunteer.R;
import com.baiduvolunteer.activity.ActivityInfoActivity;
import com.baiduvolunteer.model.ActivityInfo;

public class ActivitiesView extends LinearLayout {

	private ListView activityListView;
	private ArrayAdapter<ActivityInfo> mAdapter;

	public ActivitiesView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ActivitiesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	// public ActivitiesView(Context context, AttributeSet attrs, int defStyle)
	// {
	// super(context, attrs, defStyle);
	// // TODO Auto-generated constructor stub
	// }

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		activityListView = (ListView) findViewById(R.id.activityList);
		mAdapter = new ArrayAdapter<ActivityInfo>(getContext(), 0) {
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 10;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (convertView == null) {
					ActivityListCellHolder holder = ActivityListCellHolder
							.createFromInflater(LayoutInflater
									.from(getContext()));

					convertView = holder.container;
					convertView.setTag(holder);
				}
				ActivityListCellHolder holder = (ActivityListCellHolder) convertView
						.getTag();
				holder.titleLabel.setText("" + position);
				return convertView;
			}
		};
		activityListView.setAdapter(mAdapter);
		activityListView.setDivider(getResources().getDrawable(
				R.drawable.listviewdivider));
		activityListView.setDividerHeight(20);
		activityListView.setBackgroundColor(0xfff5f4f1);
		activityListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getContext(), ActivityInfoActivity.class);
				getContext().startActivity(intent);
			}
		});
	}

}
