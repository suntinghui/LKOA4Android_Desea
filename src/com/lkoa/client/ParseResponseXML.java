package com.lkoa.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import com.lkoa.model.Attachment;
import com.lkoa.model.BDCBTable;
import com.lkoa.model.BDCBTable.Column;
import com.lkoa.model.BDCBTable.Row;
import com.lkoa.model.CenterMsgNewsItem;
import com.lkoa.model.ContactItem;
import com.lkoa.model.DepartmentItem;
import com.lkoa.model.IdCountItem;
import com.lkoa.model.JTHDContentItem;
import com.lkoa.model.MailContentItemInfo;
import com.lkoa.model.MailItemInfo;
import com.lkoa.model.ProcessContentInfo;
import com.lkoa.model.ProcessContentInfo.Activity;
import com.lkoa.model.ProcessContentInfo.Field;
import com.lkoa.model.ProcessContentInfo.Option;
import com.lkoa.model.ProcessContentInfo.User;
import com.lkoa.model.ProcessItem;
import com.lkoa.model.RCContentItem;
import com.lkoa.model.RCListItem;
import com.lkoa.model.SmsMessage;
import com.lkoa.model.WindowDepartmentItem;
import com.lkoa.util.LogUtil;

public class ParseResponseXML {
	private static final String TAG = "ParseResponseXML";
	
	private static InputStream inStream = null;
	
	public static Object parseXML(int reqType, String responseStr) {
		// 在项目管理与资金管理的报文中，在内部又有XML的头标签，需要去掉，否则解析出错。是不是觉得很恶心。。。
		responseStr = responseStr.replace("&lt;", "<").replace("&gt;", ">").replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "").
				replace("<?xml version=\'1.0\' encoding=\'UTF-8\'?>","");
		
		Log.e("response:", responseStr);
		
