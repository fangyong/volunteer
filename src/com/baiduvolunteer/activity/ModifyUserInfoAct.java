package com.baiduvolunteer.activity;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Spinner;

import com.baiduvolunteer.R;
import com.baiduvolunteer.adapter.PopupwindowListAdapter;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.task.LoadProvinceListTask;
import com.baiduvolunteer.view.MyPopupWindow;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ModifyUserInfoAct extends Activity implements OnClickListener {

	private Button backButton;

	private Button saveButton;

	private EditText unameEt;
	private EditText telephoneEt;

	private View maleTv;
	private View femaleTv;
	private View otherTv;

	private Spinner provinceSpinner;
	private Spinner citySpinner;

	private String id;
	private String cityName;
	private String districtName;
	private String provinceId;
	private String cityId;
	private String districtId;
	private String street;
	private String telephone;
	private String addressee;
	private String provinceName;
	private int sex;

	private ArrayAdapter<String> provinceAdapter;
	private ArrayAdapter<String> cityAdapter;

	private NodeList provinceList;
	private NodeList cityList;
	private NodeList districtList;

	private View maleCheck;
	private View femaleCheck;
	private View otherCheck;

	private MyPopupWindow mpw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_userinfo);
		initViews();
	}

	void initViews() {
		ViewUtils.inject(this);

		unameEt = (EditText) findViewById(R.id.uname_et);
		backButton = (Button) findViewById(R.id.button2);
		backButton.setOnClickListener(this);
		saveButton = (Button) findViewById(R.id.button_save);
		saveButton.setText("保存");
		saveButton.setOnClickListener(this);
		provinceSpinner = (Spinner) findViewById(R.id.province_spinner);
		citySpinner = (Spinner) findViewById(R.id.city_spinner);
		provinceAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		provinceAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		provinceSpinner.setAdapter(provinceAdapter);
		cityAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		cityAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		citySpinner.setAdapter(cityAdapter);
		provinceSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				cityList = provinceList.item(position).getChildNodes().item(15)
						.getChildNodes();
				cityAdapter.clear();
				for (int i = 0; i < cityList.getLength(); i++) {
					cityAdapter.add(cityList.item(i).getChildNodes().item(7)
							.getTextContent());
				}
				cityAdapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		maleTv = findViewById(R.id.male);
		femaleTv = findViewById(R.id.female);
		otherTv = findViewById(R.id.other);
		maleTv.setOnClickListener(this);
		femaleTv.setOnClickListener(this);
		otherTv.setOnClickListener(this);
		maleCheck = findViewById(R.id.male_check);
		femaleCheck = findViewById(R.id.female_check);
		otherCheck = findViewById(R.id.other_check);
		telephoneEt = (EditText) findViewById(R.id.telephone_et);
		setProvince();
		if (User.sharedUser().uname != null) {
			unameEt.setText(User.sharedUser().uname);
		}
		if (User.sharedUser().phoneNumber != null) {
			telephoneEt.setText(User.sharedUser().phoneNumber);
		}
		if (User.sharedUser().gender != 0) {
			setSex(User.sharedUser().gender);
		}

	}

	void setProvince() {
		new LoadProvinceListTask(

		new LoadProvinceListTask.OnTaskFinishListener() {

			@Override
			public void onTaskFinish(NodeList nodeList) {

				provinceList = nodeList;
				ArrayList<String> array = new ArrayList<String>();
				provinceAdapter.clear();
				for (int i = 0; i < provinceList.getLength(); i++) {
					array.add(provinceList.item(i).getChildNodes().item(7)
							.getTextContent());
				}
				provinceAdapter.addAll(array);
				provinceAdapter.notifyDataSetChanged();
				if (User.sharedUser().province > 0) {
					for (int i = 0; i < provinceList.getLength(); i++) {
						Node node = provinceList.item(i);
						if (node.getChildNodes().item(3).getTextContent()
								.equals("" + User.sharedUser().province)) {
							provinceSpinner.setSelection(i);
							break;
						}
					}
					cityList = provinceList
							.item(provinceSpinner.getSelectedItemPosition())
							.getChildNodes().item(15).getChildNodes();
					cityAdapter.clear();
					for (int i = 0; i < cityList.getLength(); i++) {
						cityAdapter.add(cityList.item(i).getChildNodes()
								.item(7).getTextContent());
					}
					if (User.sharedUser().city > 0) {
						for (int i = 0; i < cityList.getLength(); i++) {
							Node node = cityList.item(i);
							if (node.getChildNodes().item(3).getTextContent()
									.equals("" + User.sharedUser().city)) {
								citySpinner.setSelection(i);
								break;
							}
						}
					}
				}

			}
		}, ModifyUserInfoAct.this).execute("");
	}

	@OnClick(R.id.district_spinner)
	public void onDistrictSpinnerClick(View arg0) {
		if (cityName != null) {
			final View shadow = findViewById(R.id.popwindow);
			mpw.showAtLocation(shadow, Gravity.CENTER, 0, 0);
			shadow.setVisibility(View.VISIBLE);
			mpw.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					shadow.setVisibility(View.GONE);
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (backButton == v) {
			this.finish();
		} else if (maleTv == v) {
			this.setSex(1);
		} else if (femaleTv == v) {
			this.setSex(3);
		} else if (otherTv == v) {
			this.setSex(2);
		} else if (saveButton == v) {
			this.save();
			this.finish();
		}
	}

	private void save() {
		if (!unameEt.getText().toString().isEmpty())
			User.sharedUser().uname = unameEt.getText().toString();
		if (!telephoneEt.getText().toString().isEmpty()) {
			User.sharedUser().phoneNumber = telephoneEt.getText().toString();
		}
		User.sharedUser().gender = sex;
		Node node = provinceList
				.item(provinceSpinner.getSelectedItemPosition());
		User.sharedUser().province = Integer.valueOf(node.getChildNodes()
				.item(3).getTextContent());
		node = cityList.item(citySpinner.getSelectedItemPosition());
		User.sharedUser().city = Integer.valueOf(node.getChildNodes().item(3)
				.getTextContent());
		User.sharedUser().save();
	}

	private void setSex(int sex) {
		this.sex = sex;

		if (sex == 3)
			femaleCheck.setVisibility(View.VISIBLE);
		else
			femaleCheck.setVisibility(View.INVISIBLE);

		if (sex == 1)
			maleCheck.setVisibility(View.VISIBLE);
		else {
			maleCheck.setVisibility(View.INVISIBLE);
		}
		if (sex == 2) {
			otherCheck.setVisibility(View.VISIBLE);
		} else {
			otherCheck.setVisibility(View.INVISIBLE);
		}
	}
}
