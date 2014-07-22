package com.baiduvolunteer.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.baiduvolunteer.R;
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.view.ActivityListCellHolder;
import com.lidroid.xutils.BitmapUtils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class JoinActivityAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<ActivityInfo> activityList;
	private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm");
	private BitmapUtils bitmapUtils;

	public JoinActivityAdapter(Activity activity,
			ArrayList<ActivityInfo> activityList) {
		this.activity = activity;
		this.activityList = activityList;
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.default_icon);
	}

	@Override
	public int getCount() {
		return activityList.size();
	}

	@Override
	public ActivityInfo getItem(int position) {
		return activityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			ActivityListCellHolder holder = ActivityListCellHolder
					.create(activity);
			convertView = holder.container;
			convertView.setTag(holder);
		}
		ActivityInfo activityInfo = getItem(position);
		ActivityListCellHolder holder = (ActivityListCellHolder) convertView
				.getTag();
		holder.titleLabel.setText(activityInfo.title);
		holder.locationLabel.setText(activityInfo.address);
		holder.timeLabel.setText(sdf.format(activityInfo.startTime) + "-"
				+ sdf.format(activityInfo.endTime));
		holder.distLabel.setText(activityInfo.distance + "m");
		holder.favIcon.setTag(Integer.valueOf(position));
		bitmapUtils.display(holder.imageView, activityInfo.iconUrl);
		holder.favIcon.setVisibility(View.INVISIBLE);
		return convertView;
	}

}
