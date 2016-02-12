// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.view.task;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

import com.simbiosys.teamleader.model.RESTGUIObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListRESTObjectListAdapter<T> extends BaseAdapter {

	protected ArrayList<RESTGUIObject> objectList;

	public LinkedHashMap<String, ViewHolder> mappedItems;

	LayoutInflater li;
	protected Context cx;
	protected Activity activity;
	protected int fieldCharacterLengths[];
	protected int fieldTextViewWidths[];
	protected RESTGUIObject longestObjects[];
	protected ArrayList<?> objects;
	protected String fieldsToShow[];

	public class ViewHolder {
		LinearLayout ll;
		TextView textViews[];
		public RESTGUIObject o;
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	public <T extends RESTGUIObject> ListRESTObjectListAdapter(Context cx, ArrayList<T> objects, String fieldsToShow[]) {
		this.cx = cx;
		this.activity = (Activity)cx;
		this.objects = objects;
		this.fieldsToShow = fieldsToShow;
		objectList = (ArrayList<RESTGUIObject>) objects;

		int count = fieldsToShow.length;
		fieldCharacterLengths = new int[fieldsToShow.length];
		fieldTextViewWidths = new int[fieldsToShow.length];
		longestObjects = new RESTGUIObject[fieldsToShow.length];
		
		//if (!(cx instanceof OnClickListener)) //Log.v("TeamLeader", "Need to register OnClickListener");
		//if (!(cx instanceof OnLongClickListener)) //Log.v("TeamLeader", "Need to register OnLongClickListener");

		// calculate the longest length of the characters and draw appropriately

		for (int i = 0; i < count; i++) {
			fieldCharacterLengths[i] = 0;
			// find the longest string
			for(T o:objects) {
				String field = o.get(fieldsToShow[i]);
				if (field != null) {
					if (o.get(fieldsToShow[i]).length() > fieldCharacterLengths[i]) {
						fieldCharacterLengths[i] = o.get(fieldsToShow[i]).length();
						longestObjects[i] = o;
					}
				} else {
					//Log.v("TeamLeader", "field not found to display in RESTGUIObject: " + fieldsToShow[i]);
					//Log.v("TeamLeader", "available fields: ");
					Set<String> keys = o.getValues().keySet();
					for (String key: keys) {
						//Log.v("TeamLeader", "key: " + key);
					}
				}

				// now calculate the pixel width
				TextView textViewSample = new TextView(cx);
				TextPaint paint = textViewSample.getPaint();
				Rect bounds = new Rect();	
				if (longestObjects[i] == null) {
					fieldTextViewWidths[i] = 10;
				} else {
					paint.getTextBounds(longestObjects[i].get(fieldsToShow[i]), 0, fieldCharacterLengths[i], bounds);
					fieldTextViewWidths[i] = bounds.right + 10;
				}

			}
			
		}
	}

	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public Object getItem(int position) {
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		// Not Used
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		// for each string, inflate an enter_person_item then set the fields appropriately, then add it
		if (convertView == null) {
			viewHolder = new ViewHolder();
			viewHolder.textViews = new TextView[fieldsToShow.length];
			
			LinearLayout verticalLinearLayout = new LinearLayout(cx);
			verticalLinearLayout.setOrientation(LinearLayout.VERTICAL);
			
			LinearLayout horizontalLinearLayout = new LinearLayout(cx);
			horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
			
			verticalLinearLayout.addView(horizontalLinearLayout);
			
			if (activity instanceof OnLongClickListener)
				verticalLinearLayout.setOnLongClickListener((OnLongClickListener) activity);
			if (activity instanceof OnClickListener)
				verticalLinearLayout.setOnClickListener((OnClickListener) activity);
			convertView = verticalLinearLayout;
			viewHolder.ll = (LinearLayout) horizontalLinearLayout;
			for (int i = 0; i < fieldsToShow.length; i++) {

				TextView textView = new TextView(cx);
				textView.setWidth(fieldTextViewWidths[i]);
				//Log.v("TeamLeader","setting " + fieldsToShow[i] + " text view width to: " + fieldTextViewWidths[i]);
				viewHolder.textViews[i] = textView;
				((LinearLayout)horizontalLinearLayout).addView(textView);
			}
			viewHolder.o = objectList.get(position);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		for (int i = 0; i < fieldsToShow.length; i++) {
			viewHolder.textViews[i].setText(objectList.get(position).get(fieldsToShow[i]));
		}
		return(convertView);
	}


}
