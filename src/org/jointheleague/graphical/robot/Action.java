package org.jointheleague.graphical.robot;

public class Action {

	private String action, addInfo;
	private int type;
	private int[] params;
	private boolean allowMultiple;

	public Action(String action, int type) {
		this(action, type, null, null, true);
	}
	
	public Action(String action, int type, boolean allowMultiple) {
		this(action, type, null, null, allowMultiple);
	}

	public Action(String action, int type, String addInfo) {
		this(action, type, addInfo, null, true);
	}

	public Action(String action, int type, int[] params) {
		this(action, type, null, params, true);
	}

	public Action(String action, int type, String addInfo, int[] params) {
		this(action, type, addInfo, params, true);
	}

	//Master Constructor
	public Action(String action, int type, String addInfo, int[] params,
			boolean allowMultiple) {
		this.action = action;
		this.type = type;
		this.addInfo = addInfo;
		this.params = params;
		allowMultiple = this.allowMultiple;
	}

	public String getAction() {
		return action;
	}

	public int getType() {
		return type;
	}

	public String getAddInfo() {
		return addInfo;
	}

	public int[] getParams() {
		return params;
	}

	public boolean canRunTogether(Action other) {
		return this.type != other.type;
	}

	public boolean isInverse(Action other) {
		return this.type != 0 && -this.type == other.type;
	}

	public void setParams(int[] newParams) {
		this.params = newParams;
	}
	
	public boolean allowsMultiple() {
		return allowMultiple;
	}
}
