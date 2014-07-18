package com.baiduvolunteer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class BadgedImageView extends RelativeLayout {
	private ImageView badgeImageView;
	private ImageView backgroundImageView;
	
	
	public BadgedImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		badgeImageView = new ImageView(context);
		backgroundImageView = new ImageView(context);
		
	}
	
	public BadgedImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BadgedImageView(Context context) {
		this(context, null);
	}
	
}
