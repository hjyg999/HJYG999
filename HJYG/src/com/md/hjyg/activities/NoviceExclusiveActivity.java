package com.md.hjyg.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.GoldExpandableAdapter;
import com.md.hjyg.adapter.NoviceViewPagerAdapter;
import com.md.hjyg.entity.ChildItem;
import com.md.hjyg.entity.NewUserSpecialistModel.LoanDetailsModel;
import com.md.hjyg.entity.NoviceLoan;
import com.md.hjyg.entity.NoviceLoan.LoanPicture;
import com.md.hjyg.entity.SendGiftlistModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.LruCacheWebBitmapManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.validators.RecommendedPageTransformer;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;

/**
 * ClassName: NoviceExclusiveActivity date: 2016-2-25 上午10:16:52 remark: 新手专享界面
 * 
 * @author pyc
 */
public class NoviceExclusiveActivity extends BaseActivity implements
		OnClickListener, DftErrorListener {
	public final static int Handler_What = 101;

	private TextView tv_tobuybtn, tv_invest_vale, tv_invest_term,
			tv_invest_income, tv_prizename, tv_prizename2;
	private TextView tv_prize_vale;

	private ImageView[] smallImageViews;
	private int size;
	private Context context;

	private ViewPager viewpager_prize;
	private NoviceViewPagerAdapter adapter;
	private ImageView img_left, img_right;
	private ImageView img_bottom;
	/**
	 * 装小图片的容器
	 */
	private LinearLayout lin_smlimg;

	private ExpandableListView expandableListView;
	private GoldExpandableAdapter listViewAdapter;
	private List<String> mapKey;
	private Map<String, List<ChildItem>> map;

	private int mPostion;
	private int ActivityId;
	private SendGiftlistModel model;
	private RelativeLayout rel_investinfo;
	private Intent intent;
	private DialogProgressManager mDialog;
	private DialogManager dialogManager;
	private LinearLayout lin_scr;
	private Bitmap[] bitmaps;
	private LruCacheWebBitmapManager lruCacheBitmap;
	/**
	 * 是否在加载底部图片
	 */
	private boolean isLoading;
	public final static String showViewPager = "showViewPager";
	private boolean isshowViewPager;
	private RelativeLayout rel_viewpager;
	private ImageView img_award;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_noviceexclusive_layout);
		context = getBaseContext();
		findViewAndInit();
		mDialog.initDialog();
		Save.loadingImg(handler, context, new int[] {
				R.drawable.novice_shouh_48, R.drawable.novice_shouh_m },
				Handler_What);
		// setImg();
		GetWebservice();
	}

	private void findViewAndInit() {
		intent = getIntent();
		mPostion = intent.getIntExtra("position", 0);
		ActivityId = intent.getIntExtra("ActivityId", 0);
		isshowViewPager = intent.getBooleanExtra(showViewPager, true);
		lruCacheBitmap = LruCacheWebBitmapManager.getInstance();

		HeaderViewControler.setHeaderView(this, "新手专享-投资有礼", this);
		mDialog = new DialogProgressManager(this, "努力加载中...");
		dialogManager = new DialogManager(this);
		dft.setOnDftErrorListener(this);
		// 立即投资
		tv_tobuybtn = (TextView) findViewById(R.id.tv_tobuybtn);
		tv_tobuybtn.setOnClickListener(this);

		tv_invest_vale = (TextView) findViewById(R.id.tv_invest_vale);
		tv_invest_term = (TextView) findViewById(R.id.tv_invest_term);
		tv_invest_income = (TextView) findViewById(R.id.tv_invest_income);
		tv_prizename = (TextView) findViewById(R.id.tv_prizename);
		tv_prizename2 = (TextView) findViewById(R.id.tv_prizename2);
		// 市场价
		tv_prize_vale = (TextView) findViewById(R.id.tv_prize_vale);

		viewpager_prize = (ViewPager) findViewById(R.id.viewpager_prize);
		img_left = (ImageView) findViewById(R.id.img_left);
		img_left.setOnClickListener(this);
		img_right = (ImageView) findViewById(R.id.img_rightqq);
		img_right.setOnClickListener(this);

		lin_smlimg = (LinearLayout) findViewById(R.id.lin_smlimg);
		img_bottom = (ImageView) findViewById(R.id.img_bottom);
		rel_investinfo = (RelativeLayout) findViewById(R.id.rel_investinfo);
		lin_scr = (LinearLayout) findViewById(R.id.lin_scr);
		lin_scr.setVisibility(View.INVISIBLE);

		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
		rel_viewpager = (RelativeLayout) findViewById(R.id.rel_viewpager);
		img_award = (ImageView) findViewById(R.id.img_award);

	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Handler_What:
				bitmaps = (Bitmap[]) msg.obj;
				setData();
				break;
			case LoanPicture.移动端投标页图片:
				if (isshowViewPager) {
					smallImageViews[msg.arg1].setImageBitmap((Bitmap) msg.obj);
				}else {
					img_award.setImageBitmap((Bitmap) msg.obj);
				}
				break;
			case LoanPicture.移动端产品介绍图片:
				isLoading = false;
				// 底部图片
				img_bottom.setImageBitmap((Bitmap) msg.obj);
				
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 获取欲购买新手标的信息
	 * 
	 * @param id
	 */
	private void GetWebservice() {

		dft.postGetMemberLotteryLog(ActivityId,
				Constants.NewUserSpecialistGift_URL, Method.POST,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						model = (SendGiftlistModel) dft.GetResponseObject(
								response, SendGiftlistModel.class);

						setData();
					}
				});

	}

	private void setData() {
		if (bitmaps != null && model != null) {
			size = model.NoviceLoanList.size();
			if (isshowViewPager) {
				initSmallImg();
				initViewPager();
				rel_viewpager.setVisibility(View.VISIBLE);
				img_award.setVisibility(View.GONE);
			} else {
				List<LoanPicture> loanPictures = model.NoviceLoanList
						.get(mPostion).loanPicture;
				for (int i = 0; i < loanPictures.size(); i++) {
					if (loanPictures.get(i).Type == LoanPicture.移动端投标页图片) {
						lruCacheBitmap.getBitmap(dft, loanPictures.get(i).URL,
								handler, LoanPicture.移动端投标页图片, 0);
					}
				}
				rel_viewpager.setVisibility(View.GONE);
				img_award.setVisibility(View.VISIBLE);
			}
			setScrollViewHightForGroup(mPostion);
			setBottomimg(mPostion);
			setNoviceDate(mPostion);
		}
		mDialog.dismiss();
		lin_scr.setVisibility(View.VISIBLE);

	}

	/**
	 * 设置投资信息
	 * 
	 * @param i
	 */
	private void setNoviceDate(int i) {
		// 投资金额
		String InvestAmount = Constants.StringToCurrency(
				model.NoviceLoanList.get(i).InvestAmount + "").replace(".00",
				"");
		tv_invest_vale.setText(TextUtil.getRelativeSize(InvestAmount + "元", 0,
				InvestAmount.length(), 1.35f));
		// 投资期限
		String LoanTerm = model.NoviceLoanList.get(i).LoanTerm + "";
		// 单位 ：月等
		int datatype = model.NoviceLoanList.get(i).LoanDateType;
		String type = Constants.getLoanTermType(datatype);
		tv_invest_term.setText(TextUtil.getRelativeSize(LoanTerm + type, 0,
				LoanTerm.length(), 1.35f));
		// 预期收益
		String LoanInterest = Constants.StringToCurrency(
				model.NoviceLoanList.get(i).LoanInterest + "").replace(".00",
				"");
		tv_invest_income.setText(TextUtil.getRelativeSize(LoanInterest + "元",
				0, LoanInterest.length(), 1.35f));
		tv_prizename.setText(model.NoviceLoanList.get(i).Title);
		if (model.expAmount != null) {
			tv_prizename2.setText("+" + model.expAmount);
		}
		// 市场价
		// tv_prize_vale.setText(TextUtil.getColorandSizeString(getResources()
		// .getColor(R.color.gray_s), prize_vales[i], 0, prize_vales[i]
		// .length(), 1.35f));
		// .length() - 1, 1.35f));
		tv_prize_vale.setText((int) model.NoviceLoanList.get(i).Price + "");
		tv_prize_vale.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

		int ButtonStatus = model.NoviceLoanList.get(i).ButtonStatus;
		// 设置提示栏背景 及按钮的状态
		if (ButtonStatus == 0) {
			setButtonStatus(rel_investinfo, tv_tobuybtn, "立即投资", R.color.red,
					true);
		} else if (ButtonStatus == 2) {
			setButtonStatus(rel_investinfo, tv_tobuybtn, "已结束", R.color.gray,
					false);
		} else if (ButtonStatus == 3) {
			setButtonStatus(rel_investinfo, tv_tobuybtn, "已满额", R.color.gray,
					false);
		} else {
			setButtonStatus(rel_investinfo, tv_tobuybtn, "即将开始",
					R.color.yellow_FF9934, false);
		}

	}

	private void setButtonStatus(RelativeLayout rel, TextView tv, String msg,
			int colorID, boolean Enabled) {
		rel.setBackgroundColor(getResources().getColor(colorID));
		tv.setText(msg);
		tv.setTextColor(getResources().getColor(colorID));
		tv.setEnabled(Enabled);
		// tv.setEnabled(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.tv_tobuybtn:// 立即投资

			if (Constants.GetResult_AuthToken(this).length() == 0) {

				dialogManager.initTwoBtnDialog("下次再说", "马上去", "提示",
						"您需要登录后才能购买", new OnClickListener() {

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
									NoviceExclusiveActivity.this
											.overTransition(2);
									break;

								default:
									break;
								}

							}
						});

			} else {
				if (size != 0) {
					if (model == null) {
						Toast.makeText(this, "数据加载中，请稍候！", Toast.LENGTH_SHORT)
								.show();
						return;
					}
					// if (model.AddressInfo == null) {
					// Constants.SetAwardExtractAddressInfo("", context);
					// } else {
					// Constants.SetAwardExtractAddressInfo(model.AddressInfo,
					// context);
					// }
					intent = new Intent(this, NoviceBuyActivity.class);
					intent.putExtra("ActivityId", ActivityId);
					startActivity(intent);
				}
				overTransition(2);
			}
			break;
		case R.id.img_left://
			if (size != 0 && !isLoading) {
				int p = viewpager_prize.getCurrentItem();
				viewpager_prize.setCurrentItem(p - 1);
			}
			break;
		case R.id.img_rightqq://
			if (size != 0 && !isLoading) {
				int p = viewpager_prize.getCurrentItem();
				viewpager_prize.setCurrentItem(p + 1);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}

	/**
	 * 设置底部图片
	 * 
	 * @param postion
	 */
	private void setBottomimg(int postion) {
		isLoading = true;
		// 底部图片
		for (int j = 0; j < model.NoviceLoanList.get(postion).loanPicture
				.size(); j++) {
			if (model.NoviceLoanList.get(postion).loanPicture.get(j).Type == LoanPicture.移动端产品介绍图片) {
				lruCacheBitmap
						.getBitmap(dft,
								model.NoviceLoanList.get(postion).loanPicture
										.get(j).URL, handler,
								LoanPicture.移动端产品介绍图片, 0);
			}
		}
	}

	private void initSmallImg() {
		lin_smlimg.removeAllViews();
		smallImageViews = new ImageView[size];
		LinearLayout.LayoutParams params = new LayoutParams(
				bitmaps[0].getWidth(), bitmaps[0].getHeight());
		params.leftMargin = 5;
		List<NoviceLoan> lists = model.NoviceLoanList;
		for (int i = 0; i < size; i++) {
			smallImageViews[i] = new ImageView(context);

			smallImageViews[i].setLayoutParams(params);
			for (int j = 0; j < lists.get(i).loanPicture.size(); j++) {
				if (lists.get(i).loanPicture.get(j).Type == LoanPicture.移动端投标页图片) {
					lruCacheBitmap.getBitmap(dft,
							lists.get(i).loanPicture.get(j).URL, handler,
							LoanPicture.移动端投标页图片, i);
				}
			}
			if (i == mPostion) {
				smallImageViews[i]
						.setBackgroundResource(R.drawable.novic_choice);

			} else {
				smallImageViews[i]
						.setBackgroundResource(R.drawable.novic_choice_no);
			}
			lin_smlimg.addView(smallImageViews[i]);
		}
	}

	/**
	 * 初始化ViewPager
	 */
	private void initViewPager() {
		LinearLayout.LayoutParams v_pParams = (LinearLayout.LayoutParams) viewpager_prize
				.getLayoutParams();
		v_pParams.height = bitmaps[1].getHeight();
		v_pParams.width = bitmaps[1].getWidth();
		viewpager_prize.setLayoutParams(v_pParams);
		// 设置动画
		viewpager_prize.setPageTransformer(true,
				new RecommendedPageTransformer());
		adapter = new NoviceViewPagerAdapter(this, false, model.NoviceLoanList,
				lruCacheBitmap);
		viewpager_prize.setAdapter(adapter);
		viewpager_prize.setCurrentItem(size * 100 + mPostion);
		viewpager_prize.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mPostion = position % size;
				ActivityId = model.NoviceLoanList.get(mPostion).Id;
				setNoviceDate(mPostion);
				setScrollViewHightForGroup(mPostion);
				setBottomimg(mPostion);
				for (int i = 0; i < size; i++) {
					if (smallImageViews[i] != null) {
						if (mPostion == i) {
							smallImageViews[i]
									.setBackgroundResource(R.drawable.novic_choice);
						} else {
							smallImageViews[i]
									.setBackgroundResource(R.drawable.novic_choice_no);
						}
					}
				}

			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	/** 设置expandableListView,更新ScrollView的高度 */
	private void setScrollViewHightForGroup(int i) {
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
		List<LoanDetailsModel> loanDetails = model.NoviceLoanList.get(i).loanDetails;
		for (int j = 0; j < loanDetails.size(); j++) {
			item1.add(new ChildItem("● " + loanDetails.get(j).Title,
					loanDetails.get(j).Content.replaceAll("<br />", "\n")));
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
		// setExpandableListViewHeightBasedOnChildren(expandableListView);F
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		AppController.isFromHuoQibaoDetailsActivity = false;
		GetWebservice();
	}

	@Override
	public void webLoadError() {
		mDialog.dismiss();
	}
}
