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

import com.simbiosys.teamleader.R;
import com.simbiosys.teamleader.model.Company;
import com.simbiosys.teamleader.model.Person;
import com.simbiosys.teamleader.restAPI.IncompleteCompanyException;
import com.simbiosys.teamleader.restAPI.RESTfulAPI;
import com.simbiosys.teamleader.view.crm.EnterCompanyListAdapter.ViewHolder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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


public class EnterCompany extends Activity implements
OnFocusChangeListener, ResponseHandler<EnterCompany>
{

	HashMap<String, String> properties;
	ListView lv;
	Company company;
	EnterCompanyListAdapter epla;
	String fields[];
	ProgressDialog progressDialog;
	Person person;

	public static final int GET_PERSON_ID = 1;
	boolean personLinked = false;

	EditText linkPersonEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_company);
		lv = (ListView)findViewById(R.id.listView1);

		linkPersonEditText = (EditText)findViewById(R.id.editText1);

		// set up the items
		fields = getResources().getStringArray(R.array.EnterCompanyFields);

		Intent i = getIntent();
		company = (Company)i.getSerializableExtra("company");

		epla = new EnterCompanyListAdapter(this, fields, company);
		lv.setAdapter(epla);

		properties = new HashMap<String, String>();


		if (company == null)
			company = new Company();

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
		Log.v("TeamLeader","onFocusChange received");
		// Change the value of the appropriate value in our company
		if (!arg1) {
			LinearLayout ll = (LinearLayout)arg0.getParent();
			TextView tv = (TextView)ll.getChildAt(0);
			String key = tv.getText().toString();
			EditText editText = (EditText)ll.getChildAt(1);
			String value = editText.getText().toString();
			View parent = (View) arg0.getParent().getParent();
			ViewHolder viewHolder = (ViewHolder)parent.getTag();
			viewHolder.fieldAndAnswer.remove(1);
			viewHolder.fieldAndAnswer.add(new String(value));
			if (viewHolder != null) {
				company.set(key, value);
				//				int position = viewHolder.position;
				Log.v("TeamLeader", "Setting " + key + " to " + value);
			}
		}

	}

	public void commitCompany(View arg0) {

		// print out all of the properties of this Company
		Log.v("TeamLeader","results \n" + company.toString());

		// go through and make sure we got all of the showing fields entered
		for (int i = 0; i < lv.getChildCount(); i++) {
			LinearLayout tempLL = (LinearLayout)lv.getChildAt(i);
			LinearLayout innerLL = (LinearLayout)tempLL.getChildAt(0);
			EditText tempEditText = (EditText)innerLL.getChildAt(1);
			EnterCompanyListAdapter.ViewHolder viewHolder = (ViewHolder) tempLL.getTag();
			((ArrayList<String>)(viewHolder.fieldAndAnswer)).remove(1);
			viewHolder.fieldAndAnswer.add(tempEditText.getText().toString());
		}


		for (int i = 0; i < epla.getCount(); i++) {

			//			LinearLayout tempLL = (LinearLayout)lv.getChildAt(i);
			//			LinearLayout innerLL = (LinearLayout)tempLL.getChildAt(0);
			//			EditText tempEditText = (EditText)innerLL.getChildAt(1);

			@SuppressWarnings("unchecked")
			ArrayList<String> fieldAndAnswer = (ArrayList<String>)epla.getItem(i);
			String fieldName = fieldAndAnswer.get(0);
			String answer = fieldAndAnswer.get(1);
			company.set(fieldName, answer);
		}


		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("Communicating with server");
		progressDialog.setCancelable(true);

		// send the instruction to add the Company with the TeamLeader API

		RESTfulAPI restAPI = new RESTfulAPI(this, null, progressDialog);
		try {
			personLinked = false;
			restAPI.addCompany(company);
			if (!personLinked && person != null)
				finish();
		} catch (IncompleteCompanyException e) {
			Toast.makeText(this, "Incomplete Company: required - name ", Toast.LENGTH_LONG).show();
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
	public EnterCompany handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		// No need to do anything here... Other than display a success dialog which may be more of a hindrance.
		// what's returned is an id for the Company added.  I'm not going to store this since we have to query to get
		// that id later anyway.  Might as well let the api do the work.
		//		Toast.makeText(this, "Add Company Completed", Toast.LENGTH_LONG).show();
		HttpEntity httpEntity = response.getEntity();
		InputStream inputStream = httpEntity.getContent();
		LineNumberReader lnr = new LineNumberReader(new InputStreamReader(inputStream));
		String currentString;
		while((currentString = lnr.readLine()) != null) {
			Log.v("TeamLeader", "httpResponse:" + currentString);

			if (!currentString.equals("\"OK\""))
				try {
					int id = Integer.parseInt(currentString);
					company.setId(id);

				} catch(NumberFormatException nfe) {
					nfe.printStackTrace();
				}
		}

		// if the Person was set, link them
		person = (Person) linkPersonEditText.getTag();
		if (person != null && !personLinked) {
			RESTfulAPI restAPI2 = new RESTfulAPI(this, null, progressDialog);
			restAPI2.linkCompanyToPerson(company, person);
			personLinked = true;
			finish();
		} else finish();
		return null;
	}	


	public void linkPerson(View view) {

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
		String personToSearchFor = editText.getText().toString();

		Intent i = new Intent(this, PeopleList.class);
		//		i.putExtra("person", person);
		i.putExtra("searchForPerson", personToSearchFor);
		startActivityForResult(i, GET_PERSON_ID);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case GET_PERSON_ID:
			person = (Person)data.getSerializableExtra("person");
			person.toString();
			company.set("person", person.get("id"));
			Log.v("TeamLeader","this company will be linked to person: " + person.get("id"));

			linkPersonEditText.setText(person.getName());
			linkPersonEditText.setTag(person);
			break;
		}
	}


}
