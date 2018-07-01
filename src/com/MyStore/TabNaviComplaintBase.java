package com.MyStore;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class TabNaviComplaintBase extends LinearLayout{
	Context context;
	
	public TabNaviComplaintBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.tab_navi_complatint, this, true);
	}
}