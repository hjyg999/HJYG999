package com.md.hjyg.entity;

import java.util.ArrayList;

/** 
 * ClassName: HuoQibaoDetailsModel 
 * date: 2015-11-10 上午8:53:34 
 * remark: 活期宝详情-后台返回数据
 * @author pyc
 */
public class HuoQibaoDetailsModel {
	
//	 public String Rate="";
//	 public String IncomeCalculation ="";
//	 public String EarningStartDate = "";
//	 public String LeaveAmount ="";
//	 public String StartInvestAmount ="";
//	 public String TotalAmt ="";
//	 public String PurchaseLimit ="";
//	 public String remainingamount ="";
//	 public String PaymentDate ="";
	 
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
     
     /**剩余金额*/
     public double LoanDifference;

     /**万元收益*/
     public double LoanInterest;
     /***最少产生收益金额*/
     public String suggestionBuyMoney;
     /***用户可用余额中包含的体验金额度*/
     public double LeaveExperienceAmount ;
     
     /*** 活期宝项目详情*/
     public ArrayList<AssureList> HQBDetailsList;

     /***  活期宝常见问题*/
     public ArrayList<AssureList> HQBQuestionList ;
     
     public ArrayList<AssureList> HQBDetailsListForAndroid;
     
     /***起投金额描述文字*/
     public String StartInvenstAmtDes ;

     /*** 收益方式描述文字*/
     public String InterestWayDes ;

     /*** 最低起投金额描述文字*/
     public String LeastAmtDes ;

     /*** 总额度更新描述文字*/
     public String UpdateTimeDes ;
     
     

}
