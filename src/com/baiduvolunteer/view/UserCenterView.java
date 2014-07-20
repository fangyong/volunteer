package com.baiduvolunteer.view;

import com.baiduvolunteer.R;
import com.baiduvolunteer.activity.FavoritesActivity;
import com.baiduvolunteer.activity.ModifyUserInfoAct;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class UserCenterView extends LinearLayout {

	private View favCell;

	public UserCenterView(Context context) {
		super(context);
	}

	public UserCenterView(Context context, AttributeSet attrs) {
		super(context, attrs);
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
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
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
