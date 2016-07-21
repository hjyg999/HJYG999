package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.CircularImage;
import com.md.hjyg.layoutEntities.GestureContentView;
import com.md.hjyg.layoutEntities.GestureDrawline.GestureCallBack;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 手势密码设置界面
 * 
 */
public class GestureEditActivity extends BaseActivity implements
		OnClickListener {
	/** 手机号码 */
	public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
	/** 意图 */
	public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
	/** 首次提示绘制手势密码，可以选择跳过 */
	public static final String PARAM_IS_FIRST_ADVICE = "PARAM_IS_FIRST_ADVICE";
	// private LockIndicator mLockIndicator;
	private TextView mTextTip;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
	private TextView mTextReset;
	private boolean mIsFirstInput = true;
	private String mFirstPassword = null;

//	private ImageView Cancel;
	private Intent intent;

	private Bitmap head_bgBitmap;
	private Context mcontext;
	private int ScreenHeight;
	private RelativeLayout re_head;
	private CircularImage head_sex;
	private LinearLayout gesture_tip_layout;
	private LinearLayout lin_bot;
	private boolean fromLogin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_edit);
		mcontext = getBaseContext();
		setUpViews();
		setUpListeners();
		setImg();
	}

	private void setUpViews() {
		HeaderViewControler.setHeaderView(this, "设置手势密码", this);
		fromLogin = getIntent().getBooleanExtra("fromLogin", false);
		// 头像
		re_head = (RelativeLayout) findViewById(R.id.re_head);
		head_sex = (CircularImage) findViewById(R.id.head_sex);
		// 提示信息
		gesture_tip_layout = (LinearLayout) findViewById(R.id.gesture_tip_layout);
		lin_bot = (LinearLayout) findViewById(R.id.lin_bot);

		/*
		 * mTextTitle = (TextView) findViewById(R.id.text_title); mTextCancel =
		 * (TextView) findViewById(R.id.text_cancel);
		 */

		mTextReset = (TextView) findViewById(R.id.text_reset);
		// mTextReset.setClickable(false);
		// mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);
		mTextTip = (TextView) findViewById(R.id.text_tip);
		mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
		// 初始化一个显示各个点的viewGroup
		mGestureContentView = new GestureContentView(this, false, "",
				new GestureCallBack() {
					@Override
					public void onGestureCodeInput(String inputCode) {
						if (!isInputPassValidate(inputCode)) {
							mTextTip.setText(Html
									.fromHtml("<font color='#ff9933'>最少链接4个点, 请重新绘制</font>"));
							mGestureContentView.clearDrawlineState(0L);
							mTextReset.setVisibility(View.GONE);
							return;
						}
						if (mIsFirstInput) {
							// 第一次绘制后刷新
							mFirstPassword = inputCode;
							updateCodeList(inputCode);
							mGestureContentView.clearDrawlineState(0L);
							mTextReset.setClickable(true);
							mTextReset.setVisibility(View.VISIBLE);
							// mTextReset
							// .setText(getString(R.string.reset_gesture_code));
							mIsFirstInput = false;
							mTextTip.setText("请确认手势密码");
						} else {
							if (inputCode.equals(mFirstPassword)) {
								mGestureContentView.clearDrawlineState(0L);
								Constants.SetGestureLockPassword(
										Constants.md5(mFirstPassword),
										GestureEditActivity.this);
								Constants.SetGestureLockisOpend(true,
										GestureEditActivity.this);

								Toast.makeText(GestureEditActivity.this,
										"设置成功", Toast.LENGTH_SHORT).show();
								mTextReset.setVisibility(View.GONE);
								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								if (AppController.isFromGestureVerify) {
									// startActivity( new
									// Intent(GestureEditActivity.this,
									// HomeActivity.class));
									startActivity(new Intent(
											GestureEditActivity.this,
											HomeFragmentActivity.class));
									AppController.isFromGestureVerify = false;
									overTransition(1);
									GestureEditActivity.this.finish();
								} else if (fromLogin) {
									intent = new Intent(
											GestureEditActivity.this,
											HomeFragmentActivity.class);
									intent.putExtra("tab", 2);
									intent.putExtra("fromLogin", true);
									startActivity(intent);
									overTransition(1);
									GestureEditActivity.this.finish();

								} else {

									GestureEditActivity.this.finish();
									overTransition(1);
								}

							} else {
								mTextTip.setText(Html
										.fromHtml("<font color='#ff9933'>与上一次绘制不一致，请重新绘制</font>"));
								// 左右移动动画
								Animation shakeAnimation = AnimationUtils
										.loadAnimation(
												GestureEditActivity.this,
												R.anim.shake);
								mTextTip.startAnimation(shakeAnimation);
								// 保持绘制的线，1.5秒后清除
								mGestureContentView.clearDrawlineState(1300L);
								mTextReset.setVisibility(View.GONE);
								// 两次密码不一致，刷新重新绘制
								mIsFirstInput = true;
								updateCodeList("");
								new Thread() {
									public void run() {
										try {
											Thread.sleep(1300);
											handler.sendEmptyMessage(1);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									};
								}.start();

							}
						}
						// mIsFirstInput = false;
					}

					@Override
					public void checkedSuccess() {

					}

					@Override
					public int checkedFail() {
						return 0;
					}
				});
		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(mGestureContainer);
		updateCodeList("");
	}

	private void setUpListeners() {
		// mTextCancel.setOnClickListener(this);
		mTextReset.setOnClickListener(this);
	}

	private void updateCodeList(String inputCode) {
		// 更新选择的图案
		// mLockIndicator.setPath(inputCode);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			if (AppController.isFromGestureVerify) {
				startActivity(new Intent(GestureEditActivity.this,
						GestureVerifyActivity.class));
				overTransition(1);
				this.finish();
			} else if (fromLogin) {
				intent = new Intent(GestureEditActivity.this,
						HomeFragmentActivity.class);
				intent.putExtra("tab", 2);
				intent.putExtra("fromLogin", true);
				startActivity(intent);
				overTransition(1);
				this.finish();
			} else {
				this.finish();
				overTransition(1);
			}

			break;
		case R.id.text_reset:
			mIsFirstInput = true;
			updateCodeList("");
			mTextTip.setText(getString(R.string.set_gesture_pattern));
			mTextReset.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	private boolean isInputPassValidate(String inputPassword) {
		if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
			return false;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			if (AppController.isFromGestureVerify) {
				startActivity(new Intent(GestureEditActivity.this,
						GestureVerifyActivity.class));
				overTransition(1);
				this.finish();
			} else if (fromLogin) {
				intent = new Intent(GestureEditActivity.this,
						HomeFragmentActivity.class);
				intent.putExtra("tab", 2);
				intent.putExtra("fromLogin", true);

				startActivity(intent);
				overTransition(1);
				this.finish();

			} else {
				this.finish();
				overTransition(1);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressLint("HandlerLeak")
	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mTextTip.setText(getString(R.string.set_gesture_pattern));
			mTextReset.setText("重新设置手势密码");
		};
	};

	/**
	 * 缩放图片并加载图片
	 */
	private void setImg() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				Bitmap reimg = BitmapFactory.decodeResource(getResources(),
						R.drawable.novice_red_bg);
				head_bgBitmap = Save.ScaleBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.gesture_xt_bg_gray),
						mcontext, reimg);
				ScreenHeight = ScreenUtils.getScreenHeight(mcontext);

				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						// 设置头像的大小
						LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) re_head
								.getLayoutParams();
						int bg_height = head_bgBitmap.getHeight();
						params.height = bg_height;
						params.width = bg_height;
						params.topMargin = (int) (ScreenHeight * 0.07);
						re_head.setLayoutParams(params);
						// 设置头像
						String savefilename = Constants
								.GetSaveFilename(mcontext);
						// 判断是否已经设置了头像
						if (Save.isSaveBitmap(savefilename)) {
							Bitmap image = Save.getBitmap(savefilename);
							head_sex.setImageBitmap(image);
						} else {// 没有设置，用默认的图片
							head_sex.setImageResource(R.drawable.headimg);
						}
						re_head.setVisibility(View.VISIBLE);

						LinearLayout.LayoutParams tip_params = (LinearLayout.LayoutParams) gesture_tip_layout
								.getLayoutParams();
						tip_params.topMargin = (int) (ScreenHeight * 0.018);
						tip_params.bottomMargin = (int) (ScreenHeight * 0.0625);
						gesture_tip_layout.setLayoutParams(tip_params);
						gesture_tip_layout.setVisibility(View.VISIBLE);

						int pad = (int) (ScreenHeight * 0.07);
						lin_bot.setPadding(pad, 0, pad, 0);
						lin_bot.setVisibility(View.VISIBLE);

					}
				});
			}
		}).start();
	}
}
