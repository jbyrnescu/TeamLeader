// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.view.crm;

import com.simbiosys.teamleader.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CRMMainMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crmmain_menu);
	}
	
	public void launchEnterPerson(View view) {
		Intent i = new Intent(this, EnterPerson.class);
		startActivity(i);
	}
	
	public void listPeople(View view) {
		Intent i = new Intent(this, PeopleList.class);
		startActivity(i);
	}
	
	public void launchAddCompany(View view) {
		Intent i = new Intent(this, EnterCompany.class);
		startActivity(i);
	}
	
	public void launchListCompanies(View view) {
		Intent i = new Intent(this, CompanyList.class);
		startActivity(i);
	}
	
}
