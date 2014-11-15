package com.baiduvolunteer.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfigeration;
import com.baidu.mapapi.map.MyLocationConfigeration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds.Builder;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baiduvolunteer.R;
import com.baiduvolunteer.activity.ActivityInfoActivity;
import com.baiduvolunteer.activity.PublisherAct;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.GetExtraDotRequest;
import com.baiduvolunteer.http.SearchAllRequest;
import com.baiduvolunteer.http.SearchAllRequest.SearchAllResponseHandler;
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.model.Publisher;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.util.ViewUtils;

public class IndexView extends LinearLayout implements OnClickListener {

	private boolean updating = false;

	private MapView mapView;
	private BaiduMap map;
	private ProgressDialog mpd;
	private LocationClient mLocationClient;
	private MyLocationListenner myListener;
	private SDKReceiver myReceiver;
	private boolean firstLoc = true;
	private LinearLayout infoView;
	private Marker currn = null;
	private View locationButton;
	private Object currentData;
	// private int searchCount = 0;
	private View searchButton;
	private View zoomInButton;
	private View zoomOutButton;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.M.d hh:mm");

	private BroadcastReceiver receiver1;
	private BroadcastReceiver receiver2;

	private LatLng currentLatLng;

	private LatLng userLocation;

	private int currentSelection = 0;

	private float currentZoom;

	private Marker currentMarker;

	private int ids[] = { R.drawable.icon_marka, R.drawable.icon_markb,
			R.drawable.icon_markc, R.drawable.icon_markd,
			R.drawable.icon_marke, R.drawable.icon_markf,
			R.drawable.icon_markg, R.drawable.icon_markh,
			R.drawable.icon_marki, R.drawable.icon_markj, };

	private int bids[] = { R.drawable.icon_bmarka, R.drawable.icon_bmarkb,
			R.drawable.icon_bmarkc, R.drawable.icon_bmarkd,
			R.drawable.icon_bmarke, R.drawable.icon_bmarkf,
			R.drawable.icon_bmarkg, R.drawable.icon_bmarkh,
			R.drawable.icon_bmarki, R.drawable.icon_bmarkj, };

	private int cids[] = { R.drawable.icon_cmarka, R.drawable.icon_cmarkb,
			R.drawable.icon_cmarkc, R.drawable.icon_cmarkd,
			R.drawable.icon_cmarke, R.drawable.icon_cmarkf,
			R.drawable.icon_cmarkg, R.drawable.icon_cmarkh,
			R.drawable.icon_cmarki, R.drawable.icon_cmarkj, };

	private int sids[] = { R.drawable.sicon_marka, R.drawable.sicon_markb,
			R.drawable.sicon_markc, R.drawable.sicon_markd,
			R.drawable.sicon_marke, R.drawable.sicon_markf,
			R.drawable.sicon_markg, R.drawable.sicon_markh,
			R.drawable.sicon_marki, R.drawable.sicon_markj, };

	private ArrayList<Object> markerArray = new ArrayList<Object>();
	private ArrayList<Object> resultMarkerArray = new ArrayList<Object>();

	private HashMap<String, Object> resultFilterMap = new HashMap<String, Object>();

	private EditText searchField;
	private String keyword = null;

	// private Button mSwitchButton;
	// private boolean isMap = true;
	// private ViewFlipper mFlipper;
	// private ArrayAdapter<ActivityInfo> mAdapter;
	// private ListView activityListView;

