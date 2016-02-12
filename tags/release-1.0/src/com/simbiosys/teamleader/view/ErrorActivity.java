// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.view;

import com.simbiosys.teamleader.Globals;
import com.simbiosys.teamleader.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ErrorActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_error);
		TextView textView = (TextView)findViewById(R.id.textView1);
		
		textView.setText("There was an error: " + Globals.error);
	}
}
