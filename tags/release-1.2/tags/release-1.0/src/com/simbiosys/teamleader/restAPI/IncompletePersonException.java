// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.restAPI;

import com.simbiosys.teamleader.model.Person;

public class IncompletePersonException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public IncompletePersonException(Person p) {
		this.p = p;
	}
	
	Person p;

}
