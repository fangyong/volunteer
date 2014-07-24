package com.baiduvolunteer.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baiduvolunteer.R;
import com.baiduvolunteer.config.Config;
import com.baiduvolunteer.config.Config.CityInfo;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.GetPublisherInfoRequest;
import com.baiduvolunteer.model.Publisher;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.util.ViewUtils;

public class PublisherAct extends Activity implements OnClickListener {

	private View backButton;
	private Publisher publisher;

	private ImageView publisherIcon;
	private TextView titleLabel;
	private TextView sloganLabel;
	private TextView createTimeLabel;
	private TextView membersLabel;
	private TextView locationLabel;
	private TextView contactLabel;
	private TextView phoneLabel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publisher);
		publisherIcon = (ImageView) findViewById(R.id.publisherIcon);
		titleLabel = (TextView) findViewById(R.id.titleLabel);
		sloganLabel = (TextView) findViewById(R.id.sloganLabel);
		createTimeLabel = (TextView) findViewById(R.id.createTimeLabel);
		locationLabel = (TextView) findViewById(R.id.locationLabel);
		membersLabel = (TextView) findViewById(R.id.membersLabel);
		contactLabel = (TextView) findViewById(R.id.contactLabel);
		phoneLabel = (TextView) findViewById(R.id.phoneLabel);
		String publisherId = getIntent().getStringExtra("publisherId");
		if (publisherId != null) {
			publisher = new Publisher();
			publisher.pid = publisherId;
			new GetPublisherInfoRequest().setPublisherId(publisherId)
					.setHandler(new ResponseHandler() {

						@Override
						public void handleResponse(BaseRequest request,
								int statusCode, String errorMsg, String response) {
							// TODO Auto-generated method stub
							Log.d("test", "result: " + response);
							if (statusCode == 200 && response != null) {
								try {
									JSONObject result = new JSONObject(response);
									JSONObject publisherDict = result
											.optJSONObject("result");
									if (publisherDict != null) {
										publisherDict = publisherDict
												.optJSONObject("institution");
									}
									if (publisherDict != null) {
										publisher = Publisher
												.createFromJson(publisherDict);
										updateInfo();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}).start();
		}
		backButton = findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
	}

	private void updateInfo() {
		ViewUtils.runInMainThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (publisher.logoUrl != null)
					ViewUtils.bmUtils.display(publisherIcon, publisher.logoUrl);
				titleLabel.setText(publisher.publishName);
				sloganLabel.setText(publisher.mission);
				createTimeLabel.setText(publisher.setUpTime);
				membersLabel.setText("" + publisher.memberNumber);

				if (publisher.province > 0) {
					CityInfo province = Config.sharedConfig().provinceList
							.get("" + publisher.province);
					Log.d("test", "get province: " + province.name + "id:"
							+ province.id);
					CityInfo city = null;
					if (province != null && province.subCityList != null) {
						for (CityInfo cityinfo : province.subCityList) {
							if (cityinfo.id == publisher.city) {
								city = cityinfo;
								break;
							}
						}
					}
					locationLabel.setText(province.name
							+ (city == null ? "" : city.name));
				} else {
					locationLabel.setText(null);
				}

				contactLabel.setText(publisher.linkUser);
				phoneLabel.setText(publisher.linkPhone);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (backButton == v) {
			this.finish();
		}
	}
}
