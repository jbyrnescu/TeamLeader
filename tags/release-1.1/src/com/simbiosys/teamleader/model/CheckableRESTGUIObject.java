package com.simbiosys.teamleader.model;

public abstract class CheckableRESTGUIObject extends RESTGUIObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5585377762756618140L;

	public CheckableRESTGUIObject(String resourceXML) {
		super(resourceXML);
	}

	public abstract boolean isChecked();
	public abstract void setChecked(boolean checked);

}
