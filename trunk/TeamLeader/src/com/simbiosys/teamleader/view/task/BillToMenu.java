// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.view.task;

import com.simbiosys.teamleader.Globals;
import com.simbiosys.teamleader.R;
import com.simbiosys.teamleader.view.crm.CompanyList;
import com.simbiosys.teamleader.view.crm.PeopleList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BillToMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill_to_menu);
	}
	
	public void listContacts(View view) {
		Intent i = new Intent(this, PeopleList.class);
		startActivityForResult(i, Globals.GET_BILL_TO_CONTACT_ID);
	}
	
	public void listCompanies(View view) {
		Intent i = new Intent(this, CompanyList.class);
		startActivityForResult(i, Globals.GET_BILL_TO_COMPANY_ID);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		Intent resultIntent = null;
		switch(requestCode) {
		case Globals.GET_BILL_TO_CONTACT_ID:
//			Person p = (Person) data.getSerializableExtra("person");
//			resultIntent = new Intent();
//			resultIntent.putExtra("person", p);
			setResult(Globals.GET_BILL_TO_CONTACT_ID, data);
			finish();
			break;
		case Globals.GET_BILL_TO_COMPANY_ID:
//			Company c = (Company) data.getSerializableExtra("company");
//			resultIntent = new Intent();
//			resultIntent.putExtra("company", resultIntent);
			setResult(Globals.GET_BILL_TO_COMPANY_ID, data);
			finish();
			break;
		}
	}
}
