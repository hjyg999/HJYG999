package com.md.hjyg.utility;

import com.md.hjyg.R;
import com.md.hjyg.activities.HomeFragmentActivity;
import com.md.hjyg.utils.ScreenUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ClassName: MyProgressDialog date: 2016-1-19 下午1:06:19 remark:网络处理进度条Dialog
 * 
 * @author pyc
 */
public class MyProgressDialog {

	private Activity mActivity;
	private long time;
	/** 持续时间 */
	private int continueTime = 10 * 1000;
	// private int continueTime = 0;
	private Dialog progressdialog;
	private boolean isShowing;
	private String msg;

	public MyProgressDialog(Activity mActivity) {
		this.mActivity = mActivity;

	}

	public MyProgressDialog(Activity mActivity, String msg) {
		this.mActivity = mActivity;
		this.msg = msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	/** 数据提交加载 */
	@SuppressLint("InflateParams")
	public void showProgressDialog() {
		progressdialog = new Dialog(mActivity, R.style.add_dialog);
		progressdialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (!progressdialog.isShowing()) {
					isShowing = false;
				}

			}
		});
		progressdialog.setCanceledOnTouchOutside(false);// 点击外部不销毁
		View view = LayoutInflater.from(mActivity).inflate(
				R.layout.myprogressdialog, null);
		if (msg != null && msg.length() > 0) {
			TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
			tv_msg.setText(msg);
		}
		progressdialog.setContentView(view);
		progressdialog.setOnKeyListener(keylistener);
		Window dialogWindow = progressdialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		lp.width = (int) (ScreenUtils.getScreenWidth(mActivity) * 0.65);
		dialogWindow.setAttributes(lp);
		if (!mActivity.isFinishing()) {
			try {// 抓捕异常，防止程序崩溃
				progressdialog.show();
				isShowing = true;
				time = System.currentTimeMillis() + continueTime;
				new MyAsyncTask().execute(1);
			} catch (Exception e) {
			}
		}
	}

	public void dismiss() {
		if (progressdialog != null && progressdialog.isShowing()) {
			progressdialog.dismiss();
			isShowing = false;
		}
	}

	public void setOnDismissListener(OnDismissListener listener) {
		if (progressdialog != null) {
			progressdialog.setOnDismissListener(listener);
		}
	}

	/** 网络超时10秒后显示此Dialog */
	@SuppressLint("InflateParams")
	public void showeErrorDialog() {
		progressdialog = new Dialog(mActivity, R.style.add_dialog);
		progressdialog.setCanceledOnTouchOutside(true);
		progressdialog.setOnKeyListener(null);
		View view = LayoutInflater.from(mActivity).inflate(
				R.layout.layout_alertdialog_onebtn_view, null);
		progressdialog.setContentView(view);
		Window dialogWindow = progressdialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		lp.width = (int) (ScreenUtils.getScreenWidth(mActivity) * 0.9);
		dialogWindow.setAttributes(lp);
		// 关闭按钮
		ImageView dialog_close = (ImageView) view
				.findViewById(R.id.dialog_close);
		dialog_close.setVisibility(View.INVISIBLE);
		// 信息提示
		TextView dialog_msg = (TextView) view.findViewById(R.id.dialog_msg);
		// 确定按钮
		TextView dialog_ok = (TextView) view.findViewById(R.id.dialog_ok);

		dialog_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (progressdialog.isShowing()) {
					progressdialog.dismiss();

					if (mActivity instanceof HomeFragmentActivity) {
						((HomeFragmentActivity) mActivity).finishFragment();
					} else {
						mActivity.finish();
						mActivity.overridePendingTransition(
								R.anim.trans_lift_in, R.anim.trans_right_out);
					}
				}
			}
		});
		dialog_msg.setText("连接错误，请关闭界面重试！");
		if (!mActivity.isFinishing()) {
			try {// 抓捕异常，防止程序崩溃
				progressdialog.show();
			} catch (Exception e) {
			}
		}
	}

	private class MyAsyncTask extends AsyncTask<Integer, Void, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			while (time - System.currentTimeMillis() > 0 && isShowing) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (isShowing && !mActivity.isFinishing()) {
				dismiss();
				showeErrorDialog();
			}

		}

	}

	public boolean isShowing() {
		return isShowing;
	}

	public void setisShowing() {
		isShowing = false;
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
