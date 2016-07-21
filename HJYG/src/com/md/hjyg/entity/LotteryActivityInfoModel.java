package com.md.hjyg.entity;

/** 
 * ClassName: LotteryActivityInfoModel 
 * date: 2015-10-23 下午1:29:28 
 * remark:活动信息实体类，报告主题及开始、结束时间等
 * @author pyc
 */
public class LotteryActivityInfoModel {
	public int Id ;
	public String Titles;
	public String tartTime; 
	public String endTime;
	public String CreateTime;
	/**0：即将开始，1：可以抽奖，2：抽奖结束*/
	public int Status;
	public Notification notification;
}
