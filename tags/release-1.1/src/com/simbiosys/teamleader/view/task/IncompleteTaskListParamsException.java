package com.simbiosys.teamleader.view.task;

import com.simbiosys.teamleader.model.TaskListParamsObject;

public class IncompleteTaskListParamsException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6707320216131343855L;
	TaskListParamsObject taskListParams;
	
	public IncompleteTaskListParamsException(TaskListParamsObject taskListParams) {
		this.taskListParams = taskListParams;
	}

}
