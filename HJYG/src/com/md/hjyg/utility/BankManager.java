package com.md.hjyg.utility;

import com.md.hjyg.R;
import android.widget.ImageView;

/**
 * ClassName: BankManager date: 2016-5-17 下午5:16:51 remark:
 * 
 * @author pyc
 */
public class BankManager {

	/** 设置银行卡背景图片*/
	public static void setBankCardBg(String BankName, ImageView bank_img) {
		int[] bankims = { R.drawable.bl1, R.drawable.bl2, R.drawable.bl3,
				R.drawable.bl4, R.drawable.bl5, R.drawable.bl6, R.drawable.bl7,
				R.drawable.bl8, R.drawable.bl9, R.drawable.bl10,
				R.drawable.bl11, R.drawable.bl12, R.drawable.bl13,
				R.drawable.bl14, R.drawable.bl15, R.drawable.bl16,
				R.drawable.bl17, R.drawable.bl18, R.drawable.bl19,
				R.drawable.bl20, R.drawable.bl21, R.drawable.bl22,
				R.drawable.bl23, R.drawable.bl24, R.drawable.bl25,
				R.drawable.bl26, R.drawable.bl27, R.drawable.bl28,
				R.drawable.bl29, R.drawable.bl30, R.drawable.bl31,
				R.drawable.bl32 };
		if (BankName.indexOf("花旗银行") != -1) {
			bank_img.setImageResource(bankims[0]);
		} else if (BankName.indexOf("中国建设银行") != -1) {
			bank_img.setImageResource(bankims[1]);
		} else if (BankName.indexOf("浦发银行") != -1) {
			bank_img.setImageResource(bankims[2]);
		} else if (BankName.indexOf("交通银行") != -1) {
			bank_img.setImageResource(bankims[3]);
		} else if (BankName.indexOf("兴业银行") != -1) {
			bank_img.setImageResource(bankims[4]);
		} else if (BankName.indexOf("龙江银行") != -1) {
			bank_img.setImageResource(bankims[5]);
		} else if (BankName.indexOf("农业银行") != -1) {
			bank_img.setImageResource(bankims[6]);
		} else if (BankName.indexOf("中国邮政") != -1) {
			bank_img.setImageResource(bankims[7]);
		} else if (BankName.indexOf("民生银行") != -1) {
			bank_img.setImageResource(bankims[8]);
		} else if (BankName.indexOf("农村合作信用社") != -1) {
			bank_img.setImageResource(bankims[9]);
		} else if (BankName.indexOf("渤海银行") != -1) {
			bank_img.setImageResource(bankims[10]);
		} else if (BankName.indexOf("光大银行") != -1) {
			bank_img.setImageResource(bankims[11]);
		} else if (BankName.indexOf("平安银行") != -1) {
			bank_img.setImageResource(bankims[12]);
		} else if (BankName.indexOf("宁波银行") != -1) {
			bank_img.setImageResource(bankims[13]);
		} else if (BankName.indexOf("深圳发展银行") != -1) {
			bank_img.setImageResource(bankims[14]);
		} else if (BankName.indexOf("汉口银行") != -1) {
			bank_img.setImageResource(bankims[15]);
		} else if (BankName.indexOf("工商银行") != -1) {
			bank_img.setImageResource(bankims[16]);
		} else if (BankName.indexOf("招商银行") != -1) {
			bank_img.setImageResource(bankims[17]);
		} else if (BankName.indexOf("中国银行") != -1) {
			bank_img.setImageResource(bankims[18]);
		} else if (BankName.indexOf("广发银行") != -1) {
			bank_img.setImageResource(bankims[19]);
		} else if (BankName.indexOf("中信银行") != -1) {
			bank_img.setImageResource(bankims[20]);
		} else if (BankName.indexOf("华夏银行") != -1) {
			bank_img.setImageResource(bankims[21]);
		} else if (BankName.indexOf("北京银行") != -1) {
			bank_img.setImageResource(bankims[22]);
		} else if (BankName.indexOf("南京银行") != -1) {
			bank_img.setImageResource(bankims[23]);
		} else if (BankName.indexOf("上海银行") != -1) {
			bank_img.setImageResource(bankims[24]);
		} else if (BankName.indexOf("江苏银行") != -1) {
			bank_img.setImageResource(bankims[25]);
		} else if (BankName.indexOf("广东南粤银行") != -1) {
			bank_img.setImageResource(bankims[26]);
		} else if (BankName.indexOf("桂林银行") != -1) {
			bank_img.setImageResource(bankims[27]);
		} else if (BankName.indexOf("浙商银行") != -1) {
			bank_img.setImageResource(bankims[29]);
		} else if (BankName.indexOf("杭州银行") != -1) {
			bank_img.setImageResource(bankims[30]);
		} else if (BankName.indexOf("长沙银行") != -1) {
			bank_img.setImageResource(bankims[31]);
		} else {
			bank_img.setImageResource(bankims[28]);
		}
	}

	/**
	 * 把银行卡号换成**** **** **** **** 1256格式
	 * @param cardnum
	 * @return
	 */
	public static String Newreplacecardformat(String cardnum) {

		String temp_card_details = null;
		try {
			// String str1 = cardnum.substring(0, 4);
			String str2 = cardnum.substring(cardnum.length() - 4,
					cardnum.length());
			String str3 = "";
			for (int i = 0; i < cardnum.length() - 4; i++) {
				str3 += "*";

				if (i == 7 || i == 3)
					str3 += " ";

			}
			temp_card_details = str3 + " " + str2;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return temp_card_details;
	}

}
