package com.md.hjyg.entity;

/** 
 * ClassName: GoldBeanQDModel 
 * date: 2016-6-15 下午5:09:57 
 * remark:金豆签到接口
 * @author pyc
 */
public class GoldBeanQDModel {
	public Notification notification;
	public String LeaveGoldBean;
	/**是否连续7天签到*/
	public boolean IsContinuityDay;
	/**签到赠送的金豆个数*/
	public String SignGiveNumber;

}
