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
				
				
				
				
				//오류처리...!!!!!			
				if(photo ==null){
					Toast.makeText(v.getContext(), "이미지가 없습니다.", Toast.LENGTH_LONG).show();
					return;
				}
				
				
				
				String title = ((EditText)findViewById(R.id.add_product_title)).getText().toString();
				String period = ((EditText)findViewById(R.id.add_product_period)).getText().toString();
				String price = ((EditText)findViewById(R.id.add_product_price)).getText().toString();
				String explain = ((EditText)findViewById(R.id.add_product_explain)).getText().toString();
			
				
				//서버에 추가.... 나중에 insert 메소드 안에 넣기~
				try {
					
					if(title =="" || period =="" ||explain == "" ||price =="" ){
						throw new Exception("빈칸없이 모두 입력해주세요.");
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

				// bitmap 이미지를 바이트로 전환
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(CompressFormat.PNG, 100, stream);
				byte[] b = stream.toByteArray();

			
				//인텐트를이용해서 메인 Activity 결과 전달...
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
	 * 카메라에서 이미지 가져오기
	 */
	private void doTakePhotoAction()
	{
		/*
		 * 참고 해볼곳
		 * http://2009.hfoss.org/Tutorial:Camera_and_Gallery_Demo
		 * http://stackoverflow.com/questions/1050297/how-to-get-the-url-of-the-captured-image
		 * http://www.damonkohler.com/2009/02/android-recipes.html
		 * http://www.firstclown.us/tag/android/
		 */

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		// 임시로 사용할 파일의 경로를 생성
		String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
		mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
		
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PICK_FROM_CAMERA);
	}
	
	/**
	 * 앨범에서 이미지 가져오기
	 */
	private void doTakeAlbumAction()
	{
		// 앨범 호출
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
				// 크롭이 된 이후의 이미지를 넘겨 받습니다. 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
				// 임시 파일을 삭제합니다.
				final Bundle extras = data.getExtras();
	
				if(extras != null)
				{
					photo = extras.getParcelable("data");
					productImage.setImageBitmap(photo);
				}
	
				// 임시 파일 삭제
				File f = new File(mImageCaptureUri.getPath());
				if(f.exists())
				{
					f.delete();
				}
	
				break;
			}
	
			case PICK_FROM_ALBUM:
			{
				// 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
				// 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.
				
				mImageCaptureUri = data.getData();
			}
			
			case PICK_FROM_CAMERA:
			{
				// 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
				// 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.
	
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
			.setTitle("업로드할 이미지 선택")
			.setPositiveButton("사진촬영", cameraListener)
			.setNeutralButton("앨범선택", albumListener)
			.setNegativeButton("취소", cancelListener)
			.show();
	}
	
	
	
	
	

}
