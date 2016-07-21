package com.md.hjyg.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.GoldExpandableAdapter;
import com.md.hjyg.entity.ChildItem;
import com.md.hjyg.entity.NewUserSpecialistModel.LoanDetailsModel;
import com.md.hjyg.entity.NoviceLoan.LoanPicture;
import com.md.hjyg.entity.UserSpecialHaveGiftModel;
import com.md.hjyg.layoutEntities.HeaderView;
import com.md.hjyg.layoutEntities.SelectPopupWindow;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.LruCacheWebBitmapManager;
import com.md.hjyg.utility.UmengShareManager;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;

/**
 * ClassName: NoviceExclusiveNewActivity date: 2016-3-26 上午10:23:39
 * remark:会员专享投资有礼
 * 
 * @author pyc
 */
public class NoviceExclusiveNewActivity extends BaseActivity implements
		OnClickListener, DftErrorListener {

	private Intent intent;
	/** 回到顶部按钮 */
	private ImageButton to_top;
	private ScrollView mScrollView;
	// private Bitmap[] bitmaps;
	private int ActivityId;
	// private int rid; img_award
	private ImageView img_award;
	// private TextView tv_rule;
	private TextView tv_tobuybtn, tv_Title, tv_Price;
	private Context context;

	private ExpandableListView expandableListView;
	private GoldExpandableAdapter listViewAdapter;
	private List<String> mapKey;
	private Map<String, List<ChildItem>> map;

	private TextView tv_invest_vale, tv_invest_term, tv_invest_income,tv_prizename2;
	private RelativeLayout rel_investinfo;
	private LinearLayout line_addimg;
	private boolean isFrist = true;
	private DialogProgressManager mDialog;

	private int imgindx;
	@SuppressLint("UseSparseArrays")
	private Map<Integer, ImageView> lineimgsMap = new HashMap<Integer, ImageView>();
	private DialogManager dialogManager;
	private LruCacheWebBitmapManager lruCacheBitmap;
	private UserSpecialHaveGiftModel model;
	private HeaderView mheadView;
	/** 自定义的弹出分享界面 */
	private SelectPopupWindow menuWindow;
	private UmengShareManager umengShareManager;
	private int GroupType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_noviceexclusivenew_layout);
		context = getBaseContext();

		findViewandInit();
		mDialog.initDialog();
		getUserSpecialHaveGift();

	}

	private void findViewandInit() {
		intent = getIntent();
		ActivityId = intent.getIntExtra("ActivityId", 0);
		GroupType = intent.getIntExtra("GroupType", 0);

		// 设置头部标题栏
		mheadView = (HeaderView) findViewById(R.id.mheadView);
		if (GroupType==0) {
			mheadView.setViewUI(this, "新手专享-投资有礼", "");
		}else {
			mheadView.setViewUI(this, "会员专享-投资有礼", "");
		}
		mheadView.setRightImg(R.drawable.rec_right_img);

		lruCacheBitmap = LruCacheWebBitmapManager.getInstance();
		dialogManager = new DialogManager(this);
		mDialog = new DialogProgressManager(this, "努力加载中...");
		dft.setOnDftErrorListener(this);

		mScrollView = (ScrollView) findViewById(R.id.mScrollView);
		mScrollView.setVisibility(View.INVISIBLE);
		to_top = (ImageButton) findViewById(R.id.to_top);
		to_top.setOnClickListener(this);

		img_award = (ImageView) findViewById(R.id.img_award);

		tv_Price = (TextView) findViewById(R.id.tv_Price);
		tv_Price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		tv_Title = (TextView) findViewById(R.id.tv_Title);
		tv_tobuybtn = (TextView) findViewById(R.id.tv_tobuybtn);
		tv_tobuybtn.setOnClickListener(this);

		tv_invest_vale = (TextView) findViewById(R.id.tv_invest_vale);
		tv_invest_term = (TextView) findViewById(R.id.tv_invest_term);
		tv_invest_income = (TextView) findViewById(R.id.tv_invest_income);
		rel_investinfo = (RelativeLayout) findViewById(R.id.rel_investinfo);
		line_addimg = (LinearLayout) findViewById(R.id.line_addimg);
		tv_prizename2 = (TextView) findViewById(R.id.tv_prizename2);

		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
	}

	/**
	 * 会员专享-投资有礼活动详情
	 */
	private void getUserSpecialHaveGift() {
		dft.postGetMemberLotteryLog(ActivityId,
				Constants.UserSpecialHaveGift_URL, Method.POST,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						mDialog.dismiss();
						model = (UserSpecialHaveGiftModel) dft
								.GetResponseObject(response,
										UserSpecialHaveGiftModel.class);

						setDataUI();
					}
				});
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LoanPicture.移动端投标页图片:
				img_award.setImageBitmap((Bitmap) msg.obj);
				break;
			case LoanPicture.移动端产品介绍图片:
				lineimgsMap.get(msg.arg1).setImageBitmap((Bitmap) msg.obj);

				break;

			default:
				break;
			}
		};
	};

	private void setDataUI() {
		if (model != null) {
			tv_Title.setText(model.noviceLoan.Title);
			tv_Price.setText((int) model.noviceLoan.Price + "");

			setScrollViewHightForGroup();

			// 投资金额
			String InvestAmount = Constants.StringToCurrency(
					model.noviceLoan.InvestAmount + "").replace(".00", "");
			tv_invest_vale.setText(TextUtil.getRelativeSize(InvestAmount + "元",
					0, InvestAmount.length(), 1.35f));
			// 投资期限
			String LoanTerm = model.noviceLoan.LoanTerm + "";
			// 单位 ：月等
			int datatype = model.noviceLoan.LoanDateType;
			String s_type = "";
			if (datatype == 0) {
				s_type = "个月";
			} else if (datatype == 2) {
				s_type = "天";
			} else if (datatype == 4) {
				s_type = "周";
			}
			tv_invest_term.setText(TextUtil.getRelativeSize(LoanTerm + s_type,
					0, LoanTerm.length(), 1.35f));
			// 预期收益
			String LoanInterest = Constants.StringToCurrency(
					model.noviceLoan.LoanInterest + "").replace(".00", "");
			tv_invest_income.setText(TextUtil.getRelativeSize(LoanInterest
					+ "元", 0, LoanInterest.length(), 1.35f));
			int ButtonStatus = model.noviceLoan.ButtonStatus;

			if (ButtonStatus == 0) {
				setButtonStatus(rel_investinfo, tv_tobuybtn, "立即投资",
						R.color.red, true);
			} else if (ButtonStatus == 2) {
				setButtonStatus(rel_investinfo, tv_tobuybtn, "已结束",
						R.color.gray, false);
			} else if (ButtonStatus == 3) {
				setButtonStatus(rel_investinfo, tv_tobuybtn, "已满额",
						R.color.gray, false);
			} else {
				setButtonStatus(rel_investinfo, tv_tobuybtn, "即将开始",
						R.color.yellow_FF9934, false);
			}
			
			if (GroupType == 0 &&model.expAmount != null) {
				tv_prizename2.setText("+" + model.expAmount);
			}

			if (isFrist) {
				List<LoanPicture> loanPictures = model.noviceLoan.loanPicture;
				imgindx = 0;
				line_addimg.removeAllViews();
				if (loanPictures != null) {
					for (int i = 0; i < loanPictures.size(); i++) {
						if (loanPictures.get(i).Type == LoanPicture.移动端投标页图片) {
							lruCacheBitmap.getBitmap(dft,
									loanPictures.get(i).URL, mHandler,
									LoanPicture.移动端投标页图片, 0);
						} else if (loanPictures.get(i).Type == LoanPicture.移动端产品介绍图片) {
							ImageView imageView = getImageView();
							line_addimg.addView(imageView);
							lineimgsMap.put(imgindx, imageView);
							lruCacheBitmap.getBitmap(dft,
									loanPictures.get(i).URL, mHandler,
									LoanPicture.移动端产品介绍图片, imgindx);
							imgindx++;
						} else if (loanPictures.get(i).Type == LoanPicture.PC投标成功图_9) {
							umengShareManager = new UmengShareManager(this);
							umengShareManager.setShareInit(model.linkUrl,
									loanPictures.get(i).URL,
									model.shareContent, model.shareTittle,true);
						}
					}
				}

				isFrist = false;
			}

		}

		mScrollView.setVisibility(View.VISIBLE);
	}

	private void setButtonStatus(RelativeLayout rel, TextView tv, String msg,
			int colorID, boolean Enabled) {
		rel.setBackgroundColor(getResources().getColor(colorID));
		tv.setText(msg);
		tv.setTextColor(getResources().getColor(colorID));
		tv.setEnabled(Enabled);
		// tv.setEnabled(true);
	}

	/** 设置expandableListView,更新ScrollView的高度 */
	private void setScrollViewHightForGroup() {
		// 取消系统左侧的图标
		expandableListView.setGroupIndicator(null);
		// 子条目展开时重新计算expandableListView的高度，用于刷新ScrollView的高度
		expandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {
						Constants
								.setListViewHeightBasedOnChildren(expandableListView);
						// setExpandableListViewHeightBasedOnChildren(expandableListView);
					}
				});
		// 子条目收缩时重新计算expandableListView的高度，用于刷新ScrollView的高度
		expandableListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int groupPosition) {
						Constants
								.setListViewHeightBasedOnChildren(expandableListView);
						// setExpandableListViewHeightBasedOnChildren(expandableListView);
					}
				});

		mapKey = new ArrayList<String>();
		map = new HashMap<String, List<ChildItem>>();
		mapKey.add("项目介绍");
		mapKey.add("投资须知");
		List<ChildItem> item1 = new ArrayList<ChildItem>();
		List<LoanDetailsModel> loanDetails = model.noviceLoan.loanDetails;
		if (loanDetails != null) {

			for (int j = 0; j < loanDetails.size(); j++) {
				item1.add(new ChildItem("● " + loanDetails.get(j).Title,
						loanDetails.get(j).Content.replace("<br />", "\n")));
			}
		}

		map.put(mapKey.get(0), item1);
		List<ChildItem> item2 = new ArrayList<ChildItem>();
		String[] loanDetails_2 = model.ruleDes.split("\n");
		for (int j = 0; j < loanDetails_2.length; j++) {
			item2.add(new ChildItem(loanDetails_2[j], ""));

		}
		map.put(mapKey.get(1), item2);

		listViewAdapter = new GoldExpandableAdapter(this, map, mapKey);
		expandableListView.setAdapter(listViewAdapter);
		Constants.setListViewHeightBasedOnChildren(expandableListView);
		// setExpandableListViewHeightBasedOnChildren(expandableListView);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderView.left_img_ID:
			this.finish();
			overTransition(1);
			break;
		case HeaderView.rightimg_ID:
			if (umengShareManager != null) {
				shareMenuWindow();
			}
			break;
		case R.id.weixin:
			if (umengShareManager != null) {
				umengShareManager.ShareWenXin();
			}
			break;
		case R.id.weixin_frends:
			if (umengShareManager != null) {
				umengShareManager.ShareWenXinfrends();
			}
			break;
		case R.id.qq:
			if (umengShareManager != null) {
				umengShareManager.ShareQQ();
			}
			break;
		case R.id.qqzone:
			if (umengShareManager != null) {
				umengShareManager.ShareQQzone();
			}
			break;
		case R.id.to_top:// 回到顶部按钮
			mScrollView.scrollTo(0, 0);
			break;
		case R.id.tv_tobuybtn:
			if (Constants.GetResult_AuthToken(this).length() == 0) {

				dialogManager.initTwoBtnDialog("下次再说", "马上去", "提示",
						"您需要登录后才能购买！", new OnClickListener() {

							@Override
							public void onClick(View v) {
								switch (v.getId()) {
								case DialogManager.CANCEL_BTN:
									dialogManager.dismiss();
									break;
								case DialogManager.CONFIRM_BTN:
									dialogManager.dismiss();
									AppController.isFromHuoQibaoDetailsActivity = true;
									intent = new Intent(context,
											LoginActivity.class);
									startActivity(intent);
									NoviceExclusiveNewActivity.this
											.overTransition(2);
									break;

								default:
									break;
								}
							}
						});

			} else {
				intent = new Intent(this, NoviceBuyActivity.class);
				intent.putExtra("ActivityId", ActivityId);
				startActivity(intent);
				overTransition(2);
				// intent = new Intent(context, NoviceBuyNewActivity.class);
				// intent.putExtra("Id", model.NoviceLoanList.get(type).Id);
				// intent.putExtra("InvestAmount",
				// model.NoviceLoanList.get(type).InvestAmount);
				// intent.putExtra("LeaveAmount", model.LeaveAmount);
				// intent.putExtra("Title",
				// model.NoviceLoanList.get(type).Title);
				// // intent.putExtra("loanGift",
				// // model.NoviceLoanList.get(type).loanGift);
				// intent.putParcelableArrayListExtra("loanGift",
				// model.NoviceLoanList.get(type).loanGift);
				// startActivity(intent);
				// overTransition(2);
			}
			break;

		default:
			break;
		}

	}

	/** 弹出分享界面及相关操作 */
	private void shareMenuWindow() {
		// 实例化SelectPopupWindow
		menuWindow = new SelectPopupWindow(this, this, 0);
		// 显示窗口 设置layout在PopupWindow中显示的位置
		menuWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		ScreenUtils.backgroundAlpha(this, 0.3f);
		// 分享窗口消失后，恢复背景颜色
		menuWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				ScreenUtils.backgroundAlpha(NoviceExclusiveNewActivity.this,
						1.0f);
			}
		});

	}

	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		getUserSpecialHaveGift();
	}

	private ImageView getImageView() {
		ImageView img = new ImageView(this);
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		img.setLayoutParams(params2);
		return img;
	}

	@Override
	public void webLoadError() {
		mDialog.dismiss();
	}

}
