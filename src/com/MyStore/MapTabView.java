package com.MyStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Map.MyItemizedOverlay;
import Map.MyOverlayItem;
import Parse.ProductImageParse;
import Parse.LocationParse;
import Parse.SearchingParse;
import UserInfo.Product;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapTabView extends MapActivity implements OnClickListener {
	String userID=null;
	
	MapView mapView;
	List<Overlay> mapOverlays;
	Drawable drawable;
	Drawable drawable2;
	MyItemizedOverlay itemizedOverlay;

	GeoPoint point;
	
	EditText searchTextBox;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.maptabview);
		
		userID = getIntent().getExtras().getString("userID");

		searchTextBox = (EditText) findViewById(R.id.searchEditText);

		
		mapView = (MapView) findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);

		Button searchBtn = (Button) findViewById(R.id.searchButton);
		searchBtn.setOnClickListener(this);

		mapOverlays = mapView.getOverlays(); // view 얻기

		/*
		 * 이런 이런이런 List<Address> geocodeResults=
		 * coder.getFromLocationName(placeName, 3); Iterator<Address> locations
		 * = geocodeResults.iterator();
		 * 
		 * String locInfo= "Results:\n"; double lat = 0f; double lon= 0f; while
		 * (locations.hasNext()) { Address add = locations.next(); locInfo+=
		 * String.format("Location: %f, %f", loc.getLatitude(),
		 * loc.getLongitude()); lat = add.getLatitude(); //위도 lon=
		 * add.getLongitude(); //경도 }
		 * 
		 * 
		 * GeoPoint newPoint= new GeoPoint((int)(lat * 1E6), (int)(lon* 1E6));//
		 * 해당위치로지도를매끄럽게이동 MapController mapControl = mapView.getController();
		 * mapControl.animateTo(newPoint);
		 */

		/*
		 * GeoPoint point = new
		 * GeoPoint((int)(51.5174723*1E6),(int)(-0.0899537*1E6)); OverlayItem
		 * overlayItem = new OverlayItem(point, "Tomorrow Never Dies (1997)",
		 * "(M gives Bond his mission in Daimler car)");
		 * itemizedOverlay.addOverlay(overlayItem);
		 */

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		
		if (searchTextBox.getText().toString().equals("")) {
			Toast.makeText(v.getContext(), "검색어를 입력해주세요", Toast.LENGTH_LONG)
					.show();
			return;
		}

		// 여기에 search 후 마커 찍고~ ㄷㄷㄷ 하기

		
		// map 초기화
		mapView.getOverlays().clear();

		ArrayList<Product> idList = new ArrayList();

		try {
			SearchingParse sp = new SearchingParse();
			String Json = sp.sendData(searchTextBox.getText().toString());

			JSONArray ja = new JSONArray(Json);

			for (int j = 0; j < ja.length(); j++) {
				JSONObject order = ja.getJSONObject(j);
				ProductImageParse ip = new ProductImageParse(); // test;;;;
				String id = order.getString("id");
				String title = order.getString("title");
				String period = order.getString("period");
				String price = order.getString("price");
				String explain = order.getString("comment");
				String owner = order.getString("owner");
				ip.downloadFile(id); // test,test,test
				Bitmap image = ip.getImage();

				idList.add(new Product(image, title, period, price,explain, owner));

			}

		} catch (JSONException e) {
			Toast.makeText(v.getContext(), "검색하신 상품이 없습니다.", Toast.LENGTH_LONG)
					.show();
			return;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(v.getContext(), "네트워크 연결이 원활하지 않습니다.",
					Toast.LENGTH_LONG).show();
		}

		for (int i = 0; i < idList.size(); i++) {

			try {
				LocationParse lp = new LocationParse();

				String Json = lp.sendData(idList.get(i).getOwner());
				Log.i("Json:", Json);

				JSONArray ja = new JSONArray(Json);

				// overlay 에 필요한 기본기...
				mapOverlays = mapView.getOverlays();
				drawable = getResources().getDrawable(R.drawable.smallhome);
				itemizedOverlay = new MyItemizedOverlay(drawable, mapView);

				for (int j = 0; j < ja.length(); j++) {
					JSONObject order = ja.getJSONObject(j);

					point = new GeoPoint(order.getInt("latitudeE6"),
							order.getInt("longitudeE6"));

					MyOverlayItem overlayItem = new MyOverlayItem(point, idList
							.get(i).getTitle(), idList.get(i).getExplain(),
							userID,idList.get(i).getOwner(), idList.get(i)
									.getProductImage());
					itemizedOverlay.addOverlay(overlayItem);

					mapOverlays.add(itemizedOverlay);

				}

			} catch (JSONException e) {
				Toast.makeText(v.getContext(), "", Toast.LENGTH_LONG).show();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(v.getContext(), "네트워크 연결이 원활하지 않습니다.",
						Toast.LENGTH_LONG).show();
				// e.printStackTrace();
			}

		}
		
		

		final MapController mc = mapView.getController();
		mc.animateTo(point);
		mc.setZoom(17);

		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.hideSoftInputFromWindow(searchTextBox.getWindowToken(), 0);
		
		// LocationListener의 핸들을 얻는다
		/*
		 * lm =(LocationManager)getSystemService(Context.LOCATION_SERVICE);
		 * 
		 * //GPS 위치 서비스에 연결한다 locListenD = new DispLocListener();
		 * lm.requestLocationUpdates("gps", 1000L, 10.0f, locListenD);
		 * 
		 * Location loc = lm.getLastKnownLocation( LocationManager.GPS_PROVIDER
		 * );
		 * 
		 * if(loc!=null) {
		 * 
		 * // first overlay drawable =
		 * getResources().getDrawable(R.drawable.smallhome); itemizedOverlay =
		 * new MyItemizedOverlay(drawable, mapView); //test!!!!!!!!!!!!!!
		 * GeoPoint point = new GeoPoint((int)(loc.getLatitude()*1E6),(int)(
		 * loc.getLongitude()*1E6)); //GeoPoint point = new GeoPoint(37501178,
		 * 127035111); OverlayItem overlayItem = new OverlayItem(point,
		 * "긴팔 피케티셔츠", " 산지는 1년정도 되구요... 사고나서 몇번 입지 않아서 깨끗해요.. ");
		 * itemizedOverlay.addOverlay(overlayItem);
		 * 
		 * // Location Manager에게 위치정보를 업데이트해달라고 요청한다. locListenD = new
		 * DispLocListener(); lm.requestLocationUpdates("gps", 30000L, 10.0f,
		 * locListenD);
		 * 
		 * 
		 * final MapController mc = mapView.getController();
		 * mc.animateTo(point); mc.setZoom(16); } else { Log.d("location",
		 * "location is null"); }
		 */

		/*
		 * 
		 * mapOverlays = mapView.getOverlays();
		 * 
		 * // first overlay drawable =
		 * getResources().getDrawable(R.drawable.smallhome); itemizedOverlay =
		 * new MyItemizedOverlay(drawable, mapView); //test!!!!!!!!!!!!!!
		 * //GeoPoint point = new
		 * GeoPoint((int)(51.5174723*1E6),(int)(-0.0899537*1E6)); GeoPoint point
		 * = new GeoPoint(37501178, 127035111); OverlayItem overlayItem = new
		 * OverlayItem(point, "긴팔 피케티셔츠",
		 * " 산지는 1년정도 되구요... 사고나서 몇번 입지 않아서 깨끗해요.. ");
		 * itemizedOverlay.addOverlay(overlayItem);
		 * 
		 * 
		 * GeoPoint point2 = new GeoPoint( 37500580, 127035100); //GeoPoint
		 * point2 = new GeoPoint((int)(51.515259*1E6),(int)(-0.086623*1E6));
		 * OverlayItem overlayItem2 = new OverlayItem(point2,
		 * "GoldenEye (1995)",
		 * "(Interiors Russian defence ministry council chambers in St Petersburg)"
		 * ); itemizedOverlay.addOverlay(overlayItem2);
		 * 
		 * mapOverlays.add(itemizedOverlay);
		 * 
		 * // second overlay drawable2 =
		 * getResources().getDrawable(R.drawable.smallhome); itemizedOverlay2 =
		 * new MyItemizedOverlay(drawable2, mapView);
		 * 
		 * 
		 * 
		 * //GeoPoint point3 = new
		 * GeoPoint((int)(51.513329*1E6),(int)(-0.08896*1E6)); GeoPoint point3 =
		 * new GeoPoint(37500680, 127035290); OverlayItem overlayItem3 = new
		 * OverlayItem(point3, "Sliding Doors (1998)", "(interiors)");
		 * itemizedOverlay2.addOverlay(overlayItem3);
		 * 
		 * //GeoPoint point4 = new
		 * GeoPoint((int)(51.51738*1E6),(int)(-0.08186*1E6)); GeoPoint point4 =
		 * new GeoPoint(37500780, 127035550); OverlayItem overlayItem4 = new
		 * OverlayItem(point4, "Mission: Impossible (1996)",
		 * "(Ethan & Jim cafe meeting)");
		 * itemizedOverlay2.addOverlay(overlayItem4);
		 * 
		 * mapOverlays.add(itemizedOverlay2);
		 * 
		 * final MapController mc = mapView.getController();
		 * mc.animateTo(point2); mc.setZoom(16);
		 */
	}

	

}