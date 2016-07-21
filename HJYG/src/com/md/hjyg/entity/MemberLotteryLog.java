package com.md.hjyg.entity;

/** 
 * ClassName: MemberLotteryLog 
 * date: 2015-10-23 下午1:54:29 
 * remark:其他单个用户获得的奖品信息
 * @author pyc
 */
public class MemberLotteryLog {
    // 活动名称
    public String Title ;
    
    // 手机号
    public String MobilePhone ;
    
    // 真实姓名
    public String RealName ;

    // 奖品名称
    public String PrizeName ;

    // 推荐人
    public String CommInfoRegName ;

    // 理财红包是否使用
    public boolean IsRedEnvelope ;

    // 奖品类型
    public int type ;

    // 现金红包中奖金额
    public float RedEnvelopeAmount ;

    // 投资红包中奖金额
    public float InvestRedAmount ;

    // 中奖时间
    public String CreateTime ;

    // 奖品类型
    public int Status ;

    // 活动开始时间
    public String startTime ;

    // 活动结束时间
    public String endTime ;

    // 备注说明
    public String Remark ;

    // 中奖用户ID
    public int MemberId ;

    // 奖品ID
    public int LotteryActivityId ;

    // 体验金红包中奖金额
    public String ExperienceAmount ;

    // HxCodeId
    public int HxCodeId ;
    
    // 中奖数量或者奖品数量
    public int PrizeNumber ;
    
    /**
     * 利率
     */
    public double IncreaseRate;
    public int DateType;
    // 日期期限
    public int DateTerm;
}
