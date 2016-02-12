// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.model;

import java.io.Serializable;

public class Department extends RESTGUIObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4215606544801552267L;
	
	private static String[][] commonNameToAPINameDepartment = { { "id", "id" }, {"name", "name"} };
	
	int id;
	String name;

	public Department() {
		super("Department.xml");
	}

	@Override
	public void setCommonNameToAPIName() {
		commonNameToAPIName = commonNameToAPINameDepartment;
	}
	
	public void setId(String id) {
		values.put("id", id);
		this.id = Integer.valueOf(id);
	}
	
	public void setName(String name) {
		values.put("name", name);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int int1) {
		values.put("id", Integer.valueOf(int1).toString());
		this.id = int1;
	}

}
