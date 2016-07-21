package com.md.hjyg.entity;

/** 
 * ClassName: BuyHuoQibaoUersModel 
 * date: 2015-11-13 上午11:41:27 
 * remark: 购买活期宝界面显示的信息
 * @author pyc
 */
public class BuyHuoQibaoUersModel {
	
	 public Notification notification;
     
	 /**用户ID*/
     public int MemberId ;

     /**活期宝ID*/
     public int SingleLoanId ;

     
     /**在保额-用户已购买的活期宝*/
     public double SingleAmount ;

     
     /**用户可用余额-用户信投保账户可用余额*/
     public double leaveMoney ;

     /**利息池*/
     public double SingleInterest ;
     
   
     /**建议购买金额*/
     public String suggestionBuyMoney;
   
     /**当前日期*/
     public String nowDate;
     
     /**1分钱日收益*/
     public double LoanInterests;
     
     /**用户可用余额中包含的体验金额度*/
     public double LeaveExperienceAmount ;

     /**本次可购买金额*/
     public double PurchaseLimt;
     

    
     /**起投金额*/
     public double StartInvenstAmt ;

     /** 总购买限额*/
     public double PurchaseBuyLimt;


}
