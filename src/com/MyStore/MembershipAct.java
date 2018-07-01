package com.MyStore;


import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import Insert.InsertUser;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MembershipAct extends Activity implements OnClickListener{
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.membership);
		
		
		Button btn =(Button)findViewById(R.id.res_button);
        btn.setOnClickListener( this );
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String id = ((EditText) findViewById(R.id.userFormID)).getText().toString();
		String passwd = ((EditText) findViewById(R.id.userFormPasswd)).getText().toString();
		String name = ((EditText) findViewById(R.id.userFormName)).getText().toString();
		String address = ((EditText) findViewById(R.id.userFormAddress)).getText().toString();
		String phoneNumber = ((EditText) findViewById(R.id.userFormPhoneNumber)).getText().toString();
		
		try {
			
			if(id =="" || passwd =="" ||name == "" || address == "" || phoneNumber ==""){
				throw new Exception("빈칸없이 모두 입력해주세요.");
			}
			
			Bitmap defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.no_photo2);	//default image 구하기
			
			InsertUser insertUser = new InsertUser();
			insertUser.sendData(defaultImage,id,passwd,name,address,phoneNumber);
			
			
			finish();
			
		} catch (ClientProtocolException e) {
			Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(v.getContext(), "네트워크 연결이 원활하지 않습니다.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (Exception e) {
			Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
		
	}

	

	
}
