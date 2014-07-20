package com.baiduvolunteer.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.model.Publisher;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
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
					resultAdapter.notifyDataSetChanged();
					v.clearFocus();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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
				return 10;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (typeSelector.getSelectedItemPosition() == 0) {
					ActivityListCellHolder holder = ActivityListCellHolder
							.create(getContext());
					holder.favIcon
							.setImageResource(favStates[position] == 0 ? R.drawable.icon_fav
									: R.drawable.icon_fav_sel);
					holder.favIcon.setTag(Integer.valueOf(position));
					holder.favIcon.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Integer pos = (Integer) arg0.getTag();
							favStates[pos] ^= 1;
							((ImageView) arg0)
									.setImageResource(favStates[pos] == 0 ? R.drawable.icon_fav
											: R.drawable.icon_fav_sel);
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
				if(typeSelector.getSelectedItemPosition()==0){
					Intent intent = new Intent(SearchActivity.this, ActivityInfoActivity.class);
					startActivity(intent);
				}else{
					Intent intent = new Intent(SearchActivity.this, PublisherAct.class);
					startActivity(intent);
				}
			}
		});
	}
}
