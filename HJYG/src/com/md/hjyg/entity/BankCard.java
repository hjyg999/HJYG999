package com.md.hjyg.entity;

public class BankCard {
	public int Id;
	public String BankNames;
	public String BankBranchName;
	public String Holders;
	public String BankNumber;
	public String CreateTime;
	public int MemberVerificationFrequency;
	public int CustomerServiceVerificationFrequency;
	public boolean IsDelete;
	public boolean IsValid;
	public int VerificationResult;
	public double Amount;
	public int MemberId;
	Member Member = new Member();
}
