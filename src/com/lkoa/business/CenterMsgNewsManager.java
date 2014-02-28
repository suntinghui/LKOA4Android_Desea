package com.lkoa.business;

import java.util.ArrayList;
import java.util.List;

import com.lkoa.model.CenterMsgNewsItem;

public class CenterMsgNewsManager {

	public List<CenterMsgNewsItem> getLatestNewsData() {
		List<CenterMsgNewsItem> returnList = new ArrayList<CenterMsgNewsItem>();
		for(int i=0; i<10; i++) {
			CenterMsgNewsItem item = new CenterMsgNewsItem();
			item.setTitle("帝海集团李小明总裁率...");
			item.setDate("2013-10-09");
			item.setDetails("2013年9月25日，帝海集团李小明总裁率工作组赴四川省成都市考察综合项目。...");
			
			returnList.add(item);
		}
		return returnList;
	}
	
	public List<CenterMsgNewsItem> getMoreNewsData() {
		List<CenterMsgNewsItem> returnList = new ArrayList<CenterMsgNewsItem>();
		for(int i=0; i<5; i++) {
			CenterMsgNewsItem item = new CenterMsgNewsItem();
			item.setTitle("帝海集团李小明总裁率...");
			item.setDate("2013-10-09");
			item.setDetails("2013年9月25日，帝海集团李小明总裁率工作组赴四川省成都市考察综合项目。...");
			
			returnList.add(item);
		}
		return returnList;
	}
}
