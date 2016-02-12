package com.simbiosys.teamleader.restAPI;

import com.simbiosys.teamleader.model.Company;

public class IncompleteCompanyException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6209879292550696731L;
	Company c;
	
	public IncompleteCompanyException(Company c) {
		this.c = c;
	}

}
