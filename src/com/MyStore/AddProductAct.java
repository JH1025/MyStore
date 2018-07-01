package com.MyStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;


import Insert.InsertProduct;
import UserInfo.Product;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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

public class AddProductAct extends Activity implements OnClickListener{
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;

	private Uri mImageCaptureUri;
	
	private String userID=null;
	
	private ImageView productImage;
	private Bitmap photo=null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_product);
		
		

		
		productImage = (ImageView) findViewById(R.id.add_product_image);
		productImage.setScaleType(ScaleType.FIT_XY);
		productImage.setOnClickListener(this);
		Intent i = getIntent();
		userID = i.getStringExtra("userID");
		
		Button addProductBtn = (Button)findViewById(R.id.add_product_button);
		addProductBtn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				
				
				//����ó��...!!!!!			
				if(photo ==null){
					Toast.makeText(v.getContext(), "�̹����� �����ϴ�.", Toast.LENGTH_LONG).show();
					return;
				}
				
				
				
				String title = ((EditText)findViewById(R.id.add_product_title)).getText().toString();
				String period = ((EditText)findViewById(R.id.add_product_period)).getText().toString();
				String price = ((EditText)findViewById(R.id.add_product_price)).getText().toString();
				String explain = ((EditText)findViewById(R.id.add_product_explain)).getText().toString();
			
				
				//������ �߰�.... ���߿� insert �޼ҵ� �ȿ� �ֱ�~
				try {
					
					if(title =="" || period =="" ||explain == "" ||price =="" ){
						throw new Exception("��ĭ���� ��� �Է����ּ���.");
					}
					
					
					InsertProduct insertProduct = new InsertProduct();
					String res=insertProduct.sendData(photo,title,period,price,explain,userID);
					
				
					
					
				} catch (ClientProtocolException e) {
					Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
					//e.printStackTrace();
				} catch (IOException e) {
					Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
					//e.printStackTrace();
				} catch (Exception e) {
					Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				}
				
				
				Intent i = getIntent(); 

				// bitmap �̹����� ����Ʈ�� ��ȯ
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(CompressFormat.PNG, 100, stream);
				byte[] b = stream.toByteArray();

			
				//����Ʈ���̿��ؼ� ���� Activity ��� ����...
				i.putExtra("image", b);
				i.putExtra("title", title);
				i.putExtra("period", period);
				i.putExtra("price", price);
				i.putExtra("explain", explain);
				setResult(RESULT_OK, i);
				finish();
		
			}
		});
		
		
		

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

	@Override
	public void onClick(View v)
	{
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
		
		new AlertDialog.Builder(this)
			.setTitle("���ε��� �̹��� ����")
			.setPositiveButton("�����Կ�", cameraListener)
			.setNeutralButton("�ٹ�����", albumListener)
			.setNegativeButton("���", cancelListener)
			.show();
	}
	
	
	
	
	

}
