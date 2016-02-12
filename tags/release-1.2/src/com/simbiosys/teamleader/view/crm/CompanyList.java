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
import com.simbiosys.teamleader.model.Company;
import com.simbiosys.teamleader.model.Contact;
import com.simbiosys.teamleader.restAPI.PostExecuteHandler;
import com.simbiosys.teamleader.restAPI.RESTfulAPI;
import com.simbiosys.teamleader.view.crm.ListCompanyListAdapter.ViewHolder;

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

public class CompanyList extends Activity implements ResponseHandler<ArrayList<Company>>, GetCompaniesResponse, PostExecuteHandler,
OnLongClickListener, OnClickListener {

	ListView lv;
	ArrayList<Company> companyList;
	ProgressDialog progressDialog;


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt("numCompanies", companyList.size());
		for (int i = 0; i < companyList.size(); i++) {
			outState.putSerializable("company" + i, companyList.get(i));
		}

	}

	public void restoreState(Bundle state) {
		int numCompanies = state.getInt("numCompanies");
		companyList = new ArrayList<Company>(numCompanies);
		for (int i = 0; i < numCompanies; i++) {
			companyList.add((Company)state.getSerializable("company"+i));
		}
		postExecuteHandler();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_list);
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
			EditText searchStringEditText = (EditText)findViewById(R.id.editText1);
			
			String companyToSearchFor = i.getStringExtra("searchForCompany");
			if (companyToSearchFor != null) {
				searchStringEditText.setText(companyToSearchFor);
				// call the search button automatically pressed
				Button b = (Button)findViewById(R.id.button1);
				listCompanies(b);
			} else {
				searchStringEditText.setText("");
				Button b = (Button)findViewById(R.id.button1);
				listCompanies(b);
			}
			
		}
	}

	public void listCompanies(View view) {
		EditText searchStringEditText = (EditText)findViewById(R.id.editText1);
		String searchString = searchStringEditText.getText().toString();

		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("Fetching data from Server");
		progressDialog.setCancelable(true);

		RESTfulAPI restfulAPI = new RESTfulAPI(this, this, progressDialog);
		companyList = restfulAPI.getCompanies(searchString, this);

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
		ListCompanyListAdapter lcla = new ListCompanyListAdapter(this, companyList);
		lv = (ListView)findViewById(R.id.listView1);
		lv.setAdapter(lcla);
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
	public void getCompaniesResponse(ArrayList<Company> companyList) {

		for(Contact c:companyList) {
			Log.v("TeamLeader",c.toString());
		}

		// pass this company list on this this class to use it later on a different thread (The UI thread)
		this.companyList = companyList;

	}

	@Override
	public boolean onLongClick(View arg0) {
		Log.v("TeamLeader", "LongClick registered... We can display the company now");
		Company c = ((ViewHolder)arg0.getTag()).company;
		c.toString();
		Intent i = new Intent(this, EnterCompany.class);
		i.putExtra("company", c);
		startActivityForResult(i, Globals.GET_COMPANY_ID);
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// send this data back to the calling activity and cancel this activity
		super.onActivityResult(requestCode, resultCode, data);
		finishActivity(Globals.GET_COMPANY_ID);		
	}

	@Override
	public void onClick(View arg0) {
		// send the company back to the calling Activity
		LinearLayout linearLayout = (LinearLayout)arg0;
		Company c = ((ViewHolder)linearLayout.getTag()).company;
		Intent resultIntent = new Intent();
		resultIntent.putExtra("company", c);
		setResult(RESULT_OK, resultIntent);
		finish();

	}
}
