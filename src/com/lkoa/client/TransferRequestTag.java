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
	public static final int GET_LCZW = 16;	//流程管理-流程办理-正文
	public static final int GET_ATT_LIST = 17;	//流程管理-流程办理-附件
	public static final int SET_GLBD = 18;	//流程管理-流程办理-保存
	public static final int GET_SYSADDRESS_BOOK = 19;	//通讯录-列表
	public static final int GET_DEPT = 20;	//通讯录-部门列表
	public static final int GET_RC_COUNT = 21;	//日程管理-条数
	public static final int GET_ATT = 22;	//附件-内容
	public static final int GET_RC_LIST = 23;	//日程管理-列表
	public static final int GET_RC = 24;	//日程管理-内容
	public static final int GET_JTHD_LIST = 25;	//日程管理-集体活动列表
	public static final int GET_JTHD = 26;	//日程管理-集体活动内容
	public static final int GET_MAIL = 27;	//我的邮件-内部邮件-内容
	public static final int WRITE_MAIL = 28;	//我的邮件-内部邮件-写邮件
	public static final int GET_GWGL_COUNT = 29;	//公文管理-条数
	public static final int GET_LCBD_CB = 30;	//流程管理-流程办理-从表
	public static final int GET_GLLC_LIST = 31;	//流程管理-流程办理-关联流程列表
	public static final int DEL_MAIL = 32;	//我的邮件-内部邮件-删除
	public static final int GET_KAO_QIN = 33;	//人事管理-考勤记录
	public static final int GET_WDZX = 34;	//信息中心-文档中心
	public static final int ALL_RECEIVE = 35;	//我的待办-一键接收
	
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
			requestTagMap.put(GET_LCZW, "GetLCZW");
			requestTagMap.put(GET_ATT_LIST, "GetAttList");
			requestTagMap.put(SET_GLBD, "SetGLBD");
			requestTagMap.put(GET_SYSADDRESS_BOOK, "GetSysAddress_Book");
			requestTagMap.put(GET_DEPT, "GetDept");
			requestTagMap.put(GET_RC_COUNT, "GetRCCount");
			requestTagMap.put(GET_ATT, "GetAtt");
			requestTagMap.put(GET_RC_LIST, "GetRCList");
			requestTagMap.put(GET_RC, "GetRC");
			requestTagMap.put(GET_JTHD_LIST, "GetJTHDList");
			requestTagMap.put(GET_JTHD, "GetJTHD");
			requestTagMap.put(GET_MAIL, "GetMail");
			requestTagMap.put(WRITE_MAIL, "WriteMail");
			requestTagMap.put(DEL_MAIL, "DelMail");
			requestTagMap.put(GET_GWGL_COUNT, "GetGWGLCount");
			requestTagMap.put(WRITE_SMS, "WriteSMS");
			requestTagMap.put(GET_LCBD_CB, "GetLCBDCB");
			requestTagMap.put(GET_GLLC_LIST, "GetGLLCList");
			requestTagMap.put(GET_KAO_QIN, "GetKaoQin");
			requestTagMap.put(GET_WDZX, "GetWDZX");
			requestTagMap.put(ALL_RECEIVE, "AllReceive");
			
		}
		
		return requestTagMap;
	}

}
