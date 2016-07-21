package com.md.hjyg.entity;

/**
 * ClassName: UserSpecialHaveGiftModel date: 2016-4-15 下午4:12:40 remark:
 * 
 * @author pyc
 */
public class UserSpecialHaveGiftModel {
	/**
	 * 项目详情
	 */
	public NoviceLoan noviceLoan;
	/**
	 * 投资须知
	 */
	public String ruleDes;
	/**
	 * 剩余金额：
	 */
	public String LeaveAmount;
	/**
	 * 快递方式
	 */
	public int ExtractionMethod;
	/**
	 * 姓名
	 */
	public String RealName;
	/** 电话号码 */
	public String MobilePhone;
	/** 地址 */
	public String AddressInfo;
	
	/**分享链接： */
	public String linkUrl;
	/**分享标题： */
	public String shareTittle;
	/**分享内容： */
	public String shareContent;
	/**
	 * 体验金
	 */
	public String expAmount;
}
