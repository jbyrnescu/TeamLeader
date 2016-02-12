package com.simbiosys.teamleader.model;

import java.io.Serializable;

public class TaskType extends RESTGUIObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8175430545103909809L;

	protected static String[][] commonNameToAPINameTaskType = { 
		{"name", "name"} 
	};
	
	public static String resourceXML = "EnterTaskType.xml";
	
	protected int number;
	protected String name;
	
	public TaskType() {
		super(resourceXML);

	}
	
	public int getNumber() {
		return number;
	}
	
//	public String getNumber() {
//		return (Integer.valueOf(number).toString());
//	}
	
	public void setNumber(int number) {
		values.put("number", Integer.valueOf(number).toString());
		String UIName = "Number";
		values.put(UIName, Integer.valueOf(number).toString());
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		String UIName = "Name";
		values.put(UIName, name);
		values.put("name", name);
		this.name = name;
		this.name = name;
	}

	@Override
	public void setCommonNameToAPIName() {
		commonNameToAPIName = commonNameToAPINameTaskType;
		
	}

}
