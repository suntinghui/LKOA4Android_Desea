package com.lkoa.client;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;

public class LKHttpRequest {
	
	private int tag;
	private HashMap<String, Object> requestDataMap;
	private LKAsyncHttpResponseHandler responseHandler;
	private AsyncHttpClient client;
	private LKHttpRequestQueue queue;
	
	public LKHttpRequest(HashMap<String, Object> requestMap, LKAsyncHttpResponseHandler handler){
		this.requestDataMap = requestMap;
		this.responseHandler = handler;
		client = new AsyncHttpClient();
		
		if (null != this.responseHandler){
			this.responseHandler.setRequest(this);
		}
	}
	
	public String getRequestURL(){
		SharedPreferences pre = ApplicationEnvironment.getInstance().getPreferences();
		String hostStr = pre.getString(Constants.kREALHOST, null);
		if (null == hostStr || hostStr.trim().equals("")){
			hostStr = pre.getString(Constants.kHOSTNAME, Constants.DEFAULTHOST);
		}
		
		return hostStr + requestDataMap.get(Constants.kWEBSERVICENAME);
	}
	
	public int getTag(){
		return tag;
	}
	
	public void setTag(int tag){
		this.tag = tag;
	}
	
	public LKHttpRequestQueue getRequestQueue(){
		return this.queue;
	}
	
	public void setRequestQueue(LKHttpRequestQueue queue){
		this.queue = queue;
	}
	
	public HashMap<String, Object> getRequestDataMap() {
		return requestDataMap;
	}
	
	public LKAsyncHttpResponseHandler getResponseHandler() {
		return responseHandler;
	}

	public AsyncHttpClient getClient() {
		return client;
	}
	
	
	/****************************************/
	
	public void post(){
		this.client.addHeader("SOAPAction", "http://tempuri.org/"+TransferRequestTag.getRequestTagMap().get(this.getRequestDataMap().get(Constants.kMETHODNAME)));
		this.client.post(ApplicationEnvironment.getInstance().getApplication(), this.getRequestURL(), this.getHttpEntity(this), "text/xml; charset=utf-8", this.responseHandler);
	}
	
	@SuppressWarnings("unchecked")
	private HttpEntity getHttpEntity(LKHttpRequest request){
		HashMap<String, Object> reqMap = request.getRequestDataMap();
		
		StringBuffer bodySB = new StringBuffer();
		bodySB.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
				+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
				+ "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><");
		bodySB.append(TransferRequestTag.getRequestTagMap().get(reqMap.get(Constants.kMETHODNAME)));
		bodySB.append(" xmlns=\"http://tempuri.org/\">");
		bodySB.append(this.param2String((HashMap<String, Object>)reqMap.get(Constants.kPARAMNAME)));
		bodySB.append("</");
		bodySB.append(TransferRequestTag.getRequestTagMap().get(reqMap.get(Constants.kMETHODNAME)));
		bodySB.append("></soap:Body></soap:Envelope>");
		
		request.getClient().addHeader("Content-Length", bodySB.length()+"");
		
		Log.e("reqest body:", bodySB.toString());
		
		HttpEntity entity = null;
		try {
			entity = new StringEntity(bodySB.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	private String param2String(HashMap<String, Object> paramMap){
		StringBuffer sb = new StringBuffer();
		for	(String key : paramMap.keySet()){
			Object obj = paramMap.get(key);
			if (obj instanceof String){
				sb.append("<").append(key).append("><![CDATA[").append(obj).append("]]></").append(key).append(">");
			} else {
				sb.append("<").append(key).append("><![CDATA[").append(this.hashMap2XML((HashMap<String, Object>)obj)).append("]]></").append(key).append(">");
			}
		}
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	private String hashMap2XML(HashMap<String, Object> paramMap){
		StringBuffer sb = new StringBuffer();
		for	(String key : paramMap.keySet()){
			Object obj = paramMap.get(key);
			if (obj instanceof String){
				sb.append("<").append(key).append(">").append(obj).append("</").append(key).append(">");
			} else {
				sb.append("<").append(key).append(">").append(this.hashMap2XML((HashMap<String, Object>)obj)).append("</").append(key).append(">");
			}
		}
		return sb.toString();
	}
	
}
