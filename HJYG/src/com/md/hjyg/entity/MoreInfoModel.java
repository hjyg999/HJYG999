package com.md.hjyg.entity;

import java.util.List;

/** 
 * ClassName: MoreInfoModel 
 * date: 2016-3-24 上午11:11:34
 * remark:更多相关信息
 * @author rw
 */
public class MoreInfoModel {
	/** 平台热门公告数量*/ 
    public int isHotCount;
    /**更多-平台应用数据-天数*/ 
    public String days;
    /**总交易额*/ 
    public String amount;
    /**总交易额-亿*/ 
    public String amount_yi;
    /**总交易额-万元*/ 
    public String amount_wy;
    /**总收益额-亿*/
    public String interest_yi;
    /**总收益额-万*/ 
    public String interest_wy;
    /** 总收益额-元*/ 
    public String interest_y;
    /**利息说明-信投宝*/ 
    public String interest_xtb;
    /** 利息说明-银行*/ 
    public String interest_bank;
    /**注册按钮文字说明*/ 
    public String RegistButtonInfo;
    /**注册按钮文字说明*/ 
    public String HomeButtonInfo;
    /**兑付比例*/ 
    public String percent;
    /**数据截至日期*/ 
    public String endDate;
    /**平台收益数据对比表*/  
    public List<String> interestList;
    /**认识信投宝*/
    public String konwXTB;
    /**滚动的文字信息*/
    public List<String> ScrollInfoList;
    /**新手指引-为什么选择信投宝*/
    public String whyDes_interest1;
    public String whyDes_interest2;
    public String whyDes_interest3;
}
