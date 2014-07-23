package com.baiduvolunteer.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.baiduvolunteer.R;
import com.baiduvolunteer.http.AddFavRequest;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.AddFavRequest.AddFavType;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.SearchRequest;
import com.baiduvolunteer.http.SearchRequest.SearchType;
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.model.Publisher;
import com.baiduvolunteer.util.ViewUtils;
import com.baiduvolunteer.view.ActivityListCellHolder;
import com.baiduvolunteer.view.PublisherListCellHolder;

public class SearchActivity extends Activity {
	private Spinner typeSelector;
	private EditText searchField;
	private ListView resultList;
	private ArrayAdapter<Object> resultAdapter;
	private ArrayList<ActivityInfo> activities = new ArrayList<ActivityInfo>();
	private ArrayList<Publisher> publishers = new ArrayList<Publisher>();
	private String[] types = { "活动", "组织" };
	private int[] favStates = new int[10];

	private ProgressDialog mPd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		mPd = new ProgressDialog(this);
		mPd.setCancelable(false);
		mPd.setIndeterminate(true);
		typeSelector = (Spinner) findViewById(R.id.typeSelector);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, types);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		typeSelector.setAdapter(adapter);
		typeSelector.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					searchField.setHint("请输入活动名称");
				} else {
					searchField.setHint("请输入组织名称");
				}
				resultAdapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		searchField = (EditText) findViewById(R.id.search);
		searchField.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					v.clearFocus();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					startSearch();
					return true;
				}
				return false;
			}
		});
		resultList = (ListView) findViewById(R.id.resultList);
		resultAdapter = new ArrayAdapter<Object>(this, 0) {
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				if (searchField.getText().toString().isEmpty())
					return 0;
				return activities.size();
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (typeSelector.getSelectedItemPosition() == 0) {
					ActivityListCellHolder holder = ActivityListCellHolder
							.create(getContext());
					ActivityInfo info = activities.get(position);
					holder.favIcon
							.setImageResource(!info.addedToFav ? R.drawable.icon_fav
									: R.drawable.icon_fav_sel);
					ViewUtils.bmUtils.display(holder.imageView, info.iconUrl);
					holder.titleLabel.setText(info.title);
					holder.locationLabel.setText(info.address);
					holder.distLabel.setText("" + info.distance);
					holder.favIcon.setTag(Integer.valueOf(position));
					holder.favIcon.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Integer pos = (Integer) arg0.getTag();
							final ActivityInfo info = activities.get(pos);
							mPd.show();
							if (!info.addedToFav)
								new AddFavRequest()
										.setAddType(
												AddFavType.AddFavTypeActivity)
										.setId(info.activityID)
										.setHandler(new ResponseHandler() {

											@Override
											public void handleResponse(
													BaseRequest request,
													int statusCode,
													String errorMsg,
													String response) {
												// TODO Auto-generated method
												// stub
												Log.d("test", "add fav result "
														+ response);
												if (statusCode == 200) {
													info.addedToFav = true;
													ViewUtils
															.runInMainThread(new Runnable() {

																@Override
																public void run() {
																	mPd.dismiss();
																	// TODO
																	// Auto-generated
																	// method
																	// stub
																	notifyDataSetChanged();
																}
															});
												}

											}
										}).start();
							else {
								// TODO add request and server api
							}
						}
					});
					return holder.container;
				} else {
					return PublisherListCellHolder.create(getContext()).container;
				}
			}
		};
		resultList.setAdapter(resultAdapter);
		resultList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (typeSelector.getSelectedItemPosition() == 0) {
					Intent intent = new Intent(SearchActivity.this,
							ActivityInfoActivity.class);
					ActivityInfo activity = activities.get(position);
					intent.putExtra("activity", activity);
					startActivity(intent);
				} else {
					Intent intent = new Intent(SearchActivity.this,
							PublisherAct.class);
					Publisher publisher = publishers.get(position);
					intent.putExtra("publisherId", publisher.pid);
					startActivity(intent);
				}
			}
		});
	}

	private void startSearch() {
		if (searchField.getText().toString().isEmpty()) {
			activities.clear();
			resultAdapter.notifyDataSetChanged();
		}
		new SearchRequest().setSearchType(SearchType.SearchTypeActivity)
				.setKey(ViewUtils.toUnicode(searchField.getText().toString()))
				.setHandler(new ResponseHandler() {

					@Override
					public void handleResponse(BaseRequest request,
							int statusCode, String errorMsg, String response) {
						Log.d("test", "search request " + response);
						try {
							JSONObject obj = new JSONObject(response);
							obj = obj.optJSONObject("result");
							if (obj != null) {
								JSONArray array = obj
										.optJSONArray("activities");
								if (array != null) {
									activities.clear();
									for (int i = 0; i < array.length(); i++) {
										ActivityInfo info = ActivityInfo
												.createFromJson(array
														.getJSONObject(i));
										activities.add(info);
									}
								}

							}
							ViewUtils.runInMainThread(new Runnable() {

								@Override
								public void run() {
									resultAdapter.notifyDataSetChanged();
								}
							});

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
	}
}
