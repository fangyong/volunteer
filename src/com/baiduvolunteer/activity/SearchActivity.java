package com.baiduvolunteer.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baiduvolunteer.R;
import com.baiduvolunteer.http.AddFavRequest;
import com.baiduvolunteer.http.AddFavRequest.AddFavType;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.RemoveFavRequest;
import com.baiduvolunteer.http.RemoveFavRequest.RemoveFavType;
import com.baiduvolunteer.http.SearchRequest;
import com.baiduvolunteer.http.SearchRequest.SearchType;
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.model.Publisher;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.util.ViewUtils;
import com.baiduvolunteer.view.ActivityListCellHolder;
import com.baiduvolunteer.view.MyListView;
import com.baiduvolunteer.view.MyListView.OnLoadListener;
import com.baiduvolunteer.view.PublisherListCellHolder;

public class SearchActivity extends Activity {
	private Spinner typeSelector;
	private EditText searchField;
	private MyListView resultList;
	private View footerView;
	private int size = 10;
	private int page = 1;
	private Button searchButton;
	private MyAdapter resultAdapter;
	private ArrayList<ActivityInfo> activities = new ArrayList<ActivityInfo>();
	private ArrayList<Publisher> publishers = new ArrayList<Publisher>();
	private String[] types = { "活动", "组织" };

