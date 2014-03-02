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
			case TransferRequestTag.LOGIN:
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
	
}
