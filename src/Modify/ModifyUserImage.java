package Modify;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ModifyUserImage {
	
	public ModifyUserImage(){
		
	}
	
	public void HttpFileUpload(byte[] buffer, String params, String userID) {
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
