package com.lkoa.model;

import java.io.Serializable;

public class ContactItem implements Serializable {

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
}
