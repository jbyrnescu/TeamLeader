// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

import android.util.Log;

public class Task extends CheckableRESTGUIObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4705307955229492526L;

	protected static String[][] commonNameToAPINameTask = { 
		{"description","description"}, 
		{"start","start_date"},
		{"end", "end_date"},
		{"workerId", "worker_id"},
		{"taskTypeId", "task_type_id"},
		{"duration", "duration"},
		{"priority", "priority"},
		{"billTo", "for"},
		{"for_id", "for_id"},
		{"type", "type"}
	};

	// required
	private String description;
	// seconds since Jan 1, 1970
//	private int dueDate;
	private long start=0;
	private String startFormatted;
	private long end=0;
	private String endFormatted;
	private int workerId=0;
	private Person workerPerson;
	private int taskTypeId=0;
	private int duration;
	private int priority=0;

	// optional
	// contact, company, or project_milestone (We're not doing project milestones though)
	private String billTo;
	private int forId=0;
	
	// not part of the API, but used in 
	private Company billToCompany;
	private Person billToPerson;
	
	private TaskType taskType;
	private String team;
	private String type;
	private String contactId;
	private String contactName;
	private String companyId;
	private String companyName;
	private String projectId;
	private String projectTitle;
	private String milestoneId;
	private String milestoneTitle;
	private double hourlyCostDouble;
	private String hourlyCost;
	
	// This is to store the UI state
	private boolean toInvoice = false;

	// This is to store the UI state
	public boolean getToInvoice() {
		return toInvoice;
	}
	
	// This is to store the UI state
	public void setToInvoice(boolean toInvoice) {
		this.toInvoice = toInvoice;
	}
	
	public TaskType getTaskType() {
		return taskType;
	}

	public Task() {
		super("EnterTask.xml");
	}
	
	public String get(String key) {
		return(values.get(key));
	}
	
	public LinkedHashMap<String, String> getValues() {
		return values;
	}
	public String getDescription() {
		return description;
	}
//	public int getDueDate() {
//		return dueDate;
//	}
	public int getWorkerId() {
		return workerId;
	}
	public int getTaskTypeId() {
		return taskTypeId;
	}
//	public int getDuration() {
//		return duration;
//	}
	public int getPriority() {
		return priority;
	}
	public String getBillTo() {
		return billTo;
	}
	public int getForId() {
		return forId;
	}
	
	public Company getBillToCompany() {
		return(billToCompany);
	}
	
	public Person getBillToPerson() {
		return(billToPerson);
	}

	public void setDescription(String description) {
		this.description = description;
		values.put( "description", description);
		String UIName = "Description";
		values.put(UIName, description);
	}
