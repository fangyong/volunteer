package com.baiduvolunteer.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.baiduvolunteer.R;
import com.baiduvolunteer.http.AddFavRequest;
import com.baiduvolunteer.http.AddFavRequest.AddFavType;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.RemoveFavRequest;
import com.baiduvolunteer.http.RemoveFavRequest.RemoveFavType;
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.util.ViewUtils;
import com.baiduvolunteer.view.ActivityListCellHolder;
import com.lidroid.xutils.BitmapUtils;

public class ActivitiesAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<ActivityInfo> activitiesList;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.M.dd h:mm");
	private BitmapUtils bitmapUtils;
	private ProgressDialog mPd;

	public ActivitiesAdapter(Activity activity, ArrayList<ActivityInfo> list) {
		this.activity = activity;
		this.activitiesList = list;
		bitmapUtils = new BitmapUtils(activity);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.default_icon);
		mPd = new ProgressDialog(activity);
		mPd.setCancelable(false);
		mPd.setIndeterminate(true);
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
		holder.timeLabel.setText(sdf.format(activityInfo.startTime) + "\n--"
				+ sdf.format(activityInfo.endTime));
		holder.distLabel.setText(activityInfo.distance + "m");
		holder.favIcon.setTag(Integer.valueOf(position));
		bitmapUtils.display(holder.imageView, activityInfo.iconUrl);
		holder.favIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Integer pos = (Integer) arg0.getTag();
				final ActivityInfo info = activitiesList.get(pos);

				if (!info.addedToFav) {
					mPd.show();
					new AddFavRequest()
							.setAddType(AddFavType.AddFavTypeActivity)
							.setId(info.activityID)
							.setHandler(new ResponseHandler() {

								@Override
								public void handleResponse(BaseRequest request,
										int statusCode, String errorMsg,
										String response) {
									// TODO Auto-generated method stub
									mPd.dismiss();
									Log.d("test", "add fav result " + response);
									if (statusCode == 200) {
										info.addedToFav = true;
										ViewUtils
												.runInMainThread(new Runnable() {

													@Override
													public void run() {

														// TODO Auto-generated
														// method stub
														notifyDataSetChanged();
													}
												});
									}

								}
							}).start();
				} else {
					new RemoveFavRequest().setId(info.activityID)
							.setRemoveType(RemoveFavType.RemoveFavTypeActivity)
							.setHandler(new ResponseHandler() {

								@Override
								public void handleResponse(BaseRequest request,
										int statusCode, String errorMsg,
										String response) {
									Log.d("test", "remove fav result "
											+ response);
									mPd.dismiss();
									if (statusCode == 200) {
										info.addedToFav = false;
										ViewUtils
												.runInMainThread(new Runnable() {

													@Override
													public void run() {
														mPd.dismiss();
														// TODO Auto-generated
														// method stub
														notifyDataSetChanged();
													}
												});
									}
								}
							}).start();
				}
			}
		});
		return convertView;

	}

	public ArrayList<ActivityInfo> getActivitiesList() {
		return activitiesList;
	}

	public void setActivitiesList(ArrayList<ActivityInfo> activitiesList) {
		this.activitiesList = activitiesList;
	}
}
