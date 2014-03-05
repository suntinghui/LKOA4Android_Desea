package com.lkoa.model;

import java.util.ArrayList;
import java.util.List;

public class ProcessContentInfo {
	
	public String processId;
	public String handleId;
	public String processTitle;
	
	public List<Activity> activityList;
	public List<Filed> filedList;
	
	public ProcessContentInfo() {
		activityList = new ArrayList<ProcessContentInfo.Activity>();
		filedList = new ArrayList<ProcessContentInfo.Filed>();
	}
	
	public Activity newActivity() {
		return new Activity();
	}
	
	public Filed newFiled() {
		return new Filed();
	}
	
	public User newUser() {
		return new User();
	}
	
	public Option newOption() {
		return new Option();
	}
	
	public class Activity {
		public int id;	//节点序号
		public int select;	//节点选择	1-选择	0-未选
		public String name;	//节点名称
		public int type;	//节点类型
		public int mode;	//节点模式
		public String dealTime;	//节点办理时限
		
		public int interpose;	//是否重置主办人	1-重置	其他-不重置
		public int reSelect;	//是否重置参与人	1-重置	其他-不重置
		public User zbr;	//主办人信息
		public List<User> cyrs;	//参与人信息
		
		public Activity() {
			cyrs = new ArrayList<ProcessContentInfo.User>();
		}
	}
	
	public class Filed {
		public String id;	//数据项标识
		public String name;	//数据项名称
		public int type;	//数据项类型
		public int editMode;	//处理模式
		public String showContent;	//显示内容
		public String value;
		
		public List<Option> optionList;
		
		public Filed() {
			optionList = new ArrayList<ProcessContentInfo.Option>();
		}
	}
	
	public class User {
		public String userId;
		public String userName;
	}
	
	public class Option {
		public String value;
		public String text;
		public int defaultFlag;	//是否默认选项	1-默认选中 
	}

}
