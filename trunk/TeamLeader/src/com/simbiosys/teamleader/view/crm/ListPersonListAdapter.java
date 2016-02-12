// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.view.crm;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.simbiosys.teamleader.R;
import com.simbiosys.teamleader.model.Contact;
import com.simbiosys.teamleader.model.Person;

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

public class ListPersonListAdapter extends BaseAdapter {

	ArrayList<Person> peopleList;

	public LinkedHashMap<String, ViewHolder> mappedItems;

	LayoutInflater li;
	Context cx;
	PeopleList peopleListActivity;
	int maxNameLength;
	int nameTextViewWidth;

	int maxLastName;
	int lastNameTextViewWidth;
	int cityTextViewWidth;

	public class ViewHolder {
		LinearLayout ll;
		TextView cityTextView;
		TextView lastNameTextView;
		TextView nameTextView;
		public Person person;
	}

	public ListPersonListAdapter(Context cx, ArrayList<Person> peopleList2) {
		this.cx = cx;
		this.peopleListActivity = (PeopleList)cx;
		li = (LayoutInflater)cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.peopleList = peopleList2;

		// calculate the longest length of the characters and draw appropriately
		int maxName = 0;
		Person personWithLongestFirstName = null;
		for (Contact p:peopleList2) {
			if (p.getName().length() > maxName) { 
				maxName = p.getName().length();
				personWithLongestFirstName = (Person)p;
			}
		}
		maxNameLength = maxName;

		TextView textViewSample = new TextView(cx);
		TextPaint paint = textViewSample.getPaint();
		//		//Log.v("TeamLeader","Longest string at Position: " + longestStringPos + " with the text: " + fields[longestStringPos] + " with this many characters: " + maxChars);
		Rect bounds = new Rect();
		if (personWithLongestFirstName == null) {
			nameTextViewWidth = 10;
		} else {
			paint.getTextBounds(personWithLongestFirstName.getName(), 0, maxNameLength, bounds);
			//Log.v("TeamLeader","textSize for first name is: " + bounds.right + " long");
			nameTextViewWidth = bounds.right + 10;
		}

		Person personWithLongestLastName = null;
		int max = 0;
		for (Contact p:peopleList2) {
			if (((Person)p).getSurname().length() > max) {
				max = ((Person)p).getSurname().length();
				personWithLongestLastName = ((Person)p);
			}
		}
		maxLastName = max;
		if (personWithLongestLastName == null) lastNameTextViewWidth = 10;
		else {
			paint.getTextBounds(personWithLongestLastName.getSurname(), 0, max, bounds);
			//Log.v("TeamLeader","textSize for last name is: " + bounds.right + " long");
			lastNameTextViewWidth = bounds.right + 10;
		}

		Person personWithLongestCity = null;

		max = 0;
		for(Contact p:peopleList2) {
			if (p.getCity().length() > max) {
				max = p.getCity().length();
				personWithLongestCity = ((Person)p);
			}
		}
		if (personWithLongestCity == null) cityTextViewWidth = 10;
		else {
			paint.getTextBounds(personWithLongestCity.getCity(), 0, max, bounds);
			cityTextViewWidth = bounds.right+10;
		}

	}

	@Override
	public int getCount() {
		return peopleList.size();
	}

	@Override
	public Object getItem(int position) {
		return peopleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// Not Used
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		// for each string, inflate an enter_person_item then set the fields appropriately, then add it
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LinearLayout linearLayout = new LinearLayout(cx);
			linearLayout.setOnLongClickListener(peopleListActivity);
			linearLayout.setOnClickListener(peopleListActivity);
			convertView = (LinearLayout)li.inflate(R.layout.list_person_item, linearLayout);
			TextView textView = (TextView)convertView.findViewById(R.id.textView1);
			textView.setWidth(nameTextViewWidth);
			//Log.v("TeamLeader","setting first Name text view width to: " + nameTextViewWidth);

			TextView lastNameTextView = (TextView)convertView.findViewById(R.id.textView2);
			lastNameTextView.setWidth(lastNameTextViewWidth);

			TextView cityTextView = (TextView)convertView.findViewById(R.id.textView3);
			cityTextView.setWidth(cityTextViewWidth);

			viewHolder.ll = (LinearLayout) convertView;
			viewHolder.nameTextView = textView;
			viewHolder.cityTextView = cityTextView;
			viewHolder.lastNameTextView = lastNameTextView;
			viewHolder.person = peopleList.get(position);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.nameTextView.setText(peopleList.get(position).getName());
		viewHolder.lastNameTextView.setText(((Person)peopleList.get(position)).getSurname());
		viewHolder.cityTextView.setText(peopleList.get(position).getCity());
		return(convertView);
	}


}
