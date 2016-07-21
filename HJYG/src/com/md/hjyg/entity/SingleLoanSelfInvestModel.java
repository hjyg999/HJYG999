package com.md.hjyg.entity;

/** 
 * ClassName: SingleLoanSelfInvestModel 
 * date: 2015-11-14 上午9:19:13 
 * remark:用户的单笔购买记录
 * @author pyc
 */
public class SingleLoanSelfInvestModel {
	
	 public int MemberId;

     /**活期宝ID*/ 
     public int SingleLoanId ;

     /**购买金额*/ 
     public String Amount ;

     /**购买时间*/ 
     public String CreateTime ;

     /**购买渠道*/ 
     public int ClientType ;

      /**备注*/ 
     public String Remarks;
     
     /**资金类型0=现金，1=体验金*/ 
     public int Type ;
}
