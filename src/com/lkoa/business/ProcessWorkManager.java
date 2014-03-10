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
public class ProcessWorkManager {
	
	/**
	 * 流程管理-条数
	 */
	public void getLCGLCount(String sUserId, final LKAsyncHttpResponseHandler handler) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.kWEBSERVICENAME, "WebService.asmx");
		map.put(Constant.kMETHODNAME, TransferRequestTag.GET_LCGL_COUNT);
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sUserId", sUserId);
		map.put(Constant.kPARAMNAME, paramMap);
		
		execute("正在加载数据..", map, handler);
	}
	
	/**
	 * 流程管理-列表
	 */
	public void getLCList(String sType, String sUserId, final LKAsyncHttpResponseHandler handler) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.kWEBSERVICENAME, "WebService.asmx");
		map.put(Constant.kMETHODNAME, TransferRequestTag.GET_LC_LIST);
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sUserId", sUserId);
		paramMap.put("sType", sType);
		map.put(Constant.kPARAMNAME, paramMap);
		
		execute("正在加载数据..", map, handler);
	}
	
	/**
	 * 流程管理-流程表单
	 */
	public void getLCBD(String sType, String infoId, String sUserId, final LKAsyncHttpResponseHandler handler) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.kWEBSERVICENAME, "WebService.asmx");
		map.put(Constant.kMETHODNAME, TransferRequestTag.GET_LCBD);
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sUserId", sUserId);
		paramMap.put("sType", sType);
		paramMap.put("InfoId", infoId);
		map.put(Constant.kPARAMNAME, paramMap);
		
		execute("正在加载数据..", map, handler);
	}
	
	/**
	 * 流程管理-流程办理-正文
	 */
	public void getLCZW(String infoId, String sUserId, final LKAsyncHttpResponseHandler handler) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.kWEBSERVICENAME, "WebService.asmx");
		map.put(Constant.kMETHODNAME, TransferRequestTag.GET_LCZW);
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sUserId", sUserId);
		paramMap.put("InfoId", infoId);
		map.put(Constant.kPARAMNAME, paramMap);
		
		execute("正在加载数据..", map, handler);
	}
	
	/**
	 * 流程管理-流程办理-附件
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
	 * 流程管理-流程办理-保存
	 */
	public void setGLBD(String userId, String type, String infor, final LKAsyncHttpResponseHandler handler) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.kWEBSERVICENAME, "WebService.asmx");
		map.put(Constant.kMETHODNAME, TransferRequestTag.SET_GLBD);
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sUserId", userId);
		paramMap.put("sType", type);
		
		HashMap<String, Object> inforsMap = new HashMap<String, Object>();
		inforsMap.put("Infor", URLEncoder.encode(infor));
		paramMap.put("Infors", inforsMap);
		map.put(Constant.kPARAMNAME, paramMap);
		
		String msg = "正在保存数据..";
		if(TextUtils.equals("1", type)) {
			msg = "正在提交数据..";
		}
		execute(msg ,map, handler);
	}
	
	/**
	 * 流程管理-流程办理-保存
	 */
	public void getDept(final LKAsyncHttpResponseHandler handler) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.kWEBSERVICENAME, "WebService.asmx");
		map.put(Constant.kMETHODNAME, TransferRequestTag.GET_DEPT);
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		map.put(Constant.kPARAMNAME, paramMap);
		
		String msg = "正在加载数据..";
		execute(msg ,map, handler);
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
