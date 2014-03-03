package com.lkoa.client;

import android.annotation.SuppressLint;
import java.util.HashMap;

@SuppressLint("UseSparseArrays")
public class TransferRequestTag {
	
	public static final int LOGIN						= 1 ;	//登陆
	public static final int GET_GLZX_COUNT = 2;	//获取管理中心条数
	public static final int GET_XXZX_COUNT = 3;	//获取信息中心条数
	public static final int GET_XX_LIST = 4;	//获取信息列表
	public static final int GET_XX = 5;	//获取信息详情
	
	private static HashMap<Integer, String> requestTagMap 	= null;
	
	public static HashMap<Integer, String> getRequestTagMap(){
		if (null == requestTagMap) {
			requestTagMap = new HashMap<Integer, String>();
			
			requestTagMap.put(LOGIN, "Login");
			requestTagMap.put(GET_GLZX_COUNT, "GetGLZXCount");
			requestTagMap.put(GET_XXZX_COUNT, "GetXXZXCount");
			requestTagMap.put(GET_XX_LIST, "GetXXList");
			requestTagMap.put(GET_XX, "GetXX");
		}
		
		return requestTagMap;
	}

}
