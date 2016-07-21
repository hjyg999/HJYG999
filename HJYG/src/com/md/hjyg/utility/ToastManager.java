package com.md.hjyg.utility;

import com.md.hjyg.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ClassName: ToastManager date: 2016-3-15 下午3:21:37 remark:自定义Toast
 * 
 * @author pyc
 */
public class ToastManager {

	@SuppressLint("InflateParams")
	public static void mToastshow(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.toast_meeting_layout, null);
		TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
		tv_content.setText(msg);

		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(view);
		toast.show();

	}

}
