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

import com.simbiosys.teamleader.model.Person;
import com.simbiosys.teamleader.view.task.GetUsersResponse;

public class GetUserResponseHandler implements ResponseHandler<ArrayList<Person>> {

	ArrayList<Person> personList;
	GetUsersResponse fillView;

	public GetUserResponseHandler(ArrayList<Person> personList, GetUsersResponse fillView) {
		if (personList == null) personList = new ArrayList<Person>();
		else
			this.personList = personList;
		this.fillView = fillView;
	}

	public ArrayList<Person> handleResponse(HttpResponse response) throws IOException {
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
				Person person = new Person();
				person.setId(jSonP.getInt("id"));
				person.setName(jSonP.getString("name"));
				person.setEmailAddress(jSonP.getString("email"));
				person.setTelephoneNumber(jSonP.getString("telephone"));
				person.setGSM(jSonP.getString("gsm"));
				personList.add(person);
			}

		} catch (JSONException e) {
			if (e.getMessage().contains("No value for")) {
			}
			e.printStackTrace();
		}

		fillView.getUsersResponse(personList);

		return null;

	}
}
