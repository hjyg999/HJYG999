package com.md.hjyg.entity;

import android.os.Parcel;
import android.os.Parcelable;

/** 
 * ClassName: LoanGift 
 * date: 2016-3-29 上午9:47:01 
 * remark:0元消费活动
 * @author pyc
 */
public class LoanGift implements Parcelable{
	public int Id;
	public String IconsURL;
	public boolean IsValid;
	public String Title;
	public String CreateTime;


	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(Id);
		dest.writeString(Title);
		dest.writeString(IconsURL);
		dest.writeString(CreateTime);
		dest.writeByte((byte) (IsValid ? 1 : 0));
	}
	
	private LoanGift(Parcel in) {
		Id = in.readInt();
		Title = in.readString();
		IconsURL = in.readString();
		CreateTime = in.readString();
		IsValid = in.readByte() != 0;
	}
	
	public static final Parcelable.Creator<LoanGift> CREATOR = new Parcelable.Creator<LoanGift>() {
		public LoanGift createFromParcel(Parcel in) {
			return new LoanGift(in);
		}

		public LoanGift[] newArray(int size) {
			return new LoanGift[size];
		}
	};
	
}
