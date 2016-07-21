package com.md.hjyg.entity;

/**
 * ClassName: MeetListDialogModel date: 2016-3-9 下午5:11:04 remark: 会销listDialog
 * 的Model
 * 
 * @author pyc
 */
public class MeetListDialogModel {

	private String content;
	private String date;
	private boolean isChoice;
	/**
	 * 状态 1进行中，0未开始，2已结束
	 */
	private int state;

	public MeetListDialogModel() {
	}

	/**
	 * 
	 * @param content
	 *            内容
	 * @param isChoice
	 *            是否选中
	 */
	public MeetListDialogModel(String content, boolean isChoice) {
		this.content = content;
		this.isChoice = isChoice;
	}

	public MeetListDialogModel(String content, boolean isChoice, int state) {
		this.content = content;
		this.isChoice = isChoice;
		this.state = state;
	}

	/**
	 * 
	 * @param content
	 *            内容
	 * @param date
	 *            时间
	 * @param isChoice
	 *            是否选中
	 */
	public MeetListDialogModel(String content, String date, boolean isChoice) {
		this.content = content;
		this.date = date;
		this.isChoice = isChoice;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean isChoice() {
		return isChoice;
	}

	public void setChoice(boolean isChoice) {
		this.isChoice = isChoice;
	}

	/**
	 * 状态 1进行中，0未开始，2已结束
	 */
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
