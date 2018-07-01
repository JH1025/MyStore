package com.MyStore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Parse.ProductImageParse;
import Parse.ProductsInfoParse;
import UserInfo.Product;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductsTabView extends Activity implements OnItemClickListener {
	private String userID; //test
	private ProductAdapter pAdapter;
	private ArrayList<Product> products = new ArrayList<Product>();
	private ListView list1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productstabview);
		Intent i = getIntent();
		userID = i.getExtras().getString("userID");

		try {
			
			//이어서 쭉.....
			ProductsInfoParse productParse = new ProductsInfoParse();
			String Json = productParse.sendData(userID);
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
			Toast.makeText(this, "등록된 상품이 없습니다.\n 상품을 등록해주세요.", Toast.LENGTH_LONG).show();
			//Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}catch (ClientProtocolException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}catch (IOException e) {
			Toast.makeText(this, "네트워크 연결이 원활하지 않습니다.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
		
		pAdapter = new ProductAdapter(this, R.layout.products_row, products);

		list1 = (ListView) findViewById(R.id.product_listview);
		list1.setAdapter(pAdapter);

		list1.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub

		Product data = (Product) parent.getItemAtPosition(position);
		// 다음 액티비티로 넘길 Bundle 데이터를 만든다.
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
		extras.putInt("position", position);
		extras.putString("userID", data.getOwner());

		// 인텐트를 생성한다.
		// 컨텍스트로 현재 액티비티를, 생성할 액티비티로 ItemClickExampleNextActivity 를 지정한다.
		Intent intent = new Intent(this, EditProductAct.class);
		// 위에서 만든 Bundle을 인텐트에 넣는다.
		intent.putExtras(extras);
		// 액티비티를 생성한다.
		startActivity(intent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem item = menu.add(0, 1, 0, "추가");
		item.setIcon(R.drawable.add_button);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:

			Intent j = new Intent(this, AddProductAct.class);
			j.putExtra("userID", userID);
			startActivityForResult(j, 0);

			return true;

			// 나중에 메뉴추가시.... 더추가
		}

		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//create
		if (resultCode == RESULT_OK) {
			if(true){
			}

			// 바이트로 를 bitmap 형식으로 변환...
			byte[] b = (byte[]) data.getExtras().get("image"); // 변환시킬 바이트 배열
			Bitmap image = BitmapFactory.decodeByteArray(b, 0, b.length);

			String title = data.getExtras().getString("title");
			String period = data.getExtras().getString("period");
			String price = data.getExtras().getString("price");
			String explain = data.getExtras().getString("explain");

			products.add(new Product(image, title, period, price,explain,userID));

		}
		//modify
		
		/*
		else if(resultCode == RESULT_CANCELED){
			int position = data.getExtras().getInt("position");
			// 바이트로 를 bitmap 형식으로 변환...
			//byte[] b = (byte[]) data.getExtras().get("image"); // 변환시킬 바이트 배열
			//Bitmap image = BitmapFactory.decodeByteArray(b, 0, b.length);
			
			String title = data.getExtras().getString("title");
			String period = data.getExtras().getString("period");
			String price = data.getExtras().getString("price");
			String explain = data.getExtras().getString("explain");
			
			
			//products.get(position).setProductImage(image);
			products.get(position).setTitle(title);
			products.get(position).setPeriod(period);
			products.get(position).setPrice(price);
			products.get(position).setExplain(explain);
			pAdapter.notifyDataSetChanged();

		}
		
		//delete
		else if(resultCode == RESULT_FIRST_USER){
			int position = data.getExtras().getInt("position");
			products.remove(position);
			pAdapter.notifyDataSetChanged();

		}
		*/
		
		
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
