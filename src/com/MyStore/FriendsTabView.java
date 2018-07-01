package com.MyStore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import BroadCast.FriendBroadcastReceiver;
import Parse.FriendsInfoParse;
import Parse.UserImageParse;
import Parse.UserInfoParse;
import UserInfo.OtherUser;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsTabView extends Activity implements OnItemClickListener {
	public static final String NEW_LIFEFORM_DETECTED = "com.MyStore.action.NEW_Friend"; // broadcast....
	private String userID; // test
	
	private OtherUserAdapter ouAdapter;
	public ArrayList<OtherUser> friends = new ArrayList<OtherUser>();
	private ListView list2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendstabview);
	
		
		IntentFilter filter = new IntentFilter(NEW_LIFEFORM_DETECTED);
		FriendBroadcastReceiver r = new FriendBroadcastReceiver();
		registerReceiver(r, filter);
		
		r.setFriends(friends);

		Intent i = getIntent();
		userID = i.getExtras().getString("userID");

		
		 new backgroundLoadListView().execute();
		
		// Broad caster ���

		

	}
	
	
	
	
	
	public class backgroundLoadListView extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			
			try {

				// �̾ ��.....
				FriendsInfoParse friendParse = new FriendsInfoParse();
				String Json = friendParse.sendData(userID);

				JSONArray ja = new JSONArray(Json);

				String id;
				String userID;
				String friendID;
				
				for (int j = 0; j < ja.length(); j++) {

					JSONObject order = ja.getJSONObject(j);
					id = order.getString("id");
					userID = order.getString("user_id");
					friendID = order.getString("friend_id");

					// /////////////////////testtesttest/////////////////////
					UserInfoParse uif = new UserInfoParse();
					String json2 = uif.sendData(friendID);
					JSONArray ja2 = new JSONArray(json2);
					order = ja2.getJSONObject(0);

					String passwd;
					String name;
					String address;
					String phoneNumber;

					friendID = order.getString("id");
					passwd = order.getString("password");
					name = order.getString("name");
					phoneNumber = order.getString("phoneNumber");
					address = order.getString("address");

					UserImageParse uip = new UserImageParse();
					uip.downloadFile(friendID);
					Bitmap image = uip.getImage();

					// parsing�� �̹����� byte�� ��ȯ�Ͽ� user �� �ִ´� serializable������...
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					image.compress(CompressFormat.PNG, 100, stream);
					byte[] b = stream.toByteArray();
					friends.add(new OtherUser(b, friendID, name, phoneNumber,address));
					
					
				}

			} catch (JSONException e) {
				return "��ϵ� ��� ������ �����ϴ�.";
				//Toast.makeText(getBaseContext(), "��ϵ� ��� ������ �����ϴ�.", Toast.LENGTH_LONG).show();
				// Toast.makeText(v.getContext(), e.getMessage(),
				// Toast.LENGTH_LONG).show();
			} catch (ClientProtocolException e) {
				return "�������� ����";
				//Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				//e.printStackTrace();
			} catch (IOException e) {
				return "��Ʈ��ũ ������ ��Ȱ���� �ʽ��ϴ�.";
				//Toast.makeText(getBaseContext(), "��Ʈ��ũ ������ ��Ȱ���� �ʽ��ϴ�.", Toast.LENGTH_LONG).show();
				//e.printStackTrace();
			} catch (Exception e) {
				return "����";
				//Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			}
			
			return null;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Toast.makeText(getBaseContext(), "�ٿ�ε� ���Դϴ�.", Toast.LENGTH_LONG).show();
		}
		
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(result != null){
				Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
			}
			
			ouAdapter = new OtherUserAdapter(FriendsTabView.this,R.layout.friends_row, friends);
			list2 = (ListView) findViewById(R.id.friend_listview);
			list2.setAdapter(ouAdapter);
			list2.setOnItemClickListener(FriendsTabView.this);
		}
		
	}
	
	
	

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub

		OtherUser data = (OtherUser) parent.getItemAtPosition(position);
		// ���� ��Ƽ��Ƽ�� �ѱ� Bundle �����͸� �����.
		Bundle extras = new Bundle();

		extras.putSerializable("otherUserID", data.getID());
		extras.putString("userID", userID);
		// ����Ʈ�� �����Ѵ�.
		// ���ؽ�Ʈ�� ���� ��Ƽ��Ƽ��, ������ ��Ƽ��Ƽ�� ItemClickExampleNextActivity �� �����Ѵ�.
		Intent intent = new Intent(this, OtherStoreAct.class);
		// ������ ���� Bundle�� ����Ʈ�� �ִ´�.
		intent.putExtras(extras);
		// ��Ƽ��Ƽ�� �����Ѵ�.
		startActivity(intent);

	}
	
	

	class OtherUserAdapter extends ArrayAdapter<OtherUser> {

		private ArrayList<OtherUser> items;

		public OtherUserAdapter(Context context, int textViewResourceId,
				ArrayList<OtherUser> items) {
			super(context, textViewResourceId, items);
			this.items = items;

			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;

			if (row == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				row = vi.inflate(R.layout.friends_row, null);
			}

			TextView tv1 = (TextView) row.findViewById(R.id.friend_name);
			tv1.setText(items.get(position).getName());
			TextView tv2 = (TextView) row.findViewById(R.id.friend_phoneNumber);
			tv2.setText(items.get(position).getPhoneNumber());
			TextView tv3 = (TextView) row.findViewById(R.id.friend_address);
			tv3.setText(items.get(position).getAddress());
			ImageView iv = (ImageView) row.findViewById(R.id.friend_image);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setImageBitmap(items.get(position).getProfileImage());

			/*
			 * test!!!!!! switch(position){ case 0:
			 * iv.setImageResource(R.drawable.yuna); break; case 1:
			 * iv.setImageResource(R.drawable.taeyun); break; case 2:
			 * iv.setImageResource(R.drawable.yuri); break; case 3:
			 * iv.setImageResource(R.drawable.suny); break; case 4:
			 * iv.setImageResource(R.drawable.seyun); break; case 5:
			 * iv.setImageResource(R.drawable.jesica); break; default:
			 * iv.setImageResource(R.drawable.icon);
			 * 
			 * }
			 */

			return row;

		}

	}

	
}



