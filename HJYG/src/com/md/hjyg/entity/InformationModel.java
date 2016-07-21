package com.md.hjyg.entity;

/**
 * ClassName: InformationModel date: 2016-5-16 上午11:01:43 remark:资讯model
 * 
 * @author pyc
 */
public class InformationModel {
//	public String url;
//	public String time;
//	public String title;
//	public String type;

	public int Id;

	/** 标题 */
	public String Title;

	// / 媒体
	public String media;

	// / 简介
	public String Introduction;

	// / 图标
	public String PictureUrl;
	// / 图标
	public String PictureHeadUrl;

	// / 内容
	public String Contents;

	// / 关键词
	public String Keyword;

	// / </summary>
	public boolean IsTop;

	// / 创建时间
	public String CreateTime;

	// / 修改时间
	public String UpdateTime;

	// / 是否热门
	public boolean IsHot;

	// / 是否显示
	public boolean IsShow;

	// / 栏目表
	public int ChannelId;

	// / 栏目类型
	public int TypeId;

	// / 是否在手机首页显示
	public boolean IsMobileIndex;

}
