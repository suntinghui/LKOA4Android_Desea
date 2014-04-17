package com.lkoa.model;

import java.util.List;

/**
 * 我的邮件-邮件内容
 */
public class MailContentItemInfo {

	public String id;
	public String title;
	public String content;
	public String sender;
	public String senderId;
	public String date;
	
	public String sjr;
	public String sjrId;
	
	public List<Attachment> attachments;
}
