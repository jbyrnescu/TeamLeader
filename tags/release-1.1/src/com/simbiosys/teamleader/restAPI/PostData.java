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
