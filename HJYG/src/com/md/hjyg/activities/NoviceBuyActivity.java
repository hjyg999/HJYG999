package com.md.hjyg.activities;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.ActivityBidBuyModel;
import com.md.hjyg.entity.CaptchaModel;
import com.md.hjyg.entity.MemberInvestRedEnvelopeLogTypeEnum;
import com.md.hjyg.entity.NoviceLoan.LoanPicture;
import com.md.hjyg.entity.RedEnvelopeModel;
import com.md.hjyg.entity.UserSpecialHaveGiftModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.layoutEntities.KeyBoardView;
import com.md.hjyg.layoutEntities.KeyBoardView.ClosePopupWindowListener;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.AddRateSelectDialog;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.LruCacheWebBitmapManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;

/**
 * ClassName: NoviceBuyActivity date: 2016-2-26 上午9:24:04 remark: 新手专享-购买界面
 * 
 * @author pyc
 */
public class NoviceBuyActivity extends BaseActivity implements
		OnFocusChangeListener, OnClickListener, ClosePopupWindowListener,
		DftErrorListener {

	private LinearLayout line_f;
	private LinearLayout lin_extract;
	private CaptchaModel captchaModel;
	private Context mContext;
	private ImageView iv_captcha;
	private ImageView img_award;
	private Intent intent;
	private TextView tv_prizename, tv_support, tv_recharge, tv_canUsemoney,
			tv_value, tv_extracthit;
	private EditText ed_captcha;
	private ImageView img_delete;
	private DialogManager mDialog;
	/** 提交数据加载框 */
	private DialogProgressManager dialog;
	private TextView tv_tobuy;
	private String Title;
	private String MobilePhone;
	private String RealName;
	private String AddressInfo;
	private int ActivityId;
	/**
	 * 提取方式
	 */
	private int ExtractionMethod = -1;

	// 键盘
	private KeyBoardView mKeyBoardView;
	private int KeyBoardViewHeight;
	private RelativeLayout ll;
	// private GestureDetector mGestureDetector;
	private UserSpecialHaveGiftModel model;
	private LruCacheWebBitmapManager lruCacheWebBitmapManager;
	private boolean torecharge;
	private boolean isfrist = true;
	/** 红包列表： */
	private List<RedEnvelopeModel> RedEnvelopeList;
	private AddRateSelectDialog addRateSelectDialog;
	private int InvestId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_novicebuy_layout);
		mContext = getBaseContext();
		findViewAndInit();
		Save.loadingImg(mHandler, this,
				new int[] { R.drawable.novice_shouh_120 }, 1);
		getNewUserSpecialistBuyInfo();

		getCaptchaCode();
	}

	private void findViewAndInit() {
		intent = getIntent();
		ActivityId = intent.getIntExtra("ActivityId", 0);
		// AddressInfo = Constants.GetAwardExtractAddressInfo(mContext);

		mDialog = new DialogManager(this);
		dft.setOnDftErrorListener(this);
		dialog = new DialogProgressManager(this, "努力加载中...");
		HeaderViewControler.setHeaderView(this, "购买", this);
		lruCacheWebBitmapManager = LruCacheWebBitmapManager.getInstance();

		lin_extract = (LinearLayout) findViewById(R.id.lin_extract);
		lin_extract.setOnClickListener(this);

		iv_captcha = (ImageView) findViewById(R.id.iv_captcha);
		img_award = (ImageView) findViewById(R.id.img_award);
		img_delete = (ImageView) findViewById(R.id.img_delete);
		img_delete.setOnClickListener(this);
		// 名称
		tv_prizename = (TextView) findViewById(R.id.tv_prizename);
		// 可用余额
		tv_canUsemoney = (TextView) findViewById(R.id.tv_canUsemoney);
		// 购买金额
		tv_value = (TextView) findViewById(R.id.tv_value);
		// 提供商
		tv_support = (TextView) findViewById(R.id.tv_support);

		// 充值
		tv_recharge = (TextView) findViewById(R.id.tv_recharge);
		tv_recharge.setOnClickListener(this);
		// 验证码输入框
		ed_captcha = (EditText) findViewById(R.id.ed_captcha);
		// 接收方式
		tv_extracthit = (TextView) findViewById(R.id.tv_extracthit);
		tv_tobuy = (TextView) findViewById(R.id.tv_tobuy);
		tv_tobuy.setOnClickListener(this);
		setInputTypes(ed_captcha);

		ll = (RelativeLayout) findViewById(R.id.child_layout);
		line_f = (LinearLayout) findViewById(R.id.line_f);
		// ll.setOnTouchListener(this);
		ed_captcha.setOnClickListener(this);
		Constants.SetAwardExtractType(-1, mContext);
		iv_captcha.setOnClickListener(this);
	}

	private void setUIData() {
		if (model == null) {
			return;
		}
		MobilePhone = model.MobilePhone;
		RealName = model.RealName;
		AddressInfo = model.AddressInfo;
		Title = model.noviceLoan.Title;
		Constants.SetAwardExtractAddressInfo(AddressInfo, mContext);
		// 名称
		tv_prizename.setText(model.noviceLoan.Title);
		// 可用余额
		tv_canUsemoney.setText(Constants.StringToCurrency(model.LeaveAmount
				+ "")
				+ "元");
		// 购买金额
		tv_value.setText(Constants
				.StringToCurrency(model.noviceLoan.InvestAmount + ""));
		if (isfrist) {
			ExtractionMethod = model.ExtractionMethod;
			if (ExtractionMethod == Constants.express) {
				tv_extracthit.setText("快递接收");
			} else {
				tv_extracthit.setText("门店自取");
			}
			isfrist = false;
		}

		tv_support.setText(model.noviceLoan.Introduction);

		List<LoanPicture> lists = model.noviceLoan.loanPicture;
		for (int i = 0; i < lists.size(); i++) {
			if (lists.get(i).Type == LoanPicture.PC投标成功图_9) {
				lruCacheWebBitmapManager.getBitmap(dft, lists.get(i).URL,
						mHandler, LoanPicture.PC投标成功图_9, 0);
			}
		}

	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LoanPicture.PC投标成功图_9:
				img_award.setImageBitmap((Bitmap) msg.obj);
				break;
			case 1:
				Bitmap[] bitmap = (Bitmap[]) msg.obj;
				if (bitmap != null && bitmap.length > 0) {
					ViewParamsSetUtil.setViewHandW_lin(img_award, bitmap[0]);
				}
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.lin_extract:// 接收方式
			intent = new Intent(this, GoldBaoExtractActivity.class);
			intent.putExtra("type", 1);
			// intent.putExtra("AddressInfo", AddressInfo);
			intent.putExtra("MobilePhone", MobilePhone);
			intent.putExtra("RealName", RealName);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.tv_recharge:// 充值
			torecharge = true;
			intent = new Intent(this, RechargeActivity.class);
			intent.putExtra(RechargeActivity.FROM_BUY, true);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.tv_tobuy:// 购买
			String extractType = tv_extracthit.getText().toString();
			if (extractType == null || extractType.length() == 0) {
				mDialog.initOneBtnDialog();
				mDialog.setTitleandContent("提交失败", "您还未选择礼品接收方式！");
				mDialog.Show();
				return;
			}
			String captcha = ed_captcha.getText().toString().trim();

			if (captcha.length() == 0) {
				mDialog.initOneBtnDialog();
				mDialog.setTitleandContent("提交失败", "请输入验证码！");
				mDialog.Show();
				return;
			}

			postNewUserBid(ActivityId, ExtractionMethod, captcha);
			break;
		case R.id.tv_dialog_cancel:
			mDialog.dismiss();
			this.finish();
			overTransition(1);
			break;
		case R.id.tv_dialog_confirm:
			mDialog.dismiss();
			intent = new Intent(this, LotteryObtainedActivity.class);
			startActivity(intent);
			overTransition(2);
			torecharge = true;
			break;
		case R.id.img_delete:
			ed_captcha.setText("");
			break;
		case R.id.iv_captcha:
			getCaptchaCode();
			break;
		case R.id.ed_captcha:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(ed_captcha, InputMethodManager.SHOW_FORCED);
			imm.hideSoftInputFromWindow(ed_captcha.getWindowToken(), 0);
			toShowKeyBoard();
			break;
		case AddRateSelectDialog.BTN_ID:// 弹窗确定按钮
			addRateSelectDialog.postAddRedEnvelopeLog(dft, dialog, InvestId,
					MemberInvestRedEnvelopeLogTypeEnum.消费标, mDialog, true);
			break;
		default:
			break;
		}
	}

	/** 获取图形验证码 */
	private void getCaptchaCode() {
		dft.getNumCaptcha(Constants.GetValidateCodeNumPost_URL,
				Request.Method.GET, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						captchaModel = (CaptchaModel) dft.GetResponseObject(
								jsonObject, CaptchaModel.class);
						iv_captcha
								.setImageBitmap(stringToBitmap(captchaModel.CaptchaImg));
						// 是登录之后拉取的验证码不需要再保存SeesionId
						// Constants.SetSeesionId(captchaModel.Cookie,
						// mContext);
					}
				}, null);
	}

	private void getNewUserSpecialistBuyInfo() {
		dft.postGetMemberLotteryLog(ActivityId,
				Constants.NewUserSpecialistBuy_URL, Request.Method.POST,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						model = (UserSpecialHaveGiftModel) dft
								.GetResponseObject(response,
										UserSpecialHaveGiftModel.class);
						setUIData();

					}
				});
	}

	/** 购买接口 */
	private void postNewUserBid(int id, int ExtractionMethod, String Code) {
		setBtnState(false, "处理中", getResources().getColor(R.color.gray));

		dft.postActivityBid_URL(id, ExtractionMethod, Code,
				Constants.ActivityBid_URL, Request.Method.POST,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						ActivityBidBuyModel model = (ActivityBidBuyModel) dft
								.GetResponseObject(jsonObject,
										ActivityBidBuyModel.class);
						setBtnState(true, "确   定",
								getResources().getColor(R.color.red));
						if (model.ProcessResult == 0) {
							getCaptchaCode();
							mDialog.initOneBtnDialog();
							mDialog.setTitleandContent("购买失败",
									model.ProcessMessage);
							mDialog.Show();
						} else {
							AppController.AccountInfIsChange = true;

							if (model.resultModel != null
									&& model.resultModel.IsRedEnvelope) {
								RedEnvelopeList = model.resultModel.RedEnvelopeList;
							} else {
								RedEnvelopeList = null;
							}
							// 判断是否有可用的红包加息券
							if (RedEnvelopeList != null
									&& RedEnvelopeList.size() > 0) {// 有可用的
								InvestId = model.resultModel.InvestId;
								showSelectDialog();
								// 更新数据
								getCaptchaCode();
								getNewUserSpecialistBuyInfo();

							} else {// 没有
								mDialog.initTwoBtnDialog(NoviceBuyActivity.this);
								mDialog.setTitleandContent("购买成功", "恭喜您，获得"
										+ Title);
								mDialog.Show();
							}

						}

					}
				});
	}

	/**
	 * 红包加息券选择框
	 */
	private void showSelectDialog() {

		addRateSelectDialog = new AddRateSelectDialog(this, RedEnvelopeList,
				this, true);
	}

	// 转图形验证码
	public Bitmap stringToBitmap(String in) {
		byte[] bytes = Base64.decode(in, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	private void toShowKeyBoard() {
		List<EditText> ets = new ArrayList<EditText>();
		ets.add(ed_captcha);
		// EditText editText = (EditText) this.getCurrentFocus();
		// hideSoftInputMode(editText);
		if (mKeyBoardView == null) {
			mKeyBoardView = new KeyBoardView(NoviceBuyActivity.this, ets, ll);
			mKeyBoardView.setCloseListener(this);
			mKeyBoardView.setTopMargin(true);
			mKeyBoardView.setWidth(LayoutParams.MATCH_PARENT);
			mKeyBoardView.setHeight(LayoutParams.WRAP_CONTENT);
			mKeyBoardView.getContentView().measure(0, 0);
			KeyBoardViewHeight = mKeyBoardView.getContentView()
					.getMeasuredHeight();
			mKeyBoardView.showAtLocation(ll, Gravity.BOTTOM, 0, 0);
		} else {
			mKeyBoardView.open();
		}
		int[] location = new int[2];
		// 获取控件在屏幕的坐标
		ed_captcha.getLocationOnScreen(location);
		int h = ed_captcha.getHeight();
		if (location[1] + h + KeyBoardViewHeight > ScreenUtils
				.getScreenHeight(this)) {
			LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) line_f
					.getLayoutParams();
			lp1.topMargin = -(location[1] + h + KeyBoardViewHeight - ScreenUtils
					.getScreenHeight(this)) - 17;
			line_f.requestLayout();
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			toShowKeyBoard();
		}
	}

	@Override
	public void onBackPressed() {
		if (mKeyBoardView != null) {
			mKeyBoardView.close();
			mKeyBoardView = null;
		} else {
			this.finish();
			overTransition(1);
		}
	}

	// 隐藏软键盘并且显示光标
	public void setInputTypes(EditText editText) {
		if (android.os.Build.VERSION.SDK_INT <= 10) {
			editText.setInputType(InputType.TYPE_NULL);
		} else {
			this.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			try {
				Class<EditText> cls = EditText.class;
				Method setShowSoftInputOnFocus;
				setShowSoftInputOnFocus = cls.getMethod(
						"setShowSoftInputOnFocus", boolean.class);
				setShowSoftInputOnFocus.setAccessible(true);
				setShowSoftInputOnFocus.invoke(editText, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/***
	 * 设置按钮状态，已经加载框
	 * 
	 * @param bol
	 *            是否可以点击
	 * @param msg
	 *            按钮显示的文字
	 * @param rId
	 *            背景资源ID
	 */
	private void setBtnState(boolean bol, String msg, int rId) {
		if (bol) {
			dialog.dismiss();
		} else {
			dialog.initDialog();
		}
		tv_tobuy.setEnabled(bol);
		tv_tobuy.setText(msg);
		tv_tobuy.setTextColor(rId);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (Constants.GetAwardExtractType(mContext) != -1) {
			ExtractionMethod = Constants.GetAwardExtractType(mContext);
			if (ExtractionMethod == Constants.express) {
				tv_extracthit.setText("快递接收");
			} else {
				tv_extracthit.setText("门店自取");
			}
		}

		if (torecharge) {
			torecharge = false;
			getNewUserSpecialistBuyInfo();
			getCaptchaCode();
			ed_captcha.setText("");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mKeyBoardView != null) {
			mKeyBoardView.dismiss();
		}
	}

	@Override
	public void intitLayout() {
		LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) line_f
				.getLayoutParams();
		lp1.topMargin = 0;
		line_f.requestLayout();
	}

	@Override
	public void webLoadError() {
		dialog.dismiss();
	}

	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// if (MotionEvent.ACTION_UP == event.getAction()) {
	// // onRelease();
	// }
	//
	// return mGestureDetector.onTouchEvent(event);
	// }

}
