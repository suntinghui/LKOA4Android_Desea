package com.lkoa.client;

import java.util.HashMap;

import android.annotation.SuppressLint;

@SuppressLint("UseSparseArrays")
public class TransferRequestTag {
	
	public static final int LOGIN						= 1 ;	//登陆
	public static final int GET_GLZX_COUNT = 2;	//获取管理中心条数
	public static final int GET_XXZX_COUNT = 3;	//获取信息中心条数
	public static final int GET_XX_LIST = 4;	//获取信息列表
	public static final int GET_XX = 5;	//获取信息详情
	public static final int GET_TZ_LIST = 6;	//获取通知信息列表
	public static final int GET_TZ = 7;	//信息中心-通知信息内容
	public static final int GET_BMZC = 8;	//信息中心-部门之窗条数
	public static final int GET_LCGL_COUNT = 9;	//流程管理-条数
	public static final int GET_LC_LIST = 10;	//流程管理-列表
	public static final int GET_LCBD = 11;	//流程管理-流程表单
	public static final int GET_SMS_LIST = 12;	//我的短信-列表
	public static final int WRITE_SMS = 13;	//我的短信-列表
	public static final int GET_MAIL_COUNT = 14;	//我的邮件-条数
	public static final int GET_MAIL_LIST = 15;	//我的邮件-列表
	
	private static HashMap<Integer, String> requestTagMap 	= null;
	
	public static HashMap<Integer, String> getRequestTagMap(){
		if (null == requestTagMap) {
			requestTagMap = new HashMap<Integer, String>();
			
			requestTagMap.put(LOGIN, "Login");
			requestTagMap.put(GET_GLZX_COUNT, "GetGLZXCount");
			requestTagMap.put(GET_XXZX_COUNT, "GetXXZXCount");
			requestTagMap.put(GET_XX_LIST, "GetXXList");
			requestTagMap.put(GET_XX, "GetXX");
			requestTagMap.put(GET_TZ_LIST, "GetTZList");
			requestTagMap.put(GET_TZ, "GetTZ");
			requestTagMap.put(GET_BMZC, "GetBMZC");
			requestTagMap.put(GET_LCGL_COUNT, "GetLCGLCount");
			requestTagMap.put(GET_LC_LIST, "GetLCList");
			requestTagMap.put(GET_LCBD, "GetLCBD");
			requestTagMap.put(GET_SMS_LIST, "GetSMSList");
			requestTagMap.put(GET_MAIL_COUNT, "GetMailCount");
			requestTagMap.put(GET_MAIL_LIST, "GetMailList");
		}
		
		return requestTagMap;
	}

}
