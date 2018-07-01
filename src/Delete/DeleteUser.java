package Delete;

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

public class DeleteUser {
	String url ="http://183.106.250.84/deleteUser.php";
	
	public DeleteUser(){
		
	}

    
	public String sendData(String id) throws ClientProtocolException ,IOException{
		HttpPost request = makeHttpPost(id,url);
		HttpClient client = new DefaultHttpClient();
		ResponseHandler<String> reshandler = new BasicResponseHandler();
		String result = client.execute(request, reshandler);
		
		//StringBuilder jsonHtml = new StringBuilder();
		//jsonHtml.append(result);
		
		//return jsonHtml.toString();
		return result;
	}
	
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
