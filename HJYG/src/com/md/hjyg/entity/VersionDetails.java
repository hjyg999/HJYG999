package com.md.hjyg.entity;

/**
 * 版本信息
 */
public class VersionDetails {
	public Notification Notification = new Notification();//Notification
	public String upgrade; //是否有更新true or false
	public String apkName; // app名称
    public String apkSize = "";
	public String strVersion; // app版本号
	public String downLoadUrl; // 可供更新的url地址
    public String Description = "";
    public String nowTime = "";// 服务器当前时间
    /**强制更新 true:强制更新*/
    public boolean isForceUpdate;

}