		try {
			inStream = new ByteArrayInputStream(responseStr.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		try{
			switch(reqType){
			case TransferRequestTag.LOGIN:
				//登陆
				return login();
				
			case TransferRequestTag.GET_GLZX_COUNT:
				//获取管理中心条数
				return getGLZXCount();
				
			case TransferRequestTag.GET_XXZX_COUNT:
				//信息中心-条数
				return getXXZXCount();
				
			case TransferRequestTag.GET_XX_LIST:
				//信息中心-集团新闻列表
				return getXXList();
				
			case TransferRequestTag.GET_XX:
				//信息中心-集团新闻内容
				return getXX();
				
			case TransferRequestTag.GET_TZ_LIST:
				//信息中心-通知信息列表
				return getTZList();
				
			case TransferRequestTag.GET_TZ:
				//信息中心-通知信息内容
				return getTZ();
				
			case TransferRequestTag.GET_BMZC:
				//信息中心-部门之窗条数
				return getBMZC();
				
			case TransferRequestTag.GET_LCGL_COUNT:
				//流程管理-条数
				return getLCGLCount();
				
			case TransferRequestTag.GET_LC_LIST:
				//流程管理-列表
				return getLCList();
				
			case TransferRequestTag.GET_LCBD:
				//流程管理-流程表单
				return getLCBD();
				
			case TransferRequestTag.GET_SMS_LIST:
				//我的短信-列表
				return getSmsList();
				
			case TransferRequestTag.WRITE_SMS:
				//我的短信-写短信
				return writeSms();
				
			case TransferRequestTag.GET_MAIL_COUNT:
				//我的邮件-条数
				return getMailCount();
				
			case TransferRequestTag.GET_MAIL_LIST:
				//我的邮件-内部邮件-列表
				return getMailList();
				
			case TransferRequestTag.GET_LCZW:
				//流程管理-正文
				return getLCZW();
				
			case TransferRequestTag.GET_ATT_LIST:
				//流程管理-附件
				return getAttList();
				
			case TransferRequestTag.SET_GLBD:
				//流程管理-表单保存或提交
				return setGLBD();
				
			case TransferRequestTag.GET_SYSADDRESS_BOOK:
				//通讯录-联系人列表
				return getContacts();
				
			case TransferRequestTag.GET_DEPT:
				//通讯录-部门列表
				return getDept();
				
			case TransferRequestTag.GET_ATT:
				//附件-内容
				return getAtt();
				
			case TransferRequestTag.GET_RC_COUNT:
				//日程-条数
				return getRCCount();
				
			case TransferRequestTag.GET_RC_LIST:
				//日程-列表
				return getRCList();
				
			case TransferRequestTag.GET_RC:
				//日程-列表内容
				return getRC();
				
			case TransferRequestTag.GET_JTHD_LIST:
				//日程-集团活动列表
				return getJTHDList();
				
			case TransferRequestTag.GET_JTHD:
				//日程-集团活动内容
				return getJTHD();
				
			case TransferRequestTag.GET_MAIL:
				//我的邮件-获取邮件内容
				return getMail();
				
			case TransferRequestTag.WRITE_MAIL:
				//我的邮件-获取邮件内容
				return writeMail();
				
			case TransferRequestTag.DEL_MAIL:
				//我的邮件-删除
				return delMail();
				
			case TransferRequestTag.GET_GWGL_COUNT:
				//公文管理-条数
				return getGWGLCount();
				
			case TransferRequestTag.GET_LCBD_CB:
				//流程管理-流程办理-从表
				return getLCBDCB();
				
			case TransferRequestTag.GET_GLLC_LIST:
				//流程管理-流程办理-关联流程列表
				return getGLLCList();
			}
			
		} catch(XmlPullParserException e){
			e.printStackTrace();
			
		} catch(IOException e){
			e.printStackTrace();
			
		} finally{
			try {
				if (null != inStream)
					inStream.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	
	private static Object login() throws XmlPullParserException, IOException{
		// 如果程序存在Qry标签，则说明登录成功，返回HASHMAP；如果没有Qry标签，则说明登录失败，则返回String，代表错误码。
		HashMap<String, String> respMap = null;
		String errorCode = null;
		String returnObject = null;
		boolean isLoginSuccess = false;
		
		XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inStream, "UTF-8");  
        int eventType = parser.getEventType();//产生第一个事件  
        while(eventType!=XmlPullParser.END_DOCUMENT){  
            switch(eventType){
            case XmlPullParser.START_TAG:
				if ("LoginResult".equalsIgnoreCase(parser.getName())){
					returnObject = parser.nextText();
				} 
				break;
			}
            eventType = parser.next();
        }
		
		return returnObject;
	}
	
	/**
	 * 管理中心-条数
	 */
	private static Object getGLZXCount() throws XmlPullParserException, IOException{
		Object retObj = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("GetGLZXCountResult".equalsIgnoreCase(parser.getName())) {
					retObj = parser.nextText();
				}
				break;
			}
			eventType = parser.next();
		}

		return retObj;
	}
	
	/**
	 * 信息中心-条数
	 */
	private static Object getXXZXCount() throws XmlPullParserException, IOException{
		Map<String, IdCountItem> retMap = new HashMap<String, IdCountItem>();
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		IdCountItem item = null;
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("JDXW".equalsIgnoreCase(parser.getName())) {
					item = new IdCountItem();
					retMap.put("JDXW", item);
				} else if("JDGG".equalsIgnoreCase(parser.getName())) {
					item = new IdCountItem();
					retMap.put("JDGG", item);
				} else if("TZXX".equalsIgnoreCase(parser.getName())) {
					item = new IdCountItem();
					retMap.put("TZXX", item);
				} else if("id".equalsIgnoreCase(parser.getName())) {
					item.id = parser.nextText();
				} else if("count".equalsIgnoreCase(parser.getName())) {
					item.count = Integer.parseInt(parser.nextText());
				}
				break;
				
			case XmlPullParser.END_TAG:
				if("JDXW".equalsIgnoreCase(parser.getName()) 
						|| "JDGG".equalsIgnoreCase(parser.getName())
						|| "TZXX".equalsIgnoreCase(parser.getName())) {
					item = null;
				}
				break;
			}
			eventType = parser.next();
		}

