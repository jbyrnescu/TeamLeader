package com.simbiosys.teamleader.view;

import com.simbiosys.teamleader.Globals;
import com.simbiosys.teamleader.R;
import com.simbiosys.teamleader.view.crm.CRMMainMenu;
import com.simbiosys.teamleader.view.invoice.TaskListParams;
import com.simbiosys.teamleader.view.task.TaskMainMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		Globals.application = getApplication();
	}
	
	public void launchCRMMenu(View arg0) {
		Intent i = new Intent(this, CRMMainMenu.class);
		startActivity(i);
	}
	
	public void launchTaskMainMenu(View view) {
		Intent i = new Intent(this, TaskMainMenu.class);
		startActivity(i);
	}
	
	public void listTasks(View view) {
		Intent i = new Intent(this, TaskListParams.class);
		startActivity(i);
	}
	
}
