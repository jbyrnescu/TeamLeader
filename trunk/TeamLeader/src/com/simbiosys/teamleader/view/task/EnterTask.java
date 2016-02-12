// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.view.task;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
//import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
//import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import com.simbiosys.teamleader.Globals;
import com.simbiosys.teamleader.R;
import com.simbiosys.teamleader.model.Company;
import com.simbiosys.teamleader.model.Person;
import com.simbiosys.teamleader.model.Task;
import com.simbiosys.teamleader.model.TaskType;
import com.simbiosys.teamleader.restAPI.RESTfulAPI;
import com.simbiosys.teamleader.view.ErrorActivity;
import com.simbiosys.teamleader.view.PickDateTime;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EnterTask extends Activity implements //OnClickListener, 
OnFocusChangeListener, ResponseHandler<EnterTask>
{

	public static final int DESCRIPTION_FIELD_POSITION = 0;

	//	HashMap<String, String> properties;
	Task task;
	String fields[];
	ProgressDialog progressDialog;
	//	ArrayList<ArrayList<String>> fieldsAndAnswers;
	LinearLayout fieldParentLinearLayout;

	long minDateStart;
	long maxDateStart;

	long minDateEnd;
	long maxDateEnd;

	RESTfulAPI restAPI;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putLong("minDateStart", minDateStart);
		outState.putLong("maxDateStart", maxDateStart);
		outState.putLong("minDateEnd", minDateEnd);
		outState.putLong("maxDateEnd", maxDateEnd);

		// we don't need to store this because we have to know exactly which fields/Answers go where
		// and manually stuff
		//		outState.putInt("numFieldsAndAnswers", fieldsAndAnswers.size());
		//		for (int i = 0; i < fieldsAndAnswers.size(); i++) {
		//			outState.putStringArrayList("field" + i, fieldsAndAnswers.get(i));
		//		}
		outState.putSerializable("task", task);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	public void restoreState(Bundle state) {

		minDateStart = state.getLong("minDateStart");
		maxDateStart = state.getLong("maxDateStart");
		minDateEnd = state.getLong("minDateEnd");
		maxDateEnd = state.getLong("maxDateEnd");

		// Description
		EditText descriptionEditText = (EditText) findViewById(R.id.editText6);
		descriptionEditText.setText(task.getDescription());

		// Start Date
		long timePointInSeconds = task.getStartDate();
		EditText editText1 = (EditText)findViewById(R.id.editText1);
		Date date = new Date(timePointInSeconds*1000);
		//Log.v("TeamLeader", "date entered: seconds since epoch: " + timePointInSeconds);
		//			DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
		DateFormat dateFormat = SimpleDateFormat.getDateInstance();
		String dateString = dateFormat.format(date);
		editText1.setText(dateString);

		// End Date
		long timePointInSeconds1 = task.getEndDate();
		EditText editText11 = (EditText)findViewById(R.id.editText2);
		Date date1 = new Date(timePointInSeconds1*1000);
		//Log.v("TeamLeader", "date entered: seconds since epoch: " + timePointInSeconds1);
		//			DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
		DateFormat dateFormat1 = SimpleDateFormat.getDateInstance();
		String dateString1 = dateFormat1.format(date1);
		editText11.setText(dateString1);

		// Contact
		Person p = task.getWorkerPerson();
		EditText editText = (EditText)findViewById(R.id.editText3);
		editText.setText(p.getName());

		// Task Type
		TaskType taskType = task.getTaskType();
		EditText editText2 = (EditText)findViewById(R.id.editText4);
		editText2.setText(taskType.getName());

		// Bill To
		Person billToPerson = task.getBillToPerson();
		EditText editTextBillTo = (EditText)findViewById(R.id.editText5);				
		editTextBillTo.setText(billToPerson.getName());

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_task);

		fieldParentLinearLayout = (LinearLayout)findViewById(R.id.textView1)
				.getParent().getParent();

		// set up the items
		fields = getResources().getStringArray(R.array.EnterTaskFields);

		if (savedInstanceState != null) {
			restoreState(savedInstanceState);
		} else {

			Intent intent = getIntent();
			task = (Task)intent.getSerializableExtra("task");
			if (task == null) task = new Task();

			//		fieldsAndAnswers = new ArrayList<ArrayList<String>>();
			//		for (int i = 0; i < fields.length; i++) {
			//			ArrayList<String> tempStrings = new ArrayList<String>();
			//			tempStrings.add(fields[i]);
			//			tempStrings.add(null);
			//			fieldsAndAnswers.add(tempStrings);
			//		}

			//		properties = new HashMap<String, String>();

			//		Display display = getWindowManager().getDefaultDisplay();
			//		Point size = new Point();
			//		display.getSize(size);
			//		int width = size.x;
			//		int height = size.y;
			//
			//		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, (int)(height*.65), 0);
			//		lv.setLayoutParams(lp);

		}
	}

	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		//Log.v("TeamLeader","onFocusChange received");
		// Change the value of the appropriate value in our Person
		if (!arg1 && arg0 instanceof EditText) {
			LinearLayout ll = (LinearLayout)arg0.getParent();
			TextView tv = (TextView)ll.getChildAt(0);
			String key = tv.getText().toString();
			EditText editText = (EditText)ll.getChildAt(1);
			String value = editText.getText().toString();
			//			View parent = (View) arg0.getParent().getParent();
			TaskViewHolder viewHolder = (TaskViewHolder)ll.getTag();
			if (viewHolder != null) // when the View is created all of the focuses are touched, so this may be null
			{
				task.set(key, value);
				//				int position = viewHolder.position;
				//				fieldsAndAnswers.get(position).remove(1);
				//				fieldsAndAnswers.get(position).add(value);
				//Log.v("TeamLeader", "Setting " + key + " to " + value);
			}
		}

	}

	public void commitTask(View arg0) {

		// print out all of the properties of this person
		//Log.v("TeamLeader","results \n" + task.toString());
		// there are 5 (at the time of this comment) linear Layouts 4 of them have fields
		//		int count = fieldParentLinearLayout.getChildCount()-1;
		//		for (int i = 0; i < count; i++) {
		//			String key  = fields[i];
		//			LinearLayout linearLayout = (LinearLayout) fieldParentLinearLayout.getChildAt(i);
		//			EditText editText = (EditText)linearLayout.getChildAt(1);
		//			String value = editText.getText().toString();
		//			task.set(key, value);
		//		}

		// This sets only the description.  The other fields aren't really representative of what need to be
		// sent to the RESTAPI.  They're only part of what needs to be sent.
		int i = DESCRIPTION_FIELD_POSITION;
		String key = fields[i];
		LinearLayout linearLayout = (LinearLayout) fieldParentLinearLayout.getChildAt(i);
		EditText editText = (EditText)linearLayout.getChildAt(1);
		String value = editText.getText().toString();
		task.set(key, value);

		// send the instruction to add the person with the TeamLeader API
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("Communicating with server");
		progressDialog.setCancelable(true);

		restAPI = new RESTfulAPI(this, null, progressDialog);
		try {
			restAPI.addTask(task);
			//			finish();
		} catch (IncompleteTaskException e) {
			Toast.makeText(this, "Incomplete Task: required - description, start/end date, worker, and task_type ", Toast.LENGTH_LONG).show();
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
	public EnterTask handleResponse(HttpResponse response)
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
			if (!currentString.equals("\"OK\"")) { 
				//Log.v("TeamLeader", "task add not completed successfully");
				Intent i = new Intent(this, ErrorActivity.class);
				Globals.error = currentString;
				startActivity(i);
			}

		}

		finish();

		return null;
	}

	public void linkWorker(View view) {
		Intent i = new Intent(this, GetUserList.class);
		startActivityForResult(i, Globals.GET_WORKER_PERSON_ID);
	}

	public void linkFor(View view) {
		Intent i = new Intent(this, BillToMenu.class);
		startActivityForResult(i, Globals.GET_BILL_TO);

	}

	public void listTaskTypes(View view) {
		Intent i = new Intent(this, ListTaskTypes.class);
		startActivityForResult(i, Globals.GET_TASK_TYPE_ID);
	}

	@SuppressLint("CutPasteId") @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch(requestCode) {

		case Globals.GET_BILL_TO:
			if (data != null) {
				switch(resultCode) {


				case Globals.GET_BILL_TO_CONTACT_ID:
					Person billToPerson = (Person)data.getSerializableExtra("person");
					task.setBillToPerson(billToPerson);
					EditText editTextBillTo = (EditText)findViewById(R.id.editText5);				
					editTextBillTo.setText(billToPerson.getName());
					break;

				case Globals.GET_BILL_TO_COMPANY_ID:
					Company c = (Company)data.getSerializableExtra("company");
					task.setBillToCompany(c);
					EditText editTextBillTo2 = (EditText)findViewById(R.id.editText5);
					editTextBillTo2.setText(c.getName());
					break;
				}
			}
			break;

		case Globals.GET_WORKER_PERSON_ID:
			if (data != null) {
				Person p = (Person)data.getSerializableExtra("person");
				task.setWorkerPerson(p);
				EditText editText = (EditText)findViewById(R.id.editText3);
				editText.setText(p.getName());
			}
			break;

		case Globals.GET_START_TIME_POINT:
			if (data != null) {
				long timePointInSeconds = data.getLongExtra("timePoint", 0);
				// display the result in the field
				task.setStartDate(timePointInSeconds);
				EditText editText1 = (EditText)findViewById(R.id.editText1);
				Date date = new Date(timePointInSeconds*1000);
				//Log.v("TeamLeader", "date entered: seconds since epoch: " + timePointInSeconds);
				//			DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
				DateFormat dateFormat = SimpleDateFormat.getDateInstance();
				String dateString = dateFormat.format(date);
				editText1.setText(dateString);

				// set constraints on the other time picker
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.setTimeInMillis(timePointInSeconds*1000);
				calendar.add(Calendar.HOUR, 25);
				maxDateEnd = calendar.getTimeInMillis()/1000;
				minDateEnd = timePointInSeconds;
			}
			break;
		case Globals.GET_END_TIME_POINT:
			if (data != null) {
				long timePointInSeconds1 = data.getLongExtra("timePoint", 0);
				// display the result in the field
				task.setEndDate(timePointInSeconds1);
				EditText editText11 = (EditText)findViewById(R.id.editText2);
				Date date1 = new Date(timePointInSeconds1*1000);
				//Log.v("TeamLeader", "date entered: seconds since epoch: " + timePointInSeconds1);
				//			DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
				DateFormat dateFormat1 = SimpleDateFormat.getDateInstance();
				String dateString1 = dateFormat1.format(date1);
				editText11.setText(dateString1);
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.setTimeInMillis(timePointInSeconds1*1000);
				calendar.add(Calendar.HOUR, -25);
				minDateStart = calendar.getTimeInMillis()/1000;
				maxDateStart = timePointInSeconds1;
			}
			break;
		case Globals.GET_TASK_TYPE_ID:
			if (data != null) {
				TaskType taskType = (TaskType) data.getSerializableExtra("taskType");
				task.setTaskType(taskType);
				EditText editText2 = (EditText)findViewById(R.id.editText4);
				editText2.setText(taskType.getName());
			}
			break;
		}

	}

	public void getStart(View view) {
		Intent i = new Intent(this, PickDateTime.class);
		i.putExtra("minDate", minDateStart);
		i.putExtra("maxDate", maxDateStart);
		startActivityForResult(i, Globals.GET_START_TIME_POINT);
	}

	public void getEnd(View view) {
		Intent i = new Intent(this, PickDateTime.class);
		i.putExtra("minDate", minDateEnd);
		i.putExtra("maxDate", maxDateEnd);
		startActivityForResult(i, Globals.GET_END_TIME_POINT);
	}

}

