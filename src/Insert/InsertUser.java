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

public class InsertUser {
	String url = "http://183.106.250.84/insertUser.php";

	public InsertUser() {

	}

	public String sendData(Bitmap image, String id, String passwd, String name,
			String address, String phoneNumber) throws ClientProtocolException,
			IOException {
		HttpPost request = makeHttpPost(image, id, passwd, name, address,
				phoneNumber, url);
		HttpClient client = new DefaultHttpClient();
		ResponseHandler<String> reshandler = new BasicResponseHandler();
		String result = client.execute(request, reshandler);
		return result;
	}

	private HttpPost makeHttpPost(Bitmap $image, String $id, String $passwd,
			String $name, String $address, String $phoneNumber, String $url) {
		HttpPost request = new HttpPost($url);
		Vector<NameValuePair> nameValue = new Vector<NameValuePair>();

		// bitmap 이미지를 바이트로 전환
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		$image.compress(CompressFormat.PNG, 100, stream);
		byte[] b = stream.toByteArray();

		HttpFileUpload(b, "", $id); // imgae upload ... test 파일이름 하려고 id 보냄

		nameValue.add(new BasicNameValuePair("id", $id));
		nameValue.add(new BasicNameValuePair("passwd", $passwd));
		nameValue.add(new BasicNameValuePair("name", $name));
		nameValue.add(new BasicNameValuePair("address", $address));
		nameValue.add(new BasicNameValuePair("phoneNumber", $phoneNumber));
		request.setEntity(makeEntity(nameValue));
		return request;
	}

	private HttpEntity makeEntity(Vector<NameValuePair> $nameValue) {
		HttpEntity result = null;
		try {
			result = new UrlEncodedFormEntity($nameValue, "euc-kr");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

		}
		return result;
	}

	private void HttpFileUpload(byte[] buffer, String params, String userID) {
		String urlString = "http://183.106.250.84/insertUserImage.php";
		URL connectUrl = null;

		String fileName = userID + ".png";

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {

			connectUrl = new URL(urlString);

			// open connection
			HttpURLConnection conn = (HttpURLConnection) connectUrl
					.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			// write data
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
					+ fileName + "\"" + lineEnd);
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
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			String s = b.toString();
			dos.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

}
