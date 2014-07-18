package com.baiduvolunteer.view;

import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baiduvolunteer.R;

public class ActivityListCellHolder {

	public ImageView imageView;
	public ImageView favIcon;
	public ImageView timeIcon;
	public ImageView locIcon;
	public TextView titleLabel;
	public TextView distLabel;
	public TextView timeLabel;
	public RelativeLayout container;

	public static ActivityListCellHolder createFromInflater(LayoutInflater inflater) {
		RelativeLayout rl = (RelativeLayout) inflater.inflate(
				R.layout.activity_listitem, null);
		ActivityListCellHolder holder = new ActivityListCellHolder();
		holder.container = rl;
		holder.imageView = (ImageView) rl.findViewById(R.id.imageView);
		holder.favIcon = (ImageView) rl.findViewById(R.id.rightIcon);
		holder.timeIcon = (ImageView) rl.findViewById(R.id.timeIcon);
		holder.locIcon = (ImageView) rl.findViewById(R.id.locIcon);
		holder.titleLabel = (TextView) rl.findViewById(R.id.titleLabel);
		holder.distLabel = (TextView) rl.findViewById(R.id.distLabel);
		holder.timeLabel = (TextView) rl.findViewById(R.id.timeLabel);
		return holder;
	}

}