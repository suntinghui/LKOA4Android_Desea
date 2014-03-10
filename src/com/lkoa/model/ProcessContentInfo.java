package com.lkoa.model;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import com.lkoa.util.LogUtil;

import android.text.TextUtils;
import android.util.Xml;

public class ProcessContentInfo implements Serializable {
	public static final String TAG_INFOR = "Infor";
	public static final String TAG_PROCESS_ID = "FEG_20_COL_10"; 
	public static final String TAG_HANDLE_ID = "FEG_30_COL_10";
	public static final String TAG_PROCESS_TITLE = "FEG_20_COL_20"; 
	public static final String TAG_ACTIVITYS = "Activitys"; 
	public static final String TAG_FIELDS = "Fields";
	
	public String processId;	//流程序号
	public String handleId;		//办理序号
	public String processTitle;	//流程标题
	
	public List<Activity> activityList;
	public List<Field> filedList;
	
	public ProcessContentInfo() {
		activityList = new ArrayList<ProcessContentInfo.Activity>();
		filedList = new ArrayList<ProcessContentInfo.Field>();
	}
	
	public Activity newActivity() {
		return new Activity();
	}
	
	public Field newFiled() {
		return new Field();
	}
	
	public User newUser() {
		return new User();
	}
	
	public Option newOption() {
		return new Option();
	}
	
	public Field getSinglePeopleField() {
		for(Field f : filedList) {
			if(f.type == Field.DATA_TYPE_SINGLE_PEOPLE) {
				return f;
			}
		}
		
		return null;
	}
	
	public Field getSingleDeptField() {
		for(Field f : filedList) {
			if(f.type == Field.DATA_TYPE_SINGLE_DEPARTMENT) {
				return f;
			}
		}
		
		return null;
	}
	
	public Field getMultiPeopleField() {
		for(Field f : filedList) {
			if(f.type == Field.DATA_TYPE_MULTI_PEOPLE) {
				return f;
			}
		}
		
		return null;
	}
	
	public Field getMultiDeptField() {
		for(Field f : filedList) {
			if(f.type == Field.DATA_TYPE_MULTI_DEPARTMENT) {
				return f;
			}
		}
		
		return null;
	}
	
	public class Activity implements Serializable {
		public static final String TAG_ACTIVITY = "Activity";
		public static final String TAG_ID = "Id"; 
		public static final String TAG_SELECT = "Select"; 
		public static final String TAG_NAME = "Name"; 
		public static final String TAG_TYPE = "Type"; 
		public static final String TAG_MODE = "Mode"; 
		public static final String TAG_DEALTIME = "DealTime"; 
		public static final String TAG_INTERPOSE = "Interpose"; 
		public static final String TAG_RESELECT = "ReSelect"; 
		public static final String TAG_ZBR = "ZBR"; 
		public static final String TAG_CYRS = "CYRs"; 
		public static final String TAG_CYR = "CYR"; 
		
		public int id;	//节点序号
		public int select;	//节点选择	1-选择	0-未选
		public String name;	//节点名称
		public int type;	//节点类型
		public int mode;	//节点模式
		public String dealTime;	//节点办理时限
		
		public String interpose;	//是否重置主办人	1-重置	其他-不重置
		public String reSelect;	//是否重置参与人	1-重置	其他-不重置
		public User zbr;	//主办人信息
		public List<User> cyrs;	//参与人信息
		
		public Activity() {
			cyrs = new ArrayList<ProcessContentInfo.User>();
		}
		
		public void buildXml(XmlSerializer ser) throws IOException {
			ser.startTag(null, TAG_ACTIVITY);
			
			ser.startTag(null, TAG_SELECT);
			ser.text(packetCDATA(String.valueOf(select)));
			ser.endTag(null, TAG_SELECT);
			
			ser.startTag(null, TAG_ID);
			ser.text(packetCDATA(String.valueOf(id)));
			ser.endTag(null, TAG_ID);
			
			ser.startTag(null, TAG_NAME);
			ser.text(packetCDATA(name));
			ser.endTag(null, TAG_NAME);
			
			ser.startTag(null, TAG_TYPE);
			ser.text(packetCDATA(String.valueOf(type)));
			ser.endTag(null, TAG_TYPE);
			
			ser.startTag(null, TAG_MODE);
			ser.text(packetCDATA(String.valueOf(mode)));
			ser.endTag(null, TAG_MODE);
			
			ser.startTag(null, TAG_DEALTIME);
			ser.text(packetCDATA(dealTime));
			ser.endTag(null, TAG_DEALTIME);
			
			ser.startTag(null, TAG_INTERPOSE);
			ser.text(packetCDATA(interpose));
			ser.endTag(null, TAG_INTERPOSE);
			
			ser.startTag(null, TAG_RESELECT);
			ser.text(packetCDATA(reSelect));
			ser.endTag(null, TAG_RESELECT);
			
			ser.startTag(null, TAG_ZBR);
			zbr.buildXml(ser);
			ser.endTag(null, TAG_ZBR);
			
			ser.startTag(null, TAG_CYRS);
			for(User user : cyrs) {
				ser.startTag(null, TAG_CYR);
				user.buildXml(ser);
				ser.endTag(null, TAG_CYR);
			}
			ser.endTag(null, TAG_CYRS);
			
			ser.endTag(null, TAG_ACTIVITY);
		}
	}
	
