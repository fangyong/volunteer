package com.baiduvolunteer.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baiduvolunteer.R;

public class MapViewActivity extends BaseActivity {
	private MapView mapView;
	private View backButton;
	private LatLng latlng;
	private View zoomInButton;
	private View zoomOutButton;
	private BaiduMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapview);
		mapView = (MapView) findViewById(R.id.bdmapView);
		map = mapView.getMap();
		for (int i = 0; i < mapView.getChildCount(); i++) {
			View view = mapView.getChildAt(i);
			if (view instanceof ImageView || view instanceof Button
					|| view instanceof ZoomControls)
				view.setVisibility(View.INVISIBLE);
		}
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
		double lat = getIntent().getDoubleExtra("lat", 0);
		double lng = getIntent().getDoubleExtra("lng", 0);
		latlng = new LatLng(lat, lng);
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
