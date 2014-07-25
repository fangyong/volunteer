package com.baiduvolunteer.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
	public TextView locationLabel;
	public View container;

	public static ActivityListCellHolder create(Context ctx) {
		LayoutInflater inflater = LayoutInflater.from(ctx);
		View rl = inflater.inflate(R.layout.activity_listitem, null);

		ActivityListCellHolder holder = new ActivityListCellHolder();
		holder.container = rl;
		holder.imageView = (ImageView) rl.findViewById(R.id.imageView);
		Log.d("test", "here");
		holder.favIcon = (ImageView) rl.findViewById(R.id.rightIcon);
		Log.d("test", "here1"+ rl.findViewById(R.id.distLabel));
		holder.distLabel = (TextView) rl.findViewById(R.id.distLabel);
		Log.d("test", "here2");
		holder.timeIcon = (ImageView) rl.findViewById(R.id.timeIcon);

		holder.locIcon = (ImageView) rl.findViewById(R.id.locIcon);
		holder.titleLabel = (TextView) rl.findViewById(R.id.titleLabel);

		holder.locationLabel = (TextView) rl.findViewById(R.id.locationLabel);
		holder.timeLabel = (TextView) rl.findViewById(R.id.timeLabel);

		return holder;
	}

}
