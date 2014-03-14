package com.lkoa.business;

import java.net.URLEncoder;
import java.util.HashMap;

import com.lkoa.client.Constant;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.client.LKHttpRequest;
import com.lkoa.client.LKHttpRequestQueue;
import com.lkoa.client.LKHttpRequestQueueDone;
import com.lkoa.client.TransferRequestTag;

public class MySmsManager {

	/**
	 * 我的短信-列表
	 */
	public void getSmsList(String sUserId, String sType, final LKAsyncHttpResponseHandler handler) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.kWEBSERVICENAME, "WebService.asmx");
		map.put(Constant.kMETHODNAME, TransferRequestTag.GET_SMS_LIST);
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sUserId", sUserId);
		paramMap.put("sType", sType);
		map.put(Constant.kPARAMNAME, paramMap);
		
		execute(map, handler);
	}
	
	/**
	 * 我的短信-发短信
	 * @param sSJH	接收手机号，多个时用英文逗号分割
	 * @param sJSR	接收人姓名，多个时用英文逗号分隔
	 */
	public void writeSMS(String sUserId, String sSJH, 
			String sJSR, String content, final LKAsyncHttpResponseHandler handler) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.kWEBSERVICENAME, "WebService.asmx");
		map.put(Constant.kMETHODNAME, TransferRequestTag.WRITE_SMS);
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sUserId", sUserId);
		paramMap.put("sSJH", URLEncoder.encode(sSJH));
		paramMap.put("sJSR", URLEncoder.encode(sJSR));
		paramMap.put("SMSContent", URLEncoder.encode(content));
		map.put(Constant.kPARAMNAME, paramMap);
		
		execute(map, handler);
	}
	
	private void execute(HashMap<String, Object> map, LKAsyncHttpResponseHandler handler) {
		LKHttpRequest req1 = new LKHttpRequest(map, handler);
		new LKHttpRequestQueue().addHttpRequest(req1)
		.executeQueue("正在加载数据..", new LKHttpRequestQueueDone(){

			@Override
			public void onComplete() {
				super.onComplete();
			}
		});
	}
}
