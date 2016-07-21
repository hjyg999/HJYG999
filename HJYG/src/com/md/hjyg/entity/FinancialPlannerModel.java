package com.md.hjyg.entity;

/**
 * ClassName: FinancialPlannerModel date: 2015-11-28 上午11:53:06 remark:用户统计数据
 * 
 * @author pyc
 */
public class FinancialPlannerModel {
	/** 二级好友总充值数 */
	public int TwoLevelRechargeTotal;
	
	/** 一级好友总充值数 */
	public int OneLevelRechargeTotal;
	/** 一级已投资数*/
    public int OneLevelInvestTotal ;
    /** 二级已投资数*/
    public int TwoLevelInvestTotal;
	
	/** 复制到剪贴板的链接 */
	public String Link;
	
	/** 规则信息 */
	public MemberFinancialPlannerInfo MemberFinancialPlannerInfo;

	/** 待收奖励总额 */
	public String UncollectedReward;
	
	/** 一级好友总注册数 */
	public int OneLevelRegisterTotal;
	
	/** 二级好友总注册数 */
	public int TwoLevelRegisterTotal;
	
	/**个人等级信息*/
    public LevelInfo RewardPercent;

    /** 已收奖励总额 */
    public String ReceivedReward;
	
	/**真实姓名*/
	public String RealName;

	/** 二级总收入 */
	public String TwoLevelRewardTotal;
	
	/** 一级总收入 */
	public String OneLevelRewardTotal;
	
	
	/** 规则说明-生效条件(银牌)*/
    public String RecRuleSliDes ;

    /** 规则说明-生效条件(金牌)*/
    public String RecRuleGolDes ;

    /** 规则说明-奖励方式(银牌)*/
    public String RecRuleRewardSliDes ;

    /** 规则说明-奖励方式(金牌)*/
    public String RecRuleRewardGolDes ;
    
   /** 规则说明-奖励方式-好友推荐的好友奖励(银牌)*/
    public String RecRuleRewardTwoLevelSliDes;
    public int Id;

    /** 规则说明-奖励方式-好友推荐的好友奖励(金牌)*/
    public String RecRuleRewardTwoLevelGolDes;

    /** 规则说明-奖励发放*/
    public String RecRuleRewardDes ;
    
    /** 规则说明-理财师等级更变*/
    public String RecRuleLevelChangeDes;

    /** 规则说明-备注*/
    public String RecRuleRemark ;
    /**分享描述信息：*/
    public String ShareExpDes ;
    public String ZeroRuleRewardDes;
	
/*
	*//** 1.普通2.银3.金 *//*
	public int RecommanderLevel;*/

	/**等级信息*/
	public class LevelInfo {
		public double FriendsBuyAmount;
		public int FriendsSingleBuyTerm;
		public double OwnAmount;
		public int Type;
		public double FriendsSingleBuyAmount;
		public boolean IsLock;
		public String name;
		public double Percent;
		public int Id;
		public int Level;
		public int RecommendedTotal;
		public double TwoPercent;
		public double Income;
	}
	
	/**用户所有等级信息--规则信息*/
	public class MemberFinancialPlannerInfo {
		/**是否可申请*/
		public boolean IsApply;
		public LevelInfo ThreeLevelInfo;
		/**是否审核中*/
		public boolean IsExamine;
		/**是否是理财师*/
		public boolean IsFinancial;
		public LevelInfo TwoLevelInfo;
		public LevelInfo OneLevelInfo;
		
		
	}

}
