package com.MyStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import Insert.InsertLocation;
import Map.MyItemizedOverlay;
import Map.MyOverlayItem;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//임시로... 나중에 버튼하나해서 따로 클래스 만들기!!! 
//돌아버리겠네~~~~~~~~~~~~~~~
public class SettingMyStoreAct extends MapActivity implements OnClickListener {
	private String userID;

	private List<Overlay> listOfOverlays;
	private Drawable drawable;
	private HellItemizedOverlay itemizedOverlay;
	private MapController mc;
	private MapView mapView;
	private GeoPoint p = null; // 나중에 GPS???

	private TextView latitudeBox;
	private TextView longitudeBox;

	int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_mystore);

		Intent i = getIntent();
		userID = i.getExtras().getString("userID");

		mapView = (MapView) findViewById(R.id.set_location_map);
		mapView.setBuiltInZoomControls(true);

		mc = mapView.getController();

		p = new GeoPoint(37517180, 127041268); // 초기값 GPS이용??
		mc.animateTo(p);
		mc.setZoom(17);

		// 위도 경도값 세팅하는곳
		latitudeBox = (TextView) findViewById(R.id.latitudeTextView);
		longitudeBox = (TextView) findViewById(R.id.longitudeTextView);

		latitudeBox.setText(p.getLatitudeE6() / 1E6 + "");
		longitudeBox.setText(p.getLongitudeE6() / 1E6 + "");

		listOfOverlays = mapView.getOverlays();

		// ---Add a location marker---
		drawable = this.getResources().getDrawable(R.drawable.pin); // 마커
		itemizedOverlay = new HellItemizedOverlay(drawable, this);

		OverlayItem overlayitem1 = new OverlayItem(p, "", "");

		itemizedOverlay.addOverlay(overlayitem1);

		mapView.getOverlays().add(itemizedOverlay);

		listOfOverlays.add(itemizedOverlay);

		Button sendButton = (Button) findViewById(R.id.sendLocationButton);
		Button getNowLocationButton = (Button) findViewById(R.id.getNowLocationButton);
		sendButton.setOnClickListener(this);
		getNowLocationButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sendLocationButton:
			InsertLocation il = new InsertLocation();

			try {

				il.sendData(p.getLatitudeE6(), p.getLongitudeE6(), userID);
				Toast.makeText(v.getContext(), "위치가 전송되었습니다.",
						Toast.LENGTH_LONG).show();
				// String res =
				// il.sendData(p.getLatitudeE6(),p.getLongitudeE6(),userID);
				// EditText test = (EditText)findViewById(R.id.tempEditText);
				// test.setText(res);

			} catch (ClientProtocolException e) {
				Toast.makeText(v.getContext(), e.getMessage(),
						Toast.LENGTH_LONG).show();
				// e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(v.getContext(), e.getMessage(),
						Toast.LENGTH_LONG).show();
				// e.printStackTrace();
			} catch (Exception e) {
				Toast.makeText(v.getContext(), e.getMessage(),
						Toast.LENGTH_LONG).show();
			}

			break;
		case R.id.getNowLocationButton:

			if (!chkGpsService()) {
				return;
			}
			
			Toast.makeText(v.getContext(), "GPS를 통해 위치를 받습니다. \n 사용환경에따라 위치를 찾으수 없을수도 있습니다.",Toast.LENGTH_LONG).show();
			
			onGPS();
			
			break;
		}

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * class MapOverlay extends com.google.android.maps.Overlay {
	 * 
	 * @Override public boolean onTouchEvent(MotionEvent event, MapView mapView)
	 * { //---when user lifts his finger--- if (event.getAction() == 1) {
	 * GeoPoint p = mapView.getProjection().fromPixels( (int) event.getX(),
	 * (int) event.getY()); Toast.makeText(getBaseContext(), p.getLatitudeE6() /
	 * 1E6 + "," + p.getLongitudeE6() /1E6 , Toast.LENGTH_SHORT).show(); }
	 * return false; } }
	 */

	public class HellItemizedOverlay extends ItemizedOverlay {

		private Context mContext;
		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

		public HellItemizedOverlay(Drawable defaultMarker, Context context) {
			super(boundCenterBottom(defaultMarker));
			// TODO Auto-generated constructor stub
			mContext = context;
		}

		@Override
		protected OverlayItem createItem(int i) {
			// TODO Auto-generated method stub
			return mOverlays.get(i);
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return mOverlays.size();
		}

		public void addOverlay(OverlayItem overlay) {
			mOverlays.add(overlay);
			populate();
		}

		public void removeOverlay(int index) {
			mOverlays.remove(index);
			populate();
		}

		@Override
		protected boolean onTap(int index) {
			OverlayItem item = mOverlays.get(index);

			return true;
		}

		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapview) {

			// ---when user lifts his finger---
			if (event.getAction() == MotionEvent.ACTION_DOWN) {

				p = mapView.getProjection().fromPixels((int) event.getX(),
						(int) event.getY());

				latitudeBox.setText(p.getLatitudeE6() / 1E6 + "");
				longitudeBox.setText(p.getLongitudeE6() / 1E6 + "");

				listOfOverlays.clear();
				itemizedOverlay.removeOverlay(0);
				mapView.invalidate();

				if (listOfOverlays.size() == 0) {

					OverlayItem overlayitem1 = new OverlayItem(p, "", "");

					itemizedOverlay.addOverlay(overlayitem1);

					listOfOverlays.add(itemizedOverlay);
				}

			}

			return false;
		}

	}

	private boolean chkGpsService() {

		String gs = android.provider.Settings.Secure.getString(
				getContentResolver(),

				android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		if (gs.indexOf("gps", 0) < 0) {

			// GPS OFF 일때 Dialog 띄워서 설정 화면으로 튀어봅니다..

			AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);

			gsDialog.setTitle("GPS 가 꺼져있습니다. !!!");

			gsDialog.setMessage("GPS기능이 켜져있지않으면 검색기능을 쓸수 없습니다.\nGPS기능을 설정해주세요 !!");

			gsDialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							// GPS설정 화면으로 튀어요

							Intent intent = new Intent(
									android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);

							intent.addCategory(Intent.CATEGORY_DEFAULT);

							startActivity(intent);
							

						}

					}).create().show();

			return false;

		} else {

			return true;

		}

	}
	
	private void onGPS(){
		// GPS!!!!!!!!!!
		LocationManager lm;
		LocationListener locListenD;

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locListenD = new DispLocListener();
		lm.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 30000L,
				10.0f, locListenD);

		// LocationListener의 핸들을 얻는다

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// GPS 위치 서비스에 연결한다 
		locListenD = new DispLocListener();
		lm.requestLocationUpdates("gps", 1000L, 10.0f,
				locListenD);

		Location loc = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (loc != null) {

			// Location Manager에게 위치정보를 업데이트해달라고 요청한다.
			locListenD = new DispLocListener();
			lm.requestLocationUpdates("gps", 30000L, 10.0f,
					locListenD);

		} else {
			Log.d("location", "location is null");
		}

		
	}
	

	private class DispLocListener implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {

			/*
			EditText search = (EditText) findViewById(R.id.searchEditText);

			search.setText(location.getLongitude() + "   "
					+ location.getLatitude());

			*/
			p = new GeoPoint((int) (location.getLatitude() * 1E6),
					(int) (location.getLongitude() * 1E6));
			
			

			latitudeBox.setText(p.getLatitudeE6() / 1E6 + "");
			longitudeBox.setText(p.getLongitudeE6() / 1E6 + "");

			listOfOverlays.clear();
			itemizedOverlay.removeOverlay(0);
			mapView.invalidate();

			if (listOfOverlays.size() == 0) {

				OverlayItem overlayitem1 = new OverlayItem(p, "", "");

				itemizedOverlay.addOverlay(overlayitem1);

				listOfOverlays.add(itemizedOverlay);
			}

			
			
			final MapController mc = mapView.getController();
			mc.animateTo(p);
			mc.setZoom(17);

		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

}
