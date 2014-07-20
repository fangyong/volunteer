package com.baiduvolunteer.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baiduvolunteer.R;

public class PublisherListCellHolder {

	public ImageView imageView;
	public View badgeIcon;
	public TextView titleLabel;
	public TextView activitiesLabel;
	public TextView membersLabel;
	public LinearLayout container;

	public static PublisherListCellHolder create(Context ctx) {
		LayoutInflater inflater = LayoutInflater.from(ctx);
		LinearLayout rl = (LinearLayout) inflater.inflate(
				R.layout.user_listitem, null);
		PublisherListCellHolder holder = new PublisherListCellHolder();
		holder.container = rl;
		holder.imageView = (ImageView) rl.findViewById(R.id.imageView);
		holder.badgeIcon = rl.findViewById(R.id.badgeView);
		holder.titleLabel = (TextView) rl.findViewById(R.id.titleLabel);
		holder.activitiesLabel = (TextView) rl.findViewById(R.id.activityLabel);
		holder.membersLabel = (TextView) rl.findViewById(R.id.memberLabel);
		return holder;
	}

}
