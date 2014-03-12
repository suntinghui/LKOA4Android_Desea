package com.lkoa.model;

import java.io.Serializable;
import java.util.List;

import android.text.TextUtils;

public class DepartmentItem implements Serializable, Comparable<DepartmentItem> {

	private static final long serialVersionUID = 1L;
	public String deptId;	//部门序号
	public String deptName;	//部门名称
	
	public String alpha;
	public boolean checked;
	
	public List<ContactItem> contacts;
	
	@Override
	public boolean equals(Object o) {
		DepartmentItem item = (DepartmentItem)o;
		if(TextUtils.equals(item.deptId, this.deptId)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int compareTo(DepartmentItem another) {
		if(TextUtils.equals("#", alpha)) {
			return 1;
		} else if(TextUtils.equals("#", another.alpha)) {
			return -1;
		}
		
		char curr = alpha.charAt(0);
		char other = another.alpha.charAt(0);
		if(curr > other) {
			return 1;
		} else if(curr < other) {
			return -1;
		} else {
			return 0;
		}
	}
}
