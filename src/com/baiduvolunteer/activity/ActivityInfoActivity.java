package com.baiduvolunteer.activity;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.baiduvolunteer.R;
import com.baiduvolunteer.http.BaseRequest;
import com.baiduvolunteer.http.BaseRequest.ResponseHandler;
import com.baiduvolunteer.http.GetActivityInfoRequest;
import com.baiduvolunteer.http.JointActivityRequest;
import com.baiduvolunteer.model.ActivityInfo;
import com.baiduvolunteer.model.User;
import com.baiduvolunteer.util.ViewUtils;
import com.baiduvolunteer.view.ListViewCell;
import com.lidroid.xutils.BitmapUtils;

public class ActivityInfoActivity extends BaseActivity implements
		OnClickListener {
	// private ListViewCell infoCell;
	private ListViewCell locationCell;
	private ListViewCell organizerCell;
	private ListViewCell contactCell;

	private View arrow;

	private TextView activityTitle;
	private ImageView activityPic;
	private TextView activityTime;
	private TextView activityCategory;
	private TextView activityEnrollNumber;
	private TextView activityIntro;

	private String shareUrl = "http://fir.im/FsTK";

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.M.d hh:mm");

	private View backButton;
	private View attendButton;
	private TextView attendBtnText;

	private Button shareBtn;

	private ActivityInfo activityInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		boolean joined = getIntent().getBooleanExtra("joined", false);
		setContentView(R.layout.activity_info);
		activityInfo = (ActivityInfo) getIntent().getSerializableExtra(
				"activity");
		locationCell = (ListViewCell) findViewById(R.id.locationCell);
		organizerCell = (ListViewCell) findViewById(R.id.organizerCell);
		// infoCell = (ListViewCell) findViewById(R.id.infoContainer);
		locationCell.iconView.setImageResource(R.drawable.icon_address_h);
		// organizerCell.textLabel.setText("北大青年志愿者协会");
		organizerCell.iconView.setImageResource(R.drawable.icon_organization_h);
		contactCell = (ListViewCell) findViewById(R.id.contactCell);
		arrow = contactCell.findViewById(R.id.detailIcon);
		contactCell.iconView.setImageResource(R.drawable.icon_contact_h);
		// contactCell.textLabel.setText("18201506318");

		locationCell.setOnClickListener(this);
		contactCell.setOnClickListener(this);
		organizerCell.setOnClickListener(this);
		backButton = findViewById(R.id.backBtn);
		backButton.setOnClickListener(this);
		attendButton = findViewById(R.id.joinButton);
		attendButton.setOnClickListener(this);
		activityTitle = (TextView) findViewById(R.id.activityTitle);
		activityPic = (ImageView) findViewById(R.id.activityPic);
		activityTime = (TextView) findViewById(R.id.activityTime);
		activityCategory = (TextView) findViewById(R.id.activityCategory);
		activityEnrollNumber = (TextView) findViewById(R.id.activityEnrollNumber);
		activityIntro = (TextView) findViewById(R.id.activityIntro);
		shareBtn = (Button) findViewById(R.id.shareBtn);
		organizerCell.detailIconView
				.setImageResource(R.drawable.icon_cell_detail);
		contactCell.detailIconView
				.setImageResource(R.drawable.icon_cell_detail);
		locationCell.detailIconView
				.setImageResource(R.drawable.icon_cell_detail);
		attendBtnText = (TextView) findViewById(R.id.btnText);
		if (joined) {
			attendButton.setVisibility(View.GONE);
		}

		shareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				OnekeyShare oks = new OnekeyShare();
				// oks.setNotification(R.drawable.ic_launcher,
				// getString(R.string.app_name));
				// oks.setAddress("12345678901");
				oks.setTitle(getString(R.string.evenote_title));
				oks.setTitleUrl(shareUrl);
				oks.setText("#志愿也是一种生活方式#"
						+ activityInfo.title
						+ ":"
						+ sdf.format(activityInfo.startTime)
						+ "-"
						+ sdf.format(activityInfo.endTime)
						+ ";"
						+ activityInfo.address
						+ ";点击了解一下吧 "
						+ " http://115.28.0.232/baidu/activity/jumpShare.action?id="
						+ activityInfo.activityID + "。记得报名参加哦");
				// if (captureView) {
				// oks.setViewToShare(getPage());
				// } else {
				// oks.setImagePath(MainActivity.TEST_IMAGE);
				oks.setImageUrl(activityInfo.iconUrl);
				// }
				oks.setUrl(shareUrl);
				// oks.setFilePath(MainActivity.TEST_IMAGE);
				oks.setComment(getString(R.string.share)
						+ " http://115.28.0.232/baidu/activity/jumpShare.action?id="
						+ activityInfo.activityID);
				oks.setSite(getString(R.string.app_name));
				oks.setSiteUrl("http://sharesdk.cn");
				// oks.setVenueName("ShareSDK");
				// oks.setVenueDescription("This is a beautiful place!");
				// oks.setLatitude(23.056081f);
				// oks.setLongitude(113.385708f);
				// oks.setSilent(silent);
				// if (platform != null) {
				// oks.setPlatform(platform);
				// }

				// 令编辑页面显示为Dialog模式
				oks.setDialogMode();

				// 在自动授权时可以禁用SSO方式
				oks.disableSSOWhenAuthorize();

				// 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
				// oks.setCallback(new OneKeyShareCallback());
				// oks.setShareContentCustomizeCallback(new
				// ShareContentCustomizeDemo());

				// 去除注释，演示在九宫格设置自定义的图标
				// Bitmap logo =
				// BitmapFactory.decodeResource(menu.getResources(),
				// R.drawable.ic_launcher);
				// String label =
				// menu.getResources().getString(R.string.app_name);
				// OnClickListener listener = new OnClickListener() {
				// public void onClick(View v) {
				// String text = "Customer Logo -- ShareSDK " +
				// ShareSDK.getSDKVersionName();
				// Toast.makeText(menu.getContext(), text,
				// Toast.LENGTH_SHORT).show();
				// oks.finish();
				// }
				// };
				// oks.setCustomerLogo(logo, label, listener);

				// 去除注释，则快捷分享九宫格中将隐藏新浪微博和腾讯微博
				// oks.addHiddenPlatform(SinaWeibo.NAME);
				// oks.addHiddenPlatform(TencentWeibo.NAME);

				// 为EditPage设置一个背景的View
				// oks.setEditPageBackground(getPage());
				oks.setCallback(new MyActionListener());

				oks.show(ActivityInfoActivity.this);
			}
		});
		new GetActivityInfoRequest().setActivityId(activityInfo.activityID)
				.setHandler(new ResponseHandler() {

					@Override
					public void handleResponse(BaseRequest request,
							int statusCode, String errorMsg,
							final String response) {
						Log.d("activityinfo test", response);
						ViewUtils.runInMainThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									JSONObject activity = new JSONObject(
											response);
									activity = activity.optJSONObject("result");
									if (activity == null)
										return;
									activity = activity
											.optJSONObject("activity");
									if (activity == null)
										return;
									activityInfo.loadFromJson(activity);
									Log.d("test", "isattend"
											+ activityInfo.isAttend);
									locationCell.textLabel
											.setText(activityInfo.address);
									activityTime.setText(sdf
											.format(activityInfo.startTime)
											+ "-"
											+ sdf.format(activityInfo.endTime));
									activityTitle.setText(activityInfo.title);
									activityEnrollNumber.setText(String.format(
											"报名人数： %d/%d",
											activityInfo.currentCount,
											activityInfo.totalCount));
									activityCategory.setText("领域："
											+ activityInfo.field);
									organizerCell.textLabel
											.setText(activityInfo.publisher);
									contactCell.textLabel
											.setText(activityInfo.contactPhone);
									activityIntro
											.setText(activityInfo.description);
									locationCell.detailIconView
											.setVisibility((activityInfo.latitude != 0 || activityInfo.longitude != 0) ? View.VISIBLE
													: View.INVISIBLE);
									if (activityInfo.contactPhone == null
											|| activityInfo.contactPhone
													.equals(""))
										arrow.setVisibility(View.GONE);
									attendButton.setEnabled(true);
									if (activityInfo.iconUrl != null) {
//										BitmapUtils bmUtils = new BitmapUtils(
//												ActivityInfoActivity.this);
//										bmUtils.configDefaultLoadFailedImage(R.drawable.default_icon);
//										bmUtils.configDefaultLoadingImage(R.drawable.default_icon);
//										bmUtils.configDiskCacheEnabled(true);
										ViewUtils.bmUtils.display(activityPic,
												activityInfo.iconUrl);
									}
									if (activityInfo.isAttend) {
										attendButton
												.setBackgroundColor(0xffe7e7e7);
										attendButton.setEnabled(false);
										attendBtnText.setText("  已报名");
									} else {
										attendButton
												.setBackgroundColor(0xff107cfd);
										attendButton.setEnabled(true);
										if (activityInfo.startTime.getTime() < System
												.currentTimeMillis()) {
											attendButton
													.setBackgroundColor(0xffe7e7e7);
											attendButton.setEnabled(false);
										}
										attendBtnText.setText("  我要报名");
									}

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
					}
				}).start();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == contactCell) {
			if (contactCell.textLabel.getText() != null
					&& !contactCell.textLabel.getText().toString().isEmpty())
				new AlertDialog.Builder(this)
						.setMessage(
								"确认拨打电话"
										+ contactCell.textLabel.getText()
												.toString() + "?")
						.setPositiveButton("拨打",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent = new Intent(
												Intent.ACTION_CALL);
										intent.setData(Uri.parse("tel:"
												+ contactCell.textLabel
														.getText()));
										startActivity(intent);
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								}).show();
		} else if (v == organizerCell) {
			Intent intent = new Intent(this, PublisherAct.class);
			intent.putExtra("publisherId", activityInfo.organizerID);
			this.startActivity(intent);
		} else if (v == backButton) {
			this.finish();
		} else if (v == attendButton) {
			if (activityInfo.isAttend)
				return;
			if (activityInfo.currentCount >= activityInfo.totalCount) {
				Toast.makeText(ActivityInfoActivity.this, "报名人数已满",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (User.sharedUser().phoneNumber == null
					|| User.sharedUser().phoneNumber.isEmpty()) {
				new AlertDialog.Builder(this)
						.setMessage("请先补完个人资料")
						.setPositiveButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								})
						.setNeutralButton("设置",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										Intent intent = new Intent(
												ActivityInfoActivity.this,
												ModifyUserInfoAct.class);
										startActivity(intent);
									}
								}).show();
			} else {
				attendButton.setEnabled(false);
				new JointActivityRequest().setVuid(User.sharedUser().vuid)
						.setActivityId(activityInfo.activityID)
						.setHandler(new ResponseHandler() {

							@Override
							public void handleResponse(BaseRequest request,
									int statusCode, String errorMsg,
									String response) {
								Log.d("test", "join response" + response);
								// Toast.makeText(ActivityInfoActivity.this,
								// response, Toast.LENGTH_LONG).show();
								attendButton.setBackgroundColor(0xffe7e7e7);
								activityInfo.isAttend = true;
								Toast.makeText(ActivityInfoActivity.this,
										"已提交报名请求", Toast.LENGTH_LONG).show();
							}
						}).start();
				;
				// Toast.makeText(this, "已提交报名请求",
				// Toast.LENGTH_LONG).show();

			}

		} else if (v == locationCell) {
			if (activityInfo.latitude != 0 || activityInfo.longitude != 0) {
				Intent intent = new Intent(ActivityInfoActivity.this,
						MapViewActivity.class);
				intent.putExtra("lat", activityInfo.latitude);
				intent.putExtra("lng", activityInfo.longitude);
				startActivity(intent);
			}
		}
	}

	class MyActionListener implements PlatformActionListener, Callback {

		public void onComplete(Platform plat, int action,
				HashMap<String, Object> res) {

			Message msg = new Message();
			msg.arg1 = 1;
			msg.arg2 = action;
			msg.obj = plat;
			UIHandler.sendMessage(msg, MyActionListener.this);
		}

		public void onCancel(Platform palt, int action) {
			Message msg = new Message();
			msg.arg1 = 3;
			msg.arg2 = action;
			msg.obj = palt;
			UIHandler.sendMessage(msg, this);
		}

		public void onError(Platform palt, int action, Throwable t) {
			t.printStackTrace();

			Message msg = new Message();
			msg.arg1 = 2;
			msg.arg2 = action;
			msg.obj = palt;
			UIHandler.sendMessage(msg, this);
		}

		@Override
		public boolean handleMessage(Message msg) {
			Platform plat = (Platform) msg.obj;
			String text = actionToString(msg.arg2);
			switch (msg.arg1) {
			case 1: {
				// 成功
				// text = plat.getName() + " completed at " + text;
				text = "分享成功！";
			}
				break;
			case 2: {
				// 失败
				// text = plat.getName() + " caught error at " + text;
				text = "分享失败！";
			}
				break;
			case 3: {
				// 取消
				// text = plat.getName() + " canceled at " + text;
				text = "取消...";
			}
				break;
			}

			Toast.makeText(ActivityInfoActivity.this, text, Toast.LENGTH_SHORT)
					.show();
			return false;
		}
	}

	public String actionToString(int action) {
		switch (action) {
		case Platform.ACTION_AUTHORIZING:
			return "ACTION_AUTHORIZING";
		case Platform.ACTION_GETTING_FRIEND_LIST:
			return "ACTION_GETTING_FRIEND_LIST";
		case Platform.ACTION_FOLLOWING_USER:
			return "ACTION_FOLLOWING_USER";
		case Platform.ACTION_SENDING_DIRECT_MESSAGE:
			return "ACTION_SENDING_DIRECT_MESSAGE";
		case Platform.ACTION_TIMELINE:
			return "ACTION_TIMELINE";
		case Platform.ACTION_USER_INFOR:
			return "ACTION_USER_INFOR";
		case Platform.ACTION_SHARE:
			return "ACTION_SHARE";
		default: {
			return "UNKNOWN";
		}
		}
	}
}
