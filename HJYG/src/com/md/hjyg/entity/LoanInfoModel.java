package com.md.hjyg.entity;

import java.util.ArrayList;

public class LoanInfoModel {
    public String  LeaveMoney;
    public String  InvestPercentage;
    public String  InvestPercentageRadixPoint;
    public String InvestTotal;
    public String BiddingMin;
    public String BiddingMax;
    public String LoanDifference;
    public String LoanStatus;
    public String DifferenceBiddingStratTimeOfSeconds;
    public String DifferenceBiddingEndTimeOfSeconds;
    public ArrayList<InvestModel> MemberDatumList = new ArrayList<InvestModel>();
}
