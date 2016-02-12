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

import com.simbiosys.teamleader.model.Task;
import com.simbiosys.teamleader.view.task.GetTasksResponse;

public class GetTasksResponseHandler implements ResponseHandler<ArrayList<Task>>{

	GetTasksResponse responseHandler;
	
	ArrayList<Task> taskList;
	
	
	public GetTasksResponseHandler(ArrayList<Task> taskList, GetTasksResponse responseHandler) {
		if (taskList == null)
			taskList = new ArrayList<Task>();
		else
			this.taskList = taskList;
		this.responseHandler = responseHandler;
	}
	
	public ArrayList<Task> handleResponse(HttpResponse response) throws IOException {
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
				Task task = new Task();
				task.setDescription(jSonP.getString("description"));
				task.setTeam(jSonP.getString("team"));
				task.setStartDate(Integer.valueOf(jSonP.getString("date")));
				task.setDateFormatted(jSonP.getString("date_formatted"));
				task.setDuration(jSonP.getInt("duration"));
				task.setType(jSonP.getString("type"));
				task.setContactId(jSonP.getString("contact_id"));
				task.setContactName(jSonP.getString("contact_name"));
				task.setCompanyId(jSonP.getString("company_id"));
				task.setCompanyName(jSonP.getString("company_name"));
				task.setProjectId(jSonP.getString("project_id"));
				task.setProjectTitle(jSonP.getString("project_title"));
				task.setMilestoneId(jSonP.getString("milestone_id"));
				task.setMilestoneTitle(jSonP.getString("milestone_title"));
				task.setHourlyCost(jSonP.getString("hourly_cost"));
				taskList.add(task);
			}

		} catch (JSONException e) {
			if (e.getMessage().contains("No value for")) {
			}
			e.printStackTrace();
		}

		responseHandler.getTasksResponse(taskList);

		return taskList;

	}
}
