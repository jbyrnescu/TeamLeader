// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.restAPI;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.simbiosys.teamleader.model.Company;
import com.simbiosys.teamleader.view.crm.GetCompaniesResponse;

public class GetCompaniesResponseHandler implements ResponseHandler<ArrayList<Company>> {
	
	ArrayList<Company> companyList;
	GetCompaniesResponse fillView;
	
	public GetCompaniesResponseHandler(ArrayList<Company> companyList, GetCompaniesResponse fillView2) {
		this.companyList = companyList;
		this.fillView = fillView2;
	}

	public ArrayList<Company> handleResponse(HttpResponse response) throws IOException {
		// parse the JSON and put the data in the arrayList
		HttpEntity httpEntity = response.getEntity();
//		InputStream inputStream = httpEntity.getContent();

		String jsonStr = EntityUtils.toString(httpEntity);
		LineNumberReader lnr = new LineNumberReader(new StringReader(jsonStr));
		String currentString;
		while((currentString = lnr.readLine()) != null) {
			//Log.v("TeamLeader", "httpResponse:" + currentString);
		}
		
		try {
			JSONArray jsonArray = new JSONArray(jsonStr);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jSonP = jsonArray.getJSONObject(i);	
				Company company = new Company();
				company.setId(jSonP.getInt("id"));
				company.setCity(jSonP.getString("city"));
				company.setName(jSonP.getString("name"));
				company.setEmailAddress(jSonP.getString("email"));
				company.setTelephoneNumber(jSonP.getString("telephone"));
				company.setCountry(jSonP.getString("country"));
				company.setZipCode(jSonP.getString("zipcode"));
				company.setStreet(jSonP.getString("street"));
				company.setNumber(jSonP.getString("number"));
//				company.setLanguage(jSonP.getString("language"));
				companyList.add(company);
			}
			
		} catch (JSONException e) {
			if (e.getMessage().contains("No value for")) {
			}
			e.printStackTrace();
		}
		
		fillView.getCompaniesResponse(companyList);
		
		return companyList;
		
	}
}
