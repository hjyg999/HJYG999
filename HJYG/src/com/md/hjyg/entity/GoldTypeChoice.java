package com.md.hjyg.entity;

/**
 * ClassName: GoldTypeChoice date: 2016-1-22 下午2:12:33 remark: 黄金类型选择
 * 
 * @author pyc
 */
public class GoldTypeChoice {
	private String name;
	private boolean isChoice;

	/**
	 * @param name
	 * @param isChoice
	 */
	public GoldTypeChoice(String name, boolean isChoice) {
		super();
		this.name = name;
		this.isChoice = isChoice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isChoice() {
		return isChoice;
	}

	public void setChoice(boolean isChoice) {
		this.isChoice = isChoice;
	}

}
