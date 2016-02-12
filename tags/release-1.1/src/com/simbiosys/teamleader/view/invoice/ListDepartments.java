package com.simbiosys.teamleader.view.invoice;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import com.simbiosys.teamleader.R;
import com.simbiosys.teamleader.model.Department;
import com.simbiosys.teamleader.restAPI.ListResponse;
import com.simbiosys.teamleader.restAPI.PostExecuteHandler;
import com.simbiosys.teamleader.restAPI.RESTfulAPI;
import com.simbiosys.teamleader.view.task.ListRESTObjectListAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class ListDepartments extends Activity 
implements ResponseHandler<Department>, ListResponse<Department>, PostExecuteHandler, OnClickListener {
	
	ProgressDialog progressDialog;
	ListView listView;
	
	String[] fieldsToShow = { "id", "name" };
	
	ArrayList<Department> departments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_departments);
		
		listView = (ListView)findViewById(R.id.listView1);
		
		
		// send the instruction to add the person with the TeamLeader API
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("Communicating with server");
		progressDialog.setCancelable(true);
		
		RESTfulAPI restfulAPI = new RESTfulAPI(this, this, progressDialog);
		restfulAPI.listDepartments(this);
	}

	@Override
	public Department handleResponse(HttpResponse arg0)
			throws ClientProtocolException, IOException {
		// nothing to do here.
		return null;
	}

	public void setList(ArrayList<Department> departments) {
		this.departments = departments;
	}

	@Override
	public void postExecuteHandler() {
		// Here's where we fill in and draw the list
		ListRESTObjectListAdapter<Department> listAdapter = 
				new ListRESTObjectListAdapter<Department>(this, departments, fieldsToShow);
		listView.setAdapter(listAdapter);
	}

	@Override
	public void onClick(View arg0) {
		@SuppressWarnings("unchecked")
		ListRESTObjectListAdapter<Department>.ViewHolder viewHolder = (ListRESTObjectListAdapter<Department>.ViewHolder) arg0.getTag();
		Department department = (Department)(viewHolder.o);
		Intent i = getIntent();
		i.putExtra("department", department);
		setResult(RESULT_OK, i);
		finish();
	}
}
