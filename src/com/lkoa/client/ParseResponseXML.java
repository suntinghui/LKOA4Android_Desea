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
import com.lkoa.model.CenterMsgNewsItem;
import com.lkoa.model.IdCountItem;
import com.lkoa.model.WindowDepartmentItem;

public class ParseResponseXML {
	
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
					attachment.size = Integer.parseInt(parser.nextText());
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
		List<WindowDepartmentItem> list = null;
		WindowDepartmentItem one = null;
		WindowDepartmentItem two = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("Infor".equalsIgnoreCase(parser.getName())) {
					list = new ArrayList<WindowDepartmentItem>();
					
				} else if("one".equalsIgnoreCase(parser.getName())) {
					//一级栏目
					one = new WindowDepartmentItem();
					
				} else if("id".equalsIgnoreCase(parser.getName())) {
					//栏目序号
					if(two != null) {
						two.id = parser.nextText();
					} else {
						one.id = parser.nextText();
					}
					
				} else if("name".equalsIgnoreCase(parser.getName())) {
					//栏目名称
					if(two != null) {
						two.name = parser.nextText();
					} else {
						one.name = parser.nextText();
					}
					
				} else if("count".equalsIgnoreCase(parser.getName())) {
					//数量
					if(two != null) {
						two.count = Integer.parseInt(parser.nextText());
					} else {
						one.count = Integer.parseInt(parser.nextText());
					}
					
				} else if("two".equalsIgnoreCase(parser.getName())) {
					//二级栏目
					two = new WindowDepartmentItem();
				}
				break;
				
			case XmlPullParser.END_TAG:
				if("one".equalsIgnoreCase(parser.getName())) {
					//一级栏目
					list.add(one);
					one = null;
					
				} else if("two".equalsIgnoreCase(parser.getName())) {
					//二级栏目
					one.list.add(two);
					two = null;
				}
				break;
			}
			eventType = parser.next();
		}
		
		return list;
	}
}
