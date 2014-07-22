package com.baiduvolunteer.activity;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.baiduvolunteer.R;
import com.baiduvolunteer.adapter.JoinActivityAdapter;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.GetActivitiesListRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.getJoinedActivityListRequest;
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.view.ActivityListCellHolder;

public class JoinedActivitiesActivity extends BaseActivity {
	private Button backButton;
	private ListView eventsList;
	private ArrayAdapter<ActivityInfo> mAdapter;
	private ArrayList<ActivityInfo> activityInfoList = new ArrayList<ActivityInfo>();

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

		// mAdapter = new ArrayAdapter<ActivityInfo>(this, 0) {
		// @Override
		// public int getCount() {
		// // TODO Auto-generated method stub
		// return 1;
		// }
		//
		// @Override
		// public View getView(int position, View convertView, ViewGroup parent)
		// {
		// if (convertView == null) {
		// ActivityListCellHolder holder = ActivityListCellHolder
		// .create(getContext());
		// convertView = holder.container;
		// convertView.setTag(holder);
		// }
		// ActivityListCellHolder holder = (ActivityListCellHolder) convertView
		// .getTag();
		// holder.favIcon.setVisibility(View.INVISIBLE);
		// return convertView;
		// }
		// };

		// eventsList.setAdapter(mAdapter);
		//
		eventsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(JoinedActivitiesActivity.this,
						ActivityInfoActivity.class);
				intent.putExtra("activity", activityInfoList.get(position));
				startActivity(intent);
			}
		});

		new getJoinedActivityListRequest().setvUid(User.sharedUser().vuid)
				.setHandler(new ResponseHandler() {

					@Override
					public void handleResponse(BaseRequest request,
							int statusCode, String errorMsg, String response) {
						try {

							Log.i("response test", response);

							JSONObject ret = new JSONObject(response);
							JSONArray activities = ret.optJSONObject("result")
									.optJSONArray("activities");
							for (int i = 0; i < activities.length(); i++) {
								JSONObject activity = activities
										.optJSONObject(i);
								ActivityInfo activityInfo = new ActivityInfo();
								activityInfo.activityID = activity
										.getString("activityId");
								activityInfo.title = activity
										.getString("actName");
								if (activity.optInt("isLine") == 1)
									activityInfo.isLine = true;
								else
									activityInfo.isLine = false;
								if (activity.optInt("collection") == 1)
									activityInfo.addedToFav = true;
								else
									activityInfo.addedToFav = false;
								activityInfo.publishType = activity
										.getString("publishType");
								activityInfo.contactPhone = activity
										.optString("contactPhone");
								activityInfo.startTime = new Date(Long
										.parseLong(activity
												.getString("serviceOpenTime")));
								activityInfo.endTime = new Date(Long
										.parseLong(activity
												.getString("serviceOverTime")));
								activityInfo.publisher = activity
										.getString("publisher");
								activityInfo.description = activity
										.optString("activityDes");
								activityInfo.iconUrl = activity
										.getString("logo");
								activityInfo.distance = activity
										.getString("distance");
								activityInfo.address = activity
										.optString("serviceAdress");
								activityInfo.currentCount = activity
										.optInt("apply");
								activityInfo.totalCount = activity
										.optInt("recruitment");
								activityInfo.description = activity
										.optString("activityDes");
								activityInfo.field = activity
										.optString("field");
								activityInfoList.add(activityInfo);

								eventsList.setAdapter(new JoinActivityAdapter(
										JoinedActivitiesActivity.this,
										activityInfoList));
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}).start();
	}
}