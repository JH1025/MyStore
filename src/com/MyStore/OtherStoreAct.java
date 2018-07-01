package com.MyStore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Insert.InsertFriend;
import Parse.UserImageParse;
import Parse.UserInfoParse;
import UserInfo.OtherUser;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class OtherStoreAct extends TabActivity{
	public static final String NEW_LIFEFORM_DETECTED ="com.MyStore.action.NEW_Friend"; //broadcast ���

	OtherUser otherUser;
	String userID;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_store);
        
        //test
        Intent i = getIntent();
        userID = i.getExtras().getString("userID");
        String otherUserID =i.getExtras().getString("otherUserID");
        setOtherUser(otherUserID);
        //EditText res = (EditText)findViewById(R.id.EditText01);
        // res.setText(user.getID());
        
    
        
        //ó������ �������� �޾ƿ���.... ��������............????
        //add�� ���⿡ �߰�....
        //product Item�� �߰��Ͽ�..................... ȭ�鿡 ǥ��...   
      
        
      
       TabHost mTabHost = getTabHost();
       

  /*      
        mTabHost.addTab(mTabHost.newTabSpec("tab_test1")
        		.setIndicator("�����԰���")
        		.setContent(R.id.listview1)
        		);
    */    
        TabSpec tabSpec1 = mTabHost.newTabSpec("tab_test1");
       	tabSpec1.setIndicator("���� ��ǰ",getResources().getDrawable(R.drawable.product));
        Context ctx1 = this.getApplicationContext();
        
        Intent i1 = new Intent(ctx1, OtherUserProductTabView.class);
        //test
        i1.putExtra("userID", otherUser.getID());
        
        tabSpec1.setContent(i1);
        mTabHost.addTab(tabSpec1);
        
        TabSpec tabSpec2 = mTabHost.newTabSpec("tab_test2");
       	tabSpec2.setIndicator("��ȭ�ϱ�",getResources().getDrawable(R.drawable.talk));
        Context ctx2 = this.getApplicationContext();
        
        Intent i2 = new Intent(ctx2, ChattingTabView.class);
        //test
        //i2.putExtra("userID", otherUser.getID());
        
        tabSpec2.setContent(i2);
        mTabHost.addTab(tabSpec2);
        
        
 /*       mTabHost.addTab(mTabHost.newTabSpec("tab_test2")
        		.setIndicator("��ǰ�˻�")
        		.setContent(R.id.view2)
        		);
        		*/
     
        
        
        TabSpec tabSpec3 = mTabHost.newTabSpec("tab_test3");
        tabSpec3.setIndicator("���� ����",getResources().getDrawable(R.drawable.profile));
        Context ctx3 = this.getApplicationContext();
        
        Intent i3 = new Intent(ctx3, OtherUserProfileTabView.class);	
      
        i3.putExtra("otherUser", otherUser);
        tabSpec3.setContent(i3);
        mTabHost.addTab(tabSpec3);
		
       
//		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        
        //mTabHost.setCurrentTab(0);
        
		for (int tab = 0; tab < mTabHost.getTabWidget().getChildCount(); tab++) {
			//������ color����
			RelativeLayout relLayout = (RelativeLayout) mTabHost.getTabWidget().getChildAt(tab);
			TextView tv = (TextView) relLayout.getChildAt(1);
			tv.setTextColor(ColorStateList.valueOf(Color.WHITE));
			//�������� size����
			mTabHost.getTabWidget().getChildAt(tab).getLayoutParams().height = 120;
		}

        
    }
    
    private void setOtherUser(String otherserID){
    	
    	UserInfoParse userParse = new UserInfoParse();

		//  �ϴ� ����... ���� �Լ��� ������.. check ��...
		try {
			
			String Json = userParse.sendData(otherserID);
			
			
			JSONArray ja = new JSONArray(Json);

			
			String id;
			String name;
			String address;
			String phoneNumber;
			
			JSONObject order = ja.getJSONObject(0);
			id=order.getString("id");
			name=order.getString("name");
			address=order.getString("address");
			phoneNumber = order.getString("phoneNumber");
			
		
			//image parsing
			UserImageParse ip = new UserImageParse();
			ip.downloadFile(id);	//test,test,test
			Bitmap image=ip.getImage();
			
			//parsing�� �̹����� byte�� ��ȯ�Ͽ� user �� �ִ´� serializable������...
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			image.compress(CompressFormat.PNG, 100, stream);
			byte[] b = stream.toByteArray();
		
			otherUser = new OtherUser(b,id,name,phoneNumber,address);
			

		}catch (JSONException e) {
			Toast.makeText(getBaseContext(),"���̵� �����ϴ�.", Toast.LENGTH_LONG).show();
			//e.printStackTrace();
			//Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}catch (ClientProtocolException e) {
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			//e.printStackTrace();
		}catch (IOException e) {
			Toast.makeText(getBaseContext(), "��Ʈ��ũ ������ ��Ȱ���� �ʽ��ϴ�.", Toast.LENGTH_LONG).show();
			//e.printStackTrace();
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage() , Toast.LENGTH_LONG).show();
		}
		
    	
    }
    
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem item = menu.add(0, 1, 0, "���� �߰�");
		item.setIcon(R.drawable.add_button);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			addFriendAgreement();
			return true;

			// ���߿� �޴��߰���.... ���߰�
		}

		return false;
	}
	
	private void addFriendAgreement() {

		String gs = android.provider.Settings.Secure.getString(
				getContentResolver(),

				android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		AlertDialog.Builder confirmDialog = new AlertDialog.Builder(this);

		confirmDialog.setTitle("����߰�");
		confirmDialog.setMessage("���� �߰� �Ͻðڽ��ϱ�?");

		confirmDialog.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						InsertFriend du = new InsertFriend();
						try {

							du.sendData(userID, otherUser.getID());
							
							//test broadcast
							Intent i = new Intent(NEW_LIFEFORM_DETECTED);
							i.putExtra("newFriend", otherUser);
							sendBroadcast(i);

					
							//setResult(RESULT_CANCELED, i); �ҷ����°� ����.....
							finish();

						} catch (ClientProtocolException e) {
							Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG)
									.show();
							//e.printStackTrace();
						} catch (IOException e) {
							Toast.makeText(getBaseContext(), "��Ʈ��ũ ������ ��Ȱ���� �ʽ��ϴ�.",
									Toast.LENGTH_LONG).show();
							//e.printStackTrace();
						} catch (Exception e) {
							Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG)
									.show();
						}

						Toast.makeText(getBaseContext(), "���� �߰��Ǿ����ϴ�.", Toast.LENGTH_LONG)
								.show();

					}

				});
		
		confirmDialog.setNegativeButton("���", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(getBaseContext(), "��� �Ǿ����ϴ�.", Toast.LENGTH_LONG)
								.show();

					}
				}).create().show();

	}
	

    

}
