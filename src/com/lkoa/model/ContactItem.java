package com.lkoa.model;

import java.io.Serializable;

import android.text.TextUtils;

public class ContactItem implements Serializable, Comparable<ContactItem> {
	
	public static final String ALPHA_U_SELECTED = "已选择人员";

	private static final long serialVersionUID = 1L;
	public String userId;	//用户序号
	public String userName;	//用户名称
	
	public String deptId;	//部门序号
	public String deptName;	//部门名称
	
	public String bgdh;	//办公电话
	public String yddh; //移动电话
	
	public String email1;	//邮件1
	public String email2;	//邮件2
	
	public String alphaU;	//用户名拼音首字母
	public boolean checked;	//是否被选中
	
	@Override
	public int compareTo(ContactItem another) {
		if(TextUtils.equals(ALPHA_U_SELECTED, alphaU)) return -1;
		if(TextUtils.equals(ALPHA_U_SELECTED, another.alphaU)) return 1;
		
		if(TextUtils.equals("#", alphaU)) {
			return 1;
		} else if(TextUtils.equals("#", another.alphaU)) {
			return -1;
		}
		
		char curr = alphaU.charAt(0);
		char other = another.alphaU.charAt(0);
		if(curr > other) {
			return 1;
		} else if(curr < other) {
			return -1;
		} else {
			return 0;
		}
	}
}
