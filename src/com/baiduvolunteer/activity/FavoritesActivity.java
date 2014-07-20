package com.baiduvolunteer.activity;

import java.util.ArrayList;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.model.Publisher;
import com.baiduvolunteer.view.ActivityListCellHolder;
import com.baiduvolunteer.view.PublisherListCellHolder;

public class FavoritesActivity extends Activity implements OnClickListener {

	private Button backButton;
	private TextView tab1;
	private TextView tab2;
	private View indicator1;
	private View indicator2;
	private ListView favList;
	private ArrayList<Publisher> publishers;
	private ArrayList<ActivityInfo> activities;
	private ArrayAdapter<Object> mAdapter;

	private int selectIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);
		backButton = (Button) findViewById(R.id.backButton);
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
				return selectIndex == 0 ? 5 : 1;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (selectIndex == 0) {
					ActivityListCellHolder holder = ActivityListCellHolder
							.create(getContext());
					holder.favIcon.setVisibility(View.INVISIBLE);
					return holder.container;
				} else {
					return PublisherListCellHolder.create(getContext()).container;
				}
			}
		};
		favList.setAdapter(mAdapter);
		favList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (selectIndex == 0) {
					Intent intent = new Intent(FavoritesActivity.this,
							ActivityInfoActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(FavoritesActivity.this,
							PublisherAct.class);
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
	public void onClick(View v) {
		if (v == tab1) {
			this.setSelectIndex(0);
		} else if (v == tab2) {
			this.setSelectIndex(1);
		}

	}
}
