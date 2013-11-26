package org.client.buzzready;

import java.io.IOException;
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

public class HttpConnection {

	private static HttpConnection httpconnection = null;
	private static HttpClient httpclient = null;

	public static HttpConnection getInstance() {
		if (httpconnection == null) {
			httpconnection = new HttpConnection();
		}

		return httpconnection;
	}

	private HttpConnection() {
		httpclient = new DefaultHttpClient();
	}

	public Integer registerOrLogin(int Oper, String username, String password) {
		
		HttpPost httppost = null;
		if(Oper == Constants.REGISTER_OPER)
			httppost = new HttpPost("http://www.buzzready.com/register");
		else if (Oper == Constants.LOGIN_OPER)
			httppost = new HttpPost("http://www.buzzready.com/login");
		else
			return 0;

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			
			int status = response.getStatusLine().getStatusCode();
			
			return Integer.valueOf(status);

		} catch (ClientProtocolException e) {
			System.out.println("Protocol error " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Operation failed " + e.getMessage());
		}

		return 0;
	}
}
