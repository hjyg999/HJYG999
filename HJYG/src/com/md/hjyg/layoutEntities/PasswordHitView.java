package com.md.hjyg.layoutEntities;

import com.md.hjyg.R;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ClassName: PasswordHitView date: 2015-12-2 上午9:51:00 remark:密码强弱提示控件
 * 
 * @author pyc
 */
public class PasswordHitView extends LinearLayout {
	/** 低、中、高密码强弱提示 */
	private TextView tv_week, tv_medium_password, tv_high_password;

	public PasswordHitView(Context context) {
		super(context);
	}

	public PasswordHitView(Context context, AttributeSet attrs, int paramInt) {
		super(context, attrs, paramInt);
	}

	public PasswordHitView(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflate(context, R.layout.layout_view_passwordhit, this);
		// // 如果是编辑模式，则跳过
		// if (isInEditMode()) {
		// return;
		//
		// }

		tv_high_password = (TextView) findViewById(R.id.tv_high_password);
		tv_week = (TextView) findViewById(R.id.tv_week);
		tv_medium_password = (TextView) findViewById(R.id.tv_medium_password);
	}

	/** 通过输入的密码改变颜色 */
	public void setUIbyPassword(String password) {
		if (password == null) {
			return;
		}
		int passwordlenght = password.length();

		if (passwordlenght <= 6
				&& (Constants.CheckNumber(password)
						|| Constants.ChecklowercasePassword(password) || Constants
							.CheckuppercasePassword(password))) {
			tv_week.setBackgroundResource(R.color.red);
			tv_medium_password.setBackgroundResource(R.color.gray_q);
			tv_high_password.setBackgroundResource(R.color.gray_q);
		} else if (passwordlenght <= 10
				&& (Constants.CheckNumber(password)
						|| Constants.ChecklowercasePassword(password) || Constants
							.CheckuppercasePassword(password))) {
			tv_week.setBackgroundResource(R.color.gray_q);
			tv_medium_password.setBackgroundResource(R.color.glod);
			tv_high_password.setBackgroundResource(R.color.gray_q);
		} else if (passwordlenght > 10
				&& Constants.CheckNumber(password)
				&& (Constants.ChecklowercasePassword(password) || Constants
						.CheckuppercasePassword(password))) {
			tv_week.setBackgroundResource(R.color.gray_q);
			tv_medium_password.setBackgroundResource(R.color.gray_q);
			tv_high_password.setBackgroundResource(R.color.green_p);
		}

	}

}
