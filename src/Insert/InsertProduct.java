package Insert;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

public class InsertProduct {
	   String url = "http://183.106.250.84/insertProduct.php";
	    
	    
	    
	    public InsertProduct(){
	    	
	    }
	    
	    
	    public String sendData(Bitmap image,String title,String period,String price,String explain,String owner) throws ClientProtocolException ,IOException{
			HttpPost request = makeHttpPost(image,title,period,price,explain,owner,url);
			HttpClient client = new DefaultHttpClient();
			ResponseHandler<String> reshandler = new BasicResponseHandler();
			String result = client.execute(request, reshandler);
			return result;
		}
		
		private HttpPost makeHttpPost( Bitmap $image,String $title,String $period,String $price,String $explain,String $owner, String $url){
			HttpPost request = new HttpPost($url);
			Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
			//nameValue.add(new BasicNameValuePair("id",$image)); 이미지 처리..............................
			// bitmap 이미지를 바이트로 전환
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			$image.compress(CompressFormat.PNG, 100, stream);
			byte[] b = stream.toByteArray();

			HttpFileUpload(b, ""  );	//imgae upload ... test
			
			
			nameValue.add(new BasicNameValuePair("title",$title));
			nameValue.add(new BasicNameValuePair("period",$period));
			nameValue.add(new BasicNameValuePair("price", $price));
			nameValue.add(new BasicNameValuePair("explain",$explain));
			nameValue.add(new BasicNameValuePair("owner", $owner));
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
		
		
		
		private void HttpFileUpload(byte[] buffer, String params  ) {
			String urlString = "http://183.106.250.84/insertProductImage.php";
			URL connectUrl = null;
			
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";	
			try {
				
				connectUrl = new URL(urlString);
				
				// open connection 
				HttpURLConnection conn = (HttpURLConnection)connectUrl.openConnection();			
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
				
				// write data
				DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + "productImage"+"\"" + lineEnd);
				dos.writeBytes(lineEnd);
				
				
				
				// read image????
				dos.write(buffer, 0, buffer.length);
			
				
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				
				// close streams
				dos.flush(); // finish upload...			
				
				// get response
				// get response
				int ch;
				InputStream is = conn.getInputStream();
				StringBuffer b =new StringBuffer();
				while( ( ch = is.read() ) != -1 ){
					b.append( (char)ch );
				}
				String s=b.toString(); 
				dos.close();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}		
		}


	

}
