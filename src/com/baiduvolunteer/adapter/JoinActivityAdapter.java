package com.baiduvolunteer.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baiduvolunteer.R;
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.util.ViewUtils;
import com.baiduvolunteer.view.ActivityListCellHolder;
import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class JoinActivityAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<ActivityInfo> activityList;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.M.dd h:mm");

	public JoinActivityAdapter(Activity activity,
			ArrayList<ActivityInfo> activityList) {
		this.activity = activity;
		this.activityList = activityList;
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
		holder.timeLabel.setText(sdf.format(activityInfo.startTime) + "\n--"
				+ sdf.format(activityInfo.endTime));
		// holder.distLabel.setText(activityInfo.distance + "m");
		holder.favIcon.setTag(Integer.valueOf(position));
		ImageLoader.getInstance().displayImage(activityInfo.iconUrl,
				holder.imageView);
//		ViewUtils.bmUtils.display(holder.imageView, activityInfo.iconUrl);
		holder.favIcon.setVisibility(View.INVISIBLE);
		if (User.sharedUser().currentLatlng != null && activityInfo.latitude != 0) {
			double dist = DistanceUtil.getDistance(new LatLng(
					activityInfo.latitude, activityInfo.longitude), User
					.sharedUser().currentLatlng);
			if (dist < 500) {
				holder.distLabel.setText(String.format("%.0fm", dist));
			} else if (dist < 1000) {
				holder.distLabel.setText(String.format("%.0fm", dist));
			} else if (dist < 10000) {
				holder.distLabel.setText(String.format("%.0fkm", dist / 1000));
			} else {
				holder.distLabel.setText(">10km");
			}
		} else {
			holder.distLabel.setText("未知");
			// holder.distLabel.setText(activityInfo.distance + "m");
		}
		return convertView;
	}

	public ArrayList<ActivityInfo> getActivityList() {
		return activityList;
	}

	public void setActivityList(ArrayList<ActivityInfo> activityList) {
		this.activityList = activityList;
	}

}
