package com.lkoa.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.text.TextUtils;

import com.lkoa.client.Constant;
import com.lkoa.client.LKAsyncHttpResponseHandler;
import com.lkoa.client.LKHttpRequest;
import com.lkoa.client.LKHttpRequestQueue;
import com.lkoa.client.LKHttpRequestQueueDone;
import com.lkoa.client.TransferRequestTag;
import com.lkoa.model.ContactItem;
import com.lkoa.model.DepartmentItem;

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
	
	public List<DepartmentItem> getDepts(List<ContactItem> list) {
		List<DepartmentItem> returnList = new ArrayList<DepartmentItem>();
		DepartmentItem dept = null;
		for(ContactItem item : list) {
			dept = getDeptById(returnList, item.deptId);
			if(dept == null) {
				dept = new DepartmentItem();
				dept.deptId = item.deptId;
				dept.deptName = item.deptName;
				dept.alpha = item.alphaU;
				dept.contacts = new ArrayList<ContactItem>();
				returnList.add(dept);
			}
			
			//确保部门名称不为空
			if(TextUtils.isEmpty(dept.deptName)) {
				dept.deptName = item.deptName;
			}
			
			dept.contacts.add(item);
		}
		
		return returnList;
	}
	
	private DepartmentItem getDeptById(List<DepartmentItem> list, String deptId) {
		for(DepartmentItem item : list) {
			if(TextUtils.equals(deptId, item.deptId)) {
				return item;
			}
		}
		
		return null;
	}
}
