package com.md.hjyg.entity;

import java.io.Serializable;

/**
 * ClassName: GoldAddressModel date: 2016-1-28 上午11:02:21 remark: 地址Model
 * 
 * @author pyc
 */
public class GoldAddressModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String phone;
	private boolean isdefault;
	private String address;
	private boolean isChecked;

	/**
	 * @param name
	 * @param phone
	 * @param address
	 * @param state
	 */
	public GoldAddressModel(String name, String phone, String address, boolean isdefault) {
		super();
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.isdefault = isdefault;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isIsdefault() {
		return isdefault;
	}

	public void setIsdefault(boolean isdefault) {
		this.isdefault = isdefault;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}


}
