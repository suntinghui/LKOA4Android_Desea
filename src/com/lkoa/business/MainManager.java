package com.lkoa.business;

import java.util.HashMap;

import com.lkoa.client.Constants;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.client.LKHttpRequest;
import com.lkoa.client.LKHttpRequestQueue;
import com.lkoa.client.LKHttpRequestQueueDone;

public class MainManager {

	/**
	 * 获取管理中心条数
	 * @param sUserId
	 * @return
	 */
	public void getGLZXCount(String sUserId, LKAsyncHttpResponseHandler handler) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constants.kWEBSERVICENAME, "WebService.asmx");
		map.put(Constants.kMETHODNAME, "GetGLZXCount");
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sUserId", sUserId);
		map.put(Constants.kPARAMNAME, paramMap);
		
		LKHttpRequest req1 = new LKHttpRequest(map, handler);
		new LKHttpRequestQueue().addHttpRequest(req1)
		.executeQueue(null, new LKHttpRequestQueueDone(){

			@Override
			public void onComplete() {
				super.onComplete();
			}
		});
	}
	
	// 取得列表
	private LKAsyncHttpResponseHandler getGWGLListHander(){
		return new LKAsyncHttpResponseHandler(){

			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				int count = (Integer) map.get("count");
			}
			
		};
	}
}
