// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.view;

import com.simbiosys.teamleader.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class CreateAccount extends Activity {
	
	SharedPreferences applicationLevelPreferences;
	
	String key, group;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// get the strings and store them for later use
		EditText groupEditText = (EditText)findViewById(R.id.editText1);
		EditText apiKeyEditText = (EditText)findViewById(R.id.editText2);

		if (outState.get("apiKey") == null) {
			String text = apiKeyEditText.getText().toString();
//			Log.v("TeamLeader", "Saving instance state, Account Creation, apiKey = " + text);
			outState.putString("apiKey", text);
		}
		
		outState.putString("group", groupEditText.getText().toString());

//		super.onSaveInstanceState(outState);
		
	}
	
	private void restoreState(Bundle state) {
		// get the strings and store them for later use
		EditText groupEditText = (EditText)findViewById(R.id.editText1);
		EditText apiKeyEditText = (EditText)findViewById(R.id.editText2);
		
		groupEditText.setText(state.getString("group"));
		String text = state.getString("apiKey");
		Log.v("TeamLeader", "Not setting apiKey for security reasons");
//		Log.v("TeamLeader", "Restoring instance state, setting apiKey to: " + text);
//		String text2 = apiKeyEditText.getText().toString();
		// For some strange reason the text is duplicated in the editText, because of this we don't
		// set the apiKey Text
		apiKeyEditText.setText(text);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		
		if (savedInstanceState != null) {
			Log.v("TeamLeader", "restoring Account creation state");
			restoreState(savedInstanceState);
		}
		
//		EditText tempEditText = (EditText) findViewById(R.id.editText2);
//		Log.v("TeamLeader", "current Edit Text value: " + tempEditText.getText().toString());
		
		applicationLevelPreferences = getSharedPreferences("major",MODE_PRIVATE);
	}
	
	public void commit(View arg0) {
		// get the strings and store them for later use
		EditText groupEditText = (EditText)findViewById(R.id.editText1);
		EditText apiKeyEditText = (EditText)findViewById(R.id.editText2);
		
		// store in shared preferences
		SharedPreferences.Editor applicationLevelPreferenceEditor = applicationLevelPreferences.edit();
		applicationLevelPreferenceEditor.putString("group",groupEditText.getText().toString());
		applicationLevelPreferenceEditor.putString("apiKey",apiKeyEditText.getText().toString());
		applicationLevelPreferenceEditor.commit();
		finish();
		
	}
}
