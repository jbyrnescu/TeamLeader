// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.restAPI;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

public class PostData {

		String url;
		ArrayList<NameValuePair> postParams;
		
		public PostData() {
			postParams = new ArrayList<NameValuePair>();
		}

}
