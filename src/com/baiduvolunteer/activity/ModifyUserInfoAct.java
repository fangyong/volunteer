package com.baiduvolunteer.activity;

import org.json.JSONObject;
import org.w3c.dom.NodeList;

import com.baiduvolunteer.R;
import com.baiduvolunteer.adapter.PopupwindowListAdapter;
import com.baiduvolunteer.task.LoadProvinceListTask;
import com.baiduvolunteer.view.MyPopupWindow;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

public class ModifyUserInfoAct extends Activity {

	@ViewInject(R.id.textView3)
	private TextView provinceTv;
	@ViewInject(R.id.textView4)
	private TextView cityTv;
	@ViewInject(R.id.textView9)
	private TextView districtTv;

	@ViewInject(R.id.street_et)
	private EditText streetEt;
	@ViewInject(R.id.telephone_et)
	private EditText telephoneEt;

	private ListView listView;

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

	private NodeList provinceList;
	private NodeList cityList;
	private NodeList districtList;

	private MyPopupWindow mpw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_userinfo);
		initViews();
	}

	void initViews() {
		ViewUtils.inject(this);
		View contentView = getLayoutInflater().inflate(
				R.layout.menu_province_list, null);
		findViewById(R.id.pset).setVisibility(View.GONE);
		findViewById(R.id.textView8).setVisibility(View.GONE);
		mpw = new MyPopupWindow(contentView, false);
		listView = (ListView) contentView.findViewById(R.id.listView1);
		setProvince();

		((Button) findViewById(R.id.button1)).setText("保存");
	}

	void setProvince() {
		new LoadProvinceListTask(

		new LoadProvinceListTask.OnTaskFinishListener() {

			@Override
			public void onTaskFinish(NodeList nodeList) {

				provinceList = nodeList;
				listView.setAdapter(new PopupwindowListAdapter(
						ModifyUserInfoAct.this, provinceList));
				// cityList = provinceList.item(3).getChildNodes().item(15)
				// .getChildNodes();
				// provinceName = provinceList.item(3).getChildNodes().item(7)
				// .getTextContent();
				// provinceId = provinceList.item(3).getChildNodes().item(3)
				// .getTextContent();
				// provinceTv.setText(provinceName);
			}
		}, ModifyUserInfoAct.this).execute("");
	}

	@OnClick(R.id.province_spinner)
	public void onProvinceSpinnerClick(View arg0) {
		final View shadow = findViewById(R.id.popwindow);
		mpw.showAtLocation(shadow, Gravity.CENTER, 0, 0);
		shadow.setVisibility(View.VISIBLE);
		mpw.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				shadow.setVisibility(View.GONE);
			}
		});
		if (provinceList == null)
			new LoadProvinceListTask(

			new LoadProvinceListTask.OnTaskFinishListener() {

				@Override
				public void onTaskFinish(NodeList nodeList) {

					provinceList = nodeList;
					listView.setAdapter(new PopupwindowListAdapter(
							ModifyUserInfoAct.this, provinceList));
				}
			}, ModifyUserInfoAct.this).execute("");
		else {
			listView.setAdapter(new PopupwindowListAdapter(
					ModifyUserInfoAct.this, provinceList));
		}
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				cityList = provinceList.item(arg2).getChildNodes().item(15)
						.getChildNodes();
				provinceName = provinceList.item(arg2).getChildNodes().item(7)
						.getTextContent();
				provinceId = provinceList.item(arg2).getChildNodes().item(3)
						.getTextContent();
				provinceTv.setText(provinceName);
				initCityAndDistrict();
				mpw.dismiss();
			}

			void initCityAndDistrict() {
				cityId = null;
				cityName = null;
				cityTv.setText("未选择");
				districtId = null;
				districtName = null;
				districtTv.setText("未选择");
			}
		});
		mpw.update();
	}

	@OnClick(R.id.city_spinner)
	public void onCitySpinerClick(View arg0) {
		if (provinceName != null) {
			final View shadow = findViewById(R.id.popwindow);
			mpw.showAtLocation(shadow, Gravity.CENTER, 0, 0);
			shadow.setVisibility(View.VISIBLE);
			mpw.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					shadow.setVisibility(View.GONE);
				}
			});
			listView.setAdapter(new PopupwindowListAdapter(
					ModifyUserInfoAct.this, cityList));
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					districtList = cityList.item(arg2).getChildNodes().item(15)
							.getChildNodes();
					cityName = cityList.item(arg2).getChildNodes().item(7)
							.getTextContent();
					cityId = cityList.item(arg2).getChildNodes().item(3)
							.getTextContent();
					cityTv.setText(cityName);
					initDistrict();
					mpw.dismiss();
				}

				void initDistrict() {
					districtId = null;
					districtName = null;
					districtTv.setText("未选择");
				}
			});
			mpw.update();
		}

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
			listView.setAdapter(new PopupwindowListAdapter(
					ModifyUserInfoAct.this, districtList));
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					districtName = districtList.item(arg2).getChildNodes()
							.item(7).getTextContent();
					districtId = districtList.item(arg2).getChildNodes()
							.item(3).getTextContent();
					districtTv.setText(districtName);
					mpw.dismiss();
				}
			});
			mpw.update();
		}
	}

}
