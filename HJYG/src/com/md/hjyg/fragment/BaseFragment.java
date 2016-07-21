package com.md.hjyg.fragment;

import com.md.hjyg.utils.ScreenUtils;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;

/** 
 * ClassName: BaseFragment 
 * date: 2016-3-1 下午4:49:44 
 * remark:
 * @author pyc
 */
public class BaseFragment extends Fragment{
	
	/**
	 * 设置控件的高度
	 * 
	 * @param v
	 *            要设置的控件
	 * @param weight
	 *            占屏幕的比例
	 */
	protected void setViewHight(View v, float weight) {
		if (weight > 1 || v == null) {
			return;
		}
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v
				.getLayoutParams();
		params.height = (int) (ScreenUtils.getScreenHeight(getActivity())*weight);
		v.setLayoutParams(params);
	}
	
	/**
	 * 设置控件的宽高
	 * @param v
	 * @param bitmap
	 */
	protected void setViewHight(View v, Bitmap bitmap) {
		if (bitmap == null || v == null) {
			return;
		}
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v
				.getLayoutParams();
		params.height = bitmap.getHeight();
		params.width = bitmap.getWidth();
		v.setLayoutParams(params);
	}

}
