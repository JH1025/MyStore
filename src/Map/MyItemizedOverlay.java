package Map;


import java.util.ArrayList;

import UserInfo.OtherUser;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import com.MyStore.OtherStoreAct;
import com.google.android.maps.MapView;


public class MyItemizedOverlay extends BalloonItemizedOverlay<MyOverlayItem> {

	private ArrayList<MyOverlayItem> m_overlays = new ArrayList<MyOverlayItem>();
	private Context c;
	
	public MyItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenterBottom(defaultMarker), mapView);
		c = mapView.getContext();
	}

	public void addOverlay(MyOverlayItem overlay) {
	    m_overlays.add(overlay);
	    populate();
	}
	
	public void clear(){
		m_overlays.clear();
		populate();
	}
	

	@Override
	protected MyOverlayItem createItem(int i) {
		return m_overlays.get(i);
	}

	@Override
	public int size() {
		return m_overlays.size();
	}

	@Override
	protected boolean onBalloonTap(int index) {
		// TODO Auto-generated method stub
		Bundle extras = new Bundle();

		//user 정보 parse.......
		
		//오버레이 정보 제대로 넘겨주기!!!
		extras.putString("userID", m_overlays.get(index).getUserID());
		extras.putString("otherUserID", m_overlays.get(index).getOwnerID());
		// 인텐트를 생성한다.
		// 컨텍스트로 현재 액티비티를, 생성할 액티비티로 ItemClickExampleNextActivity 를 지정한다.
		Intent intent = new Intent(c, OtherStoreAct.class);
		// 위에서 만든 Bundle을 인텐트에 넣는다.
		intent.putExtras(extras);
	
		// 액티비티를 생성한다. 부모의 context 에 실행...
		c.startActivity(intent);
		
		return true;
	}
	
}
