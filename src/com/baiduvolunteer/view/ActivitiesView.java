package com.baiduvolunteer.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

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
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baiduvolunteer.R;
import com.baiduvolunteer.activity.ActivityInfoActivity;
import com.baiduvolunteer.activity.SearchActivity;
import com.baiduvolunteer.adapter.ActivitiesAdapter;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.GetActivitiesListRequest;
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.util.ViewUtils;
import com.baiduvolunteer.view.MyListView.OnLoadListener;
import com.baiduvolunteer.view.MyListView.OnRefreshListener;

public class ActivitiesView extends LinearLayout {

	private MyListView activityListView;
	private ArrayList<ActivityInfo> activityInfoList = new ArrayList<ActivityInfo>();
	private ActivitiesAdapter mAdapter;
	private EditText searchField;
	private int page = 1;
	private int psize = 10;
	private View footerView;
	private Toast mToast;

	private ActivityInfo lastActivity;

	private HashMap<String, ActivityInfo> hashData = new HashMap<String, ActivityInfo>();

	public ActivitiesView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ActivitiesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

	}

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
				hashData = new HashMap<String, ActivityInfo>();
				page = 1;
				loadData(page);
				if (activityListView.getFooterViewsCount() == 0)
					activityListView.addFooterView(footerView);
			}
		});
		activityListView.setOnLoadListener(new OnLoadListener() {

			@Override
			public void onLoad() {
				// long time = lastActivity.createTime;
				page++;
				loadData(page);
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

		mToast = Toast.makeText(getContext(), "已经到底了！", Toast.LENGTH_SHORT);
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
		loadData(page);
	}

	public void loadData(int page) {
		if (User.sharedUser().currentLatlng != null)
			new GetActivitiesListRequest()
					.setLat(User.sharedUser().currentLatlng.latitude)
					.setLng(User.sharedUser().currentLatlng.longitude)
					.setSize(psize).setPage(page)
					.setHandler(new ResponseHandler() {

						@Override
						public void handleResponse(BaseRequest request,
								int statusCode, String errorMsg, String response) {
							Log.d("test activities", "getActivities response:"
									+ response);
							try {
								JSONObject ret = new JSONObject(response);
								ret = ret.optJSONObject("result");
								JSONArray activities = null;
								if (ret != null)
									activities = ret.optJSONArray("activities");
								if (activities != null
										&& activities.length() > 0) {
									for (int i = 0; i < activities.length(); i++) {
										JSONObject activity = activities
												.optJSONObject(i);
										ActivityInfo activityInfo = ActivityInfo
												.createFromJson(activity);
										if (!isRepeat(hashData,
												activityInfo.activityID)) {
											activityInfoList.add(activityInfo);
											hashData.put(
													activityInfo.activityID,
													activityInfo);
										}

									}
									// lastActivity = activityInfoList
									// .get(activityInfoList.size() - 1);
									// Collections.sort(activityInfoList,
									// new Comparator<ActivityInfo>() {
									// @Override
									// public int compare(
									// ActivityInfo lhs,
									// ActivityInfo rhs) {
									// // TODO Auto-generated method
									// // stub
									// if (lhs.latitude == 0
									// && rhs.latitude == 0) {
									// return rhs.activityID
									// .compareTo(lhs.activityID);
									// } else {
									// if (User.sharedUser().lastLatlng == null)
									// {
									// return lhs.activityID
									// .compareTo(rhs.activityID);
									// } else if (lhs.latitude != 0
									// && rhs.latitude != 0) {
									// return (int) (DistanceUtil.getDistance(
									// User.sharedUser().lastLatlng,
									// new LatLng(
									// lhs.latitude,
									// lhs.longitude)) -
									// DistanceUtil.getDistance(
									// User.sharedUser().lastLatlng,
									// new LatLng(
									// rhs.latitude,
									// rhs.longitude)));
									// } else {
									// return (lhs.latitude == 0) ? 1
									// : -1;
									// }
									// }
									// }
									// });
									mAdapter.setActivitiesList(activityInfoList);
									mAdapter.notifyDataSetChanged();
								} else {
									mToast.cancel();
									mToast.show();
									// if
									// (activityListView.getFooterViewsCount() >
									// 0)
									// activityListView
									// .removeFooterView(footerView);
								}
								activityListView.onRefreshComplete();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (activityListView.getFooterViewsCount() > 0)
								activityListView.removeFooterView(footerView);

						}

						@Override
						public void handleError(BaseRequest request,
								int statusCode, String errorMsg) {
							// TODO Auto-generated method stub
							super.handleError(request, statusCode, errorMsg);
						}
					}).start();
		else
			new GetActivitiesListRequest().setSize(psize).setPage(page)
					.setHandler(new ResponseHandler() {

						@Override
						public void handleResponse(BaseRequest request,
								int statusCode, String errorMsg, String response) {
							Log.d("test activities", "getActivities response:"
									+ response);
							try {
								JSONObject ret = new JSONObject(response);
								ret = ret.optJSONObject("result");
								JSONArray activities = null;
								if (ret != null)
									activities = ret.optJSONArray("activities");
								if (activities != null
										&& activities.length() > 0) {
									for (int i = 0; i < activities.length(); i++) {
										JSONObject activity = activities
												.optJSONObject(i);
										ActivityInfo activityInfo = ActivityInfo
												.createFromJson(activity);
										if (!isRepeat(hashData,
												activityInfo.activityID)) {
											activityInfoList.add(activityInfo);
											hashData.put(
													activityInfo.activityID,
													activityInfo);
										}

									}
									// lastActivity = activityInfoList
									// .get(activityInfoList.size() - 1);
									// Collections.sort(activityInfoList,
									// new Comparator<ActivityInfo>() {
									// @Override
									// public int compare(
									// ActivityInfo lhs,
									// ActivityInfo rhs) {
									// // TODO Auto-generated method
									// // stub
									// if (lhs.latitude == 0
									// && rhs.latitude == 0) {
									// return rhs.activityID
									// .compareTo(lhs.activityID);
									// } else {
									// if (User.sharedUser().lastLatlng == null)
									// {
									// return lhs.activityID
									// .compareTo(rhs.activityID);
									// } else if (lhs.latitude != 0
									// && rhs.latitude != 0) {
									// return (int) (DistanceUtil.getDistance(
									// User.sharedUser().lastLatlng,
									// new LatLng(
									// lhs.latitude,
									// lhs.longitude)) -
									// DistanceUtil.getDistance(
									// User.sharedUser().lastLatlng,
									// new LatLng(
									// rhs.latitude,
									// rhs.longitude)));
									// } else {
									// return (lhs.latitude == 0) ? 1
									// : -1;
									// }
									// }
									// }
									// });
									mAdapter.setActivitiesList(activityInfoList);
									mAdapter.notifyDataSetChanged();
								} else {
									mToast.cancel();
									mToast.show();
									// if
									// (activityListView.getFooterViewsCount() >
									// 0)
									// activityListView
									// .removeFooterView(footerView);
								}
								activityListView.onRefreshComplete();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (activityListView.getFooterViewsCount() > 0)
								activityListView.removeFooterView(footerView);

						}

						@Override
						public void handleError(BaseRequest request,
								int statusCode, String errorMsg) {
							// TODO Auto-generated method stub
							super.handleError(request, statusCode, errorMsg);
						}
					}).start();
	}

	boolean isRepeat(HashMap<String, ActivityInfo> hashData, String key) {
		if (hashData.get(key) == null)
			return false;
		else
			return true;
	}
}
