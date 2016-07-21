package com.md.hjyg.utility;

import java.util.Timer;
import java.util.TimerTask;

import com.md.hjyg.R;
import com.md.hjyg.utils.ViewParamsSetUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ClassName: DialogProgressManager date: 2016-3-28 上午9:20:32 remark:白色加载框
 * 
 * @author pyc
 */
public class DialogProgressManager {

	private Activity mActivity;
	private Dialog mDialog;
	private ImageView img_top, img_hit;
	private TextView tv_msg;
	private AnimationDrawable frameAnim;
	private int[] top_imgRids;
	private int[] hit_imgRids;
	private String msg;
	private Timer timer;
	private TimerTask mTimerTask;
//	private int hight;
	private boolean isover;
	

	public DialogProgressManager(Activity mActivity,String msg) {
		this.msg = msg;
		this.mActivity = mActivity;
//		int h= ScreenUtils.getScreenHeight(mActivity);
//		hight = h/3 - h/2;
		
		top_imgRids = new int[2];
		hit_imgRids = new int[2];
		top_imgRids[0] = R.drawable.tishzq_28x28;
		top_imgRids[1] = R.drawable.tishcw_28x28;
		hit_imgRids[0] = R.drawable.jxw_kx_94_120;
		hit_imgRids[1] = R.drawable.jxw_lh_94_120;
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 10m秒后无反应者提示网络异常
				isover = true;
				dismiss();

				break;

			default:
				break;
			}
		};
	};

	/**
	 * 初始化
	 */
	@SuppressLint("InflateParams")
	public void initDialog() {
		isover = false;
		mDialog = new Dialog(mActivity, R.style.m_dialogstyle);
		mDialog.setCanceledOnTouchOutside(false);// 点击外部不销毁
		mDialog.setOnKeyListener(keylistener);
		View view = LayoutInflater.from(mActivity).inflate(
				R.layout.dialog_img_white_layout, null);
		mDialog.setContentView(view);
		img_top = (ImageView) view.findViewById(R.id.img_top);
		frameAnim = (AnimationDrawable) mActivity.getResources().getDrawable(
				R.drawable.dialog_load);
		img_top.setImageDrawable(frameAnim);
		frameAnim.start();
		img_hit = (ImageView) view.findViewById(R.id.img_hit);
		img_hit.setVisibility(View.GONE);
		tv_msg = (TextView) view.findViewById(R.id.tv_msg);
		tv_msg.setText(msg);

//		Window dialogWindow = mDialog.getWindow();
//		dialogWindow.setGravity(Gravity.CENTER);
//		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//		lp.width = (int) (ScreenUtils.getScreenWidth(mActivity) * 0.8);
//		lp.y = hight;
//		dialogWindow.setAttributes(lp);
		ViewParamsSetUtil.setDialogPosition(mDialog);
		
		Show();

	}
	
	public void setMsg(String msg){
		this.msg = msg;
	}
	
	public boolean isShowing(){
		if (mDialog != null) {
			return mDialog.isShowing();
		}
		return false;
	}
	
	/**
	 * 取消定时器
	 */
	private void dismissTimer() {
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	/**
	 * 初始化定时器TimerTask
	 */
	private void initTimerTask() {
		mTimerTask = new TimerTask() {

			@Override
			public void run() {
				mHandler.sendEmptyMessage(0);
			}
		};
	}

	/**
	 * 10秒定时器
	 */
	private void initTimer() {
		if (timer == null) {
			timer = new Timer();
		}
		if (mTimerTask == null) {
			initTimerTask();
		}
		timer.schedule(mTimerTask, 10000);
	}

	/**
	 * 设置Dialog信息
	 * 
	 * @param i
	 *            0 为成功图片，1为失败图片
	 * @param msg
	 *            文字信息
	 */
	public void setImgandMsg(int i, String msg) {
		if (img_top != null && img_hit != null && tv_msg != null) {
			frameAnim.stop();
			img_top.setImageResource(top_imgRids[i]);
			img_hit.setImageResource(hit_imgRids[i]);
			tv_msg.setText(msg);
		}
	}

	/**
	 * 设置点击外部是否取消
	 * 
	 * @param bol
	 *            点击外部是否取消
	 * @param iskeylistener
	 *            是否屏蔽掉返回键监听
	 */
	public void setCanceledOnTouchOutside(boolean bol, boolean iskeylistener) {
		if (mDialog != null) {
			mDialog.setCanceledOnTouchOutside(bol);
			if (iskeylistener) {
				mDialog.setOnKeyListener(keylistener);
			} else {
				mDialog.setOnKeyListener(null);
			}
		}
	}

	public void Show() {
		if (!mActivity.isFinishing() && mDialog != null) {
			try {// 抓捕异常，防止程序崩溃
				mDialog.show();
				initTimer();
			} catch (Exception e) {
			}
		}
	}

	public void dismiss() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
		if (frameAnim != null) {
			frameAnim.stop();
		}
		if (img_top != null) {
			img_top.setImageDrawable(null);
		}
		frameAnim = null;
		
		dismissTimer();
	}

	public void setOnDismissListener(OnDismissListener listener) {
		if (mDialog != null && isover) {
			mDialog.setOnDismissListener(listener);
		}
	}

	/** 返回键监听，屏蔽掉按返回键Dialog消失 */
	private OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				return true;
			} else {
				return false;
			}
		}
	};
	
}
