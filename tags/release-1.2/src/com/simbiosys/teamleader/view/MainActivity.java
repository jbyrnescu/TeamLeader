// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import com.simbiosys.teamleader.R;
import com.simbiosys.teamleader.restAPI.Network;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements ResponseHandler<MainActivity> {
	
	// TODO Make it such that when switching orientations, the program doesn't delete existing data
	// TODO fix theme
	// TODO make sure returning null (pressing the back button) doesn't crash the system for every startActvityForResult.
	// TODO take care of scenario(s) where network provider switches (i.e. resend REST request)
	
	
	private SharedPreferences applicationLevelPreferences;
	private String TEAM_LEADER_GROUP_from_preferences;
	private String TEAM_LEADER_API_KEY_from_preferences;

	// API Key from TeamLeader
	// XJmis8RBahZTE1cBSEqFpnPEOcUHg564EjdXiVpi2e1x53YOyfkNsZi713F8YB3tK7gSSv0LAS8wMXba2lOlbXi2QN1FfUYQR5yArpbR8aeLXfLQqq1rdakUOcpTXdzF9Y5qe78d7cOVhqCyGuQKu1u93KSQOimN7h3cf1fc3TYb9qzGLfg56B50bOHPXTsU0lX6d297
	// Group from Team Leader: 3897
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // To test the API just uncomment this code.
//        Network network = new Network(this);
//        network.execute();
 
    }

	// This handleResponse is from feasibility testing
	@Override
	public MainActivity handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		HttpEntity httpEntity = response.getEntity();
		InputStream inputStream = httpEntity.getContent();
		LineNumberReader lnr = new LineNumberReader(new InputStreamReader(inputStream));
		String currentString;
		while((currentString = lnr.readLine()) != null) {
			Log.v("TeamLeader", currentString);
		}
		return null;
	}
	
	public void login(View arg0) {
		applicationLevelPreferences = this.getSharedPreferences("major",android.content.Context.MODE_PRIVATE);
		TEAM_LEADER_GROUP_from_preferences = applicationLevelPreferences.getString("group", "");
		TEAM_LEADER_API_KEY_from_preferences = applicationLevelPreferences.getString("apiKey","");

		if (TEAM_LEADER_GROUP_from_preferences.equals("") || TEAM_LEADER_API_KEY_from_preferences.equals("")) {
			Log.e("TeamLeader", "Account Not Set up correctly");
			Toast.makeText(this, "Please set up your API Key in the Account Creation Activity", Toast.LENGTH_LONG).show();
		} else {
			Intent i = new Intent(this, MainMenu.class);
			startActivity(i);
		}
	}
	
	public void createAccount(View arg0) {
		Intent i = new Intent(this, CreateAccount.class);
		startActivity(i);
	}
	
}
