package com.baiduvolunteer.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfigeration;
import com.baidu.mapapi.map.MyLocationConfigeration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baiduvolunteer.R;
import com.baiduvolunteer.model.ActivityInfo;

public class IndexView extends LinearLayout implements OnClickListener {

	private MapView mapView;
	private BaiduMap map;
	private LocationClient mLocationClient;
	private MyLocationListenner myListener;
	private SDKReceiver myReceiver;
	private boolean firstLoc = true;
//	private Button mSwitchButton;
//	private boolean isMap = true;
//	private ViewFlipper mFlipper;
//	private ArrayAdapter<ActivityInfo> mAdapter;
//	private ListView activityListView;

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
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
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
				OverlayOptions oo = new MarkerOptions().position(ll).icon(
						BitmapDescriptorFactory
								.fromResource(R.drawable.icon_marka));
				map.addOverlay(oo);
			}

		}

		public void onReceivePoi(BDLocation poiLocation) {
			Log.d("test", "did receive poiLocation");
		}
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
		Log.d("test", "mapview: " + mapView);
		map = mapView.getMap();
		map.setMyLocationEnabled(true);
		map.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//		activityListView = (ListView) findViewById(R.id.activitiesList);
//		mAdapter = new ArrayAdapter<ActivityInfo>(getContext(), 0) {
//			@Override
//			public int getCount() {
//				// TODO Auto-generated method stub
//				return 10;
//			}
//
//			@Override
//			public View getView(int position, View convertView, ViewGroup parent) {
//				// TODO Auto-generated method stub
//				if (convertView == null) {
//					ActivityListCellHolder holder = ActivityListCellHolder
//							.createFromInflater(LayoutInflater
//									.from(getContext()));
//
//					convertView = holder.container;
//					convertView.setTag(holder);
//				}
//				ActivityListCellHolder holder = (ActivityListCellHolder) convertView
//						.getTag();
//				holder.titleLabel.setText("" + position);
//				return convertView;
//			}
//		};
//		activityListView.setAdapter(mAdapter);
//		activityListView.setDivider(getResources().getDrawable(
//				R.drawable.listviewdivider));
//		activityListView.setDividerHeight(20);
//		activityListView.setBackgroundColor(0xfff5f4f1);
	}

	// public IndexView(Context context, AttributeSet attrs, int defStyle) {
	// super(context, attrs, defStyle);
	// // TODO Auto-generated constructor stub
	// }

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
	}
}
