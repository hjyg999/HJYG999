package com.md.hjyg.entity;

/**
 * ClassName: AuthInfoModel date: 2016-2-1 下午2:12:31 remark:授权信息
 * 
 * @author rw
 */
public class AuthInfoModel {

	private String time;

	private String imei;

	private String crc;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getCrc() {
		return crc;
	}

	public void setCrc(String crc) {
		this.crc = crc;
	}

}
