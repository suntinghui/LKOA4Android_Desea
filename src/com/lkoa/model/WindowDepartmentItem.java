package com.lkoa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WindowDepartmentItem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public WindowDepartmentItem() {
		this.list = new ArrayList<WindowDepartmentItem>();
	}

	public String id;
	public String name;
	public int count;
	
	public List<WindowDepartmentItem> list;
}
