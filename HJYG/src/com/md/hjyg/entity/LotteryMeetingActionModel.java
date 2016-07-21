package com.md.hjyg.entity;

/**
 * ClassName: LotteryMeetingActionModel date: 2016-3-11 下午5:20:53 remark: 会销活动信息
 * 
 * @author pyc
 */
public class LotteryMeetingActionModel {
	
	/** 活动简介: */
	public String Title;
	public String Url;
	public String Remark;
	public String RealName;
	public int Id;
	public int HxCodeId;
	/** 活动开始时间 : */
	public String startTime;
	/** 活动结束时间 : */
	public String endTime;
	/** 创建时间: */
	public String CreateTime;
	/** 主持人 : */
	public int MemberId;
	/** 开始标记 : */
	public boolean Start;
	/** 活动结束时间（会销主持人使用）: */
	public String UstartTime;

}
