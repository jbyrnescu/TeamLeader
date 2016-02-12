package com.simbiosys.teamleader.view.invoice;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import com.simbiosys.teamleader.Globals;
import com.simbiosys.teamleader.R;
import com.simbiosys.teamleader.model.Company;
import com.simbiosys.teamleader.model.Task;
import com.simbiosys.teamleader.model.TaskListParamsObject;
import com.simbiosys.teamleader.restAPI.PostExecuteHandler;
import com.simbiosys.teamleader.restAPI.RESTfulAPI;
import com.simbiosys.teamleader.view.ErrorActivity;
import com.simbiosys.teamleader.view.PickDateTime;
import com.simbiosys.teamleader.view.crm.CompanyList;
import com.simbiosys.teamleader.view.task.GetTasksResponse;
import com.simbiosys.teamleader.view.task.IncompleteTaskListParamsException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TaskListParams extends Activity 
implements ResponseHandler<TaskListParams>, GetTasksResponse, PostExecuteHandler {
	
	long minDateStart=0;
	long maxDateStart=0;
	long minDateEnd=0;
	long maxDateEnd=0;
	String startDate;
	String endDate;
	TaskListParamsObject taskListParams;
	
	ProgressDialog progressDialog;
	
	ArrayList<Task> taskList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list_params);
		taskListParams = new TaskListParamsObject();
		
		Date today = new Date();
		long timePointInSeconds = today.getTime()/1000;
		
		taskListParams.setStartDate(timePointInSeconds);
		EditText editText1 = (EditText)findViewById(R.id.editText1);
		Date date = new Date(timePointInSeconds*1000);
		Log.v("TeamLeader", "date entered: seconds since epoch: " + timePointInSeconds);
		//			DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
		DateFormat dateFormat = SimpleDateFormat.getDateInstance();
		String dateString = dateFormat.format(date);
		editText1.setText(dateString);
		
		// set constraints on the other time picker
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(timePointInSeconds*1000);
		calendar.add(Calendar.MONTH, 1);
		maxDateEnd = calendar.getTimeInMillis()/1000;
		minDateEnd = timePointInSeconds;
		taskListParams.setEndDate(calendar.getTimeInMillis()/1000);
		EditText editText2 = (EditText)findViewById(R.id.editText2);
		Date date2 = new Date(calendar.getTimeInMillis());
		dateString = dateFormat.format(date2);
		editText2.setText(dateString);

		
	}
	
	public void listCompanies(View view) {
		Intent i = new Intent(this, CompanyList.class);
		startActivityForResult(i, Globals.GET_COMPANY);
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
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case Globals.GET_START_TIME_POINT:
			if (data != null) {
				long timePointInSeconds = data.getLongExtra("timePoint", 0);
				// display the result in the field
				taskListParams.setStartDate(timePointInSeconds);
				EditText editText1 = (EditText)findViewById(R.id.editText1);
				Date date = new Date(timePointInSeconds*1000);
				Log.v("TeamLeader", "date entered: seconds since epoch: " + timePointInSeconds);
				//			DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
				DateFormat dateFormat = SimpleDateFormat.getDateInstance();
				String dateString = dateFormat.format(date);
				editText1.setText(dateString);
				
				// set constraints on the other time picker
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.setTimeInMillis(timePointInSeconds*1000);
				calendar.add(Calendar.MONTH, 1);
				maxDateEnd = calendar.getTimeInMillis()/1000;
				minDateEnd = timePointInSeconds;
			}
			break;
		case Globals.GET_END_TIME_POINT:
			if (data != null) {
				long timePointInSeconds1 = data.getLongExtra("timePoint", 0);
				// display the result in the field
				taskListParams.setEndDate(timePointInSeconds1);
				EditText editText11 = (EditText)findViewById(R.id.editText2);
				Date date1 = new Date(timePointInSeconds1*1000);
				Log.v("TeamLeader", "date entered: seconds since epoch: " + timePointInSeconds1);
				//			DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
				DateFormat dateFormat1 = SimpleDateFormat.getDateInstance();
				String dateString1 = dateFormat1.format(date1);
				editText11.setText(dateString1);
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.setTimeInMillis(timePointInSeconds1*1000);
				calendar.add(Calendar.MONTH, -1);
				minDateStart = calendar.getTimeInMillis()/1000;
				maxDateStart = timePointInSeconds1;
			}
			break;
		case Globals.GET_COMPANY:
			Company c = (Company)data.getSerializableExtra("company");
			taskListParams.setCompany(c);
			EditText editTextCompany = (EditText)findViewById(R.id.editText3);
			editTextCompany.setText(c.getName());
			break;
		}
	}
	
	public void commitTaskParams(View view) {
		// send the instruction to add the person with the TeamLeader API
				progressDialog = new ProgressDialog(this);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setMessage("Communicating with server");
				progressDialog.setCancelable(true);

				RESTfulAPI restAPI = new RESTfulAPI(this, this, progressDialog);
				try {
					restAPI.listTasks(taskListParams, this);
//					finish();
				} catch (IncompleteTaskListParamsException e) {
					Toast.makeText(this, "Incomplete Task params: required - description, start/end date, worker, and task_type ", Toast.LENGTH_LONG).show();
				} 
	}
	
	@Override
	public TaskListParams handleResponse(HttpResponse response)
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
			Log.v("TeamLeader", "httpResponse:" + currentString);
			// parse the response and add the id to the person
			if (!currentString.equals("\"OK\"")) { 
				Log.v("TeamLeader", "task add not completed successfully");
				Intent i = new Intent(this, ErrorActivity.class);
				Globals.error = currentString;
				startActivity(i);
			}

		}

		finish();

		return null;
	}

	@Override
	public void getTasksResponse(ArrayList<Task> taskList) {
		this.taskList = taskList;
		
	}

	@Override
	public void postExecuteHandler() {
		// start the actual task list activity and send the list of tasks to it.
		Intent i = new Intent(this, TaskList.class);
		i.putExtra("taskList", taskList);
		i.putExtra("company", taskListParams.getCompany());
		startActivity(i);
		
	}
	
}