	public static class Field implements Serializable {
		public static final int EDIT_MODE_READ = 0;	//只读
		public static final int EDIT_MODE_UPDATE = 1;	//可更新
		public static final int EDIT_MODE_MUST = 2;	//必填
		public static final int EDIT_MODE_DEFAULT = 3;	//显示默认值
		
		public static final int DATA_TYPE_CHAR = 1;	//字符
		public static final int DATA_TYPE_INT = 2;	//整数
		public static final int DATA_TYPE_FLOAT = 3;	//浮点数
		public static final int DATA_TYPE_TEXT = 4;	//文字
		public static final int DATA_TYPE_OPINION = 5;	//意见
		public static final int DATA_TYPE_ENUM = 6;	//枚举
		public static final int DATA_TYPE_DATE = 7;	//日期
		public static final int DATA_TYPE_TIME = 8;	//时间
		public static final int DATA_TYPE_DATE_AND_TIME = 9;	//日期和时间
		public static final int DATA_TYPE_SINGLE_PEOPLE = 10;	//单选人员
		public static final int DATA_TYPE_MULTI_PEOPLE = 11;	//多选人员
		public static final int DATA_TYPE_SINGLE_DEPARTMENT = 12;	//单选部门
		public static final int DATA_TYPE_MULTI_DEPARTMENT = 13;	//多选部门
		public static final int DATA_TYPE_MONTH_AND_YEAR = 17;	//年月
		public static final int DATA_TYPE_SERIAL_NUMBER = 21;	//流水号
		
		public static final String TAG_FIELD = "Field";
		public static final String TAG_ID = "LK_COLFIELDID"; 
		public static final String TAG_NAME = "LK_FIELDEDIT_TIPNAME"; 
		public static final String TAG_TYPE = "LK_FLDDBTYPE"; 
		public static final String TAG_EDIT_MODE = "LK_FIELDEDITMODE"; 
		public static final String TAG_SHOWCONTENT = "SHOWCONTENT"; 
		public static final String TAG_VALUE = "Value"; 
		public static final String TAG_OPTIONS = "OPTIONS"; 
		
		public enum ContentType {
			TEXT, EDITTEXT, PULLDOWNLIST, 
			SINGLE_PEOPLE, MULTI_PEOPLE, 
			SINGLE_DEPT, MULTI_DEPT, 
			TEXT_EDITTEXT	//用于意见
		}
		
		public String id;	//数据项标识
		public String name;	//数据项名称
		public int type;	//数据项类型
		public int editMode;	//处理模式
		public String showContent;	//显示内容
		public String value;
		
		public List<Option> optionList;
		
		public Field() {
			optionList = new ArrayList<ProcessContentInfo.Option>();
		}
		
		public ContentType getContentType() {
			if(editMode == EDIT_MODE_UPDATE || editMode == EDIT_MODE_MUST) {
				//可更新或必填
				
				if(type == DATA_TYPE_CHAR 
						|| type == DATA_TYPE_INT 
						|| type == DATA_TYPE_FLOAT
						|| type == DATA_TYPE_TEXT) {
					return ContentType.EDITTEXT;
					
				} else if(type == DATA_TYPE_OPINION) {
					return ContentType.TEXT_EDITTEXT;
					
				} else if(type == DATA_TYPE_ENUM) {
					return ContentType.PULLDOWNLIST;
					
				} else if(type == DATA_TYPE_SINGLE_PEOPLE) {
					return ContentType.SINGLE_PEOPLE;
					
				} else if(type == DATA_TYPE_SINGLE_DEPARTMENT) {
					return ContentType.SINGLE_DEPT;
					
				} else if(type == DATA_TYPE_MULTI_PEOPLE) {
					return ContentType.MULTI_PEOPLE;
					
				} else if(type == DATA_TYPE_MULTI_DEPARTMENT) {
					return ContentType.MULTI_DEPT;
				}
				
				return ContentType.TEXT;
				
			} else {
				return ContentType.TEXT;
			}
		}
		
