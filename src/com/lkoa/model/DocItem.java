package com.lkoa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DocItem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public DocItem() {
		this.list = new ArrayList<DocItem>();
	}

	public String id;
	public String name;
	public String count;
	
	public List<DocItem> list;
}
