package com.md.hjyg.utility;

import com.md.hjyg.R;
import com.md.hjyg.utils.ViewParamsSetUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ClassName: DialogManager date: 2016-3-3 下午3:39:10 remark:
 * 自定义dialog管理-仿苹果的白色dialog
 * 
 * @author pyc
 */
public class DialogManager implements OnClickListener {

	/**
	 * 取消按钮
	 */
	public final static int CANCEL_BTN = R.id.tv_dialog_cancel;
	/**
	 * 确定按钮
	 */
	public final static int CONFIRM_BTN = R.id.tv_dialog_confirm;

	private Dialog mDialog;
	private Activity mActivity;
	private TextView tv_dialog_cancel, tv_dialog_confirm;
	private TextView tv_dialog_title, tv_dialog_content;
	private ImageView img;
	private OnButListener butListener;

	// private int hight;

	public DialogManager(Activity mActivity) {
		this.mActivity = mActivity;
		// int h= ScreenUtils.getScreenHeight(mActivity);
		// hight = h/3 - h/2;
	}

	@SuppressLint("InflateParams")
	public void initTwoBtnDialog(OnClickListener listener) {
		mDialog = new Dialog(mActivity, R.style.m_dialogstyle);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
			}
		});

		View view = LayoutInflater.from(mActivity).inflate(
				R.layout.dialog_twobtn_white_layout, null);
		// 标题和内容
		tv_dialog_title = (TextView) view.findViewById(R.id.tv_dialog_title);
		tv_dialog_content = (TextView) view
				.findViewById(R.id.tv_dialog_content);
		// 取消按钮
		tv_dialog_cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
		// 图片
		img = (ImageView) view.findViewById(R.id.img);
		img.setVisibility(View.GONE);

		// 确定按钮
		tv_dialog_confirm = (TextView) view
				.findViewById(R.id.tv_dialog_confirm);

		if (listener != null) {
			tv_dialog_cancel.setOnClickListener(listener);
			tv_dialog_confirm.setOnClickListener(listener);
		} else {
			tv_dialog_cancel.setOnClickListener(this);
			tv_dialog_confirm.setOnClickListener(this);
		}

		mDialog.setContentView(view);

		// Window dialogWindow = mDialog.getWindow();
		// dialogWindow.setGravity(Gravity.CENTER);
		// WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// lp.width = (int) (ScreenUtils.getScreenWidth(mActivity) * 0.8);
		// lp.y = hight;
		// dialogWindow.setAttributes(lp);
		ViewParamsSetUtil.setDialogPosition(mDialog);

	}

	/**
	 * 
	 * @param cancel
	 *            - 取消按钮的文字
	 * @param confirm
	 *            - 确定按钮的文字
	 * @param Title
	 *            - 标题
	 * @param Content
	 *            - 内容
	 * @param listener
	 *            - 点击监听，可以传null
	 */
	public void initTwoBtnDialog(CharSequence cancel, CharSequence confirm,
			CharSequence Title, CharSequence Content, OnClickListener listener) {
		initTwoBtnDialog(listener);
		tv_dialog_title.setText(Title);
		tv_dialog_content.setText(Content);
		tv_dialog_confirm.setText(confirm);
		tv_dialog_cancel.setText(cancel);
		Show();
	}

	/**
	 * 
	 * @param Content
	 *            内容
	 * @param onDismissListener
	 */
	public void initOneBtnDialog(CharSequence Content,
			OnDismissListener onDismissListener) {
		initOneBtnDialog("确定", "提示", Content, onDismissListener);
	}

	/**
	 * @param icon
	 *            true 正确的图标
	 * @param Content
	 *            内容
	 * @param onDismissListener
	 */
	public void initOneBtnDialog(boolean icon,CharSequence Content, OnDismissListener onDismissListener){
		initOneBtnDialog("确定", "提示", Content, onDismissListener);
		if (icon) {
			img.setImageResource(R.drawable.tishzq_28x28);
		}else {
			img.setImageResource(R.drawable.tishcw_28x28);
		}
		img.setVisibility(View.VISIBLE);
	}
	public void initOneBtnDialog(boolean icon,CharSequence Title,CharSequence Content, OnDismissListener onDismissListener){
		initOneBtnDialog("确定", Title, Content, onDismissListener);
		if (icon) {
			img.setImageResource(R.drawable.tishzq_28x28);
		}else {
			img.setImageResource(R.drawable.tishcw_28x28);
		}
		img.setVisibility(View.VISIBLE);
	}

	public void initTwoBtnDialog(boolean icon, CharSequence cancel,
			CharSequence confirm, CharSequence Title, CharSequence Content,
			OnClickListener listener) {
		initTwoBtnDialog(cancel, confirm, Title, Content, listener);
		if (icon) {
			img.setImageResource(R.drawable.tishzq_28x28);
		} else {
			img.setImageResource(R.drawable.tishcw_28x28);
		}
		img.setVisibility(View.VISIBLE);
	}
	public void initTwoBtnDialog(int rid, CharSequence cancel,
			CharSequence confirm, CharSequence Title, CharSequence Content,
			OnClickListener listener) {
		initTwoBtnDialog(cancel, confirm, Title, Content, listener);
		img.setImageResource(rid);
		img.setVisibility(View.VISIBLE);
	}

	/**
	 * 一个按钮的mDialog
	 * 
	 * @param confirm
	 *            -按钮的内容
	 * @param Title
	 *            - 标题
	 * @param Content
	 *            - 内容信息
	 * @param onDismissListener
	 *            - 取消监听,，可以传null
	 */
	public void initOneBtnDialog(CharSequence confirm, CharSequence Title,
			CharSequence Content, OnDismissListener onDismissListener) {
		initOneBtnDialog();

		if (onDismissListener != null) {
			mDialog.setOnDismissListener(onDismissListener);
		}

		tv_dialog_title.setText(Title);
		tv_dialog_content.setText(Content);
		tv_dialog_confirm.setText(confirm);
		Show();
	}

	public void initOneBtnDialog(CharSequence confirm, CharSequence Title,
			CharSequence Content, OnDismissListener onDismissListener,
			OnClickListener l) {
		initOneBtnDialog();

		if (l != null) {
			tv_dialog_confirm.setOnClickListener(l);
		}

		tv_dialog_title.setText(Title);
		tv_dialog_content.setText(Content);
		tv_dialog_confirm.setText(confirm);
		Show();
	}

	@SuppressLint("InflateParams")
	public void initOneBtnDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
		mDialog = new Dialog(mActivity, R.style.m_dialogstyle);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
			}
		});

		View view = LayoutInflater.from(mActivity).inflate(
				R.layout.dialog_onebtn_white_layout, null);
		// 标题和内容
		tv_dialog_title = (TextView) view.findViewById(R.id.tv_dialog_title);
		tv_dialog_content = (TextView) view
				.findViewById(R.id.tv_dialog_content);
		// 确定按钮
		tv_dialog_confirm = (TextView) view
				.findViewById(R.id.tv_dialog_confirm);
		tv_dialog_confirm.setOnClickListener(this);
		// 图片
		img = (ImageView) view.findViewById(R.id.img);
		img.setVisibility(View.GONE);

		mDialog.setContentView(view);

		// Window dialogWindow = mDialog.getWindow();
		// dialogWindow.setGravity(Gravity.CENTER);
		// WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// lp.width = (int) (ScreenUtils.getScreenWidth(mActivity) * 0.8);
		// lp.y = hight;
		// dialogWindow.setAttributes(lp);
		ViewParamsSetUtil.setDialogPosition(mDialog);
	}

	/**
	 * 确定按钮的点击监听
	 * 
	 * @param listener
	 */
	public void setConfirmBitOnClickListener(OnClickListener listener) {
		if (tv_dialog_confirm != null) {
			tv_dialog_confirm.setOnClickListener(listener);
		}
	}

	/**
	 * 设置提示文字
	 * 
	 * @param Title
	 *            标题
	 * @param Content
	 *            内容
	 */
	public void setTitleandContent(CharSequence Title, CharSequence Content) {
		if (tv_dialog_title != null) {
			tv_dialog_title.setText(Title);
		}
		if (tv_dialog_content != null) {
			tv_dialog_content.setText(Content);
		}

	}

	/**
	 * 设置按钮的文字
	 * 
	 * @param cancel
	 *            取消
	 * @param confirm
	 *            查看
	 */
	public void setButText(CharSequence cancel, CharSequence confirm) {
		if (tv_dialog_cancel != null) {
			tv_dialog_cancel.setText(cancel);
		}
		if (tv_dialog_confirm != null) {
			tv_dialog_confirm.setText(confirm);
		}
	}

	/**
	 * 设置按钮的文字
	 * 
	 * @param confirm
	 *            确定
	 */
	public void setButText(CharSequence confirm) {
		if (tv_dialog_confirm != null) {
			tv_dialog_confirm.setText(confirm);
		}
	}

	/**
	 * mDialog 取消完成后的监听
	 * 
	 * @param listener
	 */
	public void setOnDismissListener(OnDismissListener listener) {
		if (mDialog != null) {
			mDialog.setOnDismissListener(listener);
		}
	}

	public void Show() {
		if (!mActivity.isFinishing() && mDialog != null && !mDialog.isShowing()) {
			try {// 抓捕异常，防止程序崩溃
				mDialog.show();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 自定义的按钮点击监听
	 * 
	 * @param butListener
	 */
	public void setOnButListener(OnButListener butListener) {
		this.butListener = butListener;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_dialog_cancel:
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}

			if (butListener != null) {
				butListener.dialogCancel();
			}

			break;
		case R.id.tv_dialog_confirm:
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}

			if (butListener != null) {
				butListener.dialogConfirm();
			}

			break;

		default:
			break;
		}

	}

	public void dismiss() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
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
			}
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

	public Dialog getmDialog() {
		return mDialog;
	}

	public TextView getTv_dialog_cancel() {
		return tv_dialog_cancel;
	}

	public TextView getTv_dialog_confirm() {
		return tv_dialog_confirm;
	}

	public TextView getTv_dialog_title() {
		return tv_dialog_title;
	}

	public TextView getTv_dialog_content() {
		return tv_dialog_content;
	}

	public interface OnButListener {
		/**
		 * 点击取消按钮时需要进行的操作
		 */
		public void dialogCancel();

		/**
		 * 点击确定按钮时需要进行的操作
		 */
		public void dialogConfirm();
	}

}
