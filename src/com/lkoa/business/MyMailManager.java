package com.lkoa.business;

import java.util.HashMap;

import com.lkoa.client.Constant;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.client.LKHttpRequest;
import com.lkoa.client.LKHttpRequestQueue;
import com.lkoa.client.LKHttpRequestQueueDone;
import com.lkoa.client.TransferRequestTag;

public class MyMailManager {

	/**
	 * 我的邮件-条数
	 */
	public void getMailCount(String sUserId, final LKAsyncHttpResponseHandler handler) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.kWEBSERVICENAME, "WebService.asmx");
		map.put(Constant.kMETHODNAME, TransferRequestTag.GET_MAIL_COUNT);
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sUserId", sUserId);
		map.put(Constant.kPARAMNAME, paramMap);
		
		execute(map, handler);
	}
	
	/**
	 * 我的邮件-列表
	 */
	public void getMailList(String state, String sType, String sUserId, final LKAsyncHttpResponseHandler handler) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.kWEBSERVICENAME, "WebService.asmx");
		map.put(Constant.kMETHODNAME, TransferRequestTag.GET_MAIL_LIST);
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sUserId", sUserId);
		paramMap.put("State", state);
		paramMap.put("sType", sType);
		map.put(Constant.kPARAMNAME, paramMap);
		
		execute(map, handler);
	}
	
	/**
	 * 我的邮件-内容
	 */
	public void getMail(String sUserId, String mailId, final LKAsyncHttpResponseHandler handler) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.kWEBSERVICENAME, "WebService.asmx");
		map.put(Constant.kMETHODNAME, TransferRequestTag.GET_MAIL);
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sUserId", sUserId);
		paramMap.put("MailId", mailId);
		map.put(Constant.kPARAMNAME, paramMap);
		
		execute(map, handler);
	}
	
	/**
	 * 我的邮件-写邮件
	 */
	public void writeMail(String sSendUserId, String sUserIds, 
			String mailId, String mailTitle, String mailContent,
			String state, final LKAsyncHttpResponseHandler handler) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.kWEBSERVICENAME, "WebService.asmx");
		map.put(Constant.kMETHODNAME, TransferRequestTag.WRITE_MAIL);
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sSendUserId", sSendUserId);
		paramMap.put("sUserIds", sUserIds);
		paramMap.put("MailId", mailId);
		paramMap.put("MailTitle", mailTitle);
		paramMap.put("MailContent", mailContent);
		paramMap.put("State", state);
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
