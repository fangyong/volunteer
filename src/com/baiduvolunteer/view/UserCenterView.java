package com.baiduvolunteer.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baiduvolunteer.R;
import com.baiduvolunteer.activity.FavoritesActivity;
import com.baiduvolunteer.activity.JoinedActivitiesActivity;
import com.baiduvolunteer.activity.ModifyUserInfoAct;
import com.baiduvolunteer.model.User;

public class UserCenterView extends LinearLayout {

	private View favCell;
	private View joinedCell;

	private TextView userNameLabel;
	
	public UserCenterView(Context context) {
		super(context);
	}

	public UserCenterView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void onPause() {
		// TODO Auto-generated method stub

	}
	
	public void onResume(){
		this.updateInfo();
	}
	
	private void updateInfo(){
		if(userNameLabel!=null){
			userNameLabel.setText(User.sharedUser().uname);
		}
	}
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		favCell = this.findViewById(R.id.favCell);
		favCell.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getContext(),
						FavoritesActivity.class);
				getContext().startActivity(intent);
			}
		});
		joinedCell = this.findViewById(R.id.joinedCell);
		joinedCell.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getContext(),
						JoinedActivitiesActivity.class);
				getContext().startActivity(intent);
			}
		});
		this.userNameLabel = (TextView) findViewById(R.id.userNameLabel);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		updateInfo();
		findViewById(R.id.userinfo_ll).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(getContext(),
								ModifyUserInfoAct.class);
						getContext().startActivity(intent);
					}
				});
	}

}
