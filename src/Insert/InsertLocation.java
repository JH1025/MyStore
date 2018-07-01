package Insert;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

public class InsertLocation {
	  String url = "http://183.106.250.84/insertLocation.php";
	    
	    
	    
	    public InsertLocation(){
	    	
	    }
	    
	    
	    public String sendData(int latitudeE6,int longitudeE6,String id) throws ClientProtocolException ,IOException{
			HttpPost request = makeHttpPost(latitudeE6,longitudeE6,id,url);
			HttpClient client = new DefaultHttpClient();
			ResponseHandler<String> reshandler = new BasicResponseHandler();
			String result = client.execute(request, reshandler);
			return result;
		}
		
		private HttpPost makeHttpPost( int latitudeE6,int longitudeE6,String $id, String $url){
			HttpPost request = new HttpPost($url);
			Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
			//nameValue.add(new BasicNameValuePair("id",$image)); 이미지 처리..............................
			// bitmap 이미지를 바이트로 전환
			String $latitudeE6 = Integer.toString( latitudeE6 );
			String $longitudeE6 = Integer.toString(longitudeE6);
			
			nameValue.add(new BasicNameValuePair("latitudeE6",$latitudeE6));
			nameValue.add(new BasicNameValuePair("longitudeE6",$longitudeE6));
			nameValue.add(new BasicNameValuePair("id", $id));
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
