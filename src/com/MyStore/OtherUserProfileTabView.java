package com.MyStore;

import UserInfo.OtherUser;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class OtherUserProfileTabView extends Activity implements
		OnClickListener {
	OtherUser otherUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.otheruser_profile_tabview);

		Intent i = getIntent();
		otherUser = (OtherUser) i.getExtras().getSerializable("otherUser");

		ImageView profileImage = (ImageView) findViewById(R.id.other_profile_image);
		TextView phoneNumberView = (TextView) findViewById(R.id.other_profile_phoneNumber_textView);
		TextView idView = (TextView) findViewById(R.id.other_profile_ID_textView);
		TextView nameView = (TextView) findViewById(R.id.other_profile_name_textView);
		TextView addressView = (TextView) findViewById(R.id.other_profile_address_textView);
		Button callButton = (Button) findViewById(R.id.other_call_button);

		profileImage.setScaleType(ScaleType.FIT_XY);
		profileImage.setImageBitmap(otherUser.getProfileImage());
		phoneNumberView.append(otherUser.getPhoneNumber());
		idView.append(otherUser.getID());
		nameView.append(otherUser.getName());
		addressView.append(otherUser.getAddress());

		callButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		performDial();
	}

	/** 전화걸기 실행 함수 */
	public void performDial() {
		try {
			startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + otherUser.getPhoneNumber())));
			//startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + otherUser.getPhoneNumber())));
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

	}

}
