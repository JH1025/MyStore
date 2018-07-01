package com.MyStore;

import UserInfo.User;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;




public class UserStoreAct extends TabActivity {
	User user;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_store);
        
        //test
        Intent i = getIntent();
        user=(User) i.getSerializableExtra("user");
        
        //EditText res = (EditText)findViewById(R.id.EditText01);
       // res.setText(user.getID());
        
    
        
        //처음에는 서버에서 받아오기.... 다음에는............????
        //add후 여기에 추가....
        //product Item을 추가하여..................... 화면에 표시...   
      
        
      
       TabHost mTabHost = getTabHost();
       

  /*      
        mTabHost.addTab(mTabHost.newTabSpec("tab_test1")
        		.setIndicator("내가게관리")
        		.setContent(R.id.listview1)
        		);
    */    
        TabSpec tabSpec1 = mTabHost.newTabSpec("tab_test1");
       	tabSpec1.setIndicator("내가게관리",getResources().getDrawable(R.drawable.home));
        Context ctx1 = this.getApplicationContext();
        
        Intent i1 = new Intent(ctx1, ProductsTabView.class);
        //test
        i1.putExtra("userID", user.getID());
        
        tabSpec1.setContent(i1);
        mTabHost.addTab(tabSpec1);
        
        
 /*       mTabHost.addTab(mTabHost.newTabSpec("tab_test2")
        		.setIndicator("상품검색")
        		.setContent(R.id.view2)
        		);
        		*/
        TabSpec tabSpec2 = mTabHost.newTabSpec("tab_test2");
        tabSpec2.setIndicator("상품검색",getResources().getDrawable(R.drawable.search));
        Context ctx2 = this.getApplicationContext();
        
        Intent i2 = new Intent(ctx2, MapTabView.class);	//test!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        i2.putExtra("userID", user.getID());
        tabSpec2.setContent(i2);
        mTabHost.addTab(tabSpec2);
        
        
        /*
        mTabHost.addTab(mTabHost.newTabSpec("tab_test3")
        		.setIndicator("내 단골손님 관리")
        		.setContent(R.id.listview2)
        		);
        */
        TabSpec tabSpec3 = mTabHost.newTabSpec("tab_test3");
        tabSpec3.setIndicator("내 단골손님 관리",getResources().getDrawable(R.drawable.people));
        Context ctx3 = this.getApplicationContext();
        
        Intent i3 = new Intent(ctx3, FriendsTabView.class);	
        //test
        i3.putExtra("userID", user.getID());
        
        tabSpec3.setContent(i3);
        mTabHost.addTab(tabSpec3);
        
        
        
   
        
        TabSpec tabSpec4 = mTabHost.newTabSpec("tab_test4");
        tabSpec4.setIndicator("설정",getResources().getDrawable(R.drawable.setting));
        Context ctx4 = this.getApplicationContext();
        
        Intent i4 = new Intent(ctx4, SettingTabView.class);	
        i4.putExtra("user", user);
        //test
      
        tabSpec4.setContent(i4);
        mTabHost.addTab(tabSpec4);
		
        
        TabSpec tabSpec5 = mTabHost.newTabSpec("tab_test5");
        tabSpec5.setIndicator("테스트",getResources().getDrawable(R.drawable.setting));
        Context ctx5 = this.getApplicationContext();
        
        Intent i5 = new Intent(ctx5, TestTabView.class);	
        i5.putExtra("user", user);
        //test
      
        tabSpec5.setContent(i5);
        mTabHost.addTab(tabSpec5);
        
       
//		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        
        //mTabHost.setCurrentTab(0);
        
		for (int tab = 0; tab < mTabHost.getTabWidget().getChildCount(); tab++) {
			//탭제목 color지정
			RelativeLayout relLayout = (RelativeLayout) mTabHost.getTabWidget().getChildAt(tab);
			TextView tv = (TextView) relLayout.getChildAt(1);
			tv.setTextColor(ColorStateList.valueOf(Color.WHITE));
			//탭위젯의 size지정
			mTabHost.getTabWidget().getChildAt(tab).getLayoutParams().height = 120;
		}

        
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			
			String name = data.getExtras().getString("modifiedName");
			String address = data.getExtras().getString("modifiedAddress");
			
			user.setName(name);
			user.setAddress(address);
			
		}
		
		if(resultCode == RESULT_CANCELED){
			
		}
		
	}

	
}
    
    
    
    
    
    
