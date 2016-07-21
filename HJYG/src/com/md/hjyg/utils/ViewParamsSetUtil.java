package com.md.hjyg.utils;

import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.Save;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * ClassName: ViewParamsSetUtil date: 2016-3-11 下午3:32:28 remark:设置控件的宽高工具类
 * 
 * @author pyc
 */
public class ViewParamsSetUtil {

	/**
	 * 设置控件的高度
	 * 
	 * @param v
	 *            要设置的控件
	 * @param weight
	 *            占屏幕的比例
	 */
	public static void setViewHight(View v, float weight, Context context) {
		if (weight > 1 || v == null) {
			return;
		}
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v
				.getLayoutParams();
		params.height = (int) (ScreenUtils.getScreenHeight(context) * weight);
		v.setLayoutParams(params);
	}

	/**
	 * 设置控件的宽和高——父控件为RelativeLayout
	 * 
	 * @param v
	 * @param bitmap
	 */
	public static void setViewHandW_rel(View v, Bitmap bitmap) {
		if (bitmap == null) {
			return;
		}
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v
				.getLayoutParams();
		params.height = bitmap.getHeight();
		params.width = bitmap.getWidth();
		v.setLayoutParams(params);
	}

	/**
	 * 设置控件的宽和高——父控件为LinearLayout
	 * 
	 * @param v
	 * @param bitmap
	 */
	public static void setViewHandW_lin(View v, Bitmap bitmap) {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v
				.getLayoutParams();
		params.height = bitmap.getHeight();
		params.width = bitmap.getWidth();
		v.setLayoutParams(params);
	}

	/**
	 * 设置控件的宽和高——父控件为LinearLayout
	 * 
	 * @param v
	 * @param h
	 *            0为默认
	 * @param w
	 *            0为默认
	 */
	public static void setViewHandW_lin(View v, int h, int w) {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v
				.getLayoutParams();
		if (h != 0) {
			params.height = h;
		}

		if (w != 0) {
			params.width = w;
		}
		v.setLayoutParams(params);
	}

	public static void setViewHandW_fra(View v, int h, int w) {
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) v
				.getLayoutParams();
		if (h != 0) {
			params.height = h;
		}

		if (w != 0) {
			params.width = w;
		}
		v.setLayoutParams(params);
	}

	/**
	 * 设置控件的宽和高——父控件为RelativeLayout
	 * 
	 * @param v
	 * @param h
	 *            0为默认
	 * @param w
	 *            0为默认
	 */
	public static void setViewHandW_rel(View v, int h, int w) {
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v
				.getLayoutParams();
		if (h != 0) {
			params.height = h;
		}

		if (w != 0) {
			params.width = w;
		}
		v.setLayoutParams(params);
	}

	/**
	 * 
	 * @param v
	 *            需要设置的控件
	 * @param w
	 *            切图的宽
	 * @param h
	 *            切图的高
	 * @param isLin
	 *            true父控件为线性布局，fase父控件为相对布局
	 */
	public static void setViewParams(View v, int w, int h, boolean isLin) {
		int params[] = Save.getScaleBitmapWangH(w, h);
		if (isLin) {
			setViewHandW_lin(v, params[1], params[0]);
		} else {
			setViewHandW_rel(v, params[1], params[0]);
		}
	}
	/**
	 * 父控件为Fragment
	 * @param v
	 * @param w
	 * @param h
	 */
	public static void setViewParams(View v, int w, int h) {
		int params[] = Save.getScaleBitmapWangH(w, h);
		setViewHandW_fra(v, params[1], params[0]);
		
	}

	/**
	 * 设置Dialog显示的位置
	 * 
	 * @param mDialog
	 */
	public static void setDialogPosition(Dialog mDialog) {
		Window dialogWindow = mDialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = (int) (AppController.screenWidth * 0.8);
		// hight = h/3 - h/2;
		lp.y = -AppController.screenWidth / 9;
		dialogWindow.setAttributes(lp);
	}

}
