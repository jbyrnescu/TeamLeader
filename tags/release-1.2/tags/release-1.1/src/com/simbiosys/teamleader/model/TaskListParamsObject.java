package com.simbiosys.teamleader.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TaskListParamsObject extends RESTGUIObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1060471227514193620L;
	
	String startDate;
	long startTimeInSeconds=0;
	String endDate;
	long endTimeInSeconds = 0;
	String contactOrCompanyId;
	Company company;
	public Company getCompany() {
		return company;
	}

	String companyIdString;
	String contactOrCompany;
	// This isn't implemented for the first version
	Contact contact;
	
	static private String[][] commonNameToAPINameTaskParams = { 
		{"startDate","date_from"},
		{"endDate", "date_to"},
		{"contactOrCompany", "contact_or_company"},
		{"contactOrCompanyId","contact_or_company_id"}
	};

	public TaskListParamsObject() {
		super("TaskListParams.xml");
	}

	@Override
	public void setCommonNameToAPIName() {
		commonNameToAPIName = commonNameToAPINameTaskParams;
	}
	
	public void setStartDate(String date) {
		values.put("startDate", date);
		this.startDate = date;
	}
	
	public void setStartDate(long timeInSeconds) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(timeInSeconds*1000);
		Date date = new Date(timeInSeconds*1000);
		startTimeInSeconds = timeInSeconds;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		startDate = simpleDateFormat.format(date);
		values.put("startDate", startDate);
	}
	
	public void setEndDate(String date) {
		values.put("endDate", date);
		this.endDate = date;
	}
	
	public void setEndDate(long timeInSeconds) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(timeInSeconds*1000);
		Date date = new Date(timeInSeconds*1000);
		endTimeInSeconds = timeInSeconds;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		endDate = simpleDateFormat.format(date);
		values.put("endDate", endDate);
	}
	
	public void setCompany(Company c) {
		this.company = c;
		this.companyIdString = Integer.valueOf(c.id).toString();
		values.put("contactOrCompanyId", Integer.valueOf(c.id).toString());
		this.contactOrCompany = "company";
		values.put("contactOrCompany", "company");
	}

	public boolean hasStart() {
		return( startDate!= null && !startDate.equals("") );
	}
	
	public boolean hasEnd() {
		return( endDate != null && !endDate.equals(""));
	}

}
