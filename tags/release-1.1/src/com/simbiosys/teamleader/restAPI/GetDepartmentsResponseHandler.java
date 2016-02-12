package com.simbiosys.teamleader.restAPI;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.simbiosys.teamleader.model.Department;

public class GetDepartmentsResponseHandler implements ResponseHandler<ArrayList<Department>> {
	
	ArrayList<Department> departments;
	ListResponse<Department> fillView;

	public GetDepartmentsResponseHandler(ArrayList<Department> departments, ListResponse<Department> fillView) {
		if (departments == null)
			this.departments = new ArrayList<Department>();
		else
			this.departments = departments;
		this.fillView = fillView;
	}
	
	@Override
	public ArrayList<Department> handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		// parse the JSON and put the data in the arrayList
		HttpEntity httpEntity = response.getEntity();
		//			InputStream inputStream = httpEntity.getContent();

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
				Department department = new Department();
				department.setId(jSonP.getInt("id"));
				department.setName(jSonP.getString("name"));
				departments.add(department);
			}

		} catch (JSONException e) {
			if (e.getMessage().contains("No value for")) {
			}
			e.printStackTrace();
		}

		fillView.setList(departments);

		return departments;	}

}
