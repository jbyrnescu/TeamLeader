package com.simbiosys.teamleader.view;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.simbiosys.teamleader.R;

import android.app.Activity;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker;

public class PickDateTime extends Activity implements OnDateChangedListener, OnTimeSetListener {

	long minDate = 0;
	long maxDate = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_date_time);
		

		Intent i = getIntent();
		minDate = i.getLongExtra("minDate", 0);
		maxDate = i.getLongExtra("maxDate", 0);
		

		DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker1);

		//		TimePicker timePicker = (TimePicker)findViewById(R.id.timePicker1);
		Date today = new Date();
		if (minDate == 0) minDate = today.getTime()/1000;

		if (maxDate == 0) {
			GregorianCalendar aYearFromNow = new GregorianCalendar();
			aYearFromNow.setTimeInMillis(today.getTime());
			aYearFromNow.add(Calendar.YEAR, 1);
			maxDate = aYearFromNow.getTimeInMillis()/1000;
		}
		
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(minDate*1000);
		
		datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this);
		
		/*  UNFORTUNATELY THIS DOESN'T WORK.  I WISH IT DID!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// The Choreographer gives a nullPointer exception and I don't know why.
		datePicker.setMinDate(minDate*1000);
		datePicker.setMaxDate(maxDate*1000);
		 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		 */

	}

	public void selectDateTime(View view) {
		DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker1);
		TimePicker timePicker = (TimePicker)findViewById(R.id.timePicker1);
		int dayOfMonth = datePicker.getDayOfMonth();
		int month = datePicker.getMonth();
		int year = datePicker.getYear();
		int hour = timePicker.getCurrentHour();
		int minute = timePicker.getCurrentMinute();
		GregorianCalendar calendar = new GregorianCalendar(year, month, dayOfMonth, hour, minute);
		Intent resultIntent = new Intent();
		resultIntent.putExtra("timePoint", calendar.getTimeInMillis()/(long)1000);
		setResult(RESULT_OK, resultIntent);
		finish();
	}

	@Override
	public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
		
		GregorianCalendar calendar = new GregorianCalendar(year, month, day);
		long timeInMilliSeconds = calendar.getTimeInMillis();
		
		GregorianCalendar maxCalendar = new GregorianCalendar();
		maxCalendar.setTimeInMillis(maxDate*1000);
		// This is to truncate any overlap so we don't havea  stack overflow because it's always less than timeInMilliSeconds
		GregorianCalendar newMaxCalendar = new GregorianCalendar(maxCalendar.get(Calendar.YEAR),
				maxCalendar.get(Calendar.MONTH), maxCalendar.get(Calendar.DAY_OF_MONTH)); 
		if (timeInMilliSeconds > newMaxCalendar.getTimeInMillis()) {
			datePicker.updateDate(maxCalendar.get(Calendar.YEAR), 
					maxCalendar.get(Calendar.MONTH), 
					maxCalendar.get(Calendar.DAY_OF_MONTH));
			
		}
		
		GregorianCalendar minCalendar = new GregorianCalendar();
		minCalendar.setTimeInMillis(minDate*1000);
		// This is to truncate any overlap so we don't havea  stack overflow because it's always less than timeInMilliSeconds
		GregorianCalendar newMinCalendar = new GregorianCalendar(minCalendar.get(Calendar.YEAR),
				minCalendar.get(Calendar.MONTH), minCalendar.get(Calendar.DAY_OF_MONTH));
		if (timeInMilliSeconds < newMinCalendar.getTimeInMillis()) {

			datePicker.updateDate(minCalendar.get(Calendar.YEAR), 
					minCalendar.get(Calendar.MONTH), 
					minCalendar.get(Calendar.DAY_OF_MONTH));
			
		}
	}

	@Override
	public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
		// TODO Fix Time such that user can't enter one that's longer than a day for a Task
		
	}
}
