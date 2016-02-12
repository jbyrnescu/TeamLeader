package com.simbiosys.teamleader.view.task;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.simbiosys.teamleader.R;
import com.simbiosys.teamleader.model.TaskType;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListTaskTypesListAdapter extends BaseAdapter {

	ArrayList<TaskType> taskTypeList;

	public LinkedHashMap<String, ViewHolder> mappedItems;

	LayoutInflater li;
	Context cx;
	ListTaskTypes listTaskTypesActivity;
	int maxTaskTypeNumber;
	int maxTaskTypeName;
	
	int taskTypeNumberTextViewWidth;
	int taskTypeNameTextViewWidth;

	public class ViewHolder {
		LinearLayout ll;
		TextView taskTypeNumber;
		TextView taskTypeName;
		TaskType taskType;
	}

	public ListTaskTypesListAdapter(Context cx, ArrayList<TaskType> taskTypeList) {
		this.cx = cx;
		this.listTaskTypesActivity = (ListTaskTypes)cx;
		li = (LayoutInflater)cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.taskTypeList = taskTypeList;

		// calculate the longest length of the characters and draw appropriately
		int maxName = 0;
		TaskType taskTypeWithLongestName = null;
		for (TaskType t:taskTypeList) {
			if (t.getName().length() > maxName) { 
				maxName = t.getName().length();
				taskTypeWithLongestName = (TaskType)t;
			}
		}
		maxTaskTypeName = maxName;

		TextView textViewSample = new TextView(cx);
		TextPaint paint = textViewSample.getPaint();
		//		Log.v("TeamLeader","Longest string at Position: " + longestStringPos + " with the text: " + fields[longestStringPos] + " with this many characters: " + maxChars);
		Rect bounds = new Rect();
		if (taskTypeWithLongestName == null) {
			taskTypeNameTextViewWidth = 10;
		} else {
			paint.getTextBounds(taskTypeWithLongestName.getName(), 0, maxTaskTypeName, bounds);
			Log.v("TeamLeader","textSize for name is: " + bounds.right + " long");
			taskTypeNameTextViewWidth = bounds.right + 10;
		}

		TaskType taskTypeWithLongestNumber = null;
		int max = 0;
		for (TaskType t:taskTypeList) {
			if (Integer.valueOf(t.getNumber()).toString().length() > max) {
				max = Integer.valueOf(t.getNumber()).toString().length();
				taskTypeWithLongestNumber = t;
			}
		}
		maxTaskTypeNumber = max;
		if (taskTypeWithLongestNumber == null) taskTypeNumberTextViewWidth = 10;
		else {
			paint.getTextBounds(Integer.valueOf(taskTypeWithLongestNumber.getNumber()).toString(), 0, max, bounds);
			Log.v("TeamLeader","textSize for city is: " + bounds.right + " long with " + maxTaskTypeNumber + " characters");
			taskTypeNumberTextViewWidth = bounds.right + 10;
		}

	}

	@Override
	public int getCount() {
		return taskTypeList.size();
	}

	@Override
	public Object getItem(int position) {
		return taskTypeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// Not Used
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		// for each string, inflate an enter_company_item then set the fields appropriately, then add it
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LinearLayout linearLayout = new LinearLayout(cx);
//			linearLayout.setOnLongClickListener(listTaskTypesActivity);
			linearLayout.setOnClickListener(listTaskTypesActivity);
			convertView = (LinearLayout)li.inflate(R.layout.list_task_type_item, linearLayout);
			TextView textView = (TextView)convertView.findViewById(R.id.textView1);
			textView.setWidth(taskTypeNumberTextViewWidth);
			Log.v("TeamLeader","setting first Name text view width to: " + taskTypeNumberTextViewWidth);

			TextView taskTypeNameTextView = (TextView)convertView.findViewById(R.id.textView2);
			Log.v("TeamLeader","setting cityTextView width to: " + taskTypeNumberTextViewWidth);
			taskTypeNameTextView.setWidth(taskTypeNameTextViewWidth);

			viewHolder.ll = (LinearLayout) convertView;
			viewHolder.taskTypeNumber = textView;
			viewHolder.taskTypeName = taskTypeNameTextView;
			viewHolder.taskType = taskTypeList.get(position);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.taskTypeName.setText(taskTypeList.get(position).getName());
		viewHolder.taskTypeNumber.setText(Integer.valueOf(taskTypeList.get(position).getNumber()).toString());
		return(convertView);
	}


}
