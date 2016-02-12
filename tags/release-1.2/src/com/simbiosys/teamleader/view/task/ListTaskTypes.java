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
import com.simbiosys.teamleader.model.Company;
import com.simbiosys.teamleader.model.TaskType;
import com.simbiosys.teamleader.restAPI.PostExecuteHandler;
import com.simbiosys.teamleader.restAPI.RESTfulAPI;
import com.simbiosys.teamleader.view.task.ListTaskTypesListAdapter.ViewHolder;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ListTaskTypes extends Activity implements ResponseHandler<ArrayList<Company>>, 
GetTaskTypesResponse, PostExecuteHandler,
OnClickListener {

	ListView lv;
	ArrayList<TaskType> taskTypeList;
	ProgressDialog progressDialog;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("numTaskTypes", taskTypeList.size());
		for (int i = 0; i < taskTypeList.size(); i++) {
			outState.putSerializable("taskType" + i, taskTypeList.get(i));
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	public void restoreState(Bundle state) {
		int numTaskTypes = state.getInt("numTaskTypes");
		taskTypeList = new ArrayList<TaskType>(numTaskTypes);
		for (int i = 0; i < numTaskTypes; i++) {
			taskTypeList.add((TaskType)state.getSerializable("taskType"+i));
		}
		postExecuteHandler();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_task_types);
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

			// if we were launched with parameters, call listCompanies(...) automatically
			Intent i = getIntent();
			String companyToSearchFor = i.getStringExtra("searchForTask");
			if (companyToSearchFor != null) {
				EditText searchStringEditText = (EditText)findViewById(R.id.editText1);
				searchStringEditText.setText(companyToSearchFor);
				// call the search button automatically pressed
				Button b = (Button)findViewById(R.id.button1);
				listTaskTypes(b);
			}
		}
	}

	public void listTaskTypes(View view) {
		EditText searchStringEditText = (EditText)findViewById(R.id.editText1);
		String searchString = searchStringEditText.getText().toString();

		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("Fetching data from Server");
		progressDialog.setCancelable(true);

		RESTfulAPI restfulAPI = new RESTfulAPI(this, this, progressDialog);
		taskTypeList = restfulAPI.getTaskTypes(searchString, this);

	}

	@Override
	public ArrayList<Company> handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		HttpEntity httpEntity = response.getEntity();
		InputStream inputStream = httpEntity.getContent();
		LineNumberReader lnr = new LineNumberReader(new InputStreamReader(inputStream));
		String currentString;
		while((currentString = lnr.readLine()) != null) {
			Log.v("TeamLeader", "httpResponse:" + currentString);
		}
		return null;
	}

	@Override
	public void postExecuteHandler() {
		ListTaskTypesListAdapter lttla = new ListTaskTypesListAdapter(this, taskTypeList);
		lv = (ListView)findViewById(R.id.listView1);
		lv.setAdapter(lttla);
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
	public void getTaskTypesResponse(ArrayList<TaskType> taskTypeList) {

		for(TaskType t:taskTypeList) {
			Log.v("TeamLeader",t.toString());
		}

		// pass this company list on this this class to use it later on a different thread (The UI thread)
		this.taskTypeList = taskTypeList;

	}


	// There's no REST API for this.  So no bother doing a long click for it
	//	@Override
	//	public boolean onLongClick(View arg0) {
	//		Log.v("TeamLeader", "LongClick registered... We can display the company now");
	//		Company c = ((ViewHolder)arg0.getTag()).taskType;
	//		c.toString();
	//		Intent i = new Intent(this, EnterTaskType.class);
	//		i.putExtra("company", c);
	//		startActivityForResult(i, Globals.GET_COMPANY_ID);
	//		return false;
	//	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// send this data back to the calling activity and cancel this activity
		super.onActivityResult(requestCode, resultCode, data);
		finishActivity(Globals.GET_TASK_TYPE_ID);		
	}

	@Override
	public void onClick(View arg0) {
		// send the company back to the calling Activity
		LinearLayout linearLayout = (LinearLayout)arg0;
		TaskType t = ((ViewHolder)linearLayout.getTag()).taskType;
		Intent resultIntent = new Intent();
		resultIntent.putExtra("taskType", t);
		setResult(RESULT_OK, resultIntent);
		finish();
	}

}
