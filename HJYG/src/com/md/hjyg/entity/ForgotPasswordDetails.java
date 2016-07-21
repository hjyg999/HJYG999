package com.md.hjyg.entity;

public class ForgotPasswordDetails {
    public ForgotPasswordNotification Notification = new ForgotPasswordNotification();
    public  String Code = "";
    public String Mobile = "";
    public boolean Result;
    public boolean IsValidateMobile;
    public String Message = "";
    public int WaitOfSecond;
}
