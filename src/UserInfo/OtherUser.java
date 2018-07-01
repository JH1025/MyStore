package UserInfo;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class OtherUser implements Serializable{
	byte[] profileImage;
	String id;
	String name;
	String phoneNumber;
	String address;
	
	public OtherUser(byte[] profileImage,String id,String name, String phoneNumber,String address){
		this.profileImage = profileImage;
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}
	
	public Bitmap getProfileImage(){
		Bitmap image = BitmapFactory.decodeByteArray(profileImage, 0, profileImage.length);
		return image;
	}
	
	public String getID(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	public String getPhoneNumber(){
		return phoneNumber;
	}
	public String getAddress(){
		return address;
	}

}
