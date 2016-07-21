package com.md.hjyg.entity;

/** 
 * ClassName: MeetingGoodluckModel 
 * date: 2016-3-14 下午4:55:14 
 * remark:会销抽奖信息
 * @author pyc
 */
public class MeetingGoodluckModel {
	
	public Notification notification ;

    /**已有次数*/
    public int HaveNumber;

    /**还有次数*/
    public int AlsoNumber;

    /** 抽中的奖品ID*/
    public int LotteryActivityPrizeId ;
    /** 结果*/
    public boolean Result ;

    /** 消息*/
    public String Message ;
    /** 是否登录*/
    public boolean Login ;

    public int PrizeType ;


}
