package com.md.hjyg.activities;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.CaptchaModel;
import com.md.hjyg.entity.LoanGift;
import com.md.hjyg.entity.MeetListDialogModel;
import com.md.hjyg.entity.NewUserSpecialistModel;
import com.md.hjyg.entity.Notification;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.layoutEntities.KeyBoardView;
import com.md.hjyg.layoutEntities.KeyBoardView.ClosePopupWindowListener;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.HomeWebServiceManager;
import com.md.hjyg.utility.ListDialogManager;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
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
 * ClassName: NoviceBuyNewActivity date: 2016-3-26 下午1:13:53 remark:购买礼品标
 * 
 * @author pyc
 */
public class NoviceBuyNewActivity extends BaseActivity implements
		OnClickListener, ClosePopupWindowListener {
	private TextView tv_top1, tv_kaname;
	private ListDialogManager mListDialog;
	private LinearLayout lin_choice, line_f;
	// 键盘
	private KeyBoardView mKeyBoardView;
	private int KeyBoardViewHeight;
	private RelativeLayout ll;
	private EditText ed_captcha;
	private CaptchaModel captchaModel;
	private ImageView iv_captcha;
	// private Context mContext;
	private TextView tv_tobuy;
	private DialogManager mDialog;
	private Intent intent;
	private int Id, GiftId;
	/** 提交数据加载框 */
	private DialogProgressManager dialog;
	/**
	 * 购买金额
	 */
	private double InvestAmount;
	private String LeaveAmount, Title;
	private ArrayList<LoanGift> loanGift;
	private TextView tv_canUsemoney, tv_value, tv_recharge;
	private ImageView img_delete;
	private boolean isLoading;
	private boolean torecharge;
	private HomeWebServiceManager serviceManager;
	/**
	 * 数字键盘是否处于显示状态
	 */
	private boolean boardViewisShow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_novicebuynew_layout);
		// mContext = getBaseContext();
		findViewAndInit();
		setInputTypes(ed_captcha);
		getCaptchaCode();
		setData();
	}

	private void findViewAndInit() {
		HeaderViewControler.setHeaderView(this, "购买", this);
		intent = getIntent();
		Id = intent.getIntExtra("Id", 0);
		InvestAmount = intent.getDoubleExtra("InvestAmount", 0);
		LeaveAmount = intent.getStringExtra("LeaveAmount");
		Title = intent.getStringExtra("Title");
		loanGift = intent.getParcelableArrayListExtra("loanGift");

		lin_choice = (LinearLayout) findViewById(R.id.lin_choice);
		lin_choice.setOnClickListener(this);

		line_f = (LinearLayout) findViewById(R.id.line_f);
		ed_captcha = (EditText) findViewById(R.id.ed_captcha);
		ed_captcha.setOnClickListener(this);
		ll = (RelativeLayout) findViewById(R.id.child_layout);

		iv_captcha = (ImageView) findViewById(R.id.iv_captcha);
		iv_captcha.setOnClickListener(this);
		tv_tobuy = (TextView) findViewById(R.id.tv_tobuy);
		tv_tobuy.setOnClickListener(this);
		tv_canUsemoney = (TextView) findViewById(R.id.tv_canUsemoney);
		tv_value = (TextView) findViewById(R.id.tv_value);
		tv_recharge = (TextView) findViewById(R.id.tv_recharge);
		tv_recharge.setOnClickListener(this);
		img_delete = (ImageView) findViewById(R.id.img_delete);
		img_delete.setOnClickListener(this);

		tv_kaname = (TextView) findViewById(R.id.tv_kaname);
		tv_top1 = (TextView) findViewById(R.id.tv_top1);
		tv_top1.setText("松桂园站<109路、 102路、 115路、 116路、 "
				+ "348路、 150路、 159路、 303路、 9路、 402路、 405路> ");

		dialog = new DialogProgressManager(this, "努力加载中...");
		mDialog = new DialogManager(this);
	}

	private void setData() {
		// 可用余额
		tv_canUsemoney.setText(Constants.StringToCurrency(LeaveAmount + "")
				+ "元");
		// 购买金额
		tv_value.setText(Constants.StringToCurrency(InvestAmount + ""));

		tv_kaname.setText(Title + loanGift.get(0).Title);
		GiftId = loanGift.get(0).Id;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.lin_choice:
			showHotDialog();
			break;
		case R.id.ed_captcha:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(ed_captcha, InputMethodManager.SHOW_FORCED);
			imm.hideSoftInputFromWindow(ed_captcha.getWindowToken(), 0);
			toShowKeyBoard();
			break;
		case R.id.tv_tobuy:
			String extractType = tv_kaname.getText().toString();
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

			postZeroActivityBid(Id, 1, captcha, GiftId);
			break;
		case R.id.tv_dialog_cancel:// 购买成功dialog-确定按钮
			mDialog.dismiss();
			this.finish();
			overTransition(1);
			break;
		case R.id.tv_dialog_confirm:// 购买成功dialog-查看奖品按钮
			mDialog.dismiss();
			intent = new Intent(this, LotteryObtainedActivity.class);
			startActivity(intent);
			overTransition(2);
			this.finish();
			break;
		case R.id.tv_recharge:// 充值
			torecharge = true;
			intent = new Intent(this, RechargeActivity.class);
			intent.putExtra(RechargeActivity.FROM_BUY, true);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.img_delete:
			ed_captcha.setText("");
			break;
		case R.id.iv_captcha:
			if (!isLoading) {
				getCaptchaCode();
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 选择购物卡类型Dialog
	 */
	private void showHotDialog() {
		if (loanGift == null) {
			return;
		}
		final List<MeetListDialogModel> lists = new ArrayList<MeetListDialogModel>();
		for (int i = 0; i < loanGift.size(); i++) {
			lists.add(new MeetListDialogModel(Title + loanGift.get(i).Title,
					false));
		}
		mListDialog = new ListDialogManager(this, "", lists);
		mListDialog.goneTitle();
		mListDialog.Show();
		mListDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				mListDialog = null;
				for (int i = 0; i < lists.size(); i++) {
					if (lists.get(i).isChoice()) {
						tv_kaname.setText(lists.get(i).getContent());
						GiftId = loanGift.get(i).Id;
						return;
					}
				}

			}
		});
	}

	private void toShowKeyBoard() {
		List<EditText> ets = new ArrayList<EditText>();
		ets.add(ed_captcha);
		// EditText editText = (EditText) this.getCurrentFocus();
		// hideSoftInputMode(editText);
		if (mKeyBoardView == null) {
			mKeyBoardView = new KeyBoardView(this, ets, ll);
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
		boardViewisShow = true;
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
	public void intitLayout() {
		LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) line_f
				.getLayoutParams();
		lp1.topMargin = 0;
		line_f.requestLayout();
		boardViewisShow = false;
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

	// 购买接口
	private void postZeroActivityBid(int Id, int ExtractionMethod, String Code,
			int GiftId) {
		setBtnState(false, "处理中", getResources().getColor(R.color.gray));
		dft.postZeroActivityBid(Id, ExtractionMethod, Code, GiftId,
				Constants.ZeroActivityBid_URL, Request.Method.POST,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						Notification model = (Notification) dft
								.GetResponseObject(jsonObject,
										Notification.class);
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
							mDialog.initTwoBtnDialog(NoviceBuyNewActivity.this);
							mDialog.setButText("确定", "查看奖品");
							mDialog.setTitleandContent("购买成功",
									"投资成功，您已获得活动礼品 - "
											+ tv_kaname.getText().toString());
							mDialog.Show();
						}

					}
				});
	}

	/** 获取图形验证码 */
	private void getCaptchaCode() {
		isLoading = true;
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
						isLoading = false;
					}
				}, null);
	}

	// 转图形验证码
	public Bitmap stringToBitmap(String in) {
		byte[] bytes = Base64.decode(in, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
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
	public void onBackPressed() {
		if (mKeyBoardView!= null && boardViewisShow) {
			mKeyBoardView.close();
		}else{
			this.finish();
			overTransition(1);
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (torecharge) {
			torecharge = false;
			if (serviceManager == null) {
				serviceManager = new HomeWebServiceManager(this, mHandler);
			}
			serviceManager.getZeroActivitylistinfo(false);
		}
	}
	
	@Override
	protected void onDestroy() {
		if (mKeyBoardView != null) {
			mKeyBoardView.dismiss();
			mKeyBoardView = null;
		}
		super.onDestroy();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HomeWebServiceManager.ZeroActivity_INF:
				NewUserSpecialistModel model = (NewUserSpecialistModel) msg.obj;
				if (model != null) {
					// 可用余额
					tv_canUsemoney.setText(Constants
							.StringToCurrency(model.LeaveAmount + "") + "元");
				}
				break;

			default:
				break;
			}
		};
	};

}
