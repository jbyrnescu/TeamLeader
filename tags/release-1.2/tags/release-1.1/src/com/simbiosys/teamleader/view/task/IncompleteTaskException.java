package com.simbiosys.teamleader.view.task;

import com.simbiosys.teamleader.model.Task;

public class IncompleteTaskException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3523941437510062574L;
	Task task;
	
	public IncompleteTaskException(Task task) {
		this.task = task;
	}

}
