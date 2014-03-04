package com.lkoa.model;

import java.util.List;

/**
 * 信息中心-集团新闻Item model
 */
public class CenterMsgNewsItem {

	public String id;
	public String iconUrl;
	public String title;
	public String content;
	public String date;
	
	public List<Attachment> attachments;
}
