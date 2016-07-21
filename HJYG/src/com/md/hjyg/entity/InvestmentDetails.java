package com.md.hjyg.entity;

import java.util.ArrayList;

public class InvestmentDetails {
    public LoanModel loanModel = new LoanModel();//loanModel
    public ArrayList<MemberDatumList> MemberDatumList = new ArrayList<MemberDatumList>();
    public double LoanInterest;
    public String RedirectToAction="";
    public boolean IsValidateRealName;
    public String ReleaseDate="";
    public String LoanDifference="";
    public String BiddingMin="";
    public String Repayment="";
    public int InvestPercentage;
    public double InvestPercentageRadixPoint;
    public int NumberOfUsers;
    public String EstablishmentDate = "";
    public String LeaveMoney = "";
    public ArrayList<AssureList> AssureList = new ArrayList<AssureList>();
}
