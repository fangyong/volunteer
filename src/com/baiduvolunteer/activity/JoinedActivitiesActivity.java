package com.baiduvolunteer.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.baiduvolunteer.R;
import com.baiduvolunteer.adapter.JoinActivityAdapter;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.getJoinedActivityListRequest;
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.view.MyListView;
import com.baiduvolunteer.view.MyListView.OnLoadListener;
import com.baiduvolunteer.view.MyListView.OnRefreshListener;

public class JoinedActivitiesActivity extends BaseActivity {
	private View backButton;
	private MyListView eventsList;
	private JoinActivityAdapter mAdapter;
	private View footerView;
	private ArrayList<ActivityInfo> activityInfoList = new ArrayList<ActivityInfo>();
	private int size = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joinedevents);
		backButton = findViewById(R.id.backButton);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				JoinedActivitiesActivity.this.finish();
			}
		});

		eventsList = (MyListView) findViewById(R.id.eventsList);
		footerView = getLayoutInflater().inflate(R.layout.item_foot, null);
		if (eventsList.getFooterViewsCount() == 0)
			eventsList.addFooterView(footerView);
		mAdapter = new JoinActivityAdapter(JoinedActivitiesActivity.this,
				activityInfoList);
		eventsList.setAdapter(mAdapter);
		eventsList.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				activityInfoList = new ArrayList<ActivityInfo>();
				load(System.currentTimeMillis());
				if (eventsList.getFooterViewsCount() == 0)
					eventsList.addFooterView(footerView);
			}
		});

		eventsList.setOnLoadListener(new OnLoadListener() {

			@Override
			public void onLoad() {
				// long time = activityInfoList.get(activityInfoList.size() -
				// 1).createTime;
				// load(time);
			}
		});

		eventsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(JoinedActivitiesActivity.this,
						ActivityInfoActivity.class);
				intent.putExtra("activity", activityInfoList.get(position - 1));
				startActivity(intent);
			}
		});

		load(System.currentTimeMillis());

	}

	void load(long end) {
		new getJoinedActivityListRequest().setvUid(User.sharedUser().vuid)
				.setSize(size).setEnd(end).setHandler(new ResponseHandler() {

					@Override
					public void handleError(BaseRequest request,
							int statusCode, String errorMsg) {
						// TODO Auto-generated method stub
						super.handleError(request, statusCode, errorMsg);
						eventsList.onRefreshComplete();
						if (eventsList.getFooterViewsCount() > 0)
							eventsList.removeFooterView(footerView);
					}

					@Override
					public void handleResponse(BaseRequest request,
							int statusCode, String errorMsg, String response) {
						try {

							Log.i("response test", response);

							JSONObject ret = new JSONObject(response);
							JSONArray activities = null;
							ret = ret.optJSONObject("result");
							if (ret == null)
								activities = new JSONArray();
							else
								activities = ret.optJSONArray("activities");
							if (activities.length() > 0) {
								for (int i = 0; i < activities.length(); i++) {
									JSONObject activity = activities
											.optJSONObject(i);
									ActivityInfo activityInfo = ActivityInfo
											.createFromJson(activity);
									activityInfo.index = i;
									activityInfoList.add(activityInfo);

								}
								Collections.sort(activityInfoList,
										new Comparator<ActivityInfo>() {

											@Override
											public int compare(
													ActivityInfo lhs,
													ActivityInfo rhs) {
												// TODO Auto-generated method
												// stub
												if (lhs.isLine && !rhs.isLine) {
													return -1;
												} else if (rhs.isLine
														&& !lhs.isLine) {
													return 1;
												} else {
													Date date = new Date();
													if (lhs.endTime
															.before(date)
															&& !rhs.endTime
																	.before(date)) {
														return 1;
													} else if (rhs.endTime
															.before(date)
															&& !lhs.endTime
																	.before(date)) {
														return -1;
													} else {
														return lhs.index
																- rhs.index;
													}
												}
											}
										});
								mAdapter.setActivityList(activityInfoList);
								mAdapter.notifyDataSetChanged();
								if (activities.length() < size) {
									if (activityInfoList.size() > size)
										Toast.makeText(
												JoinedActivitiesActivity.this,
												"已经到底了！", Toast.LENGTH_SHORT)
												.show();
									if (eventsList.getFooterViewsCount() > 0)
										eventsList.removeFooterView(footerView);
								}
							} else {
								// Toast.makeText(JoinedActivitiesActivity.this,
								// "已经到底了！", Toast.LENGTH_LONG).show();
								if (eventsList.getFooterViewsCount() > 0)
									eventsList.removeFooterView(footerView);
							}

							eventsList.onRefreshComplete();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}).start();
	}

}