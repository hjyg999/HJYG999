package com.md.hjyg.entity;

/** 
 * ClassName: HuoQibaoWithdrawInfo 
 * date: 2015-11-13 下午4:18:31 
 * remark: 提现相关信息
 * @author pyc
 */
public class HuoQibaoWithdrawInfo {
	
	/**消息结果*/
    public Notification notification ;

    /**活期宝可提现总金额*/ 
    public String WithdrawTotal;

    /**用户活期宝总金额*/ 
    public String TotalAmount ;

    /**用户可提取金额*/ 
    public String MemberAmount ;

    /**活期宝ID*/ 
    public int PaymentDate ;

    /**项目ID*/ 
    public int SingleId ;
    /**交易密码*/ 
    public String PayPwd;
    
    /**体现时提示的体验金额度*/
    public String ExperienceAmount;
    
    /** 到账说明相关信息*/
    public String ExtractDes;
    

}
