package com.lkoa.business;

import java.net.URLEncoder;
import java.util.HashMap;

import android.text.TextUtils;

import com.lkoa.client.Constant;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.client.LKHttpRequest;
import com.lkoa.client.LKHttpRequestQueue;
import com.lkoa.client.LKHttpRequestQueueDone;
import com.lkoa.client.TransferRequestTag;

/**
 * 流程管理
 */
public class ScheduleManager {
	
	/**
	 * 日程管理-条数
	 */
	public void getRCCount(String sUserId, final LKAsyncHttpResponseHandler handler) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.kWEBSERVICENAME, "WebService.asmx");
		map.put(Constant.kMETHODNAME, TransferRequestTag.GET_RC_COUNT);
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sUserId", sUserId);
		map.put(Constant.kPARAMNAME, paramMap);
		
		execute("正在加载数据..", map, handler);
	}
	
	private void execute(String dialogMsg, HashMap<String, Object> map, LKAsyncHttpResponseHandler handler) {
		LKHttpRequest req1 = new LKHttpRequest(map, handler);
		new LKHttpRequestQueue().addHttpRequest(req1)
		.executeQueue(dialogMsg, new LKHttpRequestQueueDone(){

			@Override
			public void onComplete() {
				super.onComplete();
			}
		});
	}
}
