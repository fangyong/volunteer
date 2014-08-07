package com.baiduvolunteer.activity;

import java.net.URISyntaxException;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baiduvolunteer.R;
import com.baiduvolunteer.model.User;

public class MapViewActivity extends BaseActivity {
	private MapView mapView;
	private View backButton;
	private LatLng latlng;
	private View zoomInButton;
	private View zoomOutButton;
	private BaiduMap map;

	private String address;
	private Button directionButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapview);
		double lat = getIntent().getDoubleExtra("lat", 0);
		double lng = getIntent().getDoubleExtra("lng", 0);
		address = getIntent().getStringExtra("address");
		latlng = new LatLng(lat, lng);
		mapView = (MapView) findViewById(R.id.bdmapView);
		map = mapView.getMap();
		for (int i = 0; i < mapView.getChildCount(); i++) {
			View view = mapView.getChildAt(i);
			if (view instanceof ImageView || view instanceof Button
					|| view instanceof ZoomControls)
				view.setVisibility(View.INVISIBLE);
		}
		directionButton = (Button) findViewById(R.id.directions);
		directionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = null;
				try {// 如果有安装百度地图 就启动百度地图
					StringBuffer sbs = new StringBuffer();
					sbs.append("intent://map/direction?origin=latlng:")
							// 我的位置
							.append(User.sharedUser().currentLatlng.latitude)
							.append(",")
							.append(User.sharedUser().currentLatlng.longitude)
							// 去的位置
							.append("|name:")
							.append("我的位置")
							.append("&destination=latlng:")
							.append(latlng.latitude)
							// 经度
							.append(",").append(latlng.longitude)
							// 纬度
							.append("|name:")
							.append(address)
							// 城市
							.append("&referer=com.menu|menu#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
					try {
						intent = Intent.getIntent(sbs.toString());
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
					startActivity(intent);
				} catch (Exception e) {// 没有百度地图则弹出网页端
					StringBuffer sb = new StringBuffer();
					sb.append(
							"http://api.map.baidu.com/direction?origin=latlng:")
							// 我的位置
							.append(User.sharedUser().currentLatlng.latitude)
							.append(",")
							.append(User.sharedUser().currentLatlng.longitude)
							// 去的位置
							.append("|name:")
							.append("我的位置")
							.append("&destination=latlng:")
							.append(latlng.latitude).append(",")
							.append(latlng.longitude).append("|name:")
							.append(address)
							// 城市
							.append("&output=html");
					Uri uri = Uri.parse(sb.toString());
					intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				}
			}
		});
		backButton = findViewById(R.id.backBtn);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
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

		mapView.setClickable(false);
		mapView.getMap().setMyLocationData(
				new MyLocationData.Builder().latitude(lat).longitude(lng)
						.build());
		MarkerOptions options = new MarkerOptions().position(latlng).icon(
				BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding));
		mapView.getMap().addOverlay(options);
		mapView.getMap().setMapStatus(MapStatusUpdateFactory.newLatLng(latlng));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// MapStatusUpdate s = MapStatusUpdateFactory.newLatLngZoom(latlng, 3);
		// if (mapView.getMap() != null)
		// mapView.getMap().animateMapStatus(s);
	}
}
