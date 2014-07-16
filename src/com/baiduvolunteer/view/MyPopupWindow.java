package com.baiduvolunteer.view;

import com.baiduvolunteer.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

@SuppressLint("ViewConstructor")
public class MyPopupWindow extends PopupWindow {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	public MyPopupWindow(View view, boolean needAnimation) {
		super(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setTouchable(true);
		setBackgroundDrawable(new BitmapDrawable());
		if (needAnimation)
			this.setAnimationStyle(R.style.PopupAnimation);
		update();
	}

}
