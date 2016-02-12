// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.view;

import com.simbiosys.teamleader.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateAccount extends Activity {
	
	SharedPreferences applicationLevelPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
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