		public void buildXml(XmlSerializer ser) throws IOException {
			ser.startTag(null, TAG_FIELD);
			
			ser.startTag(null, TAG_ID);
			ser.text(packetCDATA(String.valueOf(id)));
			ser.endTag(null, TAG_ID);
			
			ser.startTag(null, TAG_NAME);
			ser.text(packetCDATA(name));
			ser.endTag(null, TAG_NAME);
			
			ser.startTag(null, TAG_TYPE);
			ser.text(packetCDATA(String.valueOf(type)));
			ser.endTag(null, TAG_TYPE);
			
			ser.startTag(null, TAG_EDIT_MODE);
			ser.text(packetCDATA(String.valueOf(editMode)));
			ser.endTag(null, TAG_EDIT_MODE);
			
			ser.startTag(null, TAG_SHOWCONTENT);
			ser.text(packetCDATA(showContent));
			ser.endTag(null, TAG_SHOWCONTENT);
			
			ser.startTag(null, TAG_VALUE);
			ser.text(packetCDATA(value));
			ser.endTag(null, TAG_VALUE);
			
			
			ser.startTag(null, TAG_OPTIONS);
			for(Option option : optionList) {
				option.buildXml(ser);
			}
			ser.endTag(null, TAG_OPTIONS);
			
			ser.endTag(null, TAG_FIELD);
		}
	}
	
	public class User implements Serializable {
		public static final String TAG_USERID = "UserId";
		public static final String TAG_USERNAME = "UserName";
		
		public String userId;
		public String userName;
		
		public void buildXml(XmlSerializer ser) throws IOException {
			ser.startTag(null, TAG_USERID);
			if(TextUtils.isEmpty(userId)) userId = "";
			ser.text(packetCDATA(userId));
			ser.endTag(null, TAG_USERID);
			
			ser.startTag(null, TAG_USERNAME);
			if(TextUtils.isEmpty(userName)) userName = "";
			ser.text(packetCDATA(userName));
			ser.endTag(null, TAG_USERNAME);
		}
		
		@Override
		public String toString() {
			return userName;
		}
	}
	
	public class Option implements Serializable {
		public static final String TAG_ITEM = "Item"; 
		public static final String TAG_VALUE = "Value";
		public static final String TAG_TEXT = "Text"; 
		public static final String TAG_DEFAULTFLAG = "DefaultFlag"; 
		
		public String value;
		public String text;
		public int defaultFlag;	//是否默认选项	1-默认选中 
		
		public void buildXml(XmlSerializer ser) throws IOException {
			ser.startTag(null, TAG_ITEM);

			ser.startTag(null, TAG_VALUE);
			ser.text(packetCDATA(value));
			ser.endTag(null, TAG_VALUE);
			
			ser.startTag(null, TAG_TEXT);
			ser.text(packetCDATA(text));
			ser.endTag(null, TAG_TEXT);
			
			ser.startTag(null, TAG_DEFAULTFLAG);
			ser.text(packetCDATA(String.valueOf(defaultFlag)));
			ser.endTag(null, TAG_DEFAULTFLAG);
			
			ser.endTag(null, TAG_ITEM);
		}
	}
	
	public String buildXml(boolean all) throws IOException {
		StringWriter writer = new StringWriter(); 
		
		XmlSerializer ser = Xml.newSerializer();
		ser.setOutput(writer);
		
		ser.startTag(null, TAG_PROCESS_ID);
		ser.text(packetCDATA(processId));
		ser.endTag(null, TAG_PROCESS_ID);
		
		ser.startTag(null, TAG_HANDLE_ID);
		ser.text(packetCDATA(handleId));
		ser.endTag(null, TAG_HANDLE_ID);
		
		ser.startTag(null, TAG_PROCESS_TITLE);
		ser.text(packetCDATA(processTitle));
		ser.endTag(null, TAG_PROCESS_TITLE);
		
		if(all) {
			ser.startTag(null, TAG_ACTIVITYS);
			for(Activity activity : activityList) {
				activity.buildXml(ser);
			}
			ser.endTag(null, TAG_ACTIVITYS);
		}
		
		ser.startTag(null, TAG_FIELDS);
		for(Field field : filedList) {
			field.buildXml(ser);
		}
		ser.endTag(null, TAG_FIELDS);
		ser.flush();
		
		String xml = writer.toString();
		xml = xml.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
		LogUtil.i("ProcessContentInfo", "buildXml(), xml="+xml);
		return xml;
	}
	
	public static String packetCDATA(String text) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<![CDATA[").append(text).append("]]>");
		return buffer.toString();
	} 

}
