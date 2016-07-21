package com.md.hjyg.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 官方公告返回信息详情
 */
public class NewsNoticeDetails {
	public int pageIndex;
	public int pageTotal;
	public int pageCount;
	public int IsHotCount;
	public List<ArticlesPgedListModel> List = new ArrayList<ArticlesPgedListModel>();
}
