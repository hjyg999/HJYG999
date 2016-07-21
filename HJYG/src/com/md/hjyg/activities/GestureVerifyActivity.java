package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.LoginEntity;
import com.md.hjyg.layoutEntities.CircularImage;
import com.md.hjyg.layoutEntities.GestureContentView;
import com.md.hjyg.layoutEntities.GestureDrawline.GestureCallBack;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Process;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.Listener;
import com.umeng.analytics.MobclickAgent;

/**
 * 手势绘制/校验界面
 */
public class GestureVerifyActivity extends BaseActivity implements
		android.view.View.OnClickListener ,DftErrorListener{
	/** 手机号码 */
	public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
	/** 意图 */
	public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
	private TextView text_hint;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
	private TextView mTextForget;
	private TextView mTextOther;
	private Context mcontext;
	int count = 0;
	private Bitmap head_bgBitmap;

	private RelativeLayout re_head;
	private CircularImage head_sex;
	private LinearLayout gesture_tip_layout;
	private LinearLayout lin_bot;
	private int ScreenHeight;
	private DialogManager mDialog;
	private LoginEntity LoginEntity;
	private DialogProgressManager progressManager;
	private boolean isSuccess,isLoading,isDftError;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_verify);
		mcontext = getBaseContext();
		setUpViews();
		setImg();
		setUpListeners();
		getLockInfo();
		setGestureContentView();
	}

	private void setUpViews() {
		mDialog = new DialogManager(this);
		progressManager = new DialogProgressManager(this, getResources()
				.getString(R.string.loading));
		progressManager.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				isDftError = true;
				setLockInfo();
			}
		});
		dft.setOnDftErrorListener(this);
		mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
		mTextForget = (TextView) findViewById(R.id.text_forget_gesture);
		mTextOther = (TextView) findViewById(R.id.text_other_account);
		text_hint = (TextView) findViewById(R.id.text_hint);
		// 头像
		re_head = (RelativeLayout) findViewById(R.id.re_head);
		head_sex = (CircularImage) findViewById(R.id.head_sex);
		// 提示信息
		gesture_tip_layout = (LinearLayout) findViewById(R.id.gesture_tip_layout);
		lin_bot = (LinearLayout) findViewById(R.id.lin_bot);

	}

	private void setGestureContentView() {
		mGestureContentView = new GestureContentView(this, true,
				Constants.GetGestureLockPassword(mcontext),
				new GestureCallBack() {

					@Override
					public void onGestureCodeInput(String inputCode) {

					}

					@Override
					public void checkedSuccess() {
						AppController.isFromGestureVerify = false;
						isSuccess = true;
						mGestureContentView.clearDrawlineState(0L);
						// Toast.makeText(GestureVerifyActivity.this, "密码正确",
						// 0).show();
						// startActivity(new Intent(mcontext,
						// HomeActivity.class));
						setLockInfo();
					}

					@Override
					public int checkedFail() {
						count++;
						// mTextTip.setVisibility(View.VISIBLE);//提示输入错误
						if (count == 5) {

							text_hint.setText(Html
									.fromHtml("<font color='#ff9933'>您已经绘错了5次，不能再绘制，请按下面的提示进行</font>"));
							// 左右移动动画
							Animation shakeAnimation = AnimationUtils
									.loadAnimation(GestureVerifyActivity.this,
											R.anim.shake);
							text_hint.startAnimation(shakeAnimation);
							return count;
						}
						text_hint.setText(Html
								.fromHtml("<font color='#ff9933'>密码错误,您还可以重绘"
										+ (5 - count) + "次</font>"));
						// 左右移动动画
						Animation shakeAnimation = AnimationUtils
								.loadAnimation(GestureVerifyActivity.this,
										R.anim.shake);
						text_hint.startAnimation(shakeAnimation);
						mGestureContentView.clearDrawlineState(200L);
						return count;
					}
				});
		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(mGestureContainer);
	}

	private void setUpListeners() {
		// mTextCancel.setOnClickListener(this);
		mTextForget.setOnClickListener(this);
		mTextOther.setOnClickListener(this);
		// text_forget_gesture.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * case R.id.text_cancel: this.finish(); break;
		 */
		case R.id.text_forget_gesture:
			AppController.isFromGestureVerify = true;
			startActivity(new Intent(mcontext, AccountVerify.class));
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_lift_out);
			this.finish();
			break;
		case R.id.text_other_account:
			Constants.ClearSharePref(mcontext);
			Intent home = new Intent(mcontext, LoginActivity.class);
			startActivity(home);
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_lift_out);
			this.finish();
			break;
		default:
			break;
		}
	}

	private long exitTime = 0;

	/**
	 * 返回键监听，按两次返回键退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				MobclickAgent.onKillProcess(this);
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/** 获取用户锁定信息 */
	private void getLockInfo() {
		isLoading = true;
		dft.getNetInfoById(Constants.GetLockInfo_URL, Request.Method.GET,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						LoginEntity = (LoginEntity) dft.GetResponseObject(
								jsonObject, LoginEntity.class);
						isLoading = false;
						setLockInfo();

					}
				});
	}

	/**
	 * 判断账户是否锁定
	 */
	private void setLockInfo() {
		if (LoginEntity == null && !isDftError) {
			progressManager.initDialog();
			if (!isLoading) {
				getLockInfo();
			}
			return;
		}
		progressManager.dismiss();
		if (LoginEntity != null && !LoginEntity.IsLock && !isDftError) {
			Constants.ClearSharePref(mcontext);
			mDialog.initOneBtnDialog(LoginEntity.LockedMessage,
					new OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {
							startActivity(new Intent(mcontext,
									HomeFragmentActivity.class));
							overTransition(2);
						}
					});
		}else if (isSuccess) {
			startActivity(new Intent(mcontext,
					HomeFragmentActivity.class));
			overTransition(2);
			this.finish();
		}
	}

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

	@Override
	public void webLoadError() {
		isDftError = true;
		isLoading = false;
		progressManager.dismiss();
	}

}
