package com.lkoa.model;

import java.io.Serializable;

public class LinkManModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name; // 姓名
	private String phone; // 电话号码
	private String alpha; // 汉字拼音首字母

	
	public String getAlpha() {
		return alpha;
	}

	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String toString(){
		return this.name;
	}
}
