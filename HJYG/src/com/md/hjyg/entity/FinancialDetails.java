package com.md.hjyg.entity;

import java.util.ArrayList;

public class FinancialDetails {
    public boolean Result;
    public int pageIndex;
    public int pageTotal;
    public int pageCount;
    public String IncomeAmount = "";
    public String PayAmount = "";
    public ArrayList<ListModel> List = new ArrayList<ListModel>();
}
