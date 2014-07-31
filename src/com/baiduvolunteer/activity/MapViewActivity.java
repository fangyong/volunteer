package com.baiduvolunteer.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapview);
		mapView = (MapView) findViewById(R.id.bdmapView);
		backButton = findViewById(R.id.backBtn);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
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
