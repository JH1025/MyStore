package Parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import UserInfo.User;


public class ProductsInfoParse {

	String url ="http://183.106.250.84/getProducts.php";
	
	public ProductsInfoParse(){
		
	}

	public void findUserAndParseInfo(User user,String ID) {
		// TODO Auto-generated method stub
		
		try{
			String Json=sendData(ID);
			if(Json == null){
				user = null;
				return;	
			}
		
			JSONArray ja = new JSONArray(Json);

			String name;
			String id;
			String passwd;
			String address;
			String phoneNumber;
			
			
			JSONObject order = ja.getJSONObject(0);
			id=order.getString("id");
			passwd=order.getString("password");
			name=order.getString("name");
			address=order.getString("address");
			phoneNumber= order.getString("phoneNumber");
			
			//user = new User(id,passwd,name,address,phoneNumber);
			
			/*
			for (int j = 0; j < ja.length(); j++) {
				JSONObject order = ja.getJSONObject(j);
				name=order.getString("Name");
				id=order.getString("ID");
				passwd=order.getString("Passwd");
				address=order.getString("Address");
				
				user = new User(name,id,passwd,address);
				
				//Result += order.getString("Name") + " " + order.getString("ID")
				//		+ " " + order.getString("Passwd") + " "
				//		+ order.getString("Address") + "\n\n";

			}

			*/
			
		} catch (JSONException e) {
			e.printStackTrace();
			//Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}catch(ClientProtocolException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		
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

	public String DownloadHtml(String addr) {
		StringBuilder jsonHtml = new StringBuilder();
		try {
			URL url = new URL(addr);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			if (conn != null) {
				conn.setConnectTimeout(10000);
				conn.setUseCaches(false);

				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(conn.getInputStream(),
									"EUC-KR"));
					for (;;) {
						String line = br.readLine();
						if (line == null)
							break;

						jsonHtml.append(line + "\n");
					}

					br.close();
				}
				conn.disconnect();

			}

		} catch (Exception ex) {
			//오류처리...
			//Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}

		return jsonHtml.toString();
	}
	
	
	
}