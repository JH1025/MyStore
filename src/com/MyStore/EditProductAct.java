package com.MyStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import Delete.DeleteProduct;
import Modify.ModifyProduct;
import UserInfo.Product;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class EditProductAct extends Activity implements OnClickListener{
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;

	int position; //item ��ȣ
	String userID;
	
	Bitmap image;
	String title ;
	String period ;
	String price;
	String explain ;
	
	ImageView productImage; 
	EditText titleBox ;
	EditText periodBox ;
	EditText priceBox ;
	EditText explainBox;
	
	private Bitmap photo=null;
	private Uri mImageCaptureUri;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_product);
		
		Intent data =  getIntent();
		
		position = data.getExtras().getInt("position");
		userID = data.getExtras().getString("userID");
		
		// ����Ʈ�� �� bitmap �������� ��ȯ...
		byte[] b = (byte[]) data.getExtras().get("image"); // ��ȯ��ų ����Ʈ �迭
		Bitmap image = BitmapFactory.decodeByteArray(b, 0, b.length);

		title = data.getExtras().getString("title");
		period = data.getExtras().getString("period");
		price = data.getExtras().getString("price");
		explain = data.getExtras().getString("explain");
		
		
		
		productImage= (ImageView)findViewById(R.id.edit_product_image); 
		titleBox = (EditText)findViewById(R.id.edit_product_title);
		periodBox = (EditText)findViewById(R.id.edit_product_period);
		priceBox = (EditText)findViewById(R.id.edit_product_price);
		explainBox = (EditText)findViewById(R.id.edit_product_explain);
		
		productImage.setScaleType(ScaleType.FIT_XY);
		productImage.setImageBitmap(image);
		titleBox.setText(title);
		periodBox.setText(period);
		priceBox.setText(price);
		explainBox.setText(explain);
		
		productImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						doTakePhotoAction();
					}
				};
				
				DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						doTakeAlbumAction();
					}
				};
				
				DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				};
				
				new AlertDialog.Builder(EditProductAct.this)
					.setTitle("���ε��� �̹��� ����")
					.setPositiveButton("�����Կ�", cameraListener)
					.setNeutralButton("�ٹ�����", albumListener)
					.setNegativeButton("���", cancelListener)
					.show();
				
			}
		});
		
		
		
		
		
		Button modifyButton = (Button)findViewById(R.id.edit_product_button);
		Button deleteButton = (Button)findViewById(R.id.delete_product_button);
		modifyButton.setOnClickListener(this);
		deleteButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		title = titleBox.getText().toString();
		Bitmap image = photo;
		period = periodBox.getText().toString();
		price = priceBox.getText().toString();
		explain = explainBox.getText().toString();
		
		Intent i = getIntent(); 

		// bitmap �̹����� ����Ʈ�� ��ȯ
		/* �ϴ� ��������
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		photo.compress(CompressFormat.PNG, 100, stream);
		byte[] b = stream.toByteArray();
		 */
		
		/*
		switch(v.getId()){
		case R.id.edit_product_button:
			
			ModifyProduct mp = new ModifyProduct();
			//�̹����� �ϴ� �����س���...
			try {
				mp.sendData(title, period, price, explain,userID);

				//����Ʈ���̿��ؼ� ���� Activity ��� ����...
				i.putExtra("position", position);
				//i.putExtra("image", b);
				i.putExtra("title", title);
				i.putExtra("period", period);
				i.putExtra("price", price);
				i.putExtra("explain", explain);
				setResult(RESULT_CANCELED, i);
				finish();
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getBaseContext(), "�������� ����", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getBaseContext(), "������ ��Ȱ���� �ʽ��ϴ�.", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		
			
			break;
			
		case R.id.delete_product_button:
			
			DeleteProduct dp = new DeleteProduct();
			try {
				dp.sendData(title);
				//����Ʈ���̿��ؼ� ���� Activity ��� ����...
				i.putExtra("position", position);
				//i.putExtra("image", b);
				i.putExtra("title", title);
				i.putExtra("period", period);
				i.putExtra("price", price);
				i.putExtra("explain", explain);
				setResult(RESULT_FIRST_USER, i);
				finish();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getBaseContext(), "�������� ����", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getBaseContext(), "������ ��Ȱ���� �ʽ��ϴ�.", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		
		
			
			
			
			break;
		
		}
		*/
		
	
		
		
	}
	
	/**
	 * ī�޶󿡼� �̹��� ��������
	 */
	private void doTakePhotoAction()
	{
		/*
		 * ���� �غ���
		 * http://2009.hfoss.org/Tutorial:Camera_and_Gallery_Demo
		 * http://stackoverflow.com/questions/1050297/how-to-get-the-url-of-the-captured-image
		 * http://www.damonkohler.com/2009/02/android-recipes.html
		 * http://www.firstclown.us/tag/android/
		 */

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		// �ӽ÷� ����� ������ ��θ� ����
		String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
		mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
		
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PICK_FROM_CAMERA);
	}
	
	/**
	 * �ٹ����� �̹��� ��������
	 */
	private void doTakeAlbumAction()
	{
		// �ٹ� ȣ��
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		startActivityForResult(intent, PICK_FROM_ALBUM);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode != RESULT_OK)
		{
			return;
		}

		switch(requestCode)
		{
			case CROP_FROM_CAMERA:
			{
				// ũ���� �� ������ �̹����� �Ѱ� �޽��ϴ�. �̹����信 �̹����� �����شٰų� �ΰ����� �۾� ���Ŀ�
				// �ӽ� ������ �����մϴ�.
				final Bundle extras = data.getExtras();
	
				if(extras != null)
				{
					photo = extras.getParcelable("data");
					productImage.setImageBitmap(photo);
				}
	
				// �ӽ� ���� ����
				File f = new File(mImageCaptureUri.getPath());
				if(f.exists())
				{
					f.delete();
				}
	
				break;
			}
	
			case PICK_FROM_ALBUM:
			{
				// ������ ó���� ī�޶�� �����Ƿ� �ϴ�  break���� �����մϴ�.
				// ���� �ڵ忡���� ���� �ո����� ����� �����Ͻñ� �ٶ��ϴ�.
				
				mImageCaptureUri = data.getData();
			}
			
			case PICK_FROM_CAMERA:
			{
				// �̹����� ������ ������ ���������� �̹��� ũ�⸦ �����մϴ�.
				// ���Ŀ� �̹��� ũ�� ���ø����̼��� ȣ���ϰ� �˴ϴ�.
	
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(mImageCaptureUri, "image/*");
	
				intent.putExtra("outputX", 90);
				intent.putExtra("outputY", 90);
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("scale", true);
				intent.putExtra("return-data", true);
				startActivityForResult(intent, CROP_FROM_CAMERA);
	
				break;
			}
		}
	}

	
	
	
	


}
