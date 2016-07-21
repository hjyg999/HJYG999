package com.md.hjyg.entity;

/**
 * ClassName: MyHuoQibaoDetailsModel date: 2015-11-10 下午2:31:43 
 * remark:我的活期宝详情-后台返回数据
 * @author pyc
 */
public class MyHuoQibaoDetailsModel {

//	public String TotalIntestByMemberId = "";
//	public String FrozanAmtByMemberId = "";
//	public String Yesterdayearning = "";
//	public String TotalPurchaseAmtBymember = "";
//	public Double WithdrawAmount;
//	public Double LeaveAmount;
//	public String singleloanId = "";
	
	public Notification notification ;
	       
	/**用户总投资金额*/
	public double TotalAmount;

	/**昨日收益*/
	public double YesterdayInterest;

	/**累计收益*/
	public double CumulativeInterest;

	/**冻结金额*/
	public double FrozenAmount ;
	
	 /**项目ID*/
    public int Id;

    /**借款人ID*/
    public int MemberId ;

    /**起投金额*/
    public double StartInvenstAmt ;

    /**购买限额*/
    public double PurchaseLimt ;

    /**活期宝总额*/
    public double Total ;

    /**利率*/
    public double Rate ;

    /**提现比例*/
    public double WithdrawProportion ;

    /**收益起始日*/
    public String PaymentDate ;

    /**锁定期*/
    public String LockUpPeriod ;

    
    /**复投日*/
    public String InvestPeriod;
    
    /**活期宝剩余金额*/
    public double LoanDifference;

    /**万元收益*/
    public double LoanInterest;
    
    /**建议购买金额*/
    public String suggestionBuyMoney;
    /**最大收益值*/
    public double MostInterest;
    /**最小收益值*/
    public double AtLeastInterest;
    
    public String labels ;

    /** 利息值*/
    public String data;
    /** 我的活期宝总额中含有的体验金额度*/
    public String TotalExperience;
    
    /**我的活期宝持有金额*/
    public String HoldAmount;
    
    /***起投金额描述文字*/
    public String StartInvenstAmtDes ;

    /*** 收益方式描述文字*/
    public String InterestWayDes ;

    /*** 最低起投金额描述文字*/
    public String LeastAmtDes ;

}
