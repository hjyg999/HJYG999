package com.md.hjyg.entity;

import java.util.ArrayList;

/**
 * Created by gayatri on 20/7/15.
 * 充值前获取关于银行卡和用户认证的信息
 */
public class RechargeDetails {
	public Notification Notification = new Notification();
    public String RealName= "";
    public String IDNumber= "";
    public boolean IsBankCard;
    public ArrayList<BankList> BankList = new ArrayList<BankList>();
    public boolean IsRecharge;
   /** 充值温馨提示：*/
    public String RechargeDes;
    /** 更换银行卡相关提示：*/
    public String ChangeCardDes;
    public String BankCardDes;
}
