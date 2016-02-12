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

import com.simbiosys.teamleader.model.TaskType;
import com.simbiosys.teamleader.view.task.GetTaskTypesResponse;

import android.util.Log;

public class GetTaskTypesResponseHandler implements ResponseHandler<ArrayList<TaskType>> {

	ArrayList<TaskType> taskTypeList;
	GetTaskTypesResponse fillView;

	public GetTaskTypesResponseHandler(ArrayList<TaskType> taskTypeList, GetTaskTypesResponse fillView2) {
		if (taskTypeList == null)
			this.taskTypeList = new ArrayList<TaskType>();
		else
			this.taskTypeList = taskTypeList;
		this.fillView = fillView2;
	}

	public ArrayList<TaskType> handleResponse(HttpResponse response) throws IOException {
		// parse the JSON and put the data in the arrayList
		HttpEntity httpEntity = response.getEntity();
		//			InputStream inputStream = httpEntity.getContent();

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
				TaskType taskType = new TaskType();
				taskType.setNumber(jSonP.getInt("id"));
				taskType.setName(jSonP.getString("name"));
				taskTypeList.add(taskType);
			}

		} catch (JSONException e) {
			if (e.getMessage().contains("No value for")) {
			}
			e.printStackTrace();
		}

		fillView.getTaskTypesResponse(taskTypeList);

		return taskTypeList;

	}
}

