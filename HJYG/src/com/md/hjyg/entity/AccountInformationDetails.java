package com.md.hjyg.entity;

import java.util.ArrayList;

public class AccountInformationDetails {
    public String FileInside="";
    public String FileInsideName="";
    public String RegName="";
    public String RecommendName="";
    public boolean IsEnterRecommendName;
    public boolean ValidateQQ;
    public boolean qqMember;
    public ArrayList<CardType> CardType = new ArrayList<CardType>();
    public String RealName="";
    public String IDNumber = "";
    public boolean Audit;
    public String FileFacade = "";
    public String FileBack = "";
    public String FileFacadeName = "";
    public String FileBackName = "";
    public int RecommendType;
    public Notification Notification = new Notification();//Notification
    public RedirectToAction RedirectToAction = new RedirectToAction();//RedirectToAction

}
