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
	public String state;	//0-未读	1-已读
	
	public List<Attachment> attachments;
}
