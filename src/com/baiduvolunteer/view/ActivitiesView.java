package com.baiduvolunteer.view;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baiduvolunteer.R;
import com.baiduvolunteer.activity.ActivityInfoActivity;
import com.baiduvolunteer.activity.SearchActivity;
import com.baiduvolunteer.adapter.ActivitiesAdapter;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.GetActivitiesListRequest;
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.util.ViewUtils;
import com.baiduvolunteer.view.MyListView.OnLoadListener;
import com.baiduvolunteer.view.MyListView.OnRefreshListener;

public class ActivitiesView extends LinearLayout {

	private MyListView activityListView;
	private ArrayList<ActivityInfo> activityInfoList = new ArrayList<ActivityInfo>();
	private ActivitiesAdapter mAdapter;
	private EditText searchField;
	private int psize = 10;
	private View footerView;

	public ActivitiesView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ActivitiesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

	}

	// public ActivitiesView(Context context, AttributeSet attrs, int defStyle)
	// {
	// super(context, attrs, defStyle);
	// // TODO Auto-generated constructor stub
	// }

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		activityListView = (MyListView) findViewById(R.id.activityList);
		activityListView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				activityInfoList = new ArrayList<ActivityInfo>();
				loadData(System.currentTimeMillis());
				if (activityListView.getFooterViewsCount() == 0)
					activityListView.addFooterView(footerView);
			}
		});
		activityListView.setOnLoadListener(new OnLoadListener() {

			@Override
			public void onLoad() {
				long time = activityInfoList.get(activityInfoList.size() - 1).createTime;
				loadData(time);
			}
		});

		Activity activity = (Activity) getContext();
		mAdapter = new ActivitiesAdapter(activity, activityInfoList);
		activityListView.setAdapter(mAdapter);
		footerView = ((Activity) getContext()).getLayoutInflater().inflate(
				R.layout.item_foot, null);
		if (activityListView.getFooterViewsCount() == 0)
			activityListView.addFooterView(footerView);
		activityListView.setDivider(getResources().getDrawable(
				R.drawable.listviewdivider));
		activityListView.setDividerHeight(4);
		activityListView.setBackgroundColor(0xfff5f4f1);
		activityListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getContext(),
						ActivityInfoActivity.class);
				intent.putExtra("activity", activityInfoList.get(pos - 1));
				getContext().startActivity(intent);
			}
		});

		loadData(System.currentTimeMillis());
		searchField = (EditText) findViewById(R.id.search);
		searchField.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getContext(), SearchActivity.class);
				getContext().startActivity(intent);
			}
		});
	}

	public void onPause() {
		// TODO Auto-generated method stub

	}

	public void onResume() {
		// TODO Auto-generated method stub

	}

	public void loadData(long end) {
		new GetActivitiesListRequest().setSize(psize).setEnd(end)
				.setHandler(new ResponseHandler() {

					@Override
					public void handleResponse(BaseRequest request,
							int statusCode, String errorMsg, String response) {
						try {
							JSONObject ret = new JSONObject(response);
							JSONArray activities = ret.optJSONObject("result")
									.optJSONArray("activities");
							if (activities.length() > 0) {
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
									activityInfo.startTime = new Date(
											Long.parseLong(activity
													.getString("serviceOpenTime")));
									activityInfo.endTime = new Date(
											Long.parseLong(activity
													.getString("serviceOverTime")));
									activityInfo.createTime = activity
											.optLong("createTime");
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

								}
								mAdapter.setActivitiesList(activityInfoList);
								mAdapter.notifyDataSetChanged();
								activityListView.onRefreshComplete();
							} else {
								Toast.makeText(getContext(), "已经到底了！",
										Toast.LENGTH_LONG).show();
								if (activityListView.getFooterViewsCount() > 0)
									activityListView
											.removeFooterView(footerView);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}).start();
	}

}
