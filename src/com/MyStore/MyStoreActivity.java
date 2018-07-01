package com.MyStore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.MyStore.FriendsTabView.OtherUserAdapter;
import com.MyStore.FriendsTabView.backgroundLoadListView;

import Parse.FriendsInfoParse;
import Parse.UserImageParse;
import Parse.UserInfoParse;
import UserInfo.OtherUser;
import UserInfo.User;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MyStoreActivity extends Activity implements OnClickListener {
	User user;
	private final Handler myHandler = new Handler();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		user = null;

		View logInButton = findViewById(R.id.log_in_button);
		logInButton.setOnClickListener(this);
		View joinButton = findViewById(R.id.join_button);
		joinButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.log_in_button:

			startActivity(new Intent(this, SplashAct.class));

			new getUserFromServer().execute(); // 시간이 걸리는 작업

			break;

		case R.id.join_button:
			Intent j = new Intent(this, MembershipAct.class);
			startActivity(j);
			break;

		}

	}


	// thread...

	public class getUserFromServer extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// 여기서부터 초기화 작업 처리
						UserInfoParse userParse = new UserInfoParse();

						EditText idTextBox = (EditText) findViewById(R.id.id_text_box);
						EditText passwdTextBox = (EditText) findViewById(R.id.password_text_box);
/*
						// 일단 보류... 난제 함수로 떼내자.. check 로...
						try {

							String Json = userParse
									.sendData(idTextBox.getText().toString());

							JSONArray ja = new JSONArray(Json);

							String id;
							String passwd;
							String name;
							String address;
							String phoneNumber;

							JSONObject order = ja.getJSONObject(0);
							id = order.getString("id");
							passwd = order.getString("password");
							name = order.getString("name");
							address = order.getString("address");
							phoneNumber = order.getString("phoneNumber");

							if (!passwd.equals(passwdTextBox.getText().toString())) {
								throw new Exception("비밀번호가 맞지 않습니다.");
							}

							// image parsing
							UserImageParse ip = new UserImageParse();
							ip.downloadFile(id); // test,test,test
							Bitmap image = ip.getImage();

							// parsing된 이미지를 byte로 변환하여 user 에 넣는다 serializable때문에...
							ByteArrayOutputStream stream = new ByteArrayOutputStream();
							image.compress(CompressFormat.PNG, 100, stream);
							byte[] b = stream.toByteArray();


							user = new User(b, id, passwd, name, address, phoneNumber);
							
							
							
							
							
							  //testtesttest passwdTextBox.setText( "ID : " + user.getID() +
							  //"\n" +"passwd : " + user.getPasswd() + "\n" +"이름 : " +
							  //user.getName() + "\n" +"주소 : " + user.getAddress() + "\n"
							  //+"폰번호 : " + user.getPhoneNumber());
							 

						} catch (JSONException e) {
							return "아이디가 없습니다";
							//Toast.makeText(getBaseContext(), "아이디가 없습니다.",Toast.LENGTH_LONG).show();
							// e.printStackTrace();
							// Toast.makeText(v.getContext(), e.getMessage(),
							// Toast.LENGTH_LONG).show();
						} catch (ClientProtocolException e) {
							return "프로토콜 에러";
							//Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_LONG).show();
							// e.printStackTrace();
						} catch (IOException e) {
							return "네트워크 연결이 원활하지 않습니다.";
							//Toast.makeText(getBaseContext(), "네트워크 연결이 원활하지 않습니다.",Toast.LENGTH_LONG).show();
							// e.printStackTrace();
						} catch (Exception e) {
							return "비밀번호가 맞지않습니다.";
							//Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_LONG).show();
						}
	*/	
			
			return null;
		}
	
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(result != null){
				Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
				return;
			}
			
			//testtest
			user = new User(null, "", "", "", "", "");	//test
			//testtest
			
			
			
			// ... update the UI
			Intent i = new Intent(MyStoreActivity.this, UserStoreAct.class);
			i.putExtra("user", user);
			startActivity(i);
			
		}
		
	}
	


}