// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader;

import android.app.Application;


public class Globals extends Application {
	
	public static String error;	
	
	// request codes
	public static final int GET_BILL_TO = 0;
	public static final int GET_PERSON_ID = 1;
	public static final int GET_COMPANY_ID = 2;
	public static final int GET_START_TIME_POINT = 7;
	public static final int GET_END_TIME_POINT = 8;
	public static final int GET_TASK_TYPE_ID = 9;
	public static final int GET_COMPANY = 10;
	public static final int GET_DEPARTMENT = 11;
	
	// result codes	
	public static final int GET_BILL_TO_COMPANY_ID = 3;
	public static final int GET_WORKER_PERSON_ID = 4;
	public static final int GET_BILL_TO_CONTACT_ID = 5;
	public static final int RETURN_TIME_POINT = 6;



	public static Application application;
}
