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
import com.simbiosys.teamleader.view.crm.GetContactsResponse;

public class GetPeopleResponseHandler implements ResponseHandler<ArrayList<Person>> {

	ArrayList<Person> personList;
	GetContactsResponse fillView;

	public GetPeopleResponseHandler(ArrayList<Person> personList, GetContactsResponse fillView) {
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
			Log.v("TeamLeader", "httpResponse:" + currentString);
		}



		try {
			JSONArray jsonArray = new JSONArray(jsonStr);
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jSonP = jsonArray.getJSONObject(i);	
				Person person = new Person();
				person.setId(jSonP.getInt("id"));
				person.setCity(jSonP.getString("city"));
				person.setName(jSonP.getString("forename"));
				person.setSurname(jSonP.getString("surname"));
				person.setEmailAddress(jSonP.getString("email"));
				person.setTelephoneNumber(jSonP.getString("telephone"));
				person.setCountry(jSonP.getString("country"));
				person.setZipCode(jSonP.getString("zipcode"));
				person.setStreet(jSonP.getString("street"));
				person.setNumber(jSonP.getString("number"));
				person.setGender(jSonP.getString("gender"));
				person.setDOB(jSonP.getString("dob"));
				//				person.setCompany(jSonP.getString("company"));
				person.setGSM(jSonP.getString("gsm"));
				//				person.setLanguage(jSonP.getString("language"));
				personList.add(person);
			}

		} catch (JSONException e) {
			if (e.getMessage().contains("No value for")) {
			}
			e.printStackTrace();
		}

		fillView.getContactsResponse(personList);

		return null;

	}
}
