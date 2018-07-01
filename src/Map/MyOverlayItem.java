package Map;

import android.graphics.Bitmap;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class MyOverlayItem extends OverlayItem{
	private String userID;
	private String ownerID;
	private Bitmap productImage;

	public MyOverlayItem(GeoPoint point, String title, String snippet ,String userID,String ownerID ,Bitmap image) {
		super(point, title, snippet);
		this.userID = userID;
		this.ownerID = ownerID;
		this.productImage = image;
		// TODO Auto-generated constructor stub
	}
	
	public String getUserID(){
		return userID;
	}
	
	public String getOwnerID(){
		return ownerID;
	}
	
	public Bitmap getProductImage(){
		return productImage;
	}

}
