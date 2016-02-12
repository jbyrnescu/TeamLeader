// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.view.task;

import com.simbiosys.teamleader.R;
import com.simbiosys.teamleader.view.invoice.TaskListParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TaskMainMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_main_menu);
	}
	
	public void enterTask(View view) {
		Intent i = new Intent(this, EnterTask.class);
		startActivity(i);
	}
	
	public void listTasks(View view) {
		Intent i = new Intent(this, TaskListParams.class);
		startActivity(i);
	}
	
}