	private ProgressDialog mPd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		mPd = new ProgressDialog(this);
		mPd.setCancelable(false);
		mPd.setIndeterminate(true);
		searchButton = (Button) findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				activities.clear();
				page = 1;
				if (resultList.getFooterViewsCount() == 0)
					resultList.addFooterView(footerView);
				startSearch(page);
			}
		});
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
		searchField.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (searchField.getText() != null
						&& !TextUtils.isEmpty(searchField.getText())) {
					Log.d("test", "anim verify");
					if (searchButton.getVisibility() == View.VISIBLE)
						return;
					else {
						Log.d("test", "anim");
						TranslateAnimation ta = new TranslateAnimation(
								Animation.RELATIVE_TO_SELF, 1,
								Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, 0);
						ta.setDuration(200);
						ta.setFillAfter(true);
						searchButton.clearAnimation();
						searchButton.setVisibility(View.VISIBLE);
						searchButton.startAnimation(ta);

					}
				} else if (searchField.getText() == null
						|| TextUtils.isEmpty(searchField.getText())) {
					if (searchButton.getVisibility() != View.GONE) {
						Log.d("test", "anim");
						TranslateAnimation ta = new TranslateAnimation(
								Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, 1,
								Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, 0);
						ta.setDuration(200);
						ta.setFillAfter(false);
						ta.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationRepeat(Animation animation) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationEnd(Animation animation) {
								// TODO Auto-generated method stub
								searchButton.setVisibility(View.GONE);
							}
						});
						searchButton.clearAnimation();
						searchButton.startAnimation(ta);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		searchField.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					v.clearFocus();

					// startSearch();

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					return true;
				}
				return false;
			}
		});
		resultList = (MyListView) findViewById(R.id.resultList);
		footerView = getLayoutInflater().inflate(R.layout.item_foot, null);
		resultList.setOnLoadListener(new OnLoadListener() {

			@Override
			public void onLoad() {
				page++;
				startSearch(page);
			}
		});
		resultAdapter = new MyAdapter(this, 0, activities);
		resultList.setAdapter(resultAdapter);
		resultList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (typeSelector.getSelectedItemPosition() == 0) {
					// Log.d("test", "parent:" + parent.getChildAt(0));
					Intent intent = new Intent(SearchActivity.this,
							ActivityInfoActivity.class);
					ActivityInfo activity = activities.get(position
							- parent.getCount() + activities.size());
					intent.putExtra("activity", activity);
					startActivity(intent);
				} else {
					Intent intent = new Intent(SearchActivity.this,
							PublisherAct.class);
					Publisher publisher = publishers.get(position);
					intent.putExtra("publisherId", publisher.pid);
					intent.putExtra("publisher", publisher);
					startActivity(intent);
				}
			}
		});
	}

	private void startSearch(final int page) {
		// if (searchField.getText().toString().isEmpty()) {
		// activities.clear();
		// resultAdapter.notifyDataSetChanged();
		// }
		SearchRequest sreq = new SearchRequest().setSearchType(
				SearchType.SearchTypeActivity).setKey(
				searchField.getText().toString());
		if (User.sharedUser().lastLatlng != null) {
			sreq.setLat(User.sharedUser().lastLatlng.latitude).setLng(
					User.sharedUser().lastLatlng.longitude);
		}
		sreq.setPage(page).setHandler(new ResponseHandler() {

			@Override
			public void handleError(BaseRequest request, int statusCode,
					String errorMsg) {
				// TODO Auto-generated method stub
				super.handleError(request, statusCode, errorMsg);
				if (resultList.getFooterViewsCount() > 0)
					resultList.removeFooterView(footerView);
			}
			
			@Override
			public void handleResponse(BaseRequest request, int statusCode,
					String errorMsg, String response) {
				// Log.d("test", "search request " + response);
				try {
					JSONObject obj = new JSONObject(response);
					obj = obj.optJSONObject("result");
					if (obj != null) {
						JSONArray array = obj.optJSONArray("activities");
						if (array != null) {
							// activities.clear();
							if (array.length() > 0) {
								for (int i = 0; i < array.length(); i++) {
									ActivityInfo info = ActivityInfo
											.createFromJson(array
													.getJSONObject(i));
									activities.add(info);
								}
								Collections.sort(activities,
										new Comparator<ActivityInfo>() {
											@Override
											public int compare(
													ActivityInfo lhs,
													ActivityInfo rhs) {
												// TODO Auto-generated method
												// stub
												if (lhs.latitude == 0
														&& rhs.latitude == 0) {
													return rhs.activityID
															.compareTo(lhs.activityID);
												} else {
													if (User.sharedUser().lastLatlng == null) {
														return -1;
													} else if (lhs.latitude != 0
															&& rhs.latitude != 0) {
														return (int) (DistanceUtil.getDistance(
																User.sharedUser().lastLatlng,
																new LatLng(
																		lhs.latitude,
																		lhs.longitude)) - DistanceUtil.getDistance(
																User.sharedUser().lastLatlng,
																new LatLng(
																		rhs.latitude,
																		rhs.longitude)));
													} else {
														return (lhs.latitude == 0) ? 1
																: -1;
													}
												}
											}
										});
							} else {
								// if (activities.size() > size)
								// Toast.makeText(SearchActivity.this,
								// "已经到底了！", Toast.LENGTH_LONG).show();
								// if (resultList.getFooterViewsCount() > 0)
								// resultList.removeFooterView(footerView);
								Toast.makeText(SearchActivity.this, page==1?"没有搜索到结果":"已经到底了", Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(SearchActivity.this, page==1?"没有搜索到结果":"已经到底了", Toast.LENGTH_LONG).show();
						}
						if (resultList.getFooterViewsCount() > 0)
							resultList.removeFooterView(footerView);

					}
					ViewUtils.runInMainThread(new Runnable() {

						@Override
						public void run() {
							resultAdapter.setActivities(activities);
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

	class MyAdapter extends ArrayAdapter<ActivityInfo> {

		private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.M.dd h:mm");
		private ArrayList<ActivityInfo> activities;

		public MyAdapter(Context context, int resource,
				ArrayList<ActivityInfo> activities) {
			super(context, resource);
			this.activities = activities;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			// if (searchField.getText().toString().isEmpty())
			// return 0;
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
				// ImageLoader.getInstance().displayImage(info.iconUrl,
				// holder.imageView);
				ViewUtils.bmUtils().display(holder.imageView, info.iconUrl);
				holder.titleLabel.setText(info.title);
				holder.locationLabel.setText(info.address);

				// holder.distLabel.setText("" + info.distance);
				if (User.sharedUser().lastLatlng != null && info.latitude != 0) {
					double dist = DistanceUtil.getDistance(new LatLng(
							info.latitude, info.longitude),
							User.sharedUser().lastLatlng);
					if (dist < 500) {
						holder.distLabel.setText(String.format("%.0fm", dist));
					} else if (dist < 1000) {
						holder.distLabel.setText(String.format("%.0fm", dist));
					} else if (dist < 10000) {
						holder.distLabel.setText(String.format("%.0fkm",
								dist / 1000));
					} else {
						holder.distLabel.setText(">10km");
					}
				} else {
					// holder.distLabel.setText(info.distance + "m");
					holder.distLabel.setText("未知");
				}
				holder.timeLabel.setText(sdf.format(info.startTime) + "--"
						+ sdf.format(info.endTime));
				holder.favIcon.setTag(Integer.valueOf(position));
				holder.favIcon.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Integer pos = (Integer) arg0.getTag();
						final ActivityInfo info = activities.get(pos);

						if (!info.addedToFav) {
							mPd.show();
							new AddFavRequest()
									.setAddType(AddFavType.AddFavTypeActivity)
									.setId(info.activityID)
									.setHandler(new ResponseHandler() {

										@Override
										public void handleResponse(
												BaseRequest request,
												int statusCode,
												String errorMsg, String response) {
											// TODO Auto-generated method
											// stub
											mPd.dismiss();
											Log.d("test", "add fav result "
													+ response);
											if (statusCode == 200) {
												info.addedToFav = true;
												ViewUtils
														.runInMainThread(new Runnable() {

															@Override
															public void run() {

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
						} else {
							new RemoveFavRequest()
									.setId(info.activityID)
									.setRemoveType(
											RemoveFavType.RemoveFavTypeActivity)
									.setHandler(new ResponseHandler() {

										@Override
										public void handleResponse(
												BaseRequest request,
												int statusCode,
												String errorMsg, String response) {
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
						}
					}
				});
				return holder.container;
			} else {
				return PublisherListCellHolder.create(getContext()).container;
			}
		}

		public ArrayList<ActivityInfo> getActivities() {
			return activities;
		}

		public void setActivities(ArrayList<ActivityInfo> activities) {
			this.activities = activities;
		}

	}
}
