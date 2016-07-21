package com.md.hjyg.entity;

public class LoginEntity {
	public String AuthToken;
	public boolean IsFinancialplan;
	public Notification Notification = new Notification();
	// public boolean IsValid;
	/** 判断用户是否已经被锁定: */
	public boolean IsLock = true;
	/** 判断用户是否是在同一台设备上登录  */
	public boolean IsImei = false;
	/** 被锁定的提示信息: */
	public String LockedMessage;
	/** 被锁定的提示信息: */
	public String mobileNo;
}
