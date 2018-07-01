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

		//user ���� parse.......
		
		//�������� ���� ����� �Ѱ��ֱ�!!!
		extras.putString("userID", m_overlays.get(index).getUserID());
		extras.putString("otherUserID", m_overlays.get(index).getOwnerID());
		// ����Ʈ�� �����Ѵ�.
		// ���ؽ�Ʈ�� ���� ��Ƽ��Ƽ��, ������ ��Ƽ��Ƽ�� ItemClickExampleNextActivity �� �����Ѵ�.
		Intent intent = new Intent(c, OtherStoreAct.class);
		// ������ ���� Bundle�� ����Ʈ�� �ִ´�.
		intent.putExtras(extras);
	
		// ��Ƽ��Ƽ�� �����Ѵ�. �θ��� context �� ����...
		c.startActivity(intent);
		
		return true;
	}
	
}
