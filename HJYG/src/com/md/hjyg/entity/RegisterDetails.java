package com.md.hjyg.entity;

public class RegisterDetails {
    public String UserName = null;
    public String Mobile = "";
    public String Email = "";
    public String Referrer = "";
    public int RecommendType;
    public Notification Notification = new Notification();//Notification
    public RedirectToAction redirectToAction = new RedirectToAction();
    public String IntiPublicKeyModel = "";
}
