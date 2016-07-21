package com.md.hjyg.entity;

import java.util.ArrayList;

/**
 * 提现详情
 */
public class WithdrawDetails {
    public boolean IsBankCard;
    public String ErrMessage = "";
    public double WithdrawLogAmount;
    public ArrayList<BankCardList> BankCardList = new ArrayList<BankCardList>();
    public double LeaveAmount;
    public String MemberName = "";
    public String Fee;
   // public Member Member = new Member();
    /**交易密码*/ 
    public String PayPwd;
    /**手续费描述信息*/ 
    public String CommissionCharge;
    /**修改密码提示信息：*/ 
    public String ChangePwdDes;
    /**到账时间说明：*/ 
    public String WithdrawlDes;
    /**免费次数说明*/ 
    public String WithdrawFreeDesc;
}
