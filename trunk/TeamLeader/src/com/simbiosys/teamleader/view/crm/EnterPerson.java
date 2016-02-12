// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.view.crm;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import com.simbiosys.teamleader.Globals;
import com.simbiosys.teamleader.R;
import com.simbiosys.teamleader.model.Company;
import com.simbiosys.teamleader.model.Person;
import com.simbiosys.teamleader.restAPI.IncompletePersonException;
import com.simbiosys.teamleader.restAPI.RESTfulAPI;
import com.simbiosys.teamleader.view.crm.EnterPersonListAdapter.ViewHolder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EnterPerson extends Activity implements //OnClickListener, 
OnFocusChangeListener, ResponseHandler<EnterPerson>
{

	//	HashMap<String, String> properties;
	ListView lv;
	Person person;
	EnterPersonListAdapter epla;
	String fields[];
	ProgressDialog progressDialog;
	EditText linkCompanyEditText;
	boolean companyLinked = false;
	ArrayList<ArrayList<String>> fieldsAndAnswers;
	Company company;

	RESTfulAPI restAPI2, restAPI;

	@SuppressWarnings("unchecked")
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		// for all children that are showing...
		// set the viewHolder.fieldAndAnswer...
		int count = lv.getChildCount();
		for (int i = 0; i < count; i++) {
			//			String key  = fields[i];
			LinearLayout linearLayout = (LinearLayout) lv.getChildAt(i);
			LinearLayout linearLayoutChild = (LinearLayout) linearLayout.getChildAt(0);
			EditText editText = (EditText)linearLayoutChild.getChildAt(1);
			if (editText.isShown()) {
				String value = editText.getText().toString();
				EnterPersonListAdapter.ViewHolder viewHolder = (ViewHolder) linearLayout.getTag();
				viewHolder.fieldAndAnswer.set(1, value);
			}

		}

		int count2 = epla.getCount();
		for (int i = 0; i < epla.getCount(); i++) {
			@SuppressWarnings("unchecked")
			ArrayList<String> fieldAndAnswer = (ArrayList<String>) epla.getItem(i);
			String fieldName = fieldAndAnswer.get(0);
			String value = fieldAndAnswer.get(1);
			fieldsAndAnswers.set(i, (ArrayList<String>)epla.getItem(i));
			person.set(fieldName, value);
		}
		
		outState.putSerializable("person", person);
		outState.putInt("numFields", fieldsAndAnswers.size());

		for (int i = 0; i < fieldsAndAnswers.size(); i++) {
			String keyName = "fieldsAndAnswers"+i;
			ArrayList<String> values = fieldsAndAnswers.get(i);
			outState.putStringArrayList(keyName, values);
		}

		outState.putSerializable("company", company);
		outState.putBoolean("companyLinked", companyLinked);


	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private void restoreState(Bundle state) {

		this.person = (Person) state.get("person");
		int numFields = state.getInt("numFields");
		fieldsAndAnswers = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < numFields; i++) {
			ArrayList<String> stringAndAnswer = state.getStringArrayList("fieldsAndAnswers"+i);
			fieldsAndAnswers.add(stringAndAnswer);
		}
		companyLinked = state.getBoolean("companyLinked");
		company = (Company) state.get("company");
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_person);
		lv = (ListView)findViewById(R.id.listView1);

		linkCompanyEditText = (EditText)findViewById(R.id.editText1);

		// set up the items
		fields = getResources().getStringArray(R.array.EnterPersonFields);

		if (savedInstanceState != null) {
			restoreState(savedInstanceState);
		} else {

			Intent intent = getIntent();
			person = (Person)intent.getSerializableExtra("person");
			if (person == null) person = new Person();

			fieldsAndAnswers = new ArrayList<ArrayList<String>>();
			for (int i = 0; i < fields.length; i++) {
				ArrayList<String> tempStrings = new ArrayList<String>();
				tempStrings.add(fields[i]);
				tempStrings.add(null);
				fieldsAndAnswers.add(tempStrings);
			}
		}
		
		epla = new EnterPersonListAdapter(this, fields, person);
		lv.setAdapter(epla);

		//		properties = new HashMap<String, String>();
		//		person = new Person(this);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, (int)(height*.65), 0);
		lv.setLayoutParams(lp);

	}

	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		//Log.v("TeamLeader","onFocusChange received");
		// Change the value of the appropriate value in our Person
		if (!arg1) {
			LinearLayout ll = (LinearLayout)arg0.getParent();
			TextView tv = (TextView)ll.getChildAt(0);
			String key = tv.getText().toString();
			EditText editText = (EditText)ll.getChildAt(1);
			String value = editText.getText().toString();
			View parent = (View) arg0.getParent().getParent();
			ViewHolder viewHolder = (ViewHolder)parent.getTag();
			viewHolder.fieldAndAnswer.set(1, value);
			if (viewHolder != null) // when the View is created all of the focuses are touched, so this may be null
			{
				person.set(key, value);
				int position = viewHolder.position;
				fieldsAndAnswers.get(position).set(1, value);
				//Log.v("TeamLeader", "Setting " + key + " to " + value);
			}
		}

	}

	public void commitPerson(View arg0) {

		// print out all of the properties of this person

		int count = lv.getChildCount();
		for (int i = 0; i < count; i++) {
			//			String key  = fields[i];
			LinearLayout linearLayout = (LinearLayout) lv.getChildAt(i);
			LinearLayout linearLayoutChild = (LinearLayout) linearLayout.getChildAt(0);
			EditText editText = (EditText)linearLayoutChild.getChildAt(1);
			String value = editText.getText().toString();
			EnterPersonListAdapter.ViewHolder viewHolder = (ViewHolder) linearLayout.getTag();
			viewHolder.fieldAndAnswer.set(1, value);
		}

		for (int i = 0; i < epla.getCount(); i++) {
			@SuppressWarnings("unchecked")
			ArrayList<String> fieldAndAnswer = (ArrayList<String>) epla.getItem(i);
			String fieldName = fieldAndAnswer.get(0);
			String value = fieldAndAnswer.get(1);
			person.set(fieldName, value);
		}

		//Log.v("TeamLeader","results \n" + person.toString());

		// send the instruction to add the person with the TeamLeader API
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("Communicating with server");
		progressDialog.setCancelable(true);

		restAPI = new RESTfulAPI(this, null, progressDialog);
		try {
			companyLinked = false;
			restAPI.addPerson(person);
			if (!companyLinked && company != null) {
				finish();
			}
		} catch (IncompletePersonException e) {
			Toast.makeText(this, "Incomplete Person: required - first name,  last name, email ", Toast.LENGTH_LONG).show();
		} 
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

	@Override
	public EnterPerson handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		// No need to do anything here... Other than display a success dialog which may be more of a hindrance.
		// what's returned is an id for the person added.  I'm not going to store this since we have to query to get
		// that id later anyway.  Might as well let the api do the work.
		//		Toast.makeText(this, "Add person Completed", Toast.LENGTH_LONG).show();
		HttpEntity httpEntity = response.getEntity();
		InputStream inputStream = httpEntity.getContent();
		LineNumberReader lnr = new LineNumberReader(new InputStreamReader(inputStream));
		String currentString;
		while((currentString = lnr.readLine()) != null) {
			//Log.v("TeamLeader", "httpResponse:" + currentString);
			// parse the response and add the id to the person
			if (!currentString.equals("\"OK\""))
				try {
					int id = Integer.parseInt(currentString);
					person.setId(id);
					person.set("id", currentString);
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
				}
		}

		// if the company was set, link them
		company = (Company)linkCompanyEditText.getTag();
		if (company != null && !companyLinked) {
			restAPI2 = new RESTfulAPI(this, null, progressDialog);
			restAPI2.linkPersonToCompany(person, company);
			companyLinked = true;
			finish();
		} else finish();



		return null;
	}

	public void linkCompany(View view) {

		// store all of our values before we try to find the company
		//		for (int i = 0; i < lv.getCount(); i++) {
		//			person.set(fields[i], (String)lv.getItemAtPosition(i));
		//		}

		// get the string that we'll be searching for to list
		// this next statement will come in handy!
		LinearLayout ll = (LinearLayout) view.getParent();
		//		Button b = (Button)ll.findViewById(R.id.button1);
		//		String buttonText = b.getText().toString();
		EditText editText = (EditText)ll.findViewById(R.id.editText1);
		String companyToSearchFor = editText.getText().toString();

		Intent i = new Intent(this, CompanyList.class);
		//		i.putExtra("person", person);
		i.putExtra("searchForCompany", companyToSearchFor);
		startActivityForResult(i, Globals.GET_COMPANY_ID);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case Globals.GET_COMPANY_ID:
			company = (Company)data.getSerializableExtra("company");
			company.toString();
			person.set("company", company.get("id"));
			//Log.v("TeamLeader","this person will be linked to company: " + company.get("id"));

			linkCompanyEditText.setText(company.getName());
			linkCompanyEditText.setTag(company);
			break;
		}
	}


	//	@Override
	//	public void onClick(View v) {
	//		// go through and replace every one since we can't determine which one we came from.
	//		// LAME... (Either that or I don't know how to do this.)
	//		//Log.v("TeamLeader", "EditText onClick received");
	//		for(String key:epla.mappedItems.keySet()) {
	//			String value = epla.mappedItems.get(key).et.getText().toString();
	//			person.set(key, value);
	//		}
	//		
	//	}
}