//	public void setDueDate(Date dueDate) {
//		this.dueDate = dueDate;
//		// time in seconds since Jan 1, 1970
//		values.put("due_date", Integer.valueOf((int)(dueDate.getTime()/1000)).toString());
//	}
//	public void setDueDate(int dueDate) {
////		String commonName = "dueDate";
//		values.put("due_date", Integer.valueOf(dueDate).toString());
//		this.dueDate = dueDate;
//		String UIName = "Due Date";
//		values.put(UIName, Integer.valueOf(dueDate).toString());
//	}
//	public void setDueDate(String dueDate) {
//		// set the due date with a string here.
//		values.put("due_date", dueDate);
//		String UIName = "Due Date";
//		values.put(UIName, dueDate);
//		setDueDate(Integer.valueOf(dueDate));
//	}
	public void setWorkerId(int workerId) {
		values.put("worker_id", Integer.valueOf(workerId).toString());
		String UIName = "Contact";
		values.put(UIName, Integer.valueOf(workerId).toString());
		this.workerId = workerId;
	}
	public void setWorkerId(String workerId) {
		setWorkerId(Integer.valueOf(workerId));
	}

	public void setTaskType(TaskType taskType) {
		setTaskTypeId(taskType.number);
		this.taskType = taskType;
	}
	
	public void setTaskTypeId(int taskTypeId) {
		this.taskTypeId = taskTypeId;
		String UIName = "Task Type";
		values.put(UIName, Integer.valueOf(taskTypeId).toString());
		values.put("task_type_id", Integer.valueOf(taskTypeId).toString());
	}
	
	public void setTaskTypeId(String taskTypeId) {
		setTaskTypeId(Integer.valueOf(taskTypeId));
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
		String UIName = "Duration";
		values.put(UIName, Integer.valueOf(duration).toString());

		values.put("duration", Integer.valueOf(duration).toString());
	}
	
	public void setDuration(String duration) {
		String UIName = "Duration";
		values.put(UIName, duration);
		this.duration = Integer.valueOf(duration);
		values.put("duration", duration);
	}
	
	public void setPriority(int priority) {
		String UIName = "Priority";
		values.put(UIName, Integer.valueOf(priority).toString());
		values.put("priority", Integer.valueOf(priority).toString());
		this.priority = priority;
	}
	public void setPriority(String priority) {
		String UIName = "Priority";
		values.put(UIName, priority);

		values.put("priority", priority);
		this.priority = Integer.valueOf(priority);
	}
	public void setBillTo(String billTo) {
		String UIName = "Bill To";
		values.put(UIName, billTo);
		values.put("for", billTo);
		this.billTo = billTo;
	}
	
	public void setForId(int forId) {
		values.put("for_id", Integer.valueOf(forId).toString());
		this.forId = forId;
	}
	
	public void setForId(String forId) {
		this.forId = Integer.valueOf(forId);
		values.put("for_id", forId);
	}
	
	public void setBillToCompany(Company c) {
		this.billToCompany = c;
		setBillTo("company");
		setForId(c.getId());
	}
	
	public void setBillToPerson(Person p) {
		this.billToPerson = p;
		setBillTo("contact");
		setForId(p.getId());
	}
	
	public void setWorkerPerson(Person p) {
		workerPerson = p;
		setWorkerId(p.getId());
	}
	
	public void setStartDate(long start) {
		String UIName = "Start Date/Time";
		String startString = Long.valueOf(start).toString();
		values.put(UIName, startString);
		values.put("start", startString);		
		this.start = start;
		Date date1 = new Date(start*1000);
		//Log.v("TeamLeader", "date entered: seconds since epoch: " + start);
		//			DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
		DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yy", Locale.US);
		startFormatted = dateFormat1.format(date1);
	}
	
	public void setStartDate(String start) {
		setStartDate(Integer.valueOf(start));
	}

	public void setEndDate(long end) {
		String UIName = "End Date/Time";
		String endString = Long.valueOf(end).toString();
		values.put(UIName, endString);
		values.put("end", endString);
		this.end = end;
		Date date1 = new Date(end*1000);
		//Log.v("TeamLeader", "date entered: seconds since epoch: " + end);
		//			DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
		DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yy", Locale.US);
		endFormatted = dateFormat1.format(date1);
	}
	
	public void setEndDate(String end) {
		setEndDate(Integer.valueOf(end));
	}

	public boolean hasDescription() {
		return(description != null && !description.equals(""));
	}

	public boolean hasStart() {
		return (start!=0);
	}

	public boolean hasEnd() {
		return(end!=0);
	}

	public boolean hasWorker() {
		return(workerId!=0);
	}

	public boolean hasTaskType() {
		return(taskTypeId!=0);
	}

	public boolean hasBillTo() {
		boolean hasForIdBool = hasForId();
		boolean hasBillToString = billTo != null;
		boolean billToNotEmpty = true;
		if (hasBillToString) billToNotEmpty = !billTo.equals("");
			
		return(billToNotEmpty && hasForIdBool);
	}
	
	public boolean hasForId() {
		return(forId!=0 && billTo!=null);
	}

	@Override
	public void setCommonNameToAPIName() {
		commonNameToAPIName = commonNameToAPINameTask;
	}

	public void setTeam(String string) {
		values.put("team", string);
		team = string;
	}

	public void setDateFormatted(String string) {
		StringBuffer stringBuffer = new StringBuffer(string);
		if (string.contains("\\")) {
			int where;
			do {
				where = stringBuffer.indexOf("\\");
				stringBuffer.deleteCharAt(where);
			} while (where != -1);
		}
		this.startFormatted = stringBuffer.toString();
		// remove all '\' keys
		values.put("startFormatted", stringBuffer.toString());
	}

	public void setType(String type) {
		this.type = type;
		values.put("type",type);
	}

	public void setContactId(String string) {
		this.contactId = string;
		values.put("contactId", string);
		
	}

	public void setContactName(String string) {
		this.contactName = string;
		values.put("contactName", string);
	}

	public void setCompanyId(String string) {
		this.companyId = string;
		values.put("companyId", string);
		
	}

	public void setCompanyName(String string) {
		this.companyName = string;
		values.put("companyName", string);
		
	}

	public void setProjectId(String string) {
		this.projectId = string;
		values.put("projectId",string);
		
	}

	public void setProjectTitle(String string) {
		this.projectTitle = string;
		values.put("projectTitle", string);
		
	}

	public void setMilestoneId(String string) {
		this.milestoneId = string;
		values.put("milestoneId", string);
		
	}

	public void setMilestoneTitle(String string) {
		this.milestoneTitle = string;
		values.put("milestoneTitle", string);
		
	}

	public void setHourlyCost(String string) {
		this.hourlyCost = string;
		values.put("hourlyCost", string);
		this.hourlyCostDouble = Double.valueOf(string);
	}
	
	public String getStartFormatted() {
		return startFormatted;
	}
	
	public String getEndFormatted() {
		return endFormatted;
	}
	
	public Person getWorkerPerson() {
		return workerPerson;
	}

	public String getContactId() {
		return contactId;
	}

	public int getDuration() {
		return duration;
	}

	public String getTeam() {
		return team;
	}

	public String getType() {
		return type;
	}

	public String getContactName() {
		return contactName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getProjectId() {
		return projectId;
	}

	public String getProjectTitle() {
		return projectTitle;
	}

	public String getMilestoneId() {
		return milestoneId;
	}

	public String getMilestoneTitle() {
		return milestoneTitle;
	}

	public double getHourlyCost() {
		return hourlyCostDouble;
	}
	
	public String getHourlyCostString() {
		return hourlyCost;
	}
	
	public long getStartDate() {
		return start;
	}

	public long getEndDate() {
		return end;
	}

//	public void setStartFormatted(String startFormatted) {
//		this.startFormatted = startFormatted;
//	}
//
//	public void setEndFormatted(String endFormatted) {
//		this.endFormatted = endFormatted;
//	}

	@Override
	public boolean isChecked() {
		return(toInvoice);
	}

	@Override
	public void setChecked(boolean checked) {
		toInvoice = checked;		
	}
	
}
