package com.md.hjyg.entity;

public class CaptchaModel {
    public String CaptchaImg;
    public String Code;
    public String QrCode;
    public String linkUrl;
    /**分享内容*/
    public String ShareContent  = "";
	
	/**分享标提*/
    public String ShareTittle  = "";
    
    /**邀请好友赚钱一级描述信息*/
    public String OneLevelDes ;
	/** 邀请好友赚钱二级描述信息*/
    public String TwoLevelDes;
	/**奖励发放说明*/
    public String RewardDes;
    
    /**我的二维码描述信息*/
    public String MyTwoDimensionDes;
    
    /**0元消费节活动规则*/
    public String ZeroRuleRewardDes;
    
    public String Cookie;

}
