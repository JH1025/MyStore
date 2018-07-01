package com.MyStore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.MyStore.FriendsTabView.OtherUserAdapter;
import com.MyStore.FriendsTabView.backgroundLoadListView;

import Parse.FriendsInfoParse;
import Parse.ProductImageParse;
import Parse.ProductsInfoParse;
import Parse.UserImageParse;
import Parse.UserInfoParse;
import UserInfo.OtherUser;
import UserInfo.Product;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

public class OtherUserProductTabView extends Activity implements OnItemClickListener{
	private String otherUserID; //test
	private ProductAdapter pAdapter;
	private ArrayList<Product> products = new ArrayList<Product>();
	private ListView list1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productstabview);
		Intent i = getIntent();
		otherUserID = i.getExtras().getString("userID");

		
		 new backgroundLoadListView().execute();//�񵿱� �ٿ�...
	

	}

	
	
	public class backgroundLoadListView extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				
				//�̾ ��.....
				ProductsInfoParse productParse = new ProductsInfoParse();
				String Json = productParse.sendData(otherUserID);
				//EditText res = (EditText)findViewById(R.id.EditText01);
				//res.setText(userID);
				
				JSONArray ja = new JSONArray(Json);
				
				String id;
				String title;
				String period;
				String price;
				String explain;
				String owner;
				Bitmap image;
				for (int j = 0; j < ja.length(); j++) {
					JSONObject order = ja.getJSONObject(j);
					ProductImageParse ip=new ProductImageParse();	//test;;;;
					id = order.getString("id");
					title = order.getString("title");
					period = order.getString("period");
					price = order.getString("price");
					explain = order.getString("comment");
					owner = order.getString("owner");
					ip.downloadFile(id);	//test,test,test
					image=ip.getImage();
					products.add(new Product(image,title,period,price,explain,owner));
				}


			}catch (JSONException e) {
				return "��ϵ� ��ǰ�� �����ϴ�.";
				//Toast.makeText(getBaseContext(), "��ϵ� ��ǰ�� �����ϴ�.\n ��ǰ�� ������ּ���.", Toast.LENGTH_LONG).show();
				//Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			}catch (ClientProtocolException e) {
				return "�������� ����";
				//Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				//e.printStackTrace();
			}catch (IOException e) {
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
			
			//���߿� ȭ���߰��� dialog��....
			Toast.makeText(getBaseContext(), "�ٿ�ε� ���Դϴ�.", Toast.LENGTH_LONG).show();
			
		}
		
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(result != null){
				Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
			}
			
			
			
			pAdapter = new ProductAdapter(OtherUserProductTabView.this, R.layout.products_row, products);

			list1 = (ListView) findViewById(R.id.product_listview);
			list1.setAdapter(pAdapter);

			list1.setOnItemClickListener(OtherUserProductTabView.this);
		}
		
	}
	
	
	

	
	
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub

		Product data = (Product) parent.getItemAtPosition(position);
		// ���� ��Ƽ��Ƽ�� �ѱ� Bundle �����͸� �����.
		Bundle extras = new Bundle();

		Bitmap image = data.getProductImage();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 100, stream);
		byte[] b = stream.toByteArray();

		extras.putByteArray("image", b);
		extras.putString("title", data.getTitle());
		extras.putString("period", data.getPeriod());
		extras.putString("price", data.getPrice());
		extras.putString("explain", data.getExplain());

		// ����Ʈ�� �����Ѵ�.
		// ���ؽ�Ʈ�� ���� ��Ƽ��Ƽ��, ������ ��Ƽ��Ƽ�� ItemClickExampleNextActivity �� �����Ѵ�.
		Intent intent = new Intent(this, ViewOtherProductAct.class);
		// ������ ���� Bundle�� ����Ʈ�� �ִ´�.
		intent.putExtras(extras);
		// ��Ƽ��Ƽ�� �����Ѵ�.
		startActivity(intent);

	}

	class ProductAdapter extends ArrayAdapter<Product> {

		private ArrayList<Product> items;

		public ProductAdapter(Context context, int textViewResourceId,
				ArrayList<Product> items) {
			super(context, textViewResourceId, items);
			this.items = items;

			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;

			if (row == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				row = vi.inflate(R.layout.products_row, null);
			}

			TextView tv1 = (TextView) row.findViewById(R.id.product_title);
			tv1.setText(items.get(position).getTitle());
			TextView tv2 = (TextView) row.findViewById(R.id.product_period);
			tv2.setText(items.get(position).getPeriod());
			TextView tv3 = (TextView) row.findViewById(R.id.product_price);
			tv3.setText(items.get(position).getPrice());
			TextView tv4 = (TextView) row.findViewById(R.id.product_explain);
			tv4.setText(items.get(position).getExplain());
			ImageView iv = (ImageView) row.findViewById(R.id.product_image);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setImageBitmap(items.get(position).getProductImage());
			//iv.setImageResource(R.drawable.icon);
			
			return row;

		}

	}

}
