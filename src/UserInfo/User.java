package UserInfo;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class User implements Serializable{
	//EditText mResult;
		private byte[] userImage;
		private String name;
		private String id;
		private String passwd;
		private String address;
		private String phoneNumber;

		public User(byte[] image, String id, String passwd,String name, String address,String phoneNumber) {
			this.userImage = image;
			this.id = id;
			this.passwd = passwd;
			this.name = name;
			this.address = address;
			this.phoneNumber = phoneNumber;
			
		}

		public Bitmap getUserImage(){
			Bitmap image = BitmapFactory.decodeByteArray(userImage, 0, userImage.length);
			return image;
		}
		
		public String getName(){
			return name;
		}
		public String getID(){
			return id;
		}
		
		public String getPasswd(){
			return passwd;
		}
		
		public String getAddress(){
			return address;
		}
		
		public String getPhoneNumber(){
			return phoneNumber;
		}
	
		public void setUserImage(byte[] image){
			this.userImage = image;
		}
		
		
		public void setName(String name){
			this.name = name;
		}
		
		public void setAddress(String address){
			this.address = address;
		}

}
