package com.MyStore;

import com.MyStore.MyStoreActivity.getUserFromServer;

import UserInfo.User;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;


public class TestTabView extends ActivityGroup implements OnClickListener{
	User user;
	LinearLayout lay;
	
	Window w;
	View v1;
	LocalActivityManager m;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_tabview);
		
		Intent in = getIntent();
        user=(User) in.getSerializableExtra("user");
		
        lay = (LinearLayout)findViewById(R.id.testview);
        
		Button btn1 = (Button)findViewById(R.id.btn1);
		Button btn2 = (Button)findViewById(R.id.btn2);
		Button btn3 = (Button)findViewById(R.id.btn3);
		Button btn4 = (Button)findViewById(R.id.btn4);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		
		
		m = getLocalActivityManager();
		//default layout ÇÊ¿ä
		Intent i = new Intent().setClass(this, ProductsTabView.class);
		i.putExtra("userID", user.getID());
		w = m.startActivity("test", i);
		v1 = w.getDecorView();
		lay.addView(v1);
	
	
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		lay.removeAllViews();
		
		
		switch (v.getId()) {
		case R.id.btn1:
			Intent i1 = new Intent().setClass(this, ProductsTabView.class);
			i1.putExtra("userID", user.getID());
			w = m.startActivity("test", i1);
			v1 = w.getDecorView();
			lay.addView(v1);
			
			break;

		case R.id.btn2:
			Intent i2 = new Intent().setClass(this, MapTabView.class);
			i2.putExtra("userID", user.getID());
			w = m.startActivity("test", i2);
			v1 = w.getDecorView();
			lay.addView(v1);
			
			break;
		case R.id.btn3:
			Intent i3 = new Intent().setClass(this, FriendsTabView.class);
			i3.putExtra("userID", user.getID());
			w = m.startActivity("test", i3);
			v1 = w.getDecorView();
			lay.addView(v1);
			break;
		case R.id.btn4:
			Intent i4 = new Intent().setClass(this, SettingTabView.class);
			i4.putExtra("user", user);
			w = m.startActivity("test", i4);
			v1 = w.getDecorView();
			lay.addView(v1);
			break;

		}

	}
}
