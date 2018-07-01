package Parse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class UserImageParse {
	
	Bitmap bmImg;
	String fileUrl = "http://183.106.250.84/getUserImage.php";
	String imageUrl = "http://183.106.250.84/getUserImage.php";
	public UserImageParse(){
		
	}
	
	public Bitmap getImage(){
		return bmImg;
	}
	
	public String sendData(String id) throws ClientProtocolException ,IOException{
		HttpPost request = makeHttpPost(id,imageUrl);
		HttpClient client = new DefaultHttpClient();
		ResponseHandler<String> reshandler = new BasicResponseHandler();
		String result = client.execute(request, reshandler);
		
		
		//StringBuilder jsonHtml = new StringBuilder();
		//jsonHtml.append(result);
		
		//return jsonHtml.toString();
		return result;
	}

	
	
	//imageparse
	public void downloadFile(String userID) {
		URL myFileUrl = null;

		try {
			myFileUrl = new URL(fileUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			//설정...
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");	//전송방식
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			conn.connect();
			//send id
			StringBuffer buffer = new StringBuffer();
			buffer.append("id").append("=").append(userID);
			OutputStreamWriter outStream = new OutputStreamWriter(conn.getOutputStream(), "EUC-KR");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();

            //get image
			int length = conn.getContentLength();
			InputStream is = conn.getInputStream();

			bmImg = BitmapFactory.decodeStream(is);
			// imView.setImageBitmap(bmImg);//말풍선에서 쓴다고 .....

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	//id 보내는 부분....
		
		private HttpPost makeHttpPost( String $id,String $url){
			HttpPost request = new HttpPost($url);
			Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
			nameValue.add(new BasicNameValuePair("id",$id));
			request.setEntity(makeEntity(nameValue));
			return request;
		}
		
		private HttpEntity makeEntity(Vector<NameValuePair> $nameValue){
			HttpEntity result = null;
			try{
				result = new UrlEncodedFormEntity($nameValue);
				
			}catch(UnsupportedEncodingException e){
				e.printStackTrace();
				
			}
			return result;
		}


}
