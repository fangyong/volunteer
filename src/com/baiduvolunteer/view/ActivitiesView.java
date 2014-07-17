package com.baiduvolunteer.view;

import java.util.ArrayList;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baiduvolunteer.R;
import com.baiduvolunteer.activity.ActivityInfoActivity;
import com.baiduvolunteer.adapter.ActivitiesAdapter;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.GetActivitiesListRequest;
import com.baiduvolunteer.model.ActivityInfo;

public class ActivitiesView extends LinearLayout {

	private ListView activityListView;
	private ActivitiesAdapter mAdapter;

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
				Log.d("activity test",response);
				
			}
		}).start();
		ArrayList<ActivityInfo> list = new ArrayList<ActivityInfo>();
		Activity activity = (Activity)getContext();
		mAdapter = new ActivitiesAdapter(activity, list);
		activityListView.setDivider(getResources().getDrawable(
				R.drawable.listviewdivider));
		activityListView.setDividerHeight(4);
		activityListView.setBackgroundColor(0xfff5f4f1);
		activityListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getContext(), ActivityInfoActivity.class);
				getContext().startActivity(intent);
			}
		});
	}

}