	public IndexView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mpd = new ProgressDialog(getContext());
		mpd.setCancelable(false);
		mpd.setIndeterminate(true);
	}

	public IndexView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mpd = new ProgressDialog(getContext());
		mpd.setCancelable(false);
		mpd.setIndeterminate(true);
	}

	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();

		myListener = new MyLocationListenner();
		map.setMyLocationConfigeration(new MyLocationConfigeration(
				LocationMode.NORMAL, true, null));
		mapView.onResume();
		mLocationClient = new LocationClient(getContext());
		mLocationClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setNeedDeviceDirect(true);
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(10000);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		myReceiver = new SDKReceiver();
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		getContext().registerReceiver(myReceiver, iFilter);
		iFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		if (receiver1 == null)
			receiver1 = new ScreenOffReceiver();
		getContext().registerReceiver(receiver1, iFilter);
		iFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		if (receiver2 == null)
			receiver2 = new ScreenOnReceiver();
		getContext().registerReceiver(receiver2, iFilter);
	}

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			double dist = 0;
			if (location != null) {
				if (userLocation != null) {
					dist = DistanceUtil.getDistance(userLocation, new LatLng(
							location.getLatitude(), location.getLongitude()));
				}
				userLocation = new LatLng(location.getLatitude(),
						location.getLongitude());
				User.sharedUser().currentLatlng = userLocation;
			}
			if (location == null || mapView == null)
				return;
			// mLocationClient.stop();
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					.latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			map.setMyLocationData(locData);
			if (firstLoc) {
				firstLoc = false;
				MapStatusUpdate u = MapStatusUpdateFactory
						.newLatLng(userLocation);
				map.animateMapStatus(u);
				startSearch(true);
				// OverlayOptions oo = new MarkerOptions().position(ll).icon(
				// BitmapDescriptorFactory
				// .fromResource(R.drawable.icon_marka));
				// map.addOverlay(oo);
			} else if (dist > 1000) {
				startSearch(true);
			}

		}

		public void onReceivePoi(BDLocation poiLocation) {
			Log.d("test", "did receive poiLocation");
		}
	}

	public void onResume() {
		// Log.d("test", "indexView onresume");
		if (mapView != null)
			mapView.onResume();
		if (mLocationClient != null)
			mLocationClient.start();
		if (map != null && map.getMapStatus() != null
				&& markerArray.size() == 0) {
			startSearch(false);
		}
	}

	public void onPause() {
		if (mapView != null)
			mapView.onPause();
		if (mLocationClient != null)
			mLocationClient.stop();
	}

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		getContext().unregisterReceiver(receiver1);
		getContext().unregisterReceiver(receiver2);
		// Log.d("test", "onDettach");
		getContext().unregisterReceiver(myReceiver);
		mpd.dismiss();
		mLocationClient.stop();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mapView = (MapView) findViewById(R.id.bdmapView);
		for (int i = 0; i < mapView.getChildCount(); i++) {
			View view = mapView.getChildAt(i);
			if (view instanceof ImageView || view instanceof Button
					|| view instanceof ZoomControls || view instanceof TextView)
				view.setVisibility(View.INVISIBLE);
		}
		map = mapView.getMap();
		// Log.d("test", "map " + map);
		map.getUiSettings().setRotateGesturesEnabled(false);
		map.getUiSettings().setOverlookingGesturesEnabled(false);
		map.setMyLocationEnabled(true);
		// map.setMaxAndMinZoomLevel(15, 3);
		map.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
		map.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		infoView = (LinearLayout) findViewById(R.id.infoView);

		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				map.hideInfoWindow();
				InputMethodManager im = (InputMethodManager) getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				im.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
				// marker = null;
			}
		});
		zoomInButton = findViewById(R.id.zoomInButton);
		zoomOutButton = findViewById(R.id.zoomOutButton);

		zoomOutButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				map.animateMapStatus(MapStatusUpdateFactory.zoomOut());
			}
		});

		zoomInButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ZoomControls controls = (ZoomControls) mapView.getChildAt(2);
				// Log.d("test","map button "+controls.getChildAt(1).performClick());
				map.animateMapStatus(MapStatusUpdateFactory.zoomIn());
			}
		});

		map.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {

			@Override
			public void onMapStatusChangeStart(MapStatus arg0) {
				// TODO Auto-generated method stub
				// map.hideInfoWindow();
			}

			@Override
			public void onMapStatusChangeFinish(MapStatus status) {
				// TODO Auto-generated method stub
				LatLng newlatlng = status.target;
				User.sharedUser().lastLatlng = status.target;
				// if (leftTop == null
				// || leftTop.latitude < status.target.latitude
				// || leftTop.longitude > status.target.longitude
				// || rightDown == null
				// || rightDown.latitude > status.target.latitude
				// || rightDown.longitude < status.target.longitude)
				if (currentLatLng == null
						|| DistanceUtil.getDistance(currentLatLng, newlatlng) > 1000
						|| markerArray.isEmpty()) {
					// markerArray.clear();
					// map.clear();
					currentLatLng = newlatlng;
					searchMd(keyword);
					return;
				}
				// {
				// markerArray.clear();
				// resultFilterMap.clear();
				// for (Object obj : resultMarkerArray) {
				// if (obj instanceof Publisher) {
				// resultFilterMap.put("publisher"
				// + ((Publisher) obj).pid, obj);
				// } else if (obj instanceof ActivityInfo) {
				// resultFilterMap.put("activity"
				// + ((ActivityInfo) obj).activityID, obj);
				// }
				// }
				// startSearch(false);
				// }
				// searchMd();
				// currentLatLng = newlatlng;
				// if (marker == null)
				// return;

				// if (currentZoom == 0) {
				// currentZoom = status.zoom;
				// return;
				// }
				// if (currentZoom != status.zoom) {
				// currentZoom = status.zoom;
				// Point p = map.getProjection().toScreenLocation(
				// marker.getPosition());
				// p.y -= 47;
				//
				// LatLng llInfo =
				// map.getProjection().fromScreenLocation(p);
				// InfoWindow window = new InfoWindow(infoView, llInfo,
				// new OnInfoWindowClickListener() {
				//
				// @Override
				// public void onInfoWindowClick() {
				// // TODO Auto-generated method stub
				// // Log.d("test", "currentData" +
				// // currentData);
				// if (currentData instanceof Publisher) {
				// Publisher publisher = (Publisher) currentData;
				// Intent intent = new Intent(
				// getContext(),
				// PublisherAct.class);
				// intent.putExtra("publisherId",
				// publisher.pid);
				// intent.putExtra("publisher", publisher);
				// getContext().startActivity(intent);
				// } else {
				// // Log.d("test", "ddd");
				// ActivityInfo info = (ActivityInfo) currentData;
				// // Log.d("test", "ddd " +
				// // info.activityID);
				// Intent intent = new Intent(
				// getContext(),
				// ActivityInfoActivity.class);
				// intent.putExtra("activity", info);
				// getContext().startActivity(intent);
				// }
				// }
				// });
				// map.showInfoWindow(window);
				// }
				// }
			}

			@Override
			public void onMapStatusChange(MapStatus arg0) {
				// TODO Auto-generated method stub

			}
		});
		searchButton = (Button) findViewById(R.id.searchButton);
		map.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub
				if (currentMarker != null) {
					int index = currentMarker.getExtraInfo().getInt("index");
					if (!currentMarker.getExtraInfo().containsKey("index"))
						index = -1;
					if (index >= 0 && index < 10) {
						Object obj = currentMarker.getExtraInfo()
								.getSerializable("object");
						if (obj instanceof Publisher) {
							currentMarker.setIcon(BitmapDescriptorFactory
									.fromResource(ids[index]));
						} else {
							currentMarker.setIcon(BitmapDescriptorFactory
									.fromResource(cids[index]));
						}
					} else {
						Object obj = currentMarker.getExtraInfo()
								.getSerializable("object");
						if (obj instanceof Publisher) {
							currentMarker.setIcon(BitmapDescriptorFactory
									.fromResource(R.drawable.icon_markextra));
						} else {
							currentMarker.setIcon(BitmapDescriptorFactory
									.fromResource(R.drawable.icon_cmarkextra));
						}
					}
				}
				currentMarker = arg0;
				int index = currentMarker.getExtraInfo().getInt("index");
				if (!currentMarker.getExtraInfo().containsKey("index"))
					index = -1;
				currentSelection = index >= 0 ? index : -1;
				currentData = currentMarker.getExtraInfo().getSerializable(
						"object");
				if (index >= 0 && index < 10) {
					currentMarker.setIcon(BitmapDescriptorFactory
							.fromResource(sids[index]));
				} else {
					currentMarker.setIcon(BitmapDescriptorFactory
							.fromResource(R.drawable.sicon_markextra));
				}
				// updateMap(false);
				// if (index >= 0 && index < 10) {
				// if(currentData instanceof Publisher){
				// marker.setIcon(icon(BitmapDescriptorFactory.fromResource(id));
				// }
				// }

				// Point p = map.getProjection().toScreenLocation(
				// marker.getPosition());
				// p.y -= 47;

				// LatLng llInfo = map.getProjection().fromScreenLocation(p);
				// InfoWindow window = new InfoWindow(infoView, llInfo,
				// new OnInfoWindowClickListener() {
				//
				// @Override
				// public void onInfoWindowClick() {
				// // TODO Auto-generated method stub
				// // Log.d("test", "currentData" + currentData);
				// if (currentData instanceof Publisher) {
				// Publisher publisher = (Publisher) currentData;
				// Intent intent = new Intent(getContext(),
				// PublisherAct.class);
				// intent.putExtra("publisherId",
				// publisher.pid);
				// intent.putExtra("publisher", publisher);
				// getContext().startActivity(intent);
				// } else {
				// // Log.d("test", "ddd");
				// ActivityInfo info = (ActivityInfo) currentData;
				// // Log.d("test", "ddd");
				// Intent intent = new Intent(getContext(),
				// ActivityInfoActivity.class);
				// intent.putExtra("activity", info);
				// getContext().startActivity(intent);
				// Log.d("test", "ddd out");
				// }
				//
				// }
				// });
				// map.showInfoWindow(window);
				showInfoView(currentData, index);
				return true;
			}
		});
		searchField = (EditText) findViewById(R.id.search);
		locationButton = findViewById(R.id.locationButton);
		locationButton.setOnClickListener(this);
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
				} else {
					// startSearch(true);
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
								startSearch(true);
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
				if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					InputMethodManager imm = (InputMethodManager) getContext()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					if (searchField.getText() != null
							&& !TextUtils.isEmpty(searchField.getText())) {

					}
					// else if (searchField.getText() == null
					// || searchField.getText().toString().isEmpty()) {
					// if (searchButton.getVisibility() != View.GONE) {
					// Log.d("test", "anim");
					// TranslateAnimation ta = new TranslateAnimation(
					// Animation.RELATIVE_TO_SELF, 0,
					// Animation.RELATIVE_TO_SELF, 1,
					// Animation.RELATIVE_TO_SELF, 0,
					// Animation.RELATIVE_TO_SELF, 0);
					// ta.setDuration(200);
					// ta.setFillAfter(false);
					// ta.setAnimationListener(new AnimationListener() {
					//
					// @Override
					// public void onAnimationStart(Animation animation) {
					// // TODO Auto-generated method stub
					//
					// }
					//
					// @Override
					// public void onAnimationRepeat(
					// Animation animation) {
					// // TODO Auto-generated method stub
					//
					// }
					//
					// @Override
					// public void onAnimationEnd(Animation animation) {
					// // TODO Auto-generated method stub
					// searchButton.setVisibility(View.GONE);
					// }
					// });
					// searchButton.clearAnimation();
					// searchButton.startAnimation(ta);
					//
					// }
					//
					// }
					return true;
				}
				return false;
			}
		});
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mpd.show();
				searchField.clearFocus();
				InputMethodManager im = (InputMethodManager) getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				im.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
				keyword = searchField.getText().toString().trim();
				// if (keyword != null && !keyword.isEmpty()) {
				// searchField.setHint(keyword);
				// } else {
				// searchField.setHint("志愿者活动...");
				// }

				startSearch(true);
			}
		});
		// Log.d("test","color "+
		// searchField.getHintTextColors().getDefaultColor());
	}

	private void hideInfoView() {
		infoView.setVisibility(View.GONE);
	}

	private void showInfoView(Object data, int index) {
		currentData = data;
		if (data == null)
			return;
		Publisher currentPublisher = null;
		ActivityInfo info = null;
		if (data instanceof Publisher) {
			currentPublisher = (Publisher) data;
			// Log.d("test", "current: " + currentPublisher.address +
			// ","
			// + currentPublisher.publishName);
		} else {
			info = (ActivityInfo) data;
		}

		// if (currentPublisher != null)
		((ImageView) infoView.findViewById(R.id.placeMarker))
				.setImageResource(index >= 0 && index < 10 ? sids[index]
						: R.drawable.sicon_markextra_new);
		// else
		// ((ImageView) infoView.findViewById(R.id.placeMarker))
		// .setImageResource(index >= 0 && index < 10 ? cids[index]
		// : R.drawable.icon_markextra_new);
		((TextView) infoView.findViewById(R.id.titleLabel))
				.setText(currentPublisher != null ? currentPublisher.publishName
						: info.title);
		((TextView) infoView.findViewById(R.id.addressLabel))
				.setText(currentPublisher != null ? currentPublisher.address
						: info.address);
		if (info != null) {
			((TextView) infoView.findViewById(R.id.timeLabel)).setText(String
					.format("%s - %s", sdf.format(info.startTime),
							sdf.format(info.endTime)));
		} else {
			((TextView) infoView.findViewById(R.id.timeLabel)).setText("");
		}
		infoView.setVisibility(View.VISIBLE);
		infoView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (currentData instanceof Publisher) {
					Publisher publisher = (Publisher) currentData;
					Intent intent = new Intent(getContext(), PublisherAct.class);
					intent.putExtra("publisherId", publisher.pid);
					intent.putExtra("publisher", publisher);
					getContext().startActivity(intent);
				} else {
					// Log.d("test", "ddd");
					ActivityInfo info = (ActivityInfo) currentData;
					// Log.d("test", "ddd");
					Intent intent = new Intent(getContext(),
							ActivityInfoActivity.class);
					intent.putExtra("activity", info);
					getContext().startActivity(intent);
				}
			}
		});
	}

	private void startSearch(final boolean clearResult) {
		// TODO
		if (clearResult) {
			resultMarkerArray.clear();
			markerArray.clear();
			hideInfoView();
		}
		// markerArray.clear();
		if (clearResult)
			keyword = searchField.getText().toString();
		LatLng ll = (clearResult && userLocation != null) ? userLocation : map
				.getMapStatus().target;
		if (clearResult) {
			SearchAllRequest request = new SearchAllRequest();
			request.setLat(ll.latitude).setLng(ll.longitude)
					.setKey(searchField.getText().toString()).setSize(50);
			// map.getMapStatus().zoom > 14 ? 20
			// : map.getMapStatus().zoom > 5 ? 100 : 300)
			request.setResponseHandler(new SearchAllResponseHandler() {

				@Override
				public void handleSuccess(ArrayList<Object> results) {
					Log.d("test", "search all response size:" + results.size());
					if (clearResult) {
						resultFilterMap.clear();
						resultMarkerArray.clear();
					}
					ArrayList<Object> objectsToRemove = new ArrayList<Object>();
					double lat = 0;
					double lng = 0;
					int dupCount = 0;
					for (int i = 0; i < results.size(); i++) {
						Object obj = results.get(i);
						if (obj instanceof Publisher) {
							if (resultFilterMap.containsKey("publisher"
									+ ((Publisher) obj).pid)) {
								objectsToRemove.add(obj);
								continue;
							} else {
								if (resultMarkerArray.size() < 10) {
									Publisher publisher = (Publisher) obj;
									resultFilterMap.put("publisher"
											+ publisher.pid, obj);
									if (lat != 0 && lat == publisher.latitude
											&& lng != 0
											&& lng == publisher.longitude) {
										dupCount++;
										publisher.longitude += 0.0005 * dupCount;
										publisher.latitude += 0.0005 * dupCount;
									} else {
										lat = publisher.latitude;
										lng = publisher.longitude;
										dupCount = 0;
									}
									resultMarkerArray.add(obj);
									objectsToRemove.add(obj);
								}

							}
						} else if (obj instanceof ActivityInfo) {
							if (resultFilterMap.containsKey("activity"
									+ ((ActivityInfo) obj).activityID)) {
								objectsToRemove.add(obj);
								continue;
							} else {
								if (resultMarkerArray.size() < 10) {
									ActivityInfo activity = (ActivityInfo) obj;
									resultFilterMap.put("activity"
											+ activity.activityID, obj);
									if (lat != 0 && lat == activity.latitude
											&& lng != 0
											&& lng == activity.longitude) {
										dupCount++;
										activity.longitude += 0.001 * dupCount;
										activity.latitude += 0.001 * dupCount;
									} else {
										lat = activity.latitude;
										lng = activity.longitude;
										dupCount = 0;
									}
									resultMarkerArray.add(obj);
									objectsToRemove.add(obj);
								}

							}
						}
					}
					// results.removeAll(objectsToRemove);

					// resultMarkerArray.clear();
					// resultMarkerArray.addAll(results.subList(0,
					// Math.min(10, results.size())));
					// markerArray.clear();
					// if (results.size() > 10) {
					// markerArray.addAll(results.subList(10, results.size()));
					// }
					//
					// } else {
					// markerArray.clear();
					// markerArray.addAll(results);
					// }
					// GetExtraDotRequest mdRequest = new GetExtraDotRequest();
					// mdRequest.setLatMax()
					// if (clearResult)
					// markerArray.clear();
					// markerArray.addAll(results);
					// Log.d("test", "markerArray " + markerArray.size() +
					// "map size "
					// + resultFilterMap.size());

					ViewUtils.runInMainThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							updateMap(clearResult);
							searchMd(searchField.getText().toString());
							mpd.dismiss();
						}
					});
				}

				@Override
				public void handleError(int statusCode, String errorMsg) {
					// TODO Auto-generated method stub
					mpd.dismiss();
					super.handleError(statusCode, errorMsg);
				}
			});
			request.start();
		} else {
			searchMd(keyword);
		}

	}

	private void searchMd(String key) {
		LatLng ltll = null;
		LatLng rdll = null;
		try {
			Point lt = new Point(0, 0);
			if (map.getProjection() != null)
				ltll = map.getProjection().fromScreenLocation(lt);
			lt = new Point(mapView.getWidth(), mapView.getHeight());
			if (map.getProjection() != null)
				rdll = map.getProjection().fromScreenLocation(lt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ltll == null || rdll == null)
			return;
		new GetExtraDotRequest().setLatMax(ltll.latitude).setKey(key)
				.setLngMin(ltll.longitude).setLatMin(rdll.latitude)
				.setLngMax(rdll.longitude).setSize(calculateSize())
				.setHandler(new ResponseHandler() {

					@Override
					public void handleResponse(BaseRequest request,
							int statusCode, String errorMsg, String response) {
						// TODO Auto-generated method stub
						try {
							JSONObject obj = new JSONObject(response);
							obj = obj.optJSONObject("result");
							if (obj != null) {
								markerArray.clear();
								JSONArray array = obj.optJSONArray("search");
								if (array != null) {
									int count = 0;
									for (int i = 0; i < array.length()
											&& count < calculateSize(); i++) {
										JSONObject dataobj = array
												.getJSONObject(i);
										if (dataobj.optString("activityId",
												null) != null) {
											ActivityInfo info = ActivityInfo
													.createFromJson(dataobj);
											if (!resultFilterMap
													.containsKey("activity"
															+ info.activityID)) {
												markerArray.add(info);
												count++;
												continue;
											}

										} else if (dataobj.optString(
												"publisherId", null) != null) {
											Publisher publisher = Publisher
													.createFromJson(dataobj);
											if (!resultFilterMap
													.containsKey("publisher"
															+ publisher.pid)) {
												markerArray.add(publisher);
												count++;
												continue;
											}
										}

									}
								}
							}
							Log.d("test", "markers :" + markerArray.size());
							ViewUtils.runInMainThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									updateMap(false);
								}
							});

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
	}

	private int calculateSize() {
		return 30;
	}

	public void updateMap(boolean clearResult) {
		if (updating)
			return;
		updating = true;
		map.clear();
		// Log.d("test", "resultArray:" + resultMarkerArray.size()
		// + ",markerArray:" + markerArray.size());
		LatLng ltll = null;
		LatLng rdll = null;
		try {
			Point lt = new Point(0, 0);
			if (map.getProjection() != null)
				ltll = map.getProjection().fromScreenLocation(lt);
			lt = new Point(mapView.getWidth(), mapView.getHeight());
			if (map.getProjection() != null)
				rdll = map.getProjection().fromScreenLocation(lt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (clearResult) {
			currentSelection = 0;
		}
		int count = Math.min(10, resultMarkerArray.size());
		for (int i = 0; i < markerArray.size(); i++) {
			Object obj = markerArray.get(i);
			if (obj instanceof Publisher) {
				Publisher publisher = (Publisher) obj;
				LatLng ll = new LatLng(publisher.latitude, publisher.longitude);
				// if (ltll == null
				// || rdll == null
				// || (ll.latitude < ltll.latitude
				// && ll.latitude > rdll.latitude
				// && ll.longitude > ltll.longitude && ll.longitude <
				// rdll.longitude)) {
				int id = R.drawable.icon_markextra;
				if (currentData != null && currentData instanceof Publisher
						&& ((Publisher) currentData).pid.equals(publisher.pid))
					id = R.drawable.sicon_markextra;
				Bundle bundle = new Bundle();
				bundle.putSerializable("object", publisher);
				OverlayOptions oo = new MarkerOptions().extraInfo(bundle)
						.position(ll)
						.icon(BitmapDescriptorFactory.fromResource(id));
				Marker marker = (Marker) map.addOverlay(oo);
				if (currentData != null && currentData instanceof Publisher
						&& ((Publisher) currentData).pid.equals(publisher.pid)) {
					currentMarker = marker;
					showInfoView(publisher, -1);
				}

				// }

			} else if (obj instanceof ActivityInfo) {
				ActivityInfo info = (ActivityInfo) obj;
				LatLng ll = new LatLng(info.latitude, info.longitude);
				// if (ltll == null
				// || rdll == null
				// || (ll.latitude < ltll.latitude
				// && ll.latitude > rdll.latitude
				// && ll.longitude > ltll.longitude && ll.longitude <
				// rdll.longitude)) {

				int id = R.drawable.icon_cmarkextra;
				if (currentData != null
						&& currentData instanceof ActivityInfo
						&& ((ActivityInfo) currentData).activityID
								.equals(info.activityID))
					id = R.drawable.sicon_markextra;
				Bundle bundle = new Bundle();
				bundle.putSerializable("object", info);
				OverlayOptions oo = new MarkerOptions().extraInfo(bundle)
						.position(ll)
						.icon(BitmapDescriptorFactory.fromResource(id));
				Marker marker = (Marker) map.addOverlay(oo);
				if (currentData != null
						&& currentData instanceof ActivityInfo
						&& ((ActivityInfo) currentData).activityID
								.equals(info.activityID)) {
					showInfoView(info, -1);
					currentMarker = marker;
				}

				// }
			}
		}

		if (ltll != null && rdll != null)
			Log.d("test", "lt: " + ltll.latitude + "," + ltll.longitude
					+ ";rd:" + rdll.latitude + "," + rdll.longitude);
		LatLng preLatlng = null;

		for (int i = 0; i < count; i++) {
			Object obj = resultMarkerArray.get(i);
			if (obj instanceof Publisher) {

				Publisher publisher = (Publisher) obj;
				double lat = publisher.latitude;
				double lng = publisher.longitude;

				if (preLatlng != null) {
					if (lat == preLatlng.latitude && lng == preLatlng.longitude) {
						lng += 0.001;
						lat += 0.001;
					}
				}
				LatLng ll = new LatLng(lat, lng);
				preLatlng = ll;

				int id = ids[i];
				if (i == currentSelection) {
					id = sids[i];
					currentData = publisher;
				}
				// if (ltll == null
				// || rdll == null
				// || (ll.latitude < ltll.latitude
				// && ll.latitude > rdll.latitude
				// && ll.longitude > ltll.longitude && ll.longitude <
				// rdll.longitude)) {
				Bundle bundle = new Bundle();
				bundle.putInt("index", i);
				bundle.putSerializable("object", publisher);
				OverlayOptions oo = new MarkerOptions().extraInfo(bundle)
						.position(ll)
						.icon(BitmapDescriptorFactory.fromResource(id));
				Marker marker = (Marker) map.addOverlay(oo);

				// }
				if (i == currentSelection) {
					currentMarker = marker;
					showInfoView(publisher, i);
				}

			} else if (obj instanceof ActivityInfo) {
				ActivityInfo info = (ActivityInfo) obj;

				double lat = info.latitude;
				double lng = info.longitude;
				if (preLatlng != null) {
					if (info.latitude == preLatlng.latitude
							&& info.longitude == preLatlng.longitude) {
						lng += 0.001;
						lat += 0.001;
					}
				}
				LatLng ll = new LatLng(lat, lng);
				preLatlng = ll;
				int id = cids[i];
				if (i == currentSelection) {
					id = sids[i];
					currentData = info;
				}
				// if (ltll == null
				// || rdll == null
				// || (ll.latitude < ltll.latitude
				// && ll.latitude > rdll.latitude
				// && ll.longitude > ltll.longitude && ll.longitude <
				// rdll.longitude)) {
				Bundle bundle = new Bundle();
				bundle.putInt("index", i);
				bundle.putSerializable("object", info);
				OverlayOptions oo = new MarkerOptions().extraInfo(bundle)
						.position(ll)
						.icon(BitmapDescriptorFactory.fromResource(id));
				Marker marker = (Marker) map.addOverlay(oo);

				// }
				if (i == currentSelection) {
					currentMarker = marker;
					showInfoView(info, i);
				}

			}
		}
		if (clearResult) {
			zoom();
		}
		updating = false;
		// Log.d("test", "sss out");
	}

	private void zoom() {
		int inboundCount = resultMarkerArray.size();
		// map.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(userLocation,
		// map.getMaxZoomLevel()));
		if (userLocation == null || inboundCount == 0)
			return;
		double dist = 0;
		Builder builder = new Builder();
		for (Object firstObj : resultMarkerArray) {
			LatLng lastLatlng = null;
			if (firstObj instanceof Publisher) {

				Publisher publisher = (Publisher) firstObj;
				lastLatlng = new LatLng(publisher.latitude, publisher.longitude);
				dist = DistanceUtil.getDistance(userLocation, lastLatlng);
			} else if (firstObj instanceof ActivityInfo) {
				ActivityInfo info = (ActivityInfo) firstObj;
				lastLatlng = new LatLng(info.latitude, info.longitude);
				dist = DistanceUtil.getDistance(userLocation, lastLatlng);
			}

			builder.include(lastLatlng);
		}
		// Object firstobj = resultMarkerArray.get(resultMarkerArray.size() -
		// 1);

		// float zoomlevel = map.getMinZoomLevel();
		// if (dist >= 100 && dist < 500) {// 50m
		// zoomlevel = 4;
		// } else if (dist >= 500 && dist < 1000) {// 100m
		// zoomlevel = 5;
		// } else if (dist >= 1000 && dist < 2000) {// 200m
		// zoomlevel = 6;
		// } else if (dist >= 2000 && dist < 5000) {// 500m
		// zoomlevel = 7;
		// } else if (dist >= 5000 && dist < 10000) {// 1000
		// zoomlevel = 8;
		// } else if (dist >= 10000 && dist < 20000) {// 2000
		// zoomlevel = 9;
		// } else if (dist >= 20000 && dist < 50000) {// 5000
		// zoomlevel = 10;
		// } else if (dist >= 50000 && dist < 100000) {// 10000
		// zoomlevel = 11;
		// } else if (dist >= 100000 && dist < 200000) {// 20k
		// zoomlevel = 12;
		// } else if (dist >= 200000 && dist < 250000) {// 25k
		// zoomlevel = 13;
		// } else if (dist >= 2500000 && dist < 500000) {// 50k
		// zoomlevel = 14;
		// } else if (dist >= 500000 && dist < 1000000) {// 100k
		// zoomlevel = 15;
		// } else if (dist >= 1000000 && dist < 2000000) {// 200k
		// zoomlevel = 16;
		// } else if (dist >= 2000000 && dist < 5000000) {// 500k
		// zoomlevel = 17;
		// } else if (dist >= 5000000 && dist < 10000000) {// 1000k
		// zoomlevel = 18;
		// } else if (dist >= 10000000) {// 2000k
		// zoomlevel = 19;
		// }

		builder.include(userLocation);
		map.animateMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder
				.build()));
		// if (map.getMapStatus().zoom > 10) {
		// map.animateMapStatus(MapStatusUpdateFactory.zoomTo(3));
		// }

		// while (inboundCount > 0
		// && map.getMapStatus().zoom != map.getMinZoomLevel()) {
		// inboundCount = resultMarkerArray.size();
		// LatLng ltll = null;
		// LatLng rdll = null;
		// try {
		// Point lt = new Point(0, 0);
		// if (map.getProjection() != null)
		// ltll = map.getProjection().fromScreenLocation(lt);
		// lt = new Point(mapView.getWidth(), mapView.getHeight());
		// if (map.getProjection() != null)
		// rdll = map.getProjection().fromScreenLocation(lt);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// if (ltll == null || rdll == null)
		// return;
		// Log.d("test", "ltll " + ltll.latitude + "," + ltll.longitude);
		// Log.d("test", "rdll " + rdll.latitude + "," + rdll.longitude);
		// for (int i = 0; i < resultMarkerArray.size(); i++) {
		// Object obj = resultMarkerArray.get(i);
		// if (obj instanceof Publisher) {
		//
		// Publisher publisher = (Publisher) obj;
		// if (publisher.latitude <= ltll.latitude
		// && publisher.latitude >= rdll.latitude
		// && publisher.longitude >= ltll.longitude
		// && publisher.longitude <= rdll.longitude) {
		// inboundCount--;
		// }
		// } else if (obj instanceof ActivityInfo) {
		// ActivityInfo info = (ActivityInfo) obj;
		// if (info.latitude <= ltll.latitude
		// && info.latitude >= rdll.latitude
		// && info.longitude >= ltll.longitude
		// && info.longitude <= rdll.longitude) {
		// inboundCount--;
		// }
		// }
		// }
		// if (inboundCount > 0) {
		// MapStatusUpdate update = MapStatusUpdateFactory.zoomOut();
		// map.setMapStatus(update);
		// }
		// Log.d("test", "incount " + inboundCount);
		// }
	}

	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			Log.d("test", "action: " + s);

			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				Toast.makeText(getContext(),
						"key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置",
						Toast.LENGTH_LONG).show();
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				Toast.makeText(getContext(), "网络出错", Toast.LENGTH_LONG).show();
			}
		}
	}

	public class ScreenOnReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (mLocationClient != null) {
				mLocationClient.start();
			}
		}

	}

	public class ScreenOffReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (mLocationClient != null) {
				mLocationClient.stop();
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// if(v == searchField){
		// Intent intent = new Intent(getContext(), SearchActivity.class);
		// getContext().startActivity(intent);
		// }
		if (v == locationButton) {
			this.firstLoc = true;

			if (map.getLocationData() != null) {
				map.animateMapStatus(MapStatusUpdateFactory
						.newLatLng(new LatLng(map.getLocationData().latitude,
								map.getLocationData().longitude)));
			}
		}
	}
}
