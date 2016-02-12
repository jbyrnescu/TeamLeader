// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.view.crm;

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
import com.simbiosys.teamleader.model.Contact;
import com.simbiosys.teamleader.model.Person;
import com.simbiosys.teamleader.restAPI.PostExecuteHandler;
import com.simbiosys.teamleader.restAPI.RESTfulAPI;
import com.simbiosys.teamleader.view.crm.ListPersonListAdapter.ViewHolder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class PeopleList extends Activity 
implements ResponseHandler<ArrayList<Person>>, GetContactsResponse, PostExecuteHandler,
OnLongClickListener, OnClickListener {

	ListView lv;
	ArrayList<Person> peopleList;
	ProgressDialog progressDialog;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("numPeople", peopleList.size());
		for (int i = 0; i < peopleList.size(); i++) {
			outState.putSerializable("person"+i, peopleList.get(i));
		}
	}

	public void restoreState(Bundle state) {
		int numPeople = state.getInt("numPeople");
		peopleList = new ArrayList<Person>(numPeople);
		for (int i = 0; i < numPeople; i++) {
			peopleList.add((Person)state.getSerializable("person"+i));
		}
		postExecuteHandler();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_people_list);
		lv = (ListView)findViewById(R.id.listView1);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, (int)(height*.65), 0);
		lv.setLayoutParams(lp);

		if (savedInstanceState != null) {
			restoreState(savedInstanceState);
		} else {
			// if we were launched with parameters, call listPeople(...) automatically
			Intent i = getIntent();
			EditText searchStringEditText = (EditText)findViewById(R.id.editText1);
			
			String personToSearchFor = i.getStringExtra("searchForPerson");
			if (personToSearchFor != null) {

				searchStringEditText.setText(personToSearchFor);
				// call the search button automatically pressed
				Button b = (Button)findViewById(R.id.button1);
				listPeople(b);
			} else {
				searchStringEditText.setText("");
				Button b = (Button)findViewById(R.id.button1);
				listPeople(b);
			}
		}
	}

	public void listPeople(View view) {
		EditText searchStringEditText = (EditText)findViewById(R.id.editText1);
		String searchString = searchStringEditText.getText().toString();

		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("Communicating with server");
		progressDialog.setCancelable(true);

		RESTfulAPI restfulAPI = new RESTfulAPI(this, this, progressDialog);
		restfulAPI.getContacts(searchString, this);

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
		ListPersonListAdapter lpla = new ListPersonListAdapter(this, peopleList);
		lv = (ListView)findViewById(R.id.listView1);
		lv.setAdapter(lpla);
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

	public void getContactsResponse(ArrayList<Person> peopleList) {

		for(Contact p:peopleList) {
			//Log.v("TeamLeader",p.toString());
		}

		// pass this people list on this this class to use it later on a different thread (The UI thread)
		this.peopleList = (ArrayList<Person>)peopleList;

	}

	@Override
	public boolean onLongClick(View arg0) {
		//Log.v("TeamLeader", "LongClick registered... We can display the company now");
		Person p = ((ViewHolder)arg0.getTag()).person;
		p.toString();
		Intent i = new Intent(this, EnterPerson.class);
		i.putExtra("person", p);
		startActivityForResult(i, Globals.GET_PERSON_ID);
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// send this data back to the calling activity and cancel this activity
		super.onActivityResult(requestCode, resultCode, data);
		finishActivity(Globals.GET_PERSON_ID);		
	}

	@Override
	public void onClick(View arg0) {
		// send the company back to the calling Activity
		LinearLayout linearLayout = (LinearLayout)arg0;
		Person p = ((ViewHolder)linearLayout.getTag()).person;
		Intent resultIntent = new Intent();
		resultIntent.putExtra("person", p);
		setResult(Globals.GET_PERSON_ID, resultIntent);
		finish();

	}
}
