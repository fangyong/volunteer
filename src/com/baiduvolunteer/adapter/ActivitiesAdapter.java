package com.baiduvolunteer.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.baiduvolunteer.R;
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.view.ActivityListCellHolder;
import com.lidroid.xutils.BitmapUtils;

public class ActivitiesAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<ActivityInfo> activitiesList;
	private int[] favStates;
	private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm");
	private BitmapUtils bitmapUtils;

	public ActivitiesAdapter(Activity activity, ArrayList<ActivityInfo> list) {
		this.activity = activity;
		this.activitiesList = list;
		favStates = new int[list.size()];
		bitmapUtils = new BitmapUtils(activity);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.default_icon);
		Arrays.fill(favStates, 0);
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
		ActivityInfo activityInfo = getItem(position);
		if (convertView == null) {
			ActivityListCellHolder holder = ActivityListCellHolder
					.create(activity);

			convertView = holder.container;
			convertView.setTag(holder);
		}
		ActivityListCellHolder holder = (ActivityListCellHolder) convertView
				.getTag();
		holder.favIcon
				.setImageResource(activityInfo.addedToFav ? R.drawable.icon_fav_sel
						: R.drawable.icon_fav);
		holder.titleLabel.setText(activityInfo.title);
		holder.locationLabel.setText(activityInfo.address);
		holder.timeLabel.setText(sdf.format(activityInfo.startTime) + "-"
				+ sdf.format(activityInfo.endTime));
		holder.distLabel.setText(activityInfo.distance + "m");
		holder.favIcon.setTag(Integer.valueOf(position));
		bitmapUtils.display(holder.imageView, activityInfo.iconUrl);
		holder.favIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Integer pos = (Integer) arg0.getTag();
				favStates[pos] ^= 1;
				((ImageView) arg0)
						.setImageResource(favStates[pos] == 0 ? R.drawable.icon_fav
								: R.drawable.icon_fav_sel);
			}
		});
		return convertView;
	}
}
