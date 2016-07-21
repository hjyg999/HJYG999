package com.md.hjyg.layoutEntities;

import com.md.hjyg.R;
import android.app.Activity;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ClassName: HeaderViewControler date: 2016-2-19 上午11:44:27 remark:新版本头部控件
 * 
 * @author pyc
 */
public class HeaderViewControler {
	public final static int ID = R.id.img_back;

//	private TextView tv_title;
//	private ImageView img_back;

	public static void setHeaderView(Activity mActivity,String title,OnClickListener l) {
		TextView tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
		ImageView img_back = (ImageView) mActivity.findViewById(ID);
		tv_title.setText(title);
		img_back.setOnClickListener(l);

	}

}
