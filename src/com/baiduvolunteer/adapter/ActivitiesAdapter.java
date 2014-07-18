package com.baiduvolunteer.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.baiduvolunteer.activity.PublisherAct;
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.view.ActivityListCellHolder;

public class ActivitiesAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<ActivityInfo> activitiesList;

	public ActivitiesAdapter(Activity activity, ArrayList<ActivityInfo> list) {
		this.activity = activity;
		this.activitiesList = list;
	}

	@Override
	public int getCount() {
		return activitiesList.size();
	}

	@Override
	public ActivityInfo getItem(int position) {
		return activitiesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			ActivityListCellHolder holder = ActivityListCellHolder
					.createFromInflater(LayoutInflater.from(activity));

			convertView = holder.container;
			convertView.setTag(holder);
		}
		ActivityListCellHolder holder = (ActivityListCellHolder) convertView
				.getTag();
//		holder.imageView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				Intent intent = new Intent(activity, PublisherAct.class);
//				activity.startActivity(intent);
//			}
//		});
		return convertView;
	}

}
