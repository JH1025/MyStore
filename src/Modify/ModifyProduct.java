package Modify;

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

public class ModifyProduct {
	 String url = "http://183.106.250.84/modifyProduct.php";
	    
	    
	    
	    public ModifyProduct(){
	    	
	    }
	    
	    public String sendData(String title,String period,String price,String explain,String owner) throws ClientProtocolException ,IOException{
			HttpPost request = makeHttpPost(title,period,price,explain,owner,url);
			HttpClient client = new DefaultHttpClient();
			ResponseHandler<String> reshandler = new BasicResponseHandler();
			String result = client.execute(request, reshandler);
			return result;
		}
		
		private HttpPost makeHttpPost(String $title,String $period,String $price,String $explain,String $owner, String $url){
			HttpPost request = new HttpPost($url);
			Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
			nameValue.add(new BasicNameValuePair("title",$title));
			nameValue.add(new BasicNameValuePair("period",$period));
			nameValue.add(new BasicNameValuePair("price",$price));
			nameValue.add(new BasicNameValuePair("explain",$explain));
			nameValue.add(new BasicNameValuePair("owner",$owner));
			request.setEntity(makeEntity(nameValue));
			return request;
		}
		
		private HttpEntity makeEntity(Vector<NameValuePair> $nameValue){
			HttpEntity result = null;
			try{
				result = new UrlEncodedFormEntity($nameValue,"euc-kr");
				
			}catch(UnsupportedEncodingException e){
				e.printStackTrace();
				
			}
			return result;
		}

}