		return retMap;
	}

	/**
	 * 信息中心-集团新闻列表
	 */
	private static Object getXXList() throws XmlPullParserException, IOException{
		List<CenterMsgNewsItem> list = new ArrayList<CenterMsgNewsItem>();
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		CenterMsgNewsItem item = null;
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					item = new CenterMsgNewsItem();
					
				} else if("IFC_30_COL_10".equalsIgnoreCase(parser.getName())) {
					//序号
					item.id = parser.nextText();
					
				} else if("IFC_30_COL_20".equalsIgnoreCase(parser.getName())) {
					//标题
					item.title = parser.nextText();
					
				} else if("IFC_30_COL_90".equalsIgnoreCase(parser.getName())) {
					//内容
					item.content = parser.nextText();
					
				} else if("IFC_30_COL_160".equalsIgnoreCase(parser.getName())) {
					//时间
					item.date = parser.nextText();
					
				} else if("IFC_50_COL_30".equalsIgnoreCase(parser.getName())) {
					//图片路径
					item.iconUrl = parser.nextText();
				} else if("state".equalsIgnoreCase(parser.getName())) {
					//状态标志，0-未读	1-已读
					item.state = parser.nextText();
					LogUtil.i(TAG, "getXXList(), state=" + item.state);
				} 
				break;
				
			case XmlPullParser.END_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					list.add(item);
					item = null;
				}
				break;
			}
			eventType = parser.next();
		}
		
		return list;
	}

	/**
	 * 信息中心-集团新闻内容
	 */
	private static Object getXX() throws XmlPullParserException, IOException{
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		CenterMsgNewsItem item = null;
		Attachment attachment = null;
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					item = new CenterMsgNewsItem();
					
				} else if("IFC_30_COL_10".equalsIgnoreCase(parser.getName())) {
					//序号
					item.id = parser.nextText();
					
				} else if("IFC_30_COL_20".equalsIgnoreCase(parser.getName())) {
					//标题
					item.title = parser.nextText();
					
				} else if("IFC_30_COL_90".equalsIgnoreCase(parser.getName())) {
					//内容
					item.content = parser.nextText();
					
				} else if("IFC_30_COL_160".equalsIgnoreCase(parser.getName())) {
					//时间
					item.date = parser.nextText();
					
				} else if("IFC_50_COL_30".equalsIgnoreCase(parser.getName())) {
					//图片路径
					item.iconUrl = parser.nextText();
				} else if("Fjs".equalsIgnoreCase(parser.getName())) {
					//附件列表
					item.attachments = new ArrayList<Attachment>();
					parseFJs(parser, item.attachments);
					
				}
				break;
			}
			eventType = parser.next();
		}
		
		return item;
	}
	
	/**
	 * 信息中心-通知信息列表
	 */
	private static Object getTZList() throws XmlPullParserException, IOException{
		List<CenterMsgNewsItem> list = new ArrayList<CenterMsgNewsItem>();
		CenterMsgNewsItem item = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					item = new CenterMsgNewsItem();
					
				} else if("NTE_30_COL_10".equalsIgnoreCase(parser.getName())) {
					//序号
					item.id = parser.nextText();
					
				} else if("NTE_30_COL_20".equalsIgnoreCase(parser.getName())) {
					//标题
					item.title = parser.nextText();
					
				} else if("NTE_30_COL_40".equalsIgnoreCase(parser.getName())) {
					//内容
					item.content = parser.nextText();
					
				} else if("NTE_30_COL_150".equalsIgnoreCase(parser.getName())) {
					//时间
					item.date = parser.nextText();
					
				} else if("state".equalsIgnoreCase(parser.getName())) {
					//状态标志， 0-未读	1-已读
					item.state = parser.nextText();
					LogUtil.i(TAG, "getTZList(), state=" + item.state);
					
				}
				break;
				
			case XmlPullParser.END_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					list.add(item);
					item = null;
				}
				break;
			}
			eventType = parser.next();
		}
		
		return list;
	}
	
	/**
	 * 信息中心-通知信息内容
	 */
	private static Object getTZ() throws XmlPullParserException, IOException{
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		CenterMsgNewsItem item = null;
		Attachment attachment = null;
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					item = new CenterMsgNewsItem();
					
				} else if("NTE_30_COL_10".equalsIgnoreCase(parser.getName())) {
					//序号
					item.id = parser.nextText();
					
				} else if("NTE_30_COL_20".equalsIgnoreCase(parser.getName())) {
					//标题
					item.title = parser.nextText();
					
				} else if("NTE_30_COL_40".equalsIgnoreCase(parser.getName())) {
					//内容
					item.content = parser.nextText();
					
				} else if("NTE_30_COL_150".equalsIgnoreCase(parser.getName())) {
					//时间
					item.date = parser.nextText();
					
				} else if("Fjs".equalsIgnoreCase(parser.getName())) {
					//附件列表
					item.attachments = new ArrayList<Attachment>();
					
				} else if("Fj".equalsIgnoreCase(parser.getName())) {
					attachment = new Attachment();
				} else if("Id".equalsIgnoreCase(parser.getName())) {
					attachment.id = parser.nextText();
				} else if("Title".equalsIgnoreCase(parser.getName())) {
					attachment.title = parser.nextText();
				} else if("Type".equalsIgnoreCase(parser.getName())) {
					attachment.type = parser.nextText();
				} else if("Size".equalsIgnoreCase(parser.getName())) {
					attachment.size = parser.nextText();
				}
				break;
				
			case XmlPullParser.END_TAG:
				if("Fj".equalsIgnoreCase(parser.getName())) {
					item.attachments.add(attachment);
					attachment = null;
				}
				break;
			}
			eventType = parser.next();
		}
		
		return item;
	}
	
	/**
	 * 信息中心-部门之窗条数
	 */
	private static Object getBMZC() throws XmlPullParserException, IOException{
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		
		List<WindowDepartmentItem> list = new ArrayList<WindowDepartmentItem>();
		parseWinDept(parser, list);
		
		return list;
	}
	
	private static void parseWinDept(XmlPullParser parser, List<WindowDepartmentItem> list) throws XmlPullParserException, IOException {
		int eventType = parser.getEventType();
		if(eventType == XmlPullParser.END_DOCUMENT) return;
		
		WindowDepartmentItem item = null;
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("Infors".equalsIgnoreCase(parser.getName())) {
					if(item != null) {
						parseWinDept(parser, item.list);
					}
					
				} else if ("Infor".equalsIgnoreCase(parser.getName())) {
					item = new WindowDepartmentItem();
					
				} else if("id".equalsIgnoreCase(parser.getName())) {
					//栏目序号
					item.id = parser.nextText();
					
				} else if("name".equalsIgnoreCase(parser.getName())) {
					//栏目名称
					item.name = parser.nextText();
					
				} else if("count".equalsIgnoreCase(parser.getName())) {
					//数量
					item.count = parser.nextText();
				}
				break;
				
			case XmlPullParser.END_TAG:
				if("Infors".equalsIgnoreCase(parser.getName())) {
					return;
					
				} else if("Infor".equalsIgnoreCase(parser.getName())) {
					list.add(item);
					item = null;
				}
				break;
			}
			eventType = parser.next();
		}
	}
	
	/**
	 * 流程管理-条数
	 */
	private static Object getLCGLCount() throws XmlPullParserException, IOException{
		Object retObj = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("GetLCGLCountResult".equalsIgnoreCase(parser.getName())) {
					retObj = parser.nextText();
				}
				break;
			}
			eventType = parser.next();
		}

		return retObj;
	}
	
	/**
	 * 流程管理-列表
	 */
	private static Object getLCList() throws XmlPullParserException, IOException{
		List<ProcessItem> list = new ArrayList<ProcessItem>();
		ProcessItem item = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					item = new ProcessItem();
					
				} else if("FEG_20_COL_10".equalsIgnoreCase(parser.getName())) {
					//序号
					item.id = parser.nextText();
					
				} else if("FEG_20_COL_20".equalsIgnoreCase(parser.getName())) {
					//标题
					item.title = parser.nextText();
					
				} else if("FEG_15_COL_20".equalsIgnoreCase(parser.getName())) {
					//类型
					item.type = parser.nextText();
					
				} else if("FRM_20_COL_20".equalsIgnoreCase(parser.getName())) {
					//任务
					item.task = parser.nextText();
					
				} else if("SYS_30_COL_30".equalsIgnoreCase(parser.getName())) {
					//发送人
					item.sender = parser.nextText();
					
				}
				break;
				
			case XmlPullParser.END_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					list.add(item);
					item = null;
				}
				break;
			}
			eventType = parser.next();
		}
		
		return list;
	}
	
	/**
	 * 流程管理-流程表单
	 */
	private static Object getLCBD() throws XmlPullParserException, IOException{
		ProcessContentInfo info = null;
		Activity activity = null;
		Field field = null;
		User zbr = null;
		User cyr = null;
		Option option = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					info = new ProcessContentInfo();
					
				} else if("FEG_20_COL_10".equalsIgnoreCase(parser.getName())) {
					//流程序号
					info.processId = parser.nextText();
					
				} else if("FEG_30_COL_10".equalsIgnoreCase(parser.getName())) {
					//办理序号
					info.handleId = parser.nextText();
					
				} else if("FEG_20_COL_20".equalsIgnoreCase(parser.getName())) {
					//流程标题
					info.processTitle = parser.nextText();
					
				} else if("Activity".equalsIgnoreCase(parser.getName())) {
					//活动
					activity = info.newActivity();
					
				} else if("Select".equalsIgnoreCase(parser.getName())) {
					//是否选择此节点
					activity.select = Integer.parseInt(parser.nextText());
				} else if("Id".equalsIgnoreCase(parser.getName())) {
					//节点序号
					activity.id = Integer.parseInt(parser.nextText());
				} else if("Name".equalsIgnoreCase(parser.getName())) {
					//节点名称
					activity.name = parser.nextText();
				} else if("Type".equalsIgnoreCase(parser.getName())) {
					//节点类型
					activity.type = Integer.parseInt(parser.nextText());
				} else if("Mode".equalsIgnoreCase(parser.getName())) {
					//节点模式
					activity.mode = Integer.parseInt(parser.nextText());
				} else if("DealTime".equalsIgnoreCase(parser.getName())) {
					//节点办理时限
					activity.dealTime = parser.nextText();
				} else if("Interpose".equalsIgnoreCase(parser.getName())) {
					//是否重置主办人
					activity.interpose = parser.nextText();
				} else if("ReSelect".equalsIgnoreCase(parser.getName())) {
					//是否重置参与人
					activity.reSelect = parser.nextText();
				} else if("ZBR".equalsIgnoreCase(parser.getName())) {
					//主办人
					zbr = info.newUser();
				} else if("UserId".equalsIgnoreCase(parser.getName())) {
					//用户id
					if(zbr != null) {
						zbr.userId = parser.nextText();
					} else {
						cyr.userId = parser.nextText();
					}
				} else if("UserName".equalsIgnoreCase(parser.getName())) {
					//用户名称
					if(zbr != null) {
						zbr.userName = parser.nextText();
					} else if(cyr != null) {
						cyr.userName = parser.nextText();
					}
				} else if("CYR".equalsIgnoreCase(parser.getName())) {
					//用户名称
					cyr = info.newUser();
				} else if("Field".equalsIgnoreCase(parser.getName())) {
					//属性
					field = info.newFiled();
				} else if("LK_COLFIELDID".equalsIgnoreCase(parser.getName())) {
					//数据项标识
					field.id = parser.nextText();
				} else if("LK_FIELDEDIT_TIPNAME".equalsIgnoreCase(parser.getName())) {
					//数据项名称
					field.name = parser.nextText();
				} else if("LK_FLDDBTYPE".equalsIgnoreCase(parser.getName())) {
					//数据类型
					field.type = Integer.parseInt(parser.nextText());
				} else if("LK_FIELDEDITMODE".equalsIgnoreCase(parser.getName())) {
					//处理模式
					try {
						field.editMode = Integer.parseInt(parser.nextText());
					} catch(Exception e) {
						field.editMode = 0;
						LogUtil.i(TAG, "Exception: editMode is not int, set to mode 0");
					}
					
				} else if("SHOWCONTENT".equalsIgnoreCase(parser.getName())) {
					//显示内容
					field.showContent = parser.nextText();
				} else if("Value".equalsIgnoreCase(parser.getName())) {
					if(option == null) {
						field.value = parser.nextText();
					} else {
						option.value = parser.nextText();
					}
				} else if("Item".equalsIgnoreCase(parser.getName())) {
					//Option
					option = info.newOption();
				} else if("Text".equalsIgnoreCase(parser.getName())) {
					//Option显示值
					if(option != null) option.text = parser.nextText();
				} else if("DefaultFlag".equalsIgnoreCase(parser.getName())) {
					//Option 是否默认选项
					option.defaultFlag = Integer.parseInt(parser.nextText());
				}
				break;
				
			case XmlPullParser.END_TAG:
				if ("ZBR".equalsIgnoreCase(parser.getName())) {
					activity.zbr = zbr;
					zbr = null;
				} else if ("CYR".equalsIgnoreCase(parser.getName())) {
					activity.cyrs.add(cyr);
					cyr = null;
				} else if("Activity".equalsIgnoreCase(parser.getName())) {
					//活动
					info.activityList.add(activity);
					activity = null;
				} else if("Field".equalsIgnoreCase(parser.getName())) {
					//属性
					info.filedList.add(field);
					field = null;
				} else if("Item".equalsIgnoreCase(parser.getName())) {
					field.optionList.add(option);
					option = null;;
				}
				break;
			}
			eventType = parser.next();
		}
		
		return info;
	}
	
	/**
	 * 我的短信-列表
	 */
	private static Object getSmsList() throws XmlPullParserException, IOException{
		List<SmsMessage> list = new ArrayList<SmsMessage>();
		SmsMessage item = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					item = new SmsMessage();
					
				} else if("JSR".equalsIgnoreCase(parser.getName())) {
					//接收人
					item.jsr = parser.nextText();
					
				} else if("JSRHM".equalsIgnoreCase(parser.getName())) {
					//接收人手机号
					item.jsrPhone = parser.nextText();
					
				} else if("NR".equalsIgnoreCase(parser.getName())) {
					//短信内容
					item.content = parser.nextText();
					
				} else if("Date".equalsIgnoreCase(parser.getName())) {
					//发送时间
					item.date = parser.nextText();
					
				}
				break;
				
			case XmlPullParser.END_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					list.add(item);
					item = null;
				}
				break;
			}
			eventType = parser.next();
		}
		
		return list;
	}
	
	/**
	 * 我的短信-写短信
	 */
	private static Object writeSms() throws XmlPullParserException, IOException{
		String result = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("WriteSMSResult".equalsIgnoreCase(parser.getName())) {
					result = parser.nextText();
				}
				break;
			}
			eventType = parser.next();
		}
		
		return result;
	}
	
	/**
	 * 我的邮件-条数
	 */
	private static Object getMailCount() throws XmlPullParserException, IOException{
		int count = 0;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("GetMailCountResult".equalsIgnoreCase(parser.getName())) {
					count = Integer.parseInt(parser.nextText());
				}
				break;
			}
			eventType = parser.next();
		}
		return count;
	}
	
	/**
	 * 我的邮件-内部邮件-列表
	 */
	private static Object getMailList() throws XmlPullParserException, IOException{
		List<MailItemInfo> list = new ArrayList<MailItemInfo>();
		MailItemInfo item = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					item = new MailItemInfo();
					
				} else if("EML_20_COL_10".equalsIgnoreCase(parser.getName())) {
					item.id = parser.nextText();
					
				} else if("EML_30_COL_10".equalsIgnoreCase(parser.getName())) {
					item.jsId = parser.nextText();
					
				} else if("EML_20_COL_20".equalsIgnoreCase(parser.getName())) {
					item.subject = parser.nextText();
					
				} else if("SYS_30_COL_30".equalsIgnoreCase(parser.getName())) {
					item.sender = parser.nextText();
					
				} else if("EML_20_COL_100".equalsIgnoreCase(parser.getName())) {
					item.date = parser.nextText();
					
				} else if("FJCounts".equalsIgnoreCase(parser.getName())) {
					item.fjCount = Integer.parseInt(parser.nextText());
					
				}  else if("state".equalsIgnoreCase(parser.getName())) {
					try {
						item.state = Integer.parseInt(parser.nextText());
					} catch(Exception e) {
						item.state = 1;
					}
					
				}
				break;
				
			case XmlPullParser.END_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					list.add(item);
					item = null;
				}
				break;
			}
			eventType = parser.next();
		}
		
		return list;
	}
	
	/**
	 * 我的邮件-邮件内容
	 */
	private static Object getMail() throws XmlPullParserException, IOException{
		MailContentItemInfo item = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					item = new MailContentItemInfo();
					
				} else if("EML_20_COL_10".equalsIgnoreCase(parser.getName())) {
					item.id = parser.nextText();
					
				} else if("EML_20_COL_20".equalsIgnoreCase(parser.getName())) {
					item.title = parser.nextText();
					
				} else if("EML_20_COL_30".equalsIgnoreCase(parser.getName())) {
					item.content = parser.nextText();
					
				} else if("SYS_30_COL_30".equalsIgnoreCase(parser.getName())) {
					item.sender = parser.nextText();
					
				} else if("EML_20_COL_90".equalsIgnoreCase(parser.getName())) {
					item.senderId = parser.nextText();
					
				} else if("EML_20_COL_100".equalsIgnoreCase(parser.getName())) {
					item.date = parser.nextText();
					
				} else if("SJR".equalsIgnoreCase(parser.getName())) {
					item.sjr = parser.nextText();
					
				} else if("SJRId".equalsIgnoreCase(parser.getName())) {
					item.sjrId = parser.nextText();
					
				} else if("FJs".equalsIgnoreCase(parser.getName())) {
					item.attachments = new ArrayList<Attachment>();
					parseFJs(parser, item.attachments);
				}
				break;
			}
			eventType = parser.next();
		}
		
		return item;
	}
	
	/**
	 * 我的邮件-写邮件
	 */
	private static Object writeMail() throws XmlPullParserException, IOException{
		String result = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("WriteMail".equalsIgnoreCase(parser.getName())) {
					result = parser.nextText();
				}
				break;
			}
			eventType = parser.next();
		}
		
		return result;
	}
	
	/**
	 * 我的邮件-删除或清空
	 */
	private static Object delMail() throws XmlPullParserException, IOException{
		String result = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("DelMailResult".equalsIgnoreCase(parser.getName())) {
					result = parser.nextText();
				}
				break;
			}
			eventType = parser.next();
		}
		
		return result;
	}
	
	/**
	 * 流程管理-正文
	 */
	private static Object getLCZW() throws XmlPullParserException, IOException {
		String path = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("GetLCZWResult".equalsIgnoreCase(parser.getName())) {
					path = parser.nextText();
				}
				break;
			}
			eventType = parser.next();
		}
		return path;
	}
	
	/**
	 * 流程管理-附件
	 */
	private static Object getAttList() throws XmlPullParserException, IOException{
		List<Attachment> list = new ArrayList<Attachment>();
		Attachment item = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("FJ".equalsIgnoreCase(parser.getName())) {
					item = new Attachment();
					
				} else if("Id".equalsIgnoreCase(parser.getName())) {
					item.id = parser.nextText();
					
				} else if("Title".equalsIgnoreCase(parser.getName())) {
					item.title = parser.nextText();
					
				} else if("Type".equalsIgnoreCase(parser.getName())) {
					item.type = parser.nextText();
					
				} else if("Size".equalsIgnoreCase(parser.getName())) {
					item.size = parser.nextText();
					
				}
				break;
				
			case XmlPullParser.END_TAG:
				if ("FJ".equalsIgnoreCase(parser.getName())) {
					list.add(item);
					item = null;
				}
				break;
			}
			eventType = parser.next();
		}
		
		return list;
	}
	
	/**
	 * 流程管理-流程办理-保存或提交
	 */
	private static Object setGLBD() throws XmlPullParserException, IOException{
		String result = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("SetGLBDResult".equalsIgnoreCase(parser.getName())) {
					result = parser.nextText();
				}
				break;
			}
			eventType = parser.next();
		}
		
		return result;
	}
	
	/**
	 * 通讯录-列表
	 */
	private static Object getContacts() throws XmlPullParserException, IOException{
		List<ContactItem> list = new ArrayList<ContactItem>();
		ContactItem item = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					item = new ContactItem();
					
				} else if("USERID".equalsIgnoreCase(parser.getName())) {
					item.userId = parser.nextText();
					
				} else if("XM".equalsIgnoreCase(parser.getName())) {
					item.userName = parser.nextText();
					
				} else if("DEPTID".equalsIgnoreCase(parser.getName())) {
					item.deptId = parser.nextText();
					
				} else if("DEPT".equalsIgnoreCase(parser.getName())) {
					item.deptName = parser.nextText();
					
				} else if("BGDH".equalsIgnoreCase(parser.getName())) {
					item.bgdh = parser.nextText();
					
				} else if("YDDH".equalsIgnoreCase(parser.getName())) {
					item.yddh = parser.nextText();
					
				} else if("EMAIL".equalsIgnoreCase(parser.getName())) {
					item.email1 = parser.nextText();
					
				} else if("EMAIL2".equalsIgnoreCase(parser.getName())) {
					item.email2 = parser.nextText();
					
				}
				break;
				
			case XmlPullParser.END_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					list.add(item);
					item = null;
				}
				break;
			}
			eventType = parser.next();
		}
		
		return list;
	}
	
	/**
	 * 通讯录-部门列表
	 */
	private static Object getDept() throws XmlPullParserException, IOException{
		List<DepartmentItem> list = new ArrayList<DepartmentItem>();
		DepartmentItem item = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if("Infor".equalsIgnoreCase(parser.getName())) {
					item = new DepartmentItem();
					list.add(item);
					
				} else if("DeptId".equalsIgnoreCase(parser.getName())) {
					item.deptId = parser.nextText();
					
				} else if("DeptName".equalsIgnoreCase(parser.getName())) {
					item.deptName = parser.nextText();
					
				}
				break;
			}
			eventType = parser.next();
		}
		
		return list;
	}
	
	/**
	 * 附件-内容
	 */
	private static Object getAtt() throws XmlPullParserException, IOException{
		String result = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if("GetAttResult".equalsIgnoreCase(parser.getName())) {
					result = parser.nextText();
					
				}
				break;
			}
			eventType = parser.next();
		}
		
		return result;
	}
	
	/**
	 * 日程安排-条数
	 */
	private static Object getRCCount() throws XmlPullParserException, IOException{
		String result = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if("GetRCCountResult".equalsIgnoreCase(parser.getName())) {
					result = parser.nextText();
					
				}
				break;
			}
			eventType = parser.next();
		}
		
		return result;
	}
	
	private static Object getJTHDList() throws XmlPullParserException, IOException {
		return getRCList();
	}
	
	/**
	 * 日程-集团活动内容
	 */
	private static Object getJTHD() throws XmlPullParserException, IOException{
		JTHDContentItem item = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					item = new JTHDContentItem();
					
				} else if("Id".equalsIgnoreCase(parser.getName())) {
					item.id = parser.nextText();
					
				} else if("Title".equalsIgnoreCase(parser.getName())) {
					item.title = parser.nextText();
					
				} else if("HDDD".equalsIgnoreCase(parser.getName())) {
					item.hddd = parser.nextText();
					
				} else if("FQBM".equalsIgnoreCase(parser.getName())) {
					item.fqbm = parser.nextText();
					
				} else if("Date".equalsIgnoreCase(parser.getName())) {
					item.date = parser.nextText();
					
				} else if("State".equalsIgnoreCase(parser.getName())) {
					item.state = parser.nextText();
					
				} else if("CJLD ".equalsIgnoreCase(parser.getName())) {
					item.cjld = parser.nextText();
					
				} else if("CYR".equalsIgnoreCase(parser.getName())) {
					item.cyr = parser.nextText();
					
				} else if("NR".equalsIgnoreCase(parser.getName())) {
					item.nr = parser.nextText();
					
				}
				break;
			}
			eventType = parser.next();
		}
		
		return item;
	}
	
	/**
	 * 日程-列表
	 */
	private static Object getRCList() throws XmlPullParserException, IOException{
		List<RCListItem> list = new ArrayList<RCListItem>();
		RCListItem item = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					item = new RCListItem();
					
				} else if("Id".equalsIgnoreCase(parser.getName())) {
					item.id = parser.nextText();
					
				} else if("Title".equalsIgnoreCase(parser.getName())) {
					item.title = parser.nextText();
					
				} else if("FW".equalsIgnoreCase(parser.getName())) {
					item.fw = parser.nextText();
					
				} else if("Date".equalsIgnoreCase(parser.getName())) {
					item.date = parser.nextText();
					
				} else if("State".equalsIgnoreCase(parser.getName())) {
					item.state = parser.nextText();
					
				}
				break;
				
			case XmlPullParser.END_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					list.add(item);
					item = null;
				}
				break;
			}
			eventType = parser.next();
		}
		
		return list;
	}
	
	/**
	 * 日程-内容
	 */
	private static Object getRC() throws XmlPullParserException, IOException{
		RCContentItem item = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					item = new RCContentItem();
					
				} else if("Id".equalsIgnoreCase(parser.getName())) {
					item.id = parser.nextText();
					
				} else if("Title".equalsIgnoreCase(parser.getName())) {
					item.title = parser.nextText();
					
				} else if("FW".equalsIgnoreCase(parser.getName())) {
					item.fw = parser.nextText();
					
				} else if("Date".equalsIgnoreCase(parser.getName())) {
					item.date = parser.nextText();
					
				} else if("State".equalsIgnoreCase(parser.getName())) {
					item.state = parser.nextText();
					
				} else if("ZXR".equalsIgnoreCase(parser.getName())) {
					item.zxr = parser.nextText();
					
				} else if("CYR".equalsIgnoreCase(parser.getName())) {
					item.cyr = parser.nextText();
					
				} else if("NR".equalsIgnoreCase(parser.getName())) {
					item.nr = parser.nextText();
					
				} else if("FJs".equalsIgnoreCase(parser.getName())) {
					item.attachments = new ArrayList<Attachment>();
					parseFJs(parser, item.attachments);
				}
				break;
			}
			eventType = parser.next();
		}
		
		return item;
	}
	
	/**
	 * 公文管理-条数
	 */
	private static Object getGWGLCount() throws XmlPullParserException, IOException{
		String result = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if("GetGWGLCountResult".equalsIgnoreCase(parser.getName())) {
					result = parser.nextText();
					
				}
				break;
			}
			eventType = parser.next();
		}
		
		return result;
	}
	
	/**
	 * 流程管理-流程办理-从表
	 */
	private static Object getLCBDCB() throws XmlPullParserException, IOException{
		List<BDCBTable> list = new ArrayList<BDCBTable>();
		
		BDCBTable table = null;
		Row row = null;
		Column column = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("Table".equalsIgnoreCase(parser.getName())) {
					table = new BDCBTable();
					list.add(table);
					
				} else if("Thead".equalsIgnoreCase(parser.getName())) {
					table.head = table.newHead();
					
				} else if("Tbody".equalsIgnoreCase(parser.getName())) {
					table.body = table.newBody();
					
				} else if("Tr".equalsIgnoreCase(parser.getName())) {
					row = table.newRow();
					if(table.body != null) {
						table.body.rows.add(row);
					} else {
						table.head.row = row;
					}
					
				} else if("des".equalsIgnoreCase(parser.getName())) {
					String des = parser.nextText();
					column = table.newColumn();
					column.des = des;
					row.columns.add(column);
					
				}
				break;
			}
			eventType = parser.next();
		}
		
		return list;
	}
	
	/**
	 * 流程管理-流程办理-关联流程列表
	 */
	private static Object getGLLCList() throws XmlPullParserException, IOException{
		List<ProcessItem> list = new ArrayList<ProcessItem>();
		ProcessItem item = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					item = new ProcessItem();
					
				} else if("FEG_20_COL_10".equalsIgnoreCase(parser.getName())) {
					item.id = parser.nextText();
					
				} else if("FEG_20_COL_20".equalsIgnoreCase(parser.getName())) {
					item.title = parser.nextText();
					
				} else if("FEG_15_COL_20".equalsIgnoreCase(parser.getName())) {
					item.type = parser.nextText();
					
				} else if("FRM_20_COL_20".equalsIgnoreCase(parser.getName())) {
					item.task = parser.nextText();
					
				} else if("SYS_30_COL_30".equalsIgnoreCase(parser.getName())) {
					item.sender = parser.nextText();
					
				}
				break;
				
			case XmlPullParser.END_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					list.add(item);
					item = null;
				}
				break;
			}
			eventType = parser.next();
		}
		
		return list;
	}
	
	private static void parseFJs(XmlPullParser parser, List<Attachment> outList) throws XmlPullParserException, IOException {
		Attachment item = null;
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("FJ".equalsIgnoreCase(parser.getName())) {
					item = new Attachment();
					outList.add(item);
				} else if("Id".equalsIgnoreCase(parser.getName())) {
					item.id = parser.nextText();
					
				} else if("Title".equalsIgnoreCase(parser.getName())) {
					item.title = parser.nextText();
					
				} else if("Type".equalsIgnoreCase(parser.getName())) {
					item.type = parser.nextText();
					
				} else if("Size".equalsIgnoreCase(parser.getName())) {
					item.size = parser.nextText();
				}
				break;
				
			case XmlPullParser.END_TAG:
				if ("FJs".equalsIgnoreCase(parser.getName())) {
					return;
				}
				break;
			}
			eventType = parser.next();
		}
	}
}
