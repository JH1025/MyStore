package com.MyStore;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashAct extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		initialize();
	}

	private void initialize() {
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				
				finish(); // ��Ƽ��Ƽ ����
			}
		};

		handler.sendEmptyMessageDelayed(0, 5000); // ms, 3���� �����Ŵ
	}

}
