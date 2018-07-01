package UserInfo;

import android.graphics.Bitmap;

public class Product {
	private Bitmap productImage;
	private String title;
	private String period;
	private String price;
	private String explain;
	private String owner;
	
	public Product(Bitmap image,String title, String period,String price,String explain ,String owner){
		this.productImage = image;
		this.title = title;
		this.period = period;
		this.price = price;
		this.explain = explain;
		this.owner = owner;
	}
	
	public Bitmap getProductImage(){
		return productImage;
	}
	
	public String getTitle(){
		return title;
	}
	public String getPeriod(){
		return period;
	}
	public String getPrice(){
		return price;
	}
	
	public String getExplain(){
		return explain;
	}
	public String getOwner(){
		return owner;
	}
	
	
	public void setProductImage(Bitmap productImage){
		this.productImage = productImage;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	public void setPeriod(String period){
		this.period = period;
	}
	public void setPrice(String price){
		this.price = price;
	}
	
	public void setExplain(String explain){
		this.explain = explain;
	}
	
	

}
