package com.md.hjyg.entity;

/** 
 * ClassName: ChildItem 
 * date: 2016-1-20 下午3:57:39 
 * remark: expandableListView里 子listView里显示的数据
	 
 * @author pyc
 */
public class ChildItem {
	private String itemTitle;
	private String itemContent;
	public ChildItem (String itemTitle,String itemContent){
		this.itemContent = itemContent;
		this.itemTitle = itemTitle;
	}
	public String getItemTitle() {
		return itemTitle;
	}
	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}
	public String getItemContent() {
		return itemContent;
	}
	public void setItemContent(String itemContent) {
		this.itemContent = itemContent;
	}

}
