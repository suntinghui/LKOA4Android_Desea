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
 * 附件管理
 */
public class AttachmentManager {
	
	/**
	 * 附件 - 列表
	 * @param id	关联id，包括信息序号、通知序号、邮件序号、流程序号、日程序号
	 */
	public void getAttList(String id, final LKAsyncHttpResponseHandler handler) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.kWEBSERVICENAME, "WebService.asmx");
		map.put(Constant.kMETHODNAME, TransferRequestTag.GET_ATT_LIST);
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("Id", id);
		map.put(Constant.kPARAMNAME, paramMap);
		
		execute("正在加载数据..", map, handler);
	}
	
	/**
	 * 附件-内容
	 */
	public void getAtt(String attId, final LKAsyncHttpResponseHandler handler) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.kWEBSERVICENAME, "WebService.asmx");
		map.put(Constant.kMETHODNAME, TransferRequestTag.GET_ATT);
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("AttId", attId);
		map.put(Constant.kPARAMNAME, paramMap);
		
		String msg = "正在加载数据..";
		execute(msg ,map, handler);
	}
	
	public String getUrl(String path) {
		StringBuilder builder = new StringBuilder("http://docs.google.com/gview?embedded=true&url=");
		builder.append(Constant.DEFAULTHOST);
		builder.append(path);
		
		return builder.toString();
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
