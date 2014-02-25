package com.lkoa.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.util.Xml;

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
			case TransferRequestTag.GETLOGININFOSERVICE:
				return login();
				
				
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
		boolean isLoginSuccess = false;
		
		XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inStream, "UTF-8");  
        int eventType = parser.getEventType();//产生第一个事件  
        while(eventType!=XmlPullParser.END_DOCUMENT){  
            switch(eventType){
            case XmlPullParser.START_TAG:
            	if ("Qry".equalsIgnoreCase(parser.getName())){
            		isLoginSuccess = true;
            		respMap = new HashMap<String, String>();
            	} else if ("UserID_LK".equalsIgnoreCase(parser.getName())){
            		respMap.put("UserID_LK", parser.nextText());
            	} else if ("UserName_LK".equalsIgnoreCase(parser.getName())){
            		respMap.put("UserName_LK", parser.nextText());
            	} else if ("DeptID_LK".equalsIgnoreCase(parser.getName())){
            		respMap.put("DeptID_LK", parser.nextText());
            	} else if ("DeptName_LK".equalsIgnoreCase(parser.getName())){
            		respMap.put("DeptName_LK", parser.nextText());
            	} else if ("PosID_LK".equalsIgnoreCase(parser.getName())){
            		respMap.put("PosID_LK", parser.nextText());
            	} else if ("ClassID_LK".equalsIgnoreCase(parser.getName())){
            		respMap.put("ClassID_LK", parser.nextText());
            	} else if ("LoginName".equalsIgnoreCase(parser.getName())){
            		respMap.put("LoginName", parser.nextText());
            	} else if ("UserPassWord_LK".equalsIgnoreCase(parser.getName())){
            		respMap.put("UserPassWord_LK", parser.nextText());
            	} else if ("UserPassWordDecode_LK".equalsIgnoreCase(parser.getName())){
            		respMap.put("UserPassWordDecode_LK", parser.nextText());
            	} else if ("EnableIdentity_LK".equalsIgnoreCase(parser.getName())){
            		respMap.put("EnableIdentity_LK", parser.nextText());
            	} else if ("WebService".equalsIgnoreCase(parser.getName())){
            		respMap.put("WebService", parser.nextText());
            	}
            	
            	break;
            	
            case XmlPullParser.TEXT:
            	// 不止一个，但是最后一个最我想要的。
            	errorCode = parser.getText();
            	break;
            	
            case XmlPullParser.END_TAG:
            	if ("getLoginInfoServiceResult".equalsIgnoreCase(parser.getName())){
            		if (isLoginSuccess){
            			Editor editor = ApplicationEnvironment.getInstance().getPreferences().edit();
            			editor.putString(Constants.kUSERID, respMap.get("UserID_LK"));
            			editor.putString(Constants.kDEPTID, respMap.get("DeptID_LK"));
            			editor.putString(Constants.kPASSWORD_MD5, respMap.get("UserPassWord_LK"));
            			editor.putString(Constants.kREALHOST, respMap.get("WebService"));
            			Log.e("联网地址：", null==respMap.get("WebService")?"":respMap.get("WebService"));
            			editor.commit();
            			
            			return respMap;
            		} else {
            			return errorCode;
            		}
            	}
            	break;
            }
            
            eventType = parser.next();
        }
		
		return null;
	}
	
}
