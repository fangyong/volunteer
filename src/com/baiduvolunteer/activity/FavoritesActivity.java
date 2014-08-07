package com.baiduvolunteer.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baiduvolunteer.R;
import com.baiduvolunteer.http.AddFavRequest;
import com.baiduvolunteer.http.AddFavRequest.AddFavType;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.GetActivityCollectionListRequest;
import com.baiduvolunteer.http.GetPublisherCollectionsRequest;
import com.baiduvolunteer.http.RemoveFavRequest;
import com.baiduvolunteer.http.RemoveFavRequest.RemoveFavType;
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.model.Publisher;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.util.ViewUtils;
import com.baiduvolunteer.view.ActivityListCellHolder;
import com.baiduvolunteer.view.MyListView;
import com.baiduvolunteer.view.MyListView.OnLoadListener;
import com.baiduvolunteer.view.MyListView.OnRefreshListener;
import com.baiduvolunteer.view.PublisherListCellHolder;

public class FavoritesActivity extends Activity implements OnClickListener,
		OnRefreshListener, OnLoadListener {

	private View backButton;
	private TextView tab1;
	private TextView tab2;
	private View indicator1;
	private View indicator2;
	private MyListView favList;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	private ArrayList<Publisher> publishers = new ArrayList<Publisher>();
	private ArrayList<ActivityInfo> activities = new ArrayList<ActivityInfo>();
	private ArrayAdapter<Object> mAdapter;
	private ProgressDialog mPd;
	private int page1 = 0;
	private int page2 = 0;

	private int selectIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mPd = new ProgressDialog(this);
		mPd.setCancelable(false);
		mPd.setIndeterminate(true);
		setContentView(R.layout.activity_favorites);
		backButton = findViewById(R.id.backButton);
		tab1 = (TextView) findViewById(R.id.title1);
		tab2 = (TextView) findViewById(R.id.title2);
		indicator1 = findViewById(R.id.indicator1);
		indicator2 = findViewById(R.id.indicator2);
		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		favList = (MyListView) findViewById(R.id.favList);
		favList.setonRefreshListener(this);
		favList.setOnLoadListener(this);
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
					// holder.favIcon.setVisibility(View.INVISIBLE);
					holder.titleLabel.setText(info.title);
					holder.favIcon.setTag(Integer.valueOf(position));
					holder.timeLabel.setText(sdf.format(info.startTime)
							+ "\n --" + sdf.format(info.endTime));
					holder.locationLabel.setText(info.address);
					// ViewUtils.bmUtils.display(holder.imageView,
					// info.iconUrl);
					ImageLoader.getInstance().displayImage(info.iconUrl,
							holder.imageView);
					if (User.sharedUser().currentLatlng != null
							&& info.latitude != 0) {
						double dist = DistanceUtil.getDistance(new LatLng(
								info.latitude, info.longitude), User
								.sharedUser().currentLatlng);
						if (dist < 500) {
							holder.distLabel.setText(String.format("%.0fm",
									dist));
						} else if (dist < 1000) {
							holder.distLabel.setText(String.format("%.0fm",
									dist));
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
					holder.favIcon
							.setImageResource(info.addedToFav ? R.drawable.icon_fav_sel
									: R.drawable.icon_fav);
					holder.favIcon.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Integer pos = (Integer) arg0.getTag();
							final ActivityInfo info = activities.get(pos);

							if (!info.addedToFav) {
								mPd.show();
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
													String errorMsg,
													String response) {
												Log.d("test",
														"remove fav result "
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
					Publisher publisher = publishers.get(position);
					PublisherListCellHolder holder = PublisherListCellHolder
							.create(getContext());
					Log.d("test", "publisherName" + publisher.publishName);
					holder.titleLabel.setText(publisher.publishName);
					holder.activitiesLabel.setText(String.format("发起%d个活动",
							publisher.activityNum));
					holder.membersLabel.setText(String.format("共%d个人参加",
							publisher.activityJoinNum));
					holder.favButton
							.setImageResource(publisher.isCollection ? R.drawable.icon_fav_sel
									: R.drawable.icon_fav);
					holder.favButton.setTag(Integer.valueOf(position));
					if (User.sharedUser().currentLatlng != null
							&& publisher.latitude != 0) {
						double dist = DistanceUtil.getDistance(new LatLng(
								publisher.latitude, publisher.longitude), User
								.sharedUser().currentLatlng);
						if (dist < 500) {
							holder.distLabel.setText(String.format("%.0fm",
									dist));
						} else if (dist < 1000) {
							holder.distLabel.setText(String.format("%.0fm",
									dist));
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
					holder.favButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							mPd.show();
							final Publisher publisher = publishers
									.get((Integer) v.getTag());
							if (!publisher.isCollection)
								new AddFavRequest()
										.setAddType(
												AddFavType.AddFavTypePublisher)
										.setPublisherType(
												publisher.publisherType)
										.setId(publisher.pid)
										.setHandler(new ResponseHandler() {

											@Override
											public void handleResponse(
													BaseRequest request,
													int statusCode,
													String errorMsg,
													String response) {
												// TODO Auto-generated method
												// stub
												publisher.isCollection = true;
												notifyDataSetChanged();
												mPd.dismiss();
											}

											@Override
											public void handleError(
													BaseRequest request,
													int statusCode,
													String errorMsg) {
												// TODO Auto-generated method
												// stub
												super.handleError(request,
														statusCode, errorMsg);
												ViewUtils
														.runInMainThread(new Runnable() {

															@Override
															public void run() {
																// TODO
																// Auto-generated
																// method stub
																mPd.dismiss();
															}
														});
											}
										}).start();
							else {
								new RemoveFavRequest()
										.setId(publisher.pid)
										.setPublisherType(
												publisher.publisherType)
										.setRemoveType(
												RemoveFavType.RemoveFavTypePublisher)
										.setHandler(new ResponseHandler() {

											@Override
											public void handleResponse(
													BaseRequest request,
													int statusCode,
													String errorMsg,
													String response) {
												// TODO Auto-generated method
												// stub
												Log.d("test",
														"remove fav resp "
																+ response);
												mPd.dismiss();
												publisher.isCollection = false;
												notifyDataSetChanged();
											}

											@Override
											public void handleError(
													BaseRequest request,
													int statusCode,
													String errorMsg) {
												// TODO Auto-generated method
												// stub
												super.handleError(request,
														statusCode, errorMsg);
												ViewUtils
														.runInMainThread(new Runnable() {

															@Override
															public void run() {
																// TODO
																// Auto-generated
																// method stub
																mPd.dismiss();
															}
														});
											}
										}).start();
							}
						}
					});
					ImageLoader.getInstance().displayImage(publisher.logoUrl,
							holder.imageView);
//					ViewUtils.bmUtils.display(holder.imageView,
//							publisher.logoUrl);
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
					ActivityInfo info = activities.get(position - 1);
					Intent intent = new Intent(FavoritesActivity.this,
							ActivityInfoActivity.class);
					intent.putExtra("activity", info);
					startActivity(intent);
				} else {
					Publisher publisher = publishers.get(position - 1);
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
	public void onLoad() {
		// TODO Auto-generated method stub
		if (selectIndex == 0) {
			loadActivity(false);
		} else if (selectIndex == 1) {
			loadPublisher(false);
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (selectIndex == 0) {
			loadActivity(true);
		} else if (selectIndex == 1) {
			loadPublisher(true);
		}
	}

	private void loadActivity(final boolean startOver) {
		if (startOver)
			page1 = 1;
		new GetActivityCollectionListRequest().setPage(page1).setSize(20)
				.setHandler(new ResponseHandler() {

					@Override
					public void handleResponse(BaseRequest request,
							int statusCode, String errorMsg, String response) {
						// TODO Auto-generated method stub
						page1++;
						Log.d("test", "get fav list 1 :" + response);
						try {
							JSONObject resultObj = new JSONObject(response);
							resultObj = resultObj.optJSONObject("result");
							if (resultObj == null)
								return;
							JSONArray array = resultObj
									.optJSONArray("activities");
							if (startOver)
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
									favList.onRefreshComplete();
								}
							});
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}).start();
	}

	private void loadPublisher(final boolean startOver) {
		if (startOver)
			page2 = 1;
		new GetPublisherCollectionsRequest().setPage(page2).setSize(20)
				.setHandler(new ResponseHandler() {

					@Override
					public void handleResponse(BaseRequest request,
							int statusCode, String errorMsg, String response) {
						// TODO Auto-generated method stub
						Log.d("test", "get fav list 2: " + response);
						page2++;
						try {
							JSONObject resultObj = new JSONObject(response);
							resultObj = resultObj.optJSONObject("result");
							if (resultObj == null)
								return;
							JSONArray array = resultObj
									.optJSONArray("publishers");
							if (array == null)
								return;
							if (startOver)
								publishers.clear();
							for (int i = 0; i < array.length(); i++) {
								JSONObject obj = array.optJSONObject(i);
								if (obj == null)
									continue;
								Publisher publisher = Publisher
										.createFromJson(obj);
								publishers.add(publisher);
								ViewUtils.runInMainThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										mAdapter.notifyDataSetChanged();
										favList.onRefreshComplete();
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
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		loadActivity(true);
		loadPublisher(true);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
