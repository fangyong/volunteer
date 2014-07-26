package com.baiduvolunteer.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.baiduvolunteer.R;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.GetActivityCollectionListRequest;
import com.baiduvolunteer.http.GetPublisherCollectionsRequest;
import com.baiduvolunteer.http.MD5;
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.model.Publisher;
import com.baiduvolunteer.util.ViewUtils;
import com.baiduvolunteer.view.ActivityListCellHolder;
import com.baiduvolunteer.view.PublisherListCellHolder;

public class FavoritesActivity extends Activity implements OnClickListener {

	private View backButton;
	private TextView tab1;
	private TextView tab2;
	private View indicator1;
	private View indicator2;
	private ListView favList;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	private ArrayList<Publisher> publishers = new ArrayList<Publisher>();
	private ArrayList<ActivityInfo> activities = new ArrayList<ActivityInfo>();
	private ArrayAdapter<Object> mAdapter;

	private int selectIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);
		backButton = findViewById(R.id.backButton);
		tab1 = (TextView) findViewById(R.id.title1);
		tab2 = (TextView) findViewById(R.id.title2);
		indicator1 = findViewById(R.id.indicator1);
		indicator2 = findViewById(R.id.indicator2);
		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		favList = (ListView) findViewById(R.id.favList);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FavoritesActivity.this.finish();
			}
		});
		mAdapter = new ArrayAdapter<Object>(this, 0) {
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return selectIndex == 0 ? activities.size() : publishers.size();
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (selectIndex == 0) {
					ActivityInfo info = activities.get(position);
					ActivityListCellHolder holder = ActivityListCellHolder
							.create(getContext());
					holder.favIcon.setVisibility(View.INVISIBLE);
					holder.titleLabel.setText(info.title);
					holder.timeLabel.setText(sdf.format(info.startTime) + "-"
							+ sdf.format(info.endTime));
					holder.locationLabel.setText(info.address);
					return holder.container;
				} else {
					Publisher publisher = publishers.get(position);
					PublisherListCellHolder holder = PublisherListCellHolder
							.create(getContext());
					Log.d("test", "publisherName" + publisher.publishName);
					holder.titleLabel.setText(publisher.publishName);
					holder.activitiesLabel.setText(String.format("发起%d个活动",
							publisher.activityNum));
					holder.membersLabel.setText(String.format("共%d个人参加",
							publisher.activityJoinNum));
					ViewUtils.bmUtils.display(holder.imageView, publisher.logoUrl);
					return holder.container;
				}
			}
		};

		favList.setAdapter(mAdapter);
		favList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (selectIndex == 0) {
					ActivityInfo info = activities.get(position);
					Intent intent = new Intent(FavoritesActivity.this,
							ActivityInfoActivity.class);
					intent.putExtra("activity", info);
					startActivity(intent);
				} else {
					Publisher publisher = publishers.get(position);
					Intent intent = new Intent(FavoritesActivity.this,
							PublisherAct.class);
					intent.putExtra("publisherId", publisher.pid);
					startActivity(intent);
				}

			}
		});
	}

	public void setSelectIndex(int selectIndex) {
		boolean changed = this.selectIndex != selectIndex;
		this.selectIndex = selectIndex;
		if (selectIndex == 0) {
			tab1.setTextColor(0xff5190fc);
			tab2.setTextColor(0xff000000);
			indicator1.setBackgroundColor(0xff107cfd);
			indicator2.setBackgroundColor(0x00000000);
			tab1.setClickable(false);
			tab2.setClickable(true);
		} else {
			tab2.setTextColor(0xff5190fc);
			tab1.setTextColor(0xFF000000);
			indicator2.setBackgroundColor(0xff107cfd);
			indicator1.setBackgroundColor(0x00000000);
			tab2.setClickable(false);
			tab1.setClickable(true);
		}
		if (changed)
			mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new GetActivityCollectionListRequest().setHandler(
				new ResponseHandler() {

					@Override
					public void handleResponse(BaseRequest request,
							int statusCode, String errorMsg, String response) {
						// TODO Auto-generated method stub
						Log.d("test", "get fav list 1 :" + response);
						try {
							JSONObject resultObj = new JSONObject(response);
							resultObj = resultObj.optJSONObject("result");
							if (resultObj == null)
								return;
							JSONArray array = resultObj
									.optJSONArray("activities");
							activities.clear();
							for (int i = 0; i < array.length(); i++) {
								JSONObject obj = array.optJSONObject(i);
								if (obj != null) {
									ActivityInfo info = ActivityInfo
											.createFromJson(obj);
									activities.add(info);
								}
							}
							ViewUtils.runInMainThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									mAdapter.notifyDataSetChanged();
								}
							});
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}).start();
		new GetPublisherCollectionsRequest().setHandler(new ResponseHandler() {

			@Override
			public void handleResponse(BaseRequest request, int statusCode,
					String errorMsg, String response) {
				// TODO Auto-generated method stub
				Log.d("test", "get fav list 2: " + response);
				try {
					JSONObject resultObj = new JSONObject(response);
					resultObj = resultObj.optJSONObject("result");
					if (resultObj == null)
						return;
					JSONArray array = resultObj.optJSONArray("publishers");
					if (array == null)
						return;
					publishers.clear();
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.optJSONObject(i);
						if (obj == null)
							continue;
						Publisher publisher = Publisher.createFromJson(obj);
						publishers.add(publisher);
						ViewUtils.runInMainThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								mAdapter.notifyDataSetChanged();
							}
						});
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		if (v == tab1) {
			this.setSelectIndex(0);
		} else if (v == tab2) {
			this.setSelectIndex(1);
		}

	}
}
