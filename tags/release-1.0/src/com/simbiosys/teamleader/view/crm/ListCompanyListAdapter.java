// Copyright (C) 2014 John Charles Byrne Distributed under the terms of GPL.  Please leave this notice intact when copying 
package com.simbiosys.teamleader.view.crm;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.simbiosys.teamleader.R;
import com.simbiosys.teamleader.model.Company;
import com.simbiosys.teamleader.model.Contact;

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

public class ListCompanyListAdapter extends BaseAdapter {

	ArrayList<Company> companyList;

	public LinkedHashMap<String, ViewHolder> mappedItems;

	LayoutInflater li;
	Context cx;
	CompanyList companyListActivity;
	int maxNameLength;
	int nameTextViewWidth;

	int maxCityName;
	int cityTextViewWidth;
	int countryTextViewWidth;

	public class ViewHolder {
		LinearLayout ll;
		TextView cityTextView;
		TextView countryTextView;
		TextView nameTextView;
		Company company;
	}

	public ListCompanyListAdapter(Context cx, ArrayList<Company> companyList2) {
		this.cx = cx;
		this.companyListActivity = (CompanyList)cx;
		li = (LayoutInflater)cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.companyList = companyList2;

		// calculate the longest length of the characters and draw appropriately
		int maxName = 0;
		Company companyWithLongestName = null;
		for (Contact c:companyList2) {
			if (c.getName().length() > maxName) { 
				maxName = c.getName().length();
				companyWithLongestName = (Company)c;
			}
		}
		maxNameLength = maxName;

		TextView textViewSample = new TextView(cx);
		TextPaint paint = textViewSample.getPaint();
		//		Log.v("TeamLeader","Longest string at Position: " + longestStringPos + " with the text: " + fields[longestStringPos] + " with this many characters: " + maxChars);
		Rect bounds = new Rect();
		if (companyWithLongestName == null) {
			nameTextViewWidth = 10;
		} else {
			paint.getTextBounds(companyWithLongestName.getName(), 0, maxNameLength, bounds);
			Log.v("TeamLeader","textSize for name is: " + bounds.right + " long");
			nameTextViewWidth = bounds.right + 10;
		}

		Company companyWithLongestCityName = null;
		int max = 0;
		for (Company p:companyList2) {
			if (p.getCity().length() > max) {
				max = p.getCity().length();
				companyWithLongestCityName = p;
			}
		}
		maxCityName = max;
		if (companyWithLongestCityName == null) cityTextViewWidth = 10;
		else {
			paint.getTextBounds(companyWithLongestCityName.getCity(), 0, max, bounds);
			Log.v("TeamLeader","textSize for city is: " + bounds.right + " long with " + maxCityName + " characters");
			cityTextViewWidth = bounds.right + 10;
		}

		Company companyWithLongestCountry = null;

		max = 0;
		for(Contact c:companyList2) {
			if (c.getCountry().length() > max) {
				max = c.getCountry().length();
				companyWithLongestCountry = (Company)c;
			}
		}
		if (companyWithLongestCountry == null) countryTextViewWidth = 10;
		else {
			paint.getTextBounds(companyWithLongestCountry.getCountry(), 0, max, bounds);
			Log.v("TeamLeader","textSize for country is: " + bounds.right + " long");
			countryTextViewWidth = bounds.right+10;
		}

	}

	@Override
	public int getCount() {
		return companyList.size();
	}

	@Override
	public Object getItem(int position) {
		return companyList.get(position);
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
			linearLayout.setOnLongClickListener(companyListActivity);
			linearLayout.setOnClickListener(companyListActivity);
			convertView = (LinearLayout)li.inflate(R.layout.list_company_item, linearLayout);
			TextView textView = (TextView)convertView.findViewById(R.id.textView1);
			textView.setWidth(nameTextViewWidth);
			Log.v("TeamLeader","setting first Name text view width to: " + nameTextViewWidth);

			TextView cityTextView = (TextView)convertView.findViewById(R.id.textView2);
			Log.v("TeamLeader","setting cityTextView width to: " + cityTextViewWidth);
			cityTextView.setWidth(cityTextViewWidth);

			TextView countryTextView = (TextView)convertView.findViewById(R.id.textView3);
			countryTextView.setWidth(countryTextViewWidth);

			viewHolder.ll = (LinearLayout) convertView;
			viewHolder.nameTextView = textView;
			viewHolder.cityTextView = cityTextView;
			viewHolder.countryTextView = countryTextView;
			viewHolder.company = companyList.get(position);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.nameTextView.setText(companyList.get(position).getName());
		viewHolder.cityTextView.setText(companyList.get(position).getCity());
		viewHolder.countryTextView.setText(companyList.get(position).getCountry());
		return(convertView);
	}


}
