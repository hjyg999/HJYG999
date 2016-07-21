package com.md.hjyg.layoutEntities;

import com.md.hjyg.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 分享弹出窗口
 */
public class SelectPopupWindow extends PopupWindow {

	TextView weixin, weixin_frends, qq, qqzone, sina, email, sms, copy,
			btn_cancel;
	LinearLayout linear;
	private View mMenuView;
	Activity context;

	public SelectPopupWindow(Activity context, OnClickListener itemsOnClick,
			boolean bol) {
		super(context);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.layout_picpopupwindow, null);
		TextView tv_titile = (TextView) mMenuView.findViewById(R.id.tv_titile);
		if (bol) {
			tv_titile.setVisibility(View.VISIBLE);
		} else {
			tv_titile.setVisibility(View.GONE);
		}
		weixin = (TextView) mMenuView.findViewById(R.id.weixin);
		weixin_frends = (TextView) mMenuView.findViewById(R.id.weixin_frends);
		qq = (TextView) mMenuView.findViewById(R.id.qq);
		qqzone = (TextView) mMenuView.findViewById(R.id.qqzone);
		sina = (TextView) mMenuView.findViewById(R.id.sina);
		sms = (TextView) mMenuView.findViewById(R.id.sms);
		email = (TextView) mMenuView.findViewById(R.id.email);
		copy = (TextView) mMenuView.findViewById(R.id.copy);
		btn_cancel = (TextView) mMenuView.findViewById(R.id.btn_cancel);
		linear = (LinearLayout) mMenuView.findViewById(R.id.linear);
		// 取消按钮
		btn_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 销毁弹出框
				dismiss();
			}
		});
		// 设置按钮监听
		weixin.setOnClickListener(itemsOnClick);
		weixin_frends.setOnClickListener(itemsOnClick);
		qq.setOnClickListener(itemsOnClick);
		qqzone.setOnClickListener(itemsOnClick);
		sina.setOnClickListener(itemsOnClick);
		sms.setOnClickListener(itemsOnClick);
		email.setOnClickListener(itemsOnClick);
		copy.setOnClickListener(itemsOnClick);
		// btn_cancel.setOnClickListener(itemsOnClick);
		linear.setOnClickListener(itemsOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(context.getResources().getColor(
				R.color.bg_list));
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.linear).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}

	/**
	 * 消费标分享弹窗
	 * 
	 * @param context
	 * @param itemsOnClick
	 */
	@SuppressLint("InflateParams")
	public SelectPopupWindow(Activity context, OnClickListener itemsOnClick,int tab) {
		super(context);
		this.context = (Activity) context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.layout_consumppopupwindow, null);
		weixin = (TextView) mMenuView.findViewById(R.id.weixin);
		weixin_frends = (TextView) mMenuView.findViewById(R.id.weixin_frends);
		qq = (TextView) mMenuView.findViewById(R.id.qq);
		qqzone = (TextView) mMenuView.findViewById(R.id.qqzone);
		btn_cancel = (TextView) mMenuView.findViewById(R.id.btn_cancel);
		// 取消按钮
		btn_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 销毁弹出框
				dismiss();
			}
		});
		// 设置按钮监听
		weixin.setOnClickListener(itemsOnClick);
		weixin_frends.setOnClickListener(itemsOnClick);
		qq.setOnClickListener(itemsOnClick);
		qqzone.setOnClickListener(itemsOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(context.getResources().getColor(
				R.color.bg_list));
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);

	}

	/** 分享红包弹窗 */
	public SelectPopupWindow(Activity context, OnClickListener itemsOnClick) {
		super(context);
		mMenuView = LayoutInflater.from(context).inflate(
				R.layout.layout_redpak_popup, null);
		weixin = (TextView) mMenuView.findViewById(R.id.weixin);
		weixin_frends = (TextView) mMenuView.findViewById(R.id.weixin_frends);
		qq = (TextView) mMenuView.findViewById(R.id.qq);
		btn_cancel = (TextView) mMenuView.findViewById(R.id.btn_cancel);
		// 取消按钮
		btn_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 销毁弹出框
				dismiss();
			}
		});
		// 设置按钮监听
		weixin.setOnClickListener(itemsOnClick);
		weixin_frends.setOnClickListener(itemsOnClick);
		qq.setOnClickListener(itemsOnClick);

		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		ColorDrawable dw = new ColorDrawable(context.getResources().getColor(
				R.color.bg_list));
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
	}

	/** 选择证件类型弹窗 */
	public SelectPopupWindow(Activity context, final Handler mHandler, int Width) {
		super(context);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.card_type_layout, null);
		RelativeLayout rel_type0 = (RelativeLayout) mMenuView
				.findViewById(R.id.rel_type0);
		RelativeLayout rel_type1 = (RelativeLayout) mMenuView
				.findViewById(R.id.rel_type1);
		RelativeLayout rel_type2 = (RelativeLayout) mMenuView
				.findViewById(R.id.rel_type2);
		final CheckBox checkBox0 = (CheckBox) mMenuView
				.findViewById(R.id.checkBox0);
		final CheckBox checkBox1 = (CheckBox) mMenuView
				.findViewById(R.id.checkBox1);
		final CheckBox checkBox2 = (CheckBox) mMenuView
				.findViewById(R.id.checkBox2);
		// checkBox0.setChecked(true);
		// 设置按钮监听
		rel_type0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkBox0.isChecked()) {
					checkBox0.setChecked(false);
					checkBox1.setChecked(false);
					checkBox2.setChecked(false);
				} else {
					checkBox1.setChecked(false);
					checkBox2.setChecked(false);
					// checkBox0.setFocusable(true);
					checkBox0.setChecked(true);
				}
				// dismiss();

			}
		});
		rel_type1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkBox1.isChecked()) {
					checkBox0.setChecked(false);
					checkBox1.setChecked(false);
					checkBox2.setChecked(false);
				} else {
					checkBox0.setChecked(false);
					checkBox2.setChecked(false);
					checkBox1.setChecked(true);
				}
				// dismiss();
				mHandler.sendEmptyMessage(1);
			}
		});
		rel_type2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkBox2.isChecked()) {
					checkBox0.setChecked(false);
					checkBox1.setChecked(false);
					checkBox2.setChecked(false);
				} else {
					checkBox0.setChecked(false);
					checkBox1.setChecked(false);
					checkBox2.setChecked(true);
				}
				// dismiss();
				mHandler.sendEmptyMessage(2);
			}
		});

		// checkBox0.get

		checkBox0.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					checkBox0.setFocusable(true);
					checkBox0.setFocusableInTouchMode(true);
					checkBox0.setBackgroundResource(R.drawable.pressed);
					mHandler.sendEmptyMessage(0);
					dismiss();
				}

			}
		});

		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(Width);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimRghtTop);

	}

}
