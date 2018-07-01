package com.MyStore;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import Delete.DeleteUser;
import UserInfo.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class SettingTabView extends Activity implements OnItemClickListener{
	private User user; //test
	private SettingAdapter sAdapter;
	private ArrayList<Setting> settings = new ArrayList<Setting>();
	private ListView list;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingtabview);
		
		
	
		Intent i = getIntent();
		user = (User)i.getExtras().getSerializable("user");
	
		settings.add(new Setting("������ ����                              >>","�� �������� �����մϴ�."));
		settings.add(new Setting("���� ����                                 >>","�� ���� �ɼ��� �����մϴ�."));
		settings.add(new Setting("ȸ�� Ż��                                 >>","ȸ�� ������ Ż���մϴ�."));
		settings.add(new Setting("default                                   >>",""));
		settings.add(new Setting("default                                   >>",""));
		
		sAdapter = new SettingAdapter(this, R.layout.setting_row, settings);

		list = (ListView) findViewById(R.id.setting_listview);
		list.setAdapter(sAdapter);

		list.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub
	
		switch(position){
		case 0:
			Intent i = new Intent(this,SettingMyProfileAct.class);
			i.putExtra("user", user);
			startActivityForResult(i, 0);
			break;
		case 1:
			Intent j = new Intent(this,SettingMyStoreAct.class);
			j.putExtra("userID", user.getID());	//user ��ü??
			startActivity(j);
			break;
		case 2:
			confirmDeleteAgreement();
			break;
		default:
			Toast.makeText(v.getContext(), "default �Դϴ�...",Toast.LENGTH_LONG).show();
			break;
		}
	}

	

	private void confirmDeleteAgreement() {

		String gs = android.provider.Settings.Secure.getString(
				getContentResolver(),

				android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		AlertDialog.Builder confirmDialog = new AlertDialog.Builder(this);

		confirmDialog.setTitle("ȸ��Ż��");
		confirmDialog.setMessage("ȸ��Ż�� �Ͻø� ��������� ���� �˴ϴ�.\n���� �Ͻðڽ��ϱ�?");

		confirmDialog.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						DeleteUser du = new DeleteUser();
						try {

							du.sendData(user.getID());
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

						Toast.makeText(getBaseContext(), "ȸ�� Ż�� �Ͽ����ϴ�.", Toast.LENGTH_LONG)
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
	
	
	
	
	class SettingAdapter extends ArrayAdapter<Setting> {

		private ArrayList<Setting> items;

		public SettingAdapter(Context context, int textViewResourceId,
				ArrayList<Setting> items) {
			super(context, textViewResourceId, items);
			this.items = items;

			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;

			if (row == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				row = vi.inflate(R.layout.setting_row, null);
			}

			TextView tv1 = (TextView) row.findViewById(R.id.setting_title);
			tv1.setText(items.get(position).getTitle());
			TextView tv2 = (TextView) row.findViewById(R.id.setting_explain);
			tv2.setText(items.get(position).getExplain());
			
			return row;

		}

	}
	
	
	public class Setting {
		String title;
		String explain;
		
		public Setting(String title, String explain){
			this.title = title;
			this.explain = explain;
		}
		
		
		public String getTitle(){
			return title;
		}
		public String getExplain(){
			return explain;
		}
		

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			
			String name = data.getExtras().getString("modifiedName");
			String address = data.getExtras().getString("modifiedAddress");
			byte[] userImage = data.getExtras().getByteArray("modifiedImage");
			
			user.setName(name);
			user.setAddress(address);
			user.setUserImage(userImage);
			
		}
	}




}
