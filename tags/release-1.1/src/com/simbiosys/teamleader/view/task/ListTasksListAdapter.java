package com.simbiosys.teamleader.view.task;

import java.util.ArrayList;

import com.simbiosys.teamleader.model.CheckableRESTGUIObject;
import com.simbiosys.teamleader.model.Task;
import com.simbiosys.teamleader.view.invoice.TaskList;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListTasksListAdapter<T> extends ListRESTObjectListAdapter<CheckableRESTGUIObject>
implements OnCheckedChangeListener {


	//	public ListTasksListAdapter(Context cx, ArrayList<RESTGUIObject> objects,
	//			String[] fieldsToShow) {
	//		super(cx, objects, fieldsToShow);
	//	}

	public ListTasksListAdapter(TaskList cx, ArrayList<Task> taskList,
			String[] fieldsToShow) {
		super(cx, taskList, fieldsToShow);
	}

	public class ViewHolder {
		LinearLayout ll;
		TextView textViews[];
		public CheckableRESTGUIObject o;
		public boolean isChecked = false;
		CheckBox checkBox;
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

			verticalLinearLayout.setOnLongClickListener((OnLongClickListener) activity);
			verticalLinearLayout.setOnClickListener((OnClickListener) activity);
			convertView = verticalLinearLayout;
			viewHolder.ll = (LinearLayout) horizontalLinearLayout;
			for (int i = 0; i < fieldsToShow.length; i++) {

				TextView textView = new TextView(cx);
				textView.setWidth(fieldTextViewWidths[i]);
				Log.v("TeamLeader","setting " + fieldsToShow[i] + " text view width to: " + fieldTextViewWidths[i]);
				viewHolder.textViews[i] = textView;
				((LinearLayout)horizontalLinearLayout).addView(textView);
			}
			// lastly, create a checkbox for checking
			CheckBox checkBox = new CheckBox(cx);
			checkBox.setChecked(false);
			((LinearLayout)horizontalLinearLayout).addView(checkBox);
			viewHolder.checkBox = checkBox;

			checkBox.setOnCheckedChangeListener(this);
			viewHolder.o = (CheckableRESTGUIObject)objectList.get(position);
			checkBox.setChecked(viewHolder.o.isChecked());
			horizontalLinearLayout.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		for (int i = 0; i < fieldsToShow.length; i++) {
			viewHolder.textViews[i].setText(objectList.get(position).get(fieldsToShow[i]));
		}
		return(convertView);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
		// get the parent layout, then get the tag, then set the checked variable in the ViewHolder
		LinearLayout parent = (LinearLayout)checkBox.getParent();
		ViewHolder viewHolder = (ViewHolder)parent.getTag();
		if (viewHolder != null) {
			viewHolder.isChecked = isChecked;
			viewHolder.o.setChecked(isChecked);
		}
	}

}
