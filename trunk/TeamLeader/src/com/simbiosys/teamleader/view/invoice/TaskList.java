// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.view.invoice;

import java.io.File;
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
import com.simbiosys.teamleader.model.Company;
import com.simbiosys.teamleader.model.Department;
import com.simbiosys.teamleader.model.Task;
import com.simbiosys.teamleader.restAPI.RESTfulAPI;
import com.simbiosys.teamleader.view.ErrorActivity;
import com.simbiosys.teamleader.view.task.ListTasksListAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class TaskList extends Activity 
implements OnLongClickListener, OnClickListener, ResponseHandler<File> {

	ArrayList<Task> taskList;
	ProgressDialog progressDialog;
	Department department;
	int invoiceId;
	Company company;
	String filename;
	EditText fileSaveAsEditText;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("company", company);
		outState.putString("filename", filename);
		outState.putSerializable("taskList", taskList);
		outState.putSerializable("department", department);
		outState.putInt("invoiceId",invoiceId);
	}

	@SuppressWarnings("unchecked")
	private void restoreState(Bundle state) {
		this.taskList = (ArrayList<Task>)state.getSerializable("taskList");
		this.department = (Department)state.getSerializable("department");
		this.invoiceId = state.getInt("invoiceId");
		this.company = (Company)state.getSerializable("company");
		this.filename = state.getString("filename");
		
		// restore the UI
		EditText editText = (EditText)findViewById(R.id.editText1);
		if (department != null)
			editText.setText(department.getName());
		
		
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);
		
		if (savedInstanceState != null) {
			restoreState(savedInstanceState);
		}

		Intent i = getIntent();
		taskList = (ArrayList<Task>) i.getSerializableExtra("taskList");
		company = (Company) i.getSerializableExtra("company");
		if (taskList == null && taskList instanceof ArrayList<?>) {
			//Log.v("TeamLeader", "Task list empty");
			Toast.makeText(this,  "Task list empty",  Toast.LENGTH_LONG).show();
		}
		String[] fieldsToShow = { "description", "startFormatted", "duration" };
		// create our List Adapter
		ListTasksListAdapter<Task> taskListAdapter = new ListTasksListAdapter<Task>(this, taskList, fieldsToShow );		

		// set the list adapter
		ListView lv = (ListView)findViewById(R.id.listView1);
		lv.setAdapter(taskListAdapter);
		
		fileSaveAsEditText = (EditText)findViewById(R.id.editText2);
		
	}

	public void createInvoice(View view) {
		//Log.v("TeamLeader", "creating invoice");
		ArrayList<Task> includedTaskList = new ArrayList<Task>();
		// go through all of the checked Tasks and create an invoice for them.
		ListView listView = (ListView)findViewById(R.id.listView1);
		int childCount = listView.getChildCount();
		for (int i = 0; i < childCount; i++) {
			LinearLayout verticalLayout = (LinearLayout)listView.getChildAt(i);
			LinearLayout horizontalLayout = (LinearLayout)verticalLayout.getChildAt(0);
			@SuppressWarnings("rawtypes")
			ListTasksListAdapter.ViewHolder viewHolder = (ListTasksListAdapter.ViewHolder)horizontalLayout.getTag();
			if (viewHolder.o.isChecked()) {
				includedTaskList.add((Task)(viewHolder.o));
			}
		}
		
		// make sure we get the filename and store it before going off into the ether
		filename = fileSaveAsEditText.getText().toString();

		// send the instruction to add the person with the TeamLeader API
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("Communicating with server");
		progressDialog.setCancelable(true);

		RESTfulAPI restAPI = new RESTfulAPI(this, null, progressDialog);
		restAPI.creatInvoice(includedTaskList, department, company);


	}

	@Override
	public boolean onLongClick(View arg0) {
		// TODO Display all Task information in a view
		return false;
	}

	@Override
	public void onClick(View arg0) {
		// do nothing
	}

	public File handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		// No need to do anything here... Other than display a success dialog which may be more of a hindrance.
		// what's returned is an id for the person added.  I'm not going to store this since we have to query to get
		// that id later anyway.  Might as well let the api do the work.
		//		Toast.makeText(this, "Add person Completed", Toast.LENGTH_LONG).show();
		HttpEntity httpEntity = response.getEntity();
		InputStream inputStream = httpEntity.getContent();
		// We need to create a PDF file from the contents that are returned with this call.
		// Then store the file on the long-term storage

		LineNumberReader lnr = new LineNumberReader(new InputStreamReader(inputStream));
		String currentString;
		while((currentString = lnr.readLine()) != null) {
			//Log.v("TeamLeader", "httpResponse:" + currentString);
			// parse the response 
			// we'll use this later to generate a pdf with the invoice
			try {
				invoiceId = Integer.valueOf(currentString);
			}
			catch (NumberFormatException nfe) { 
				Intent i = new Intent(this, ErrorActivity.class);
				Globals.error = currentString;
				startActivity(i);
			}
			//Log.v("TeamLeader", "invoiceId: " + invoiceId);
		}
		
		// now that we have our invoice id, we can generate a pdf file.
		GetPDFResponseHandler responseHandler2 = new GetPDFResponseHandler(this, filename);
		RESTfulAPI restfulAPI2 = new RESTfulAPI(responseHandler2, null, progressDialog);
		restfulAPI2.getInvoicePDF(invoiceId);

		//		finish();
		return null;
	}

	public void listDepartments(View view) {
		Intent i = new Intent(this, ListDepartments.class);
		startActivityForResult(i, Globals.GET_DEPARTMENT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
		case Globals.GET_DEPARTMENT:
			department = (Department)data.getSerializableExtra("department");

			EditText editText = (EditText)findViewById(R.id.editText1);
			editText.setText(department.getName());
			break;
		}
	}




}
