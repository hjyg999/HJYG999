package com.md.hjyg.entity;

/**
 * ClassName: GoldBeanQDInfoModel date: 2016-6-15 下午6:02:51 remark:金豆签到信息
 * 
 * @author pyc
 */
public class GoldBeanQDInfoModel {
	/** 用户Id： */
	public int MemberId;
	/** 签到时间： */
	public String CreateTime;
	/** 连续签到天数： */
	public int ContinuityDay;
	/**
	 * 签到客户端类型 ： PC = 0, Mobile = 1, IOSAPP = 2, AndroidAPP = 3
	 * */
	public int ClientType;
	/** 签到赠送金豆数： */
	public int GiveNumber;
	/** 是否已经签到： */
	public boolean IsSignin;
	public Notification notification;
}
