package com.md.hjyg.entity;

import com.md.hjyg.R;

/**
 * ClassName: ConsumptionInfoModel date: 2016-6-28 下午2:57:13 remark:消费标分类信息
 * 
 * @author pyc
 */
public class ConsumptionInfoModel {
	private int imgRid = R.drawable.gift_fq;
	private String title = "婚庆";
	private String content = "定制礼服";
	private int id;

	/**
	 * @param imgRid 图片id
	 * @param title 标题
	 * @param content 内容
	 * @param id 标的id
	 */
	public ConsumptionInfoModel(int imgRid, String title, String content, int id) {
		super();
		this.imgRid = imgRid;
		this.title = title;
		this.content = content;
		this.id = id;
	}

	public int getImgRid() {
		return imgRid;
	}

	public void setImgRid(int imgRid) {
		this.imgRid = imgRid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
