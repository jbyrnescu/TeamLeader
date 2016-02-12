package com.simbiosys.teamleader.view.crm;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.simbiosys.teamleader.R;
import com.simbiosys.teamleader.model.Person;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EnterPersonListAdapter extends BaseAdapter {
	
	String fields[];
	ArrayList<ArrayList<String>> fieldAndAnswer;
	public LinkedHashMap<String, ViewHolder> mappedItems;
	
	LayoutInflater li;
	Context cx;
	EnterPerson ep;
	int maxChars;
	int textViewWidth;
	Person person;
	
	public class ViewHolder {
		int position;
		LinearLayout ll;
		TextView tv;
		EditText et;
		String value;
		Person person;
		ArrayList<String> fieldAndAnswer;
	}
	
	
	public EnterPersonListAdapter(Context cx, String fields[], Person person) {
		this.fields = fields;
		this.person = person;
		this.cx = cx;
		this.ep = (EnterPerson)cx;
		fieldAndAnswer = new ArrayList<ArrayList<String>>(); 
		for (int i = 0; i < fields.length; i++) {
			ArrayList<String> tempStrings = new ArrayList<String>(2);
			tempStrings.add(fields[i]);
			if (person != null)
				tempStrings.add(person.get(fields[i]));
			else tempStrings.add("");
			fieldAndAnswer.add(tempStrings);
		}
		li = (LayoutInflater)cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		int longestStringPos = 0;
		// calculate the longest length of the characters and draw appropriately
		int max = 0;
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].length() > max) { 
				max = fields[i].length();
				longestStringPos = i;
			}
		}
		maxChars = max;
//		textViewWidth = pixelsPerChar * maxChars;
		
		TextView textViewSample = new TextView(cx);
		TextPaint paint = textViewSample.getPaint();
		Log.v("TeamLeader","Longest string at Position: " + longestStringPos + " with the text: " + fields[longestStringPos] + " with this many characters: " + maxChars);
		Rect bounds = new Rect();
		paint.getTextBounds(fields[longestStringPos], 0, maxChars, bounds);
		Log.v("TeamLeader","textSize is: " + bounds.right + " long");
		textViewWidth = bounds.right +5;
		
	}

	@Override
	public int getCount() {
		return fields.length;
	}

	@Override
	public Object getItem(int position) {
		return fieldAndAnswer.get(position);
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
			viewHolder.fieldAndAnswer = (ArrayList<String>) getItem(position);
			LinearLayout linearLayout = new LinearLayout(cx);
			convertView = (LinearLayout)li.inflate(R.layout.enter_person_item, linearLayout);
			TextView textView = (TextView)convertView.findViewById(R.id.textView1);

			textView.setWidth(textViewWidth);
			Log.v("TeamLeader","setting text view width to: " + textViewWidth);

			EditText editText = (EditText)convertView.findViewById(R.id.editText1);
			String UIName = fieldAndAnswer.get(position).get(0);
			if (person != null) {
				String currentAnswer = person.get(UIName); 
				editText.setText(currentAnswer);
			}
			
			editText.setOnFocusChangeListener(ep);
			viewHolder.ll = (LinearLayout) convertView;
			viewHolder.tv = textView;
			viewHolder.et = editText;
			viewHolder.position = position;
			viewHolder.toString();
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
			if (fieldAndAnswer.get(position).get(1) == null)
				fieldAndAnswer.get(position).add(viewHolder.et.getText().toString());
			else {
				Log.v("TeamLeader", "There was an existing viewHolder here");
//				fieldAndAnswer.get(position).remove(1);
//				fieldAndAnswer.get(position).add(viewHolder.et.getText().toString());
			}
		}
		viewHolder.tv.setText(fieldAndAnswer.get(position).get(0));
		viewHolder.et.setText(fieldAndAnswer.get(position).get(1));
		viewHolder.et.setHint(fieldAndAnswer.get(position).get(0));
		return(convertView);
	}

	
}
