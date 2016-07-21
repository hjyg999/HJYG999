package com.md.hjyg.entity;

/** 
 * ClassName: SingleLoanWithdrawModel 
 * date: 2015-11-14 上午11:04:06 
 * remark: 用户活期宝提取记录
 * @author pyc
 */
public class SingleLoanWithdrawModel {
	
	/**用户ID*/ 
    public int MemberId ;

    /**活期宝ID*/ 
    public int SingleLoanId ;

    /**提取金额*/ 
    public String Amount ;

    /**状态*/ 
    public int Status ;

    /**创建时间*/ 
    public String CreateTime ;

    /**备注*/ 
    public String Remarks ;
    
    /**资金类型0=现金，1=体验金*/ 
    public int Type ;
    
}
