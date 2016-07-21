package com.md.hjyg.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class AssureList implements Parcelable {
	/*
	 * public int Id; public int MemberId; public int LoanId; public int
	 * AssureAmount; public int Type; public String CreateTime= ""; public
	 * String LinkUrl = ""; public String AssureInfo = "";
	 */

	public int Id;
	/***/
	public int LoanId;
	/** 描述标题 */
	public String Title;
	/** 内容 */
	public String Content;
	/** Pc端是否显示 */
	public boolean PcIsDisplay;
	/** 微信端是否显示 */
	public boolean MobileIsDisplay;
	/** App是否显示 */
	public boolean AppIsDisplay;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(Id);
		dest.writeInt(LoanId);
		dest.writeString(Title);
		dest.writeString(Content);
		dest.writeByte((byte) (PcIsDisplay ? 1 : 0));
		dest.writeByte((byte) (MobileIsDisplay ? 1 : 0));
		dest.writeByte((byte) (AppIsDisplay ? 1 : 0));

	}
	
	private AssureList(Parcel in) {
		Id = in.readInt();
		LoanId = in.readInt();
		Title = in.readString();
		Content = in.readString();
		PcIsDisplay = in.readByte() != 0;
		MobileIsDisplay = in.readByte() != 0;
		AppIsDisplay = in.readByte() != 0;
	}

	public static final Parcelable.Creator<AssureList> CREATOR = new Parcelable.Creator<AssureList>() {
		public AssureList createFromParcel(Parcel in) {
			return new AssureList(in);
		}

		public AssureList[] newArray(int size) {
			return new AssureList[size];
		}
	};

	

}
