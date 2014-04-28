package cam.flytesolutions.groupview;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class GetDataFromDB {

	public String getDataFromDB(String email, String date) {
		try {

			HttpPost httppost;
			HttpClient httpclient;
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost("http://flytesolutions.com/flyte_medical_iv/GetUsers.php"); 
			String response = "";
			// Add your data   
			List < NameValuePair > nameValuePairs = new ArrayList < NameValuePair > (5);
			nameValuePairs.add(new BasicNameValuePair("email", email));
			nameValuePairs.add(new BasicNameValuePair("date", date));

			try {
			     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			     Log.d("myapp", "works till here. 2");
			     try {
			         HttpResponse response1 = httpclient.execute(httppost);
			         response = EntityUtils.toString(response1.getEntity());
			         Log.d("myapp", "response " + response1.getEntity());
			     } catch (ClientProtocolException e) {
			         e.printStackTrace();
			     } catch (IOException e) {
			         e.printStackTrace();
			     }
			 } catch (UnsupportedEncodingException e) {
			     e.printStackTrace();
			 } 
			
			
			//ResponseHandler<String> responseHandler = new BasicResponseHandler();
			//final String response = httpclient.execute(httppost,responseHandler);
			return response.trim();

		} catch (Exception e) {
			System.out.println("ERROR : " + e.getMessage());
			return "error";
		}
	}
}
