package com.md.hjyg.utility;

import java.util.Timer;
import java.util.TimerTask;

import com.md.hjyg.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ClassName: LoadingMenager date: 2016-4-12 下午1:50:16 remark:加载管理
 * 
 * @author pyc
 */
public class LoadingMenager {

	/**
	 * 线性布局
	 */
	public final static int Root_ID = R.id.lin_inc_loading;
	/**
	 * imgView的ID
	 */
	public final static int Root_imgID = R.id.img_loading_m;
	private ImageView img_loading_m;
	private TextView tv_msg;
	private AnimationDrawable frameAnim;
	private View view;
	private Timer timer;
	private TimerTask mTimerTask;
	private OnClickListener Listener;
	private String msg = "努力加载中...";

	public LoadingMenager(Context context, View v, OnClickListener l) {
		this.view = v;
		this.Listener = l;
		view.setVisibility(View.VISIBLE);
		v.setOnClickListener(l);
		img_loading_m = (ImageView) v.findViewById(R.id.img_loading_m);
		frameAnim = (AnimationDrawable) context.getResources().getDrawable(
				R.drawable.meeting_loading_m);
		tv_msg = (TextView) v.findViewById(R.id.tv_msg);
		reset();
	}

	public LoadingMenager(Context context, View v, String msg, OnClickListener l) {
		this(context, v, l);
		this.msg = msg;
	}

	/**
	 * 加载完成，取消界面
	 */
	public void dismiss() {
		view.setVisibility(View.GONE);
		if (frameAnim != null) {
			frameAnim.stop();
		}
		img_loading_m.setImageDrawable(null);
		frameAnim = null;
		dismissTimer();
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

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				frameAnim.stop();
				img_loading_m.setImageResource(R.drawable.jxw_ku_142x180);
				// img_loading_m.setOnClickListener(Listener);
				tv_msg.setText("加载失败！");
				dismissTimer();
				mHandler.sendEmptyMessageDelayed(1, 500);
				break;
			case 1:
				dismiss();
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 重置加载
	 */
	public void reset() {
		tv_msg.setText(msg);
		img_loading_m.setOnClickListener(null);
		img_loading_m.setImageDrawable(frameAnim);
		frameAnim.start();
		initTimer();
	}

}
