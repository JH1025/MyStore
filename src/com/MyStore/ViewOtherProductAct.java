package com.MyStore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class ViewOtherProductAct extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_other_product);

		Intent data = getIntent();

		// 바이트로 를 bitmap 형식으로 변환...
		byte[] b = (byte[]) data.getExtras().get("image"); // 변환시킬 바이트 배열
		Bitmap image = BitmapFactory.decodeByteArray(b, 0, b.length);

		String title = data.getExtras().getString("title");
		String period = data.getExtras().getString("period");
		String price = data.getExtras().getString("price");
		String explain = data.getExtras().getString("explain");

		ImageView productImage = (ImageView) findViewById(R.id.other_product_image);
		TextView titleBox = (TextView) findViewById(R.id.other_product_title);
		TextView periodBox = (TextView) findViewById(R.id.other_product_period);
		TextView priceBox = (TextView) findViewById(R.id.other_product_price);
		TextView explainBox = (TextView) findViewById(R.id.other_product_explain);

		productImage.setScaleType(ScaleType.FIT_XY);
		productImage.setImageBitmap(image);
		titleBox.append(title);
		periodBox.append(period);
		priceBox.append(price);
		explainBox.append(explain);
	}

}
