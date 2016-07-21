package com.md.hjyg.layoutEntities;

import com.md.hjyg.R;
import com.md.hjyg.utility.Save;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * ClassName: LoginBotomImgViewControler date: 2016-4-12 上午9:36:52
 * remark:注册登录界面底部广告图片
 * 
 * @author pyc
 */
public class LoginBotomImgViewControler {

	public final static int ID = R.id.bottom_img;
	private ImageView imgView;

	public LoginBotomImgViewControler(Activity mActivity, OnClickListener l) {
		imgView = (ImageView) mActivity.findViewById(ID);
		imgView.setOnClickListener(l);
		Save.loadingImg(mHandler, mActivity,
				new int[] { R.drawable.aliyun_taipy });
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Bitmap[] bitmaps = (Bitmap[]) msg.obj;
				imgView.setImageBitmap(bitmaps[0]);
				break;

			default:
				break;
			}

		};
	};

}
