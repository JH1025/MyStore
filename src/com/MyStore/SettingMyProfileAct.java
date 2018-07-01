package com.MyStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import Modify.ModifyUserImage;
import Modify.ModifyUserInfo;
import UserInfo.User;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class SettingMyProfileAct extends Activity implements OnClickListener {
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;

	private ImageView userImageView;
	private Uri mImageCaptureUri;
	private Bitmap photo=null;
	
	
	User user;

	EditText nameBox;
	EditText addressBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_myprofile);

		Intent i = getIntent();
		user = (User) i.getExtras().getSerializable("user");
		
		
		userImageView=  (ImageView)findViewById(R.id.profile_image);
		userImageView.setScaleType(ScaleType.FIT_XY);
		userImageView.setImageBitmap(user.getUserImage());
		userImageView.setOnClickListener(this);

		TextView phoneNumberView = (TextView) findViewById(R.id.profile_phoneNumber_textView);
		TextView IdView = (TextView) findViewById(R.id.profile_ID_textView);

		phoneNumberView.append(user.getPhoneNumber());
		IdView.append(user.getID());

		nameBox = (EditText) findViewById(R.id.profile_name_textbox);
		addressBox = (EditText) findViewById(R.id.profile_address_textbox);

		nameBox.setText(user.getName());
		addressBox.setText(user.getAddress());

		Button modifyButton = (Button) findViewById(R.id.profile_modify_button);

		modifyButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ModifyUserInfo mf = new ModifyUserInfo();
				ModifyUserImage mi = new ModifyUserImage();
				try {
					String res = mf.sendData(user.getID(),
							nameBox.getText().toString(), addressBox.getText()
									.toString());
					
					userImageView.buildDrawingCache(); 
					Bitmap image = userImageView.getDrawingCache(); 
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					image.compress(CompressFormat.PNG, 100, stream);
					byte[] b = stream.toByteArray();
					
					mi.HttpFileUpload(b, "", user.getID());	//image update
					
					Intent i = getIntent();
					// bitmap 이미지를 바이트로 전환
					
					i.putExtra("modifiedImage", b);
					i.putExtra("modifiedName", nameBox.getText().toString());
					i.putExtra("modifiedAddress", addressBox.getText().toString());
					setResult(RESULT_OK, i);

					finish();

				} catch (ClientProtocolException e) {
					Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG)
							.show();
					e.printStackTrace();
				} catch (IOException e) {
					Toast.makeText(v.getContext(), "네트워크 연결이 원활하지 않습니다.",
							Toast.LENGTH_LONG).show();
					e.printStackTrace();
				} catch (Exception e) {
					Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG)
							.show();
				}

				
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
		Uri mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
		
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
					userImageView.setImageBitmap(photo);
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
