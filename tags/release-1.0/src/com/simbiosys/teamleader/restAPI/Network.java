// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.restAPI;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;

import com.simbiosys.teamleader.view.MainActivity;


import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;


public class Network extends AsyncTask<Void, Void, Void> {
	
	MainActivity ma;
	AndroidHttpClient httpClient;
	
	public static final String TEAM_LEADER_GROUP = "3897";
	public static final String TEAM_LEADER_API_KEY = "XJmis8RBahZTE1cBSEqFpnPEOcUHg564EjdXiVpi2e1x53YOyfkNsZi713F8YB3tK7gSSv0LAS8wMXba2lOlbXi2QN1FfUYQR5yArpbR8aeLXfLQqq1rdakUOcpTXdzF9Y5qe78d7cOVhqCyGuQKu1u93KSQOimN7h3cf1fc3TYb9qzGLfg56B50bOHPXTsU0lX6d297"; 


	public Network(MainActivity ma) {
		this.ma = ma;
	}
	
	@Override
	protected Void doInBackground(Void... arg0) {
	       httpClient = AndroidHttpClient.newInstance("TeamLeaderAndroidHttpClient");
	        String baseURL = "https://www.teamleader.be/api/getDepartments.php";
	       String url = baseURL + 
	    		   "?" + "api_group" + "=" + TEAM_LEADER_GROUP +
	    		   "&api_secret=" +TEAM_LEADER_API_KEY;
	       
//	       String url2 = "https://www.teamleader.be/api/getContact.php" +
//	    		   "?" + "api_group" + "=" + TEAM_LEADER_GROUP +
//	    		   "&api_secret=" +TEAM_LEADER_API_KEY +
//	    		   "&date_from=" + "01/09/2014" +
//	    		   "&date_to="+"24/09/2014"
//	    		   ;
	       
	        HttpPost httpPost = new HttpPost();
	        try {
				httpPost.setURI(new URI(url));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
//	        httpPost.addHeader("api_group",TEAM_LEADER_GROUP);
//	        httpPost.addHeader("contact_id",TEAM_LEADER_API_KEY);
	        
//	        httpPost.setEntity(entity);

	        try {
				httpClient.execute(httpPost, ma);
			} catch (ClientProtocolException e) {
				Log.v("TeamLeader", "ClientProtocolException");
				e.printStackTrace();
			} catch (IOException e) {
				Log.v("TeamLeader", "IOException");
				e.printStackTrace();
			}
	        if (httpClient != null) httpClient.close();

	        return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		super.onPostExecute(result);
	}
	
	

}
