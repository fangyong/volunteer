package com.baiduvolunteer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baiduvolunteer.R;

public class SettingCell extends RelativeLayout {
	public ImageView iconView;
	public ImageView detailIconView;
	public TextView textLabel;

	public SettingCell(Context context) {
		this(context, null);
	}

	public SettingCell(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SettingCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		iconView = (ImageView) findViewById(R.id.iconView);
		detailIconView = (ImageView) findViewById(R.id.detailIcon);
		textLabel = (TextView) findViewById(R.id.textLabel);
	}

}
