package com.lkoa.business;

import java.util.HashMap;

import com.lkoa.client.Constant;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.client.LKHttpRequest;
import com.lkoa.client.LKHttpRequestQueue;
import com.lkoa.client.LKHttpRequestQueueDone;
import com.lkoa.client.TransferRequestTag;

public class ContactsManager {

	/**
	 * 获取通讯录
	 */
	public void getSysAddress_Book(final LKAsyncHttpResponseHandler handler) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.kWEBSERVICENAME, "WebService.asmx");
		map.put(Constant.kMETHODNAME, TransferRequestTag.GET_SYSADDRESS_BOOK);
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		map.put(Constant.kPARAMNAME, paramMap);
		
		execute(map, handler);
	}
	
	/**
	 * 获取部门
	 */
	public void getDept(final LKAsyncHttpResponseHandler handler) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.kWEBSERVICENAME, "WebService.asmx");
		map.put(Constant.kMETHODNAME, TransferRequestTag.GET_DEPT);
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
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
