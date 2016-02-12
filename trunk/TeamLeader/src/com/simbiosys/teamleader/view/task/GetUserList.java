// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.view.task;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import com.simbiosys.teamleader.Globals;
import com.simbiosys.teamleader.R;
import com.simbiosys.teamleader.model.Person;
import com.simbiosys.teamleader.restAPI.PostExecuteHandler;
import com.simbiosys.teamleader.restAPI.RESTfulAPI;
import com.simbiosys.teamleader.view.task.ListRESTObjectListAdapter.ViewHolder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class GetUserList extends Activity 
implements ResponseHandler<ArrayList<Person>>, GetUsersResponse, PostExecuteHandler,
OnLongClickListener, OnClickListener {

	ListView lv;
	ArrayList<Person> peopleList;
	ProgressDialog progressDialog;
	
	String fieldsToShow[] = { "name", "email" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_user_list);
		lv = (ListView)findViewById(R.id.listView1);
		


		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, (int)(height*.65), 0);
		lv.setLayoutParams(lp);

		// if we were launched with parameters, call listPeople(...) automatically
		Button b = new Button(this);
		listUsers(b);
	}

	public void listUsers(View view) {

		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("Communicating with server");
		progressDialog.setCancelable(true);

		RESTfulAPI restfulAPI = new RESTfulAPI(this, this, progressDialog);
		restfulAPI.getUsers(this);

	}
	
	@Override
	public ArrayList<Person> handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		HttpEntity httpEntity = response.getEntity();
		InputStream inputStream = httpEntity.getContent();
		LineNumberReader lnr = new LineNumberReader(new InputStreamReader(inputStream));
		String currentString;
		while((currentString = lnr.readLine()) != null) {
			//Log.v("TeamLeader", "httpResponse:" + currentString);
		}
		return null;
	}
	
	@Override
	public void postExecuteHandler() {
		ListRESTObjectListAdapter<Person> lrola = new ListRESTObjectListAdapter<Person>(this, peopleList, fieldsToShow);
		lv = (ListView)findViewById(R.id.listView1);
		lv.setAdapter(lrola);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(progressDialog!=null)
			if(progressDialog.isShowing()){
				progressDialog.cancel();
			}
		super.onDestroy();
	}

	public void getUsersResponse(ArrayList<Person> peopleList) {

		for(Person p:peopleList) {
			//Log.v("TeamLeader",p.toString());
		}

		// pass this people list on this this class to use it later on a different thread (The UI thread)
		this.peopleList = (ArrayList<Person>)peopleList;

	}

	@Override
	public boolean onLongClick(View arg0) {
//		we're not editing users here...
		return false;
	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// send this data back to the calling activity and cancel this activity
//		super.onActivityResult(requestCode, resultCode, data);
//		finishActivity(Globals.GET_PERSON_ID);		
//	}

	@Override
	public void onClick(View arg0) {
		// send the company back to the calling Activity
		LinearLayout linearLayout = (LinearLayout)arg0;
		@SuppressWarnings("rawtypes")
		Person p = (Person)((ViewHolder)linearLayout.getTag()).o;
		Intent resultIntent = new Intent();
		resultIntent.putExtra("person", p);
		setResult(Globals.GET_PERSON_ID, resultIntent);
		finish();

	}
}
