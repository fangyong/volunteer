package com.baiduvolunteer.view;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

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
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
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
import com.baiduvolunteer.R;
import com.baiduvolunteer.activity.ActivityInfoActivity;
import com.baiduvolunteer.activity.PublisherAct;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.SearchRequest;
import com.baiduvolunteer.http.SearchRequest.SearchType;
import com.baiduvolunteer.model.Publisher;
import com.baiduvolunteer.util.ViewUtils;

public class IndexView extends LinearLayout implements OnClickListener {

	private MapView mapView;
	private BaiduMap map;
	private LocationClient mLocationClient;
	private MyLocationListenner myListener;
	private SDKReceiver myReceiver;
	private boolean firstLoc = true;
	private LinearLayout infoView;
	private Marker marker = null;
	private Button locationButton;
	private Publisher currentPublisher;

	private int ids[] = { R.drawable.icon_marka, R.drawable.icon_markb,
			R.drawable.icon_markc, R.drawable.icon_markd,
			R.drawable.icon_marke, R.drawable.icon_markf,
			R.drawable.icon_markg, R.drawable.icon_markh,
			R.drawable.icon_marki, R.drawable.icon_markj, };

	private ArrayList<Publisher> publishers = new ArrayList<Publisher>();

	private EditText searchField;

	// private Button mSwitchButton;
	// private boolean isMap = true;
	// private ViewFlipper mFlipper;
	// private ArrayAdapter<ActivityInfo> mAdapter;
	// private ListView activityListView;

	public IndexView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public IndexView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
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
		option.setScanSpan(1000);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		myReceiver = new SDKReceiver();
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		getContext().registerReceiver(myReceiver, iFilter);
	}

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mapView == null)
				return;
			// mLocationClient.stop();
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					.direction(location.getDirection())
					.latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			map.setMyLocationData(locData);
			if (firstLoc) {
				firstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				map.animateMapStatus(u);
				// OverlayOptions oo = new MarkerOptions().position(ll).icon(
				// BitmapDescriptorFactory
				// .fromResource(R.drawable.icon_marka));
				// map.addOverlay(oo);
			}

		}

		public void onReceivePoi(BDLocation poiLocation) {
			Log.d("test", "did receive poiLocation");
		}
	}

	public void onResume() {
		Log.d("test", "indexView onresume");
		if (mapView != null)
			mapView.onResume();
		if (mLocationClient != null)
			mLocationClient.start();
		if (map != null && map.getMapStatus() != null && publishers.size() == 0) {
			startSearch();
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
		Log.d("test", "onDettach");
		getContext().unregisterReceiver(myReceiver);
		mLocationClient.stop();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mapView = (MapView) findViewById(R.id.bdmapView);
		map = mapView.getMap();
		map.setMyLocationEnabled(true);
		map.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		infoView = (LinearLayout) LayoutInflater.from(getContext()).inflate(
				R.layout.map_infowindow, null);

		ViewGroup.LayoutParams lp = new LayoutParams(560, 160);
		infoView.setLayoutParams(lp);
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
				marker = null;
			}
		});
		map.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {

			@Override
			public void onMapStatusChangeStart(MapStatus arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onMapStatusChangeFinish(MapStatus arg0) {
				// TODO Auto-generated method stub
				if (marker == null)
					return;
				Point p = map.getProjection().toScreenLocation(
						marker.getPosition());
				p.y -= 47;

				LatLng llInfo = map.getProjection().fromScreenLocation(p);
				InfoWindow window = new InfoWindow(infoView, llInfo,
						new OnInfoWindowClickListener() {

							@Override
							public void onInfoWindowClick() {
								// TODO Auto-generated method stub
								Intent intent = new Intent(getContext(),
										PublisherAct.class);
								intent.putExtra("publisherId", currentPublisher.pid);
								getContext().startActivity(intent);
							}
						});
				map.showInfoWindow(window);
			}

			@Override
			public void onMapStatusChange(MapStatus arg0) {
				// TODO Auto-generated method stub

			}
		});
		map.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub
				map.hideInfoWindow();
				marker = arg0;
				int index = marker.getExtraInfo().getInt("index");
				currentPublisher = publishers.get(index);
				Point p = map.getProjection().toScreenLocation(
						marker.getPosition());
				p.y -= 47;
				Log.d("test", "current: " + currentPublisher.address + ","
						+ currentPublisher.publishName);
				((ImageView) infoView.findViewById(R.id.placeMarker))
						.setImageResource(ids[index]);
				((TextView) infoView.findViewById(R.id.titleLabel))
						.setText(currentPublisher.publishName);
				((TextView) infoView.findViewById(R.id.addressLabel))
						.setText(currentPublisher.address);
				LatLng llInfo = map.getProjection().fromScreenLocation(p);
				InfoWindow window = new InfoWindow(infoView, llInfo,
						new OnInfoWindowClickListener() {

							@Override
							public void onInfoWindowClick() {
								// TODO Auto-generated method stub
								Intent intent = new Intent(getContext(),
										PublisherAct.class);
								intent.putExtra("publisherId", currentPublisher.pid);
								getContext().startActivity(intent);
							}
						});
				map.showInfoWindow(window);
				return true;
			}
		});
		searchField = (EditText) findViewById(R.id.search);
		locationButton = (Button) findViewById(R.id.locationButton);
		locationButton.setOnClickListener(this);
		searchField.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					InputMethodManager imm = (InputMethodManager) getContext()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					startSearch();
					return true;
				}
				return false;
			}
		});
	}

	private void startSearch() {
		// TODO
		LatLng ll = map.getMapStatus().target;
		new SearchRequest().setLat(ll.latitude).setLng(ll.longitude)
				.setKey(searchField.getText().toString())
				.setSearchType(SearchType.SearchTypePublisher)
				.setHandler(new ResponseHandler() {

					@Override
					public void handleResponse(BaseRequest request,
							int statusCode, String errorMsg, String response) {
						// TODO Auto-generated method stub
						Log.d("test", "search response "+ statusCode+":" + response);
						try {
							JSONObject obj = new JSONObject(response);
							obj = obj.optJSONObject("result");
							if (obj != null) {
								JSONArray array = obj
										.optJSONArray("publishers");
								if (array != null) {
									publishers.clear();
									for (int i = 0; i < array.length(); i++) {
										Publisher info = Publisher
												.createFromJson(array
														.getJSONObject(i));
										publishers.add(info);
									}
								}

							}
							ViewUtils.runInMainThread(new Runnable() {

								@Override
								public void run() {
									updateMap();
								}
							});

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
	}

	public void updateMap() {
		map.clear();
		int count = Math.min(10, publishers.size());
		Log.d("test", "count: " + count);
		for (int i = 0; i < count; i++) {
			Publisher publisher = publishers.get(i);
			LatLng ll = new LatLng(publisher.latitude, publisher.longtitude);
			Log.d("test", "publisher:" + publisher.latitude + ","
					+ publisher.longtitude);
			int id = ids[i];
			Bundle bundle = new Bundle();
			bundle.putInt("index", i);
			OverlayOptions oo = new MarkerOptions().extraInfo(bundle)
					.position(ll)
					.icon(BitmapDescriptorFactory.fromResource(id));
			map.addOverlay(oo);
		}
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// if(v == searchField){
		// Intent intent = new Intent(getContext(), SearchActivity.class);
		// getContext().startActivity(intent);
		// }
		if (v == locationButton) {
			this.firstLoc = true;

			publishers.clear();
			map.clear();
			startSearch();
		}
	}
}
