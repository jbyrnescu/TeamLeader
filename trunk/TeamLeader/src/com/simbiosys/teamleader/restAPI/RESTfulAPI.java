// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.restAPI;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.simbiosys.teamleader.Globals;
import com.simbiosys.teamleader.model.Company;
import com.simbiosys.teamleader.model.Department;
import com.simbiosys.teamleader.model.Person;
import com.simbiosys.teamleader.model.Task;
import com.simbiosys.teamleader.model.TaskListParamsObject;
import com.simbiosys.teamleader.model.TaskType;
import com.simbiosys.teamleader.view.ErrorActivity;
import com.simbiosys.teamleader.view.crm.GetCompaniesResponse;
import com.simbiosys.teamleader.view.crm.GetContactsResponse;
import com.simbiosys.teamleader.view.task.GetTasksResponse;
import com.simbiosys.teamleader.view.task.IncompleteTaskException;
import com.simbiosys.teamleader.view.task.IncompleteTaskListParamsException;
import com.simbiosys.teamleader.view.task.ListTaskTypes;
import com.simbiosys.teamleader.view.task.GetUserList;

//import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class RESTfulAPI extends AsyncTask<PostData, Void, Void> {

	// There were some problems with showing a progress dialog on an existing AsyncTask Thread.  So this solves that problem.
	public static boolean nestedCall = false;
	public static Stack<RESTfulAPI> callStack;

	ResponseHandler<?> responseHandler;
	AndroidHttpClient httpClient;
	private SharedPreferences applicationLevelPreferences;
	private String TEAM_LEADER_GROUP_from_preferences;
	private String TEAM_LEADER_API_KEY_from_preferences;
	ResponseHandler<?> tempResponseHandler;
	PostExecuteHandler postExecuteHandler;

	ProgressDialog progressDialog;

	boolean networking = true;

	//	public static final String TEAM_LEADER_GROUP = "3897";
	//	public static final String TEAM_LEADER_API_KEY = "XJmis8RBahZTE1cBSEqFpnPEOcUHg564EjdXiVpi2e1x53YOyfkNsZi713F8YB3tK7gSSv0LAS8wMXba2lOlbXi2QN1FfUYQR5yArpbR8aeLXfLQqq1rdakUOcpTXdzF9Y5qe78d7cOVhqCyGuQKu1u93KSQOimN7h3cf1fc3TYb9qzGLfg56B50bOHPXTsU0lX6d297";

	public RESTfulAPI(ResponseHandler<?> responseHandler, PostExecuteHandler postExecuteHandler, ProgressDialog progressDialog) {

		this.responseHandler = responseHandler;
		this.postExecuteHandler = postExecuteHandler;
		this.progressDialog = progressDialog;
		applicationLevelPreferences = Globals.application.getSharedPreferences("major",android.content.Context.MODE_PRIVATE);
		TEAM_LEADER_GROUP_from_preferences = applicationLevelPreferences.getString("group", "");
		TEAM_LEADER_API_KEY_from_preferences = applicationLevelPreferences.getString("apiKey","");

		if (TEAM_LEADER_GROUP_from_preferences.equals("") || TEAM_LEADER_API_KEY_from_preferences.equals("")) {
			//Log.v("TeamLeader", "Account Not Set up correctly");
			Toast.makeText(Globals.application, "Please set up your API Key in the Account Creation Activity", Toast.LENGTH_LONG).show();
		}

		if (callStack == null) {
			callStack = new Stack<RESTfulAPI>();
			callStack.push(this);
		} else callStack.push(this);

	}

	public boolean isNestedCall() {
		return(callStack.size() > 1);
	}

	@Override
	protected void onPreExecute() {


		try {
			// check the network before we send the command
			ConnectivityManager connMgr = (ConnectivityManager) Globals.application.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				// fetch data
			} else {
				networking = false;
				Globals.error = "No networking, not connected";
				Toast.makeText(Globals.application, "No networking, can't send information", Toast.LENGTH_LONG).show();
				//		        Intent i = new Intent(Globals.application, ErrorActivity.class);
				//		        Globals.application.startActivity(i);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Globals.error = "No networking, not connected";
			//	        Intent i = new Intent(Globals.application, ErrorActivity.class);
			//	        Globals.application.startActivity(i);
			//Log.v("TeamLeader", "bad network connectivity");
			networking = false;
			Toast.makeText(Globals.application, "No networking, can't send information", Toast.LENGTH_LONG).show();
		}

		if (!isNestedCall()) {
			progressDialog.show();
		}
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(PostData... postData) {
		if (networking == false) return (null);
		httpClient = AndroidHttpClient.newInstance("TeamLeaderAndroidHttpClient");
		//	        String baseURL = "https://www.teamleader.be/api/getContacts.php";
		//	       String url = baseURL + 
		//	    		   "?" + "api_group" + "=" + TEAM_LEADER_GROUP +
		//	    		   "&api_secret=" +TEAM_LEADER_API_KEY +
		//	    		   "&amount=" + "10" +
		//	    		   "&pageno=" + "0";
		//	       
		//	       String url2 = "https://www.teamleader.be/api/getContact.php" +
		//	    		   "?" + "api_group" + "=" + TEAM_LEADER_GROUP +
		//	    		   "&api_secret=" +TEAM_LEADER_API_KEY +
		//	    		   "&contact_id=" + "1444447";

		//Log.v("TeamLeader","sending URL: " + postData[0].url);
		System.out.println("sending URL: " + postData[0].url);

		HttpPost httpPost = new HttpPost();
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(postData[0].postParams));
		} catch (UnsupportedEncodingException e1) {

			e1.printStackTrace();
		}
		try {
			httpPost.setURI(new URI(postData[0].url));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		//	        httpPost.addHeader("api_group",TEAM_LEADER_GROUP);
		//	        httpPost.addHeader("contact_id",TEAM_LEADER_API_KEY);

		//	        httpPost.setEntity(entity);

		Header[] headers = httpPost.getAllHeaders();
		//Log.v("TeamLeader","headers:"+headers.length);	        
		for(Header header:headers) {
			String name = header.getName();
			String value = header.getValue();
			//Log.v("TeamLeader",name+":"+value);
		}



		try {
			httpClient.execute(httpPost, responseHandler);
		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (IOException e) {
			Globals.error = e.toString();
			Intent i = new Intent(Globals.application, ErrorActivity.class);
			Globals.application.startActivity(i);
			e.printStackTrace();
		} 
		if (httpClient != null) httpClient.close();

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		// replace any response handlers that have been changed
		responseHandler = tempResponseHandler;
		if (callStack.size() == 1) progressDialog.dismiss();
		callStack.pop();
		if (postExecuteHandler != null) {
			postExecuteHandler.postExecuteHandler();
		}

	}


	public ArrayList<Person> getContacts(String s, GetContactsResponse fillView) {
		String baseURL = "https://www.teamleader.be/api/getContacts.php";
		String amount = "100";
		String pageNo = "0";

		PostData postData = new PostData();
		postData.url = baseURL+"?"+ "api_group" + "=" + TEAM_LEADER_GROUP_from_preferences + 
				"&api_secret=" + TEAM_LEADER_API_KEY_from_preferences;

		ArrayList<Person> personList = new ArrayList<Person>();
		GetPeopleResponseHandler getContactsResponseHandler = new GetPeopleResponseHandler(personList, fillView);

		postData.postParams.add(new BasicNameValuePair("amount", amount));
		postData.postParams.add(new BasicNameValuePair("pageno",pageNo));
		postData.postParams.add(new BasicNameValuePair("searchby",s));

		// temporarily replace the response Handler
		//		ResponseHandler<?> tempResponseHandler = responseHandler;

		responseHandler = getContactsResponseHandler;

		this.execute(postData);


		return(personList);
	}

	public int addPerson(Person p) throws IncompletePersonException {
		PostData postData = new PostData();

		String baseURL = "https://www.teamleader.be/api/addContact.php";

		// add the parameters
		postData.url = baseURL + "?" + "api_group" + "=" + TEAM_LEADER_GROUP_from_preferences + 
				"&api_secret=" + TEAM_LEADER_API_KEY_from_preferences;

		HashMap<String, String> fields = p.getAPINamesValuesOnly();
		//		HashMap<String, String> fieldNamesToAPINames = p.getUINameToAPINameMap();

		// check to make sure we have the required fields:
		// required: forename, surname, email
		if (!p.hasEmailAddress() || !p.hasForename() || !p.hasSurname()) {
			throw new IncompletePersonException(p);
		}

		//		p.logMaps();
		fields.toString();

		// if we made it here, we're ok all of the fields should be there.
		for(String key:fields.keySet()) {
			if (fields.get(key) != null && !fields.get(key).equals("") && key != null) {
				//Log.v("TeamLeader", "adding post param: " + key +  "," + fields.get(key));
				postData.postParams.add(new BasicNameValuePair(key, fields.get(key)));
			}
		}

		//		RESTfulAPI restfulAPI = new RESTfulAPI(responseHandler);
		this.execute(postData);

		return(0);
	}

	public int addCompany(Company p) throws IncompleteCompanyException {
		PostData postData = new PostData();

		String baseURL = "https://www.teamleader.be/api/addCompany.php";

		// add the parameters
		postData.url = baseURL + "?" + "api_group" + "=" + TEAM_LEADER_GROUP_from_preferences + 
				"&api_secret=" + TEAM_LEADER_API_KEY_from_preferences;

		HashMap<String, String> fields = p.getAPINamesValuesOnly();

		// check to make sure we have the required fields:
		// required: name
		if (!p.hasName()) {
			throw new IncompleteCompanyException(p);
		}

		p.logMaps();

		// if we made it here, we're ok all of the fields should be there.
		for(String key:fields.keySet()) {
			if (fields.get(key) != null && !fields.get(key).equals("") && key != null) {
				//Log.v("TeamLeader", "Adding post parameter: " + fields.get(key));
				postData.postParams.add(new BasicNameValuePair(key, fields.get(key)));
			}
		}

		//		RESTfulAPI restfulAPI = new RESTfulAPI(responseHandler);
		this.execute(postData);

		return(0);
	}

	public ArrayList<Company> getCompanies(String searchString, GetCompaniesResponse fillView) {

		String baseURL = "https://www.teamleader.be/api/getCompanies.php";
		String amount = "100";
		String pageNo = "0";

		PostData postData = new PostData();
		postData.url = baseURL+"?"+ "api_group" + "=" + TEAM_LEADER_GROUP_from_preferences + 
				"&api_secret=" + TEAM_LEADER_API_KEY_from_preferences;

		ArrayList<Company> companyList = new ArrayList<Company>();
		GetCompaniesResponseHandler getCompaniesResponseHandler = new GetCompaniesResponseHandler(companyList, fillView);

		postData.postParams.add(new BasicNameValuePair("amount", amount));
		postData.postParams.add(new BasicNameValuePair("pageno",pageNo));
		postData.postParams.add(new BasicNameValuePair("searchby", searchString));

		// temporarily replace the response Handler
		tempResponseHandler = responseHandler;

		responseHandler = getCompaniesResponseHandler;

		this.execute(postData);

		return(companyList);
	}

	public void linkPersonToCompany(Person p, Company c) {

		String baseURL = "https://www.teamleader.be/api/linkContactToCompany.php";

		PostData postData = new PostData();
		postData.url = baseURL+"?"+ "api_group" + "=" + TEAM_LEADER_GROUP_from_preferences + 
				"&api_secret=" + TEAM_LEADER_API_KEY_from_preferences;
		String contact_id = Integer.valueOf(p.getId()).toString();
		//Log.v("TeamLeader", "adding PostParameter: contact_id," + contact_id);
		String company_id = Integer.valueOf(c.getId()).toString();
		//Log.v("TeamLeader", "adding PostParameter: company_id," + company_id);
		//Log.v("TeamLeader", "adding PostParameter: mode," +"link");

		postData.postParams.add(new BasicNameValuePair("contact_id", contact_id));
		postData.postParams.add(new BasicNameValuePair("company_id", company_id));
		postData.postParams.add(new BasicNameValuePair("mode","link"));

		this.execute(postData);
	}

	public void linkCompanyToPerson(Company c, Person p) {

		String baseURL = "https://www.teamleader.be/api/linkContactToCompany.php";

		PostData postData = new PostData();
		postData.url = baseURL+"?"+ "api_group" + "=" + TEAM_LEADER_GROUP_from_preferences + 
				"&api_secret=" + TEAM_LEADER_API_KEY_from_preferences;
		String contact_id = Integer.valueOf(p.getId()).toString();
		//Log.v("TeamLeader", "adding PostParameter: contact_id," + contact_id);
		String company_id = Integer.valueOf(c.getId()).toString();
		//Log.v("TeamLeader", "adding PostParameter: company_id," + company_id);
		//Log.v("TeamLeader", "adding PostParameter: mode," +"link");

		postData.postParams.add(new BasicNameValuePair("contact_id", contact_id));
		postData.postParams.add(new BasicNameValuePair("company_id", company_id));
		postData.postParams.add(new BasicNameValuePair("mode","link"));

		this.execute(postData);
	}

	public void addTask(Task task) throws IncompleteTaskException {

		PostData postData = new PostData();

		String baseURL = "https://www.teamleader.be/api/addTimetracking.php";

		// add the parameters
		postData.url = baseURL + "?" + "api_group" + "=" + TEAM_LEADER_GROUP_from_preferences + 
				"&api_secret=" + TEAM_LEADER_API_KEY_from_preferences;

		HashMap<String, String> fields = task.getAPINamesValuesOnly();

		// check to make sure we have the required fields:
		// required: name
		if (!task.hasDescription() || !task.hasStart()
				|| !task.hasEnd() || !task.hasWorker() || !task.hasTaskType()
				|| !task.hasForId()
				|| !task.hasBillTo() ) {
			throw new IncompleteTaskException(task);
		}

		// if we made it here, we're ok all of the fields should be there.
		for(String key:fields.keySet()) {
			if (fields.get(key) != null && !fields.get(key).equals("") && key != null) {
				//Log.v("TeamLeader", "Adding post parameter: " + key + ":" + fields.get(key));
				postData.postParams.add(new BasicNameValuePair(key, fields.get(key)));
			}
		}

		//		RESTfulAPI restfulAPI = new RESTfulAPI(responseHandler);
		this.execute(postData);

	}

	public ArrayList<TaskType> getTaskTypes(String searchString,
			ListTaskTypes listTaskTypes) {

		String baseURL = "https://www.teamleader.be/api/getTaskTypes.php";

		PostData postData = new PostData();
		postData.url = baseURL+"?"+ "api_group" + "=" + TEAM_LEADER_GROUP_from_preferences + 
				"&api_secret=" + TEAM_LEADER_API_KEY_from_preferences;

		ArrayList<TaskType> taskTypeList = new ArrayList<TaskType>();
		GetTaskTypesResponseHandler getTaskTypesResponseHandler = new GetTaskTypesResponseHandler(taskTypeList, listTaskTypes);

		// temporarily replace the response Handler
		tempResponseHandler = responseHandler;

		responseHandler = getTaskTypesResponseHandler;

		this.execute(postData);

		return(taskTypeList);
	}

	public ArrayList<Person> getUsers(GetUserList fillView) {
		String baseURL = "https://www.teamleader.be/api/getUsers.php";

		PostData postData = new PostData();
		postData.url = baseURL+"?"+ "api_group" + "=" + TEAM_LEADER_GROUP_from_preferences + 
				"&api_secret=" + TEAM_LEADER_API_KEY_from_preferences;

		ArrayList<Person> personList = new ArrayList<Person>();
		GetUserResponseHandler getContactsResponseHandler = new GetUserResponseHandler(personList, fillView);

		// temporarily replace the response Handler
		//		ResponseHandler<?> tempResponseHandler = responseHandler;

		responseHandler = getContactsResponseHandler;

		this.execute(postData);

		return(personList);

	}

	public ArrayList<Task> listTasks(TaskListParamsObject taskListParams, GetTasksResponse taskResponseHandler) throws IncompleteTaskListParamsException {
		String baseURL = "https://www.teamleader.be/api/getTimetracking.php";

		PostData postData = new PostData();
		postData.url = baseURL+"?"+ "api_group" + "=" + TEAM_LEADER_GROUP_from_preferences + 
				"&api_secret=" + TEAM_LEADER_API_KEY_from_preferences;

		ArrayList<Task> taskList = new ArrayList<Task>();
		GetTasksResponseHandler getTasksResponseHandler = new GetTasksResponseHandler(taskList, taskResponseHandler);

		// temporarily replace the response Handler
		tempResponseHandler = responseHandler;

		responseHandler = getTasksResponseHandler;

		HashMap<String, String> fields = taskListParams.getAPINamesValuesOnly();

		// check to make sure we have the required fields:
		// required: name
		if (!taskListParams.hasStart() || !taskListParams.hasEnd() ) {
			throw new IncompleteTaskListParamsException(taskListParams);
		}

		// if we made it here, we're ok all of the fields should be there.
		for(String key:fields.keySet()) {
			if (fields.get(key) != null && !fields.get(key).equals("") && key != null) {
				//Log.v("TeamLeader", "Adding post parameter: " + key + ":" + fields.get(key));
				postData.postParams.add(new BasicNameValuePair(key, fields.get(key)));
			}
		}

		this.execute(postData);

		return(taskList);

	}

	public void creatInvoice(ArrayList<Task> taskList, Department d, Company company) {
		String baseURL = "https://www.teamleader.be/api/addInvoice.php";

		PostData postData = new PostData();
		postData.url = baseURL+"?"+ "api_group" + "=" + TEAM_LEADER_GROUP_from_preferences + 
				"&api_secret=" + TEAM_LEADER_API_KEY_from_preferences;

		postData.postParams.add(new BasicNameValuePair("sys_department_id", Integer.valueOf(d.getId()).toString()));
		postData.postParams.add(new BasicNameValuePair("contact_or_company", "company"));
		postData.postParams.add(new BasicNameValuePair("contact_or_company_id", Integer.valueOf(company.getId()).toString()));

		//		double hourlyCost;
		double amount;
		int duration;

		for (int i = 0; i < taskList.size(); i++) {
			postData.postParams.add(new BasicNameValuePair("description_"+(i+1), taskList.get(i).getDescription()));
			postData.postParams.add(new BasicNameValuePair("price_"+(i+1), taskList.get(i).get("hourlyCost")));
			// I'm pretty sure duration is in minutes
			duration = taskList.get(i).getDuration()/60;
			//			hourlyCost = taskList.get(i).getHourlyCost();
			amount = duration;
			postData.postParams.add(new BasicNameValuePair("amount_"+(i+1), Double.valueOf(amount).toString()));
			postData.postParams.add(new BasicNameValuePair("vat_"+(i+1), "00"));
		}
		//Log.v("TeamLeader", "Added post parameters: " + postData.postParams.toString());

		this.execute(postData);

	}

	public void listDepartments(ListResponse<Department> listDepartmentResponse) {
		String baseURL = "https://www.teamleader.be/api/getDepartments.php";

		PostData postData = new PostData();
		postData.url = baseURL+"?"+ "api_group" + "=" + TEAM_LEADER_GROUP_from_preferences + 
				"&api_secret=" + TEAM_LEADER_API_KEY_from_preferences;

		// temporarily replace the response Handler
		tempResponseHandler = responseHandler;

		GetDepartmentsResponseHandler getDeparmentsHandler = new GetDepartmentsResponseHandler(null, listDepartmentResponse);

		// temporarily replace the response Handler
		tempResponseHandler = responseHandler;

		responseHandler = getDeparmentsHandler;

		this.execute(postData);

	}

	public void getInvoicePDF(int invoiceId) {

		String baseURL = "https://www.teamleader.be/api/downloadInvoicePDF.php";

		PostData postData = new PostData();
		postData.url = baseURL+"?"+ "api_group" + "=" + TEAM_LEADER_GROUP_from_preferences + 
				"&api_secret=" + TEAM_LEADER_API_KEY_from_preferences;

		postData.postParams.add(new BasicNameValuePair("invoice_id", Integer.valueOf(invoiceId).toString()));

		this.execute(postData);

	}


}
