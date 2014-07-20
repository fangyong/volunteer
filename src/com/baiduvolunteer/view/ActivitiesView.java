package com.baiduvolunteer.view;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class ActivitiesView extends LinearLayout {

	private ListView activityListView;
	private ArrayList<ActivityInfo> activityInfoList = new ArrayList<ActivityInfo>();
	private ActivitiesAdapter mAdapter;
	private EditText searchField;

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
		activityListView = (ListView) findViewById(R.id.activityList);
		new GetActivitiesListRequest().setHandler(new ResponseHandler() {

			@Override
			public void handleResponse(BaseRequest request, int statusCode,
					String errorMsg, String response) {
				try {
					JSONObject ret = new JSONObject(response);
					JSONArray activities = ret.optJSONObject("result")
							.optJSONArray("activities");
					for (int i = 0; i < activities.length(); i++) {
						JSONObject activity = activities.optJSONObject(i);
						ActivityInfo activityInfo = new ActivityInfo();
						activityInfo.activityID = activity
								.getString("activityId");
						activityInfo.title = activity.getString("actName");
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
						activityInfo.publisher = activity
								.getString("publisher");
						activityInfo.address = activity
								.optString("serviceAdress");
						activityInfoList.add(activityInfo);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
		Activity activity = (Activity) getContext();
		mAdapter = new ActivitiesAdapter(activity, activityInfoList);
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
				getContext().startActivity(intent);
			}
		});
		activityListView.setAdapter(mAdapter);
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
}
