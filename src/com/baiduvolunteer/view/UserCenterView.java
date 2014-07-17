package com.baiduvolunteer.view;

import com.baiduvolunteer.R;
import com.baiduvolunteer.activity.ModifyUserInfoAct;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class UserCenterView extends LinearLayout {

	public UserCenterView(Context context) {
		super(context);
	}

	public UserCenterView(Context context, AttributeSet attrs) {
		super(context, attrs);
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
