package com.md.hjyg.activities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

import com.md.hjyg.R;
import com.md.hjyg.adapter.LotteryMainListAdapter;
import com.md.hjyg.entity.AwardInfo;
import com.md.hjyg.entity.LotteryActivityMessage;
import com.md.hjyg.entity.NewLotteryInfoModel;
import com.md.hjyg.entity.NewLotteryInfoModel.NewLotteryActivityPrize;
import com.md.hjyg.layoutEntities.LotteryPopupWindow;
import com.md.hjyg.layoutEntities.RefreshableScrollView;
import com.md.hjyg.layoutEntities.RefreshableScrollView.RefreshListener;
import com.md.hjyg.utility.LotteryWebServiceManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utility.TimeTaskScroll;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * ClassName: LotteryRecordsActivity <br/>
 * date: 2015-10-19 下午4:38:58 <br/>
 * remark:抽奖活动首页
 * 
 * @author rw
 */
public class LotteryMainActivity extends BaseActivity implements
		OnClickListener {

	/** 开始抽奖按钮 */
	private TextView toLottery_tv;
	/** 返回按钮 */
	private ImageView lay_back_investment;
	/** 头部图片 */
	private ImageView top_img, noda_img;
	private TextView noda_tv;
	// img_rul
	// private ImageView img_rul2;
	// private TextView img_rul_text, img_rul_text2;
	// private LinearLayout lin_pad2;
	/** 查看我的奖品按钮 */
	private TextView myawards;
	/** 活动规则 */
	// private TextView lottery_rules_text;
	/** 查看抽奖攻略按钮 */
	// private TextView lottery_strategy;
	/** 查看其他人的奖励按钮 */
	// private TextView lottery_records;
	/** 回到顶部按钮 */
	private ImageButton to_top;
	/** 立即投资按钮 */
	private TextView tv_btn;

	private ScrollView scrollView;

	// 所有奖品id
	private int[] awards = { R.id.award1, R.id.award2, R.id.award3,
			R.id.award4, R.id.award5, R.id.award6, R.id.award7, R.id.award8,
			R.id.award9, R.id.award10, R.id.award11, R.id.award12 };
	// 记录所有的奖品
	private LinearLayout flls[] = new LinearLayout[awards.length];
	/** 所有奖品的文字提示的控件ID */
	private int[] awards_text = { R.id.award1_text, R.id.award2_text,
			R.id.award3_text, R.id.award4_text, R.id.award5_text,
			R.id.award6_text, R.id.award7_text, R.id.award8_text,
			R.id.award9_text, R.id.award10_text, R.id.award11_text,
			R.id.award12_text };
	/** 所有奖品的文字提示的控件 */
	private TextView awards_textView[] = new TextView[awards_text.length];
	/** 实物奖品显示图片的控件ID */
	private int[] awards_imgId = { R.id.award1_img, R.id.award2_img,
			R.id.award3_img, R.id.award4_img, R.id.award5_img, R.id.award6_img,
			R.id.award7_img, R.id.award8_img, R.id.award9_img,
			R.id.award10_img, R.id.award11_img, R.id.award12_img };
	/** 实物奖品显示图片的控件 */
	private ImageView awards_imageView[] = new ImageView[awards_imgId.length];
	/** 红包奖品显示图片的控件ID */
	private int[] awards_redbag = { R.id.award1_img_redbag,
			R.id.award2_img_redbag, R.id.award3_img_redbag,
			R.id.award4_img_redbag, R.id.award5_img_redbag,
			R.id.award6_img_redbag, R.id.award7_img_redbag,
			R.id.award8_img_redbag, R.id.award9_img_redbag,
			R.id.award10_img_redbag, R.id.award11_img_redbag,
			R.id.award12_img_redbag };
	/** 红包奖品显示图片的控件 */
	private RelativeLayout awards_Rel[] = new RelativeLayout[awards_redbag.length];
	/** 红包奖品金额显示的控件ID */
	private int[] award_redbag_AmountId = { R.id.award1_redbag_Amount,
			R.id.award2_redbag_Amount, R.id.award3_redbag_Amount,
			R.id.award4_redbag_Amount, R.id.award5_redbag_Amount,
			R.id.award6_redbag_Amount, R.id.award7_redbag_Amount,
			R.id.award8_redbag_Amount, R.id.award9_redbag_Amount,
			R.id.award10_redbag_Amount, R.id.award11_redbag_Amount,
			R.id.award12_redbag_Amount };
	/** 红包奖品金额显示的控件 */
	private TextView award_redbag_Amount[] = new TextView[award_redbag_AmountId.length];

	LinearLayout linear_circle_top_hor, linear_circle_top_hor_l,
			linear_circle_top_hor_r, linear_circle_bottom_hor,
			linear_circle_bottom_hor_l, linear_circle_bottom_hor_r,
			linear_circle_left_ver, linear_circle_right_ver;

	private ListView lottery_main_lv;

	// private int[] awards_win_Ids = { R.drawable.lottery_award_redbag_iv_m,
	// R.drawable.lottery_award_dimond_iv_m,
	// R.drawable.lottery_award_gold1_iv_m,
	// R.drawable.lottery_award_gold2_iv_m,
	// R.drawable.lottery_award_gold3_iv_m,
	// R.drawable.lottery_award_iphon_iv_m };
	// private Bitmap[] awards_win_bitmaps = new Bitmap[awards_win_Ids.length];
	// TextView text_reg,text_reg1;
	private int[] text_reg = { R.id.text_reg0, R.id.text_reg1 };
	/** 还有几次抽奖机会文字提示 */
	private TextView lottery_hit;
	/** 还有几次抽奖机会数字提示 */
	private TextView lottery_hit_amount;

	public String awards_names[] = { "iphone6s", "理财红包10元", "钻石2分", "理财红包30元",
			"理财红包50元", "金镶玉（叶）", "金镶玉（佛）", "理财红包8元", "现金红包3元", "钻石10分",
			"现金红包5元", "水晶包金吊坠" };
	// private TextView lottery_rules;
	Map<String, AwardInfo> awards_nameTobitmapId = new HashMap<String, AwardInfo>();
	Map<integer, AwardInfo> awards_intTobitmapId = new HashMap<integer, AwardInfo>();

	private int number;
	int awards_wins_indx = 0;
	/** 获取奖品的编号 */
	private int lastNumber = 0;
	/** 用来判断第一个是黄色的还是白色的圆点 */
	int n = 0;

	/** 还有的抽奖次数 */
	private int haveNumber = 0;
	private int alsoNumber = 0;
	/** 奖品信息 */
	// List<LotteryActivityPrizeModel> list;
	/** 获取的奖品信息 */
	LotteryActivityMessage lotteryActivityMessage;
	// int widthWindow ;
	// int heightWindow;
	int qwidthWindow;
	int qheightWindow;

	/** 水平增加圆点的个数 */
	int nWView;
	/** 垂直增加圆点的个数 */
	int nHView;
	/** 控制圆点闪烁 */
	boolean isBlink = true;
	/** 圆点的间距 */
	int Margin = 0;

	/** 控制每次listview显示的条数 */
	int list_itemcount = 10;

	// private int index = 0;
	// private String itemcount = "30";
	public String MemberId;
	/** 弹窗 */
	LotteryPopupWindow mpWindow;
	// RelativeLayout lottery_rel_top;, lottery_lin_bot
	LinearLayout lottery_lin_cer;
	LotteryWebServiceManager lotteryManager;
	int Status = -1;
	private NewLotteryInfoModel Model;
	/** 奖品信息 */
	private List<NewLotteryActivityPrize> PrizeList;
	/** 下拉刷新控件 */
	private RefreshableScrollView refreshableScrollView;
	/** 是否在刷新 */
	boolean isrefreshing = false;
	/** 抽奖规则 */
	private TextView tv_LotteryRule;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:// 抽奖转盘转动
				int i = (Integer) msg.obj;
				setCurrentAwardColor(i);
				setToLotteryInValid();
				break;
			case 30:// 抽奖转盘结束
				// toLottery_tv.setBackgroundColor(getResources().getColor(
				// R.color.red));
				toLottery_tv.setEnabled(false);
				// lottery_hit_amount.setText(haveNumber + "");
				// showPopupWindow();
				showwinPopupWindow();
				lotteryManager.GetNetInfo(0);
				if (haveNumber > 0) {
					lottery_hit.setText("您已有");
					lottery_hit_amount.setText(haveNumber + "");
				} else {
					lottery_hit.setText("剩余");
					lottery_hit_amount.setText(alsoNumber + "");
				}
				// initLottrey();
				break;

			case 102:// 设置显示的奖品的循序

				Model = (NewLotteryInfoModel) msg.obj;
				// 设置抽奖规则
				tv_LotteryRule.setText(TextUtil.getHtmlText(Model.LotteryRule));
				PrizeList = Model.PrizeList;
				for (int j = 0; j < PrizeList.size(); j++) {
					NewLotteryActivityPrize prize = PrizeList.get(j);
					// 设置奖品图片 及红包金额
					if (prize.type == 0) {
						awards_imageView[j].setVisibility(View.VISIBLE);
						awards_Rel[j].setVisibility(View.GONE);
						if (awards_nameTobitmapId
								.get(PrizeList.get(j).PrizeName) != null) {

							awards_imageView[j]
									.setImageResource(awards_nameTobitmapId
											.get(PrizeList.get(j).PrizeName)
											.getBitmapId());
						}

					} else {
						awards_imageView[j].setVisibility(View.GONE);
						awards_Rel[j].setVisibility(View.VISIBLE);
						award_redbag_Amount[j]
								.setText((int) PrizeList.get(j).RedEnvelopeAmount
										+ "");
					}
					// 设置名称
					awards_textView[j].setTextSize(12);
					awards_textView[j].setText(PrizeList.get(j).PrizeName);
//					if (prize.PrizeName.equals("金镶玉（叶）")) {
//						awards_textView[j].setText("金镶玉(叶)");
//					} else if (prize.PrizeName.equals("金镶玉（佛）")) {
//						awards_textView[j].setText("金镶玉(佛)");
//					} else if (prize.PrizeName.equals("华为mate8")) {
//						awards_textView[j].setTextSize(10);
//						awards_textView[j].setText(PrizeList.get(j).PrizeName);
//					} else if (prize.PrizeName.equals("限量版同号人民币")) {
//						awards_textView[j].setTextSize(8);
//						awards_textView[j].setText("限量版\n同号人民币");
//
//					} else {
//					}
				}

				// 初始化抽奖次数
				haveNumber = Model.HaveNumber;
				alsoNumber = Model.AlsoNumber;
				long startTime = DateUtil
						.StrToLong(Model.LotteryActivity.startTime);
				long endTime = DateUtil
						.StrToLong(Model.LotteryActivity.endTime.replace("00:00:00", "23:59:59"));
				long NowTime = DateUtil.StrToLong(Model.NowTime);
				if (Constants.GetResult_AuthToken(LotteryMainActivity.this)
						.length() == 0 && endTime > NowTime) {
					alsoNumber = 2;
					haveNumber = 0;
				} else if (endTime <= NowTime) {
					alsoNumber = 0;
					haveNumber = 0;
				}
				if (haveNumber > 0) {
					lottery_hit.setText("您已有");
					lottery_hit_amount.setText(haveNumber + "");
				} else {
					if (Constants.GetResult_AuthToken(getBaseContext()).length()>0) {
						lottery_hit.setText("剩余");
					}else {
						lottery_hit.setText("您有");
					}
					lottery_hit_amount.setText(alsoNumber + "");
				}

				// 判断活动是否开始和结束

				toLottery_tv.setEnabled(false);
				toLottery_tv.setBackgroundResource(R.drawable.bg_ra_gray_q);
				toLottery_tv.setTextColor(getResources().getColor(
						R.color.gray_sq));
				if (startTime > NowTime) {// 活动还未开始
					toLottery_tv.setText("即将开始");

				} else if (endTime <= NowTime) {// 活动已经结束

					toLottery_tv.setText("活动结束");
				} else {// 活动正在进行
						// 初始化按钮
					toLottery_tv.setText("立即抽奖");
					if (haveNumber == 0 && alsoNumber == 0) {
					} else {
						toLottery_tv.setEnabled(true);
						toLottery_tv
								.setBackgroundResource(R.drawable.bg_ra_red_lottery_selector);
						toLottery_tv.setTextColor(getResources().getColor(
								R.color.white));
					}
				}

				// 所有用户中奖记录
				if (Model.MemberLotteryLogList != null
						&& Model.MemberLotteryLogList.size() > 0) {
					lottery_main_lv.setVisibility(View.VISIBLE);
					noda_img.setVisibility(View.GONE);
					noda_tv.setVisibility(View.GONE);
					if (Model.MemberLotteryLogList.size() > 7) {
						new Timer().schedule(new TimeTaskScroll(
								LotteryMainActivity.this, lottery_main_lv,
								Model.MemberLotteryLogList, 0), 20, 20);
					} else {
						lottery_main_lv.setAdapter(new LotteryMainListAdapter(
								LotteryMainActivity.this,
								Model.MemberLotteryLogList, 0));
						// Constants
						// .setListViewHeightBasedOnChildren(lottery_main_lv);
					}
				} else {
					lottery_main_lv.setVisibility(View.GONE);
					noda_img.setVisibility(View.VISIBLE);
					noda_tv.setVisibility(View.VISIBLE);

				}

				if (isrefreshing) {
					// 刷新完成
					refreshableScrollView.finishRefresh();
					isrefreshing = false;
				}

				break;

			case 105:// 抽奖的到的奖品信息
				lotteryActivityMessage = (LotteryActivityMessage) msg.obj;
				haveNumber = lotteryActivityMessage.HaveNumber;
				alsoNumber = lotteryActivityMessage.AlsoNumber;
				// lottery_hit_amount.setText(haveNumber + "");
				// lastNumber = lotteryActivityMessage.LotteryActivityPrizeId;
				lastNumber = lotteryActivityMessage.LotteryActivityPrizeId % 12;
				if (lastNumber == 0) {
					lastNumber = 12;
				}
				if (!lotteryActivityMessage.Result) {
					Constants.showOkPopup(LotteryMainActivity.this,
							lotteryActivityMessage.notification.ProcessMessage);
					toLottery_tv.setEnabled(true);
					break;
				} else {
					starttoLottery();
				}
				break;

			case 200:// 闪烁灯闪烁
				if (n == 0) {
					n = 1;
				} else {
					n = 0;
				}
				// LinearLayoutaddView(n);
				intcircle();
				break;
			default:
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_lottery_main_activity);
		findViews();
		init();
		inntAwardInfo();
		lotteryManager.GetNetInfo(0);

		// 下拉刷新监听
		refreshableScrollView.setRefreshListener(new RefreshListener() {

			@Override
			public void onRefresh(RefreshableScrollView view) {
				if (!isrefreshing) {
					lotteryManager.GetNetInfo(0);
					isrefreshing = true;
				}

				// mHandler.sendEmptyMessage(0);
			}
		}, 201);

		// lotteryManager.GetLotteryActivityById("1");

		// LotteryWebServiceManager lotWebSerManager = new
		// LotteryWebServiceManager(this, mHandler);

		// lotteryManager.GetMemberLotteryLogByCount("10","1");
		// lotteryManager.GetMemberLotteryLogByMember("6017");
		// lotteryManager.GetLotteryActivityNumber("6017","1");
		// lotteryManager.MemberLotteryActivity("6017","1");
		// lotteryManager.GetLotteryActivityHaveNumber("6017","1");

	}

	/**
	 * 初始化UI
	 */
	public void findViews() {
		toLottery_tv = (TextView) findViewById(R.id.toLottery_tv);
		tv_LotteryRule = (TextView) findViewById(R.id.tv_LotteryRule);
		lay_back_investment = (ImageView) findViewById(R.id.lay_back_investment);
		myawards = (TextView) findViewById(R.id.myawards);

		// lottery_rules = (TextView) findViewById(R.id.lottery_rules);
		// lottery_strategy = (TextView) findViewById(R.id.lottery_strategy);
		// lottery_records = (TextView) findViewById(R.id.lottery_records);
		to_top = (ImageButton) findViewById(R.id.to_top);
		tv_btn = (TextView) findViewById(R.id.tv_btn);
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		lottery_main_lv = (ListView) findViewById(R.id.lottery_main_lv);
		lottery_hit = (TextView) findViewById(R.id.lottery_hit);
		lottery_hit_amount = (TextView) findViewById(R.id.lottery_hit_amount);
		// lottery_rules_text = (TextView)
		// findViewById(R.id.lottery_rules_text);
		// setTextViewType();
		// 下拉刷新控件
		refreshableScrollView = (RefreshableScrollView) findViewById(R.id.refreshableScrollView);

		for (int i = 0; i < awards.length; i++) {
			flls[i] = (LinearLayout) findViewById(awards[i]);
		}
		for (int i = 0; i < awards_text.length; i++) {
			awards_textView[i] = (TextView) findViewById(awards_text[i]);
		}
		for (int i = 0; i < awards_imgId.length; i++) {
			awards_imageView[i] = (ImageView) findViewById(awards_imgId[i]);
		}

		// for (int i = 0; i < awards_win_Ids.length; i++) {
		// awards_win_bitmaps[i] = BitmapFactory.decodeResource(
		// getResources(), awards_win_Ids[i]);
		// }
		for (int i = 0; i < awards_redbag.length; i++) {
			awards_Rel[i] = (RelativeLayout) findViewById(awards_redbag[i]);
		}
		for (int i = 0; i < award_redbag_AmountId.length; i++) {
			award_redbag_Amount[i] = (TextView) findViewById(award_redbag_AmountId[i]);
		}

		linear_circle_top_hor = (LinearLayout) findViewById(R.id.linear_circle_top_hor);
		linear_circle_bottom_hor = (LinearLayout) findViewById(R.id.linear_circle_bottom_hor);
		linear_circle_left_ver = (LinearLayout) findViewById(R.id.linear_circle_left_ver);
		linear_circle_right_ver = (LinearLayout) findViewById(R.id.linear_circle_right_ver);

		linear_circle_top_hor_l = (LinearLayout) findViewById(R.id.linear_circle_top_hor_l);
		linear_circle_top_hor_r = (LinearLayout) findViewById(R.id.linear_circle_top_hor_r);
		linear_circle_bottom_hor_l = (LinearLayout) findViewById(R.id.linear_circle_bottom_hor_l);
		linear_circle_bottom_hor_r = (LinearLayout) findViewById(R.id.linear_circle_bottom_hor_r);

	}

	public void init() {
		toLottery_tv.setOnClickListener(this);
		lay_back_investment.setOnClickListener(this);
		myawards.setOnClickListener(this);
		// lottery_rules.setOnClickListener(this);
		// lottery_strategy.setOnClickListener(this);
		// lottery_records.setOnClickListener(this);
		to_top.setOnClickListener(this);
		tv_btn.setOnClickListener(this);

		lotteryManager = new LotteryWebServiceManager(this, mHandler,dft);

		// addUnderlinedText();
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		qwidthWindow = metric.widthPixels;
		qheightWindow = metric.heightPixels;

		// // 设置抽奖头部的高度
		// lottery_rel_top = (RelativeLayout)
		// findViewById(R.id.lottery_rel_top);
		// LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
		// lottery_rel_top
		// .getLayoutParams();
		// params.height = (int) ((qheightWindow * 0.18));
		// lottery_rel_top.setLayoutParams(params);
		// 设置头部图片
		top_img = (ImageView) findViewById(R.id.top_img);
		Bitmap bitmap_bg = BitmapFactory.decodeResource(getResources(),
				R.drawable.lottery_main_bg);
		top_img.setImageBitmap(Save.ScaleBitmap(bitmap_bg, this));

		noda_img = (ImageView) findViewById(R.id.noda_img);
		noda_tv = (TextView) findViewById(R.id.noda_tv);
		Bitmap bitmap_noda = BitmapFactory.decodeResource(getResources(),
				R.drawable.lottery_noda);
		noda_img.setImageBitmap(Save.ScaleBitmap(bitmap_noda, this, bitmap_bg));
		noda_img.setVisibility(View.GONE);
		noda_tv.setVisibility(View.GONE);

		// rule_img = (ImageView) findViewById(R.id.rule_img);
		// Bitmap bitmap_r = BitmapFactory.decodeResource(getResources(),
		// R.drawable.lottery_ruls_img);
		// rule_img.setImageBitmap(Save.ScaleBitmap(bitmap_r, this, bitmap_bg));

		// img_rul = (ImageView) findViewById(R.id.img_rul);
		// Bitmap bitmap_rul = BitmapFactory.decodeResource(getResources(),
		// R.drawable.lottery_main_lucky);
		// bitmap_rul = Save.ScaleBitmap(bitmap_rul, this, bitmap_bg);
		// img_rul.setImageBitmap(bitmap_rul);

		// img_rul2 = (ImageView) findViewById(R.id.img_rul2);
		// img_rul2.setImageBitmap(bitmap_rul);

		// int h = (int) (bitmap_rul.getHeight() * 0.616);
		// img_rul_text = (TextView) findViewById(R.id.img_rul_text);
		// LinearLayout.LayoutParams params_text = (LinearLayout.LayoutParams)
		// img_rul_text
		// .getLayoutParams();
		// params_text.height = h;
		// img_rul_text.setLayoutParams(params_text);
		//
		// img_rul_text2 = (TextView) findViewById(R.id.img_rul_text2);
		// img_rul_text2.setLayoutParams(params_text);

		// lin_pad2 = (LinearLayout) findViewById(R.id.lin_pad2);
		// android:paddingBottom="10dp"
		// android:paddingLeft="10dp"
		// android:paddingRight="10dp"
		// android:paddingTop="15dp"
		// int px = ScreenUtils.dip2px(this, 10);
		// lin_pad2.setPadding(px, bitmap_rul.getHeight() - 25, px, px);

		// 设置抽奖部分的高度
		lottery_lin_cer = (LinearLayout) findViewById(R.id.lottery_lin_cer);
		LinearLayout.LayoutParams params_l = (LinearLayout.LayoutParams) lottery_lin_cer
				.getLayoutParams();
		// params_l.height = (int) ((qheightWindow * 0.56));
		params_l.height = ScreenUtils.getScreenWidth(this);
		lottery_lin_cer.setLayoutParams(params_l);

		// lottery_lin_bot = (LinearLayout) findViewById(R.id.lottery_lin_bot);
		// LinearLayout.LayoutParams params_b = (LinearLayout.LayoutParams)
		// lottery_lin_bot
		// .getLayoutParams();
		// params_b.height = (int) ((qheightWindow * 0.15));
		// lottery_lin_bot.setLayoutParams(params_b);

		int widthWindow = qwidthWindow * 9 / 10; // 屏幕宽度（像素）
		// int heightWindow = (int) (((double) qheightWindow) * 0.56 * 0.9); //
		// 屏幕宽度（像素）
		// 获取图片的宽度
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.lottery_yellow_iv);
		int bitmapwidth = bitmap.getWidth();
		int t = 21;
		for (int i = 5; i < 21; i++) {
			if ((widthWindow - i) % (bitmapwidth + i) <= t) {
				t = (widthWindow - i) % (bitmapwidth + i);
				Margin = i;
			}
		}

		if (Margin == 0) {
			Margin = 5;
		}

		nWView = (widthWindow - Margin) / (bitmapwidth + Margin);
		// int k = ((heightWindow - Margin) % (bitmapwidth + Margin));
		// if (k >= Margin + bitmapwidth / 2) {
		// nHView = ((heightWindow - Margin) / (bitmapwidth + Margin)) + 1;
		// } else {
		// nHView = ((heightWindow - Margin) / (bitmapwidth + Margin));
		// }
		nHView = nWView;

		intcircle();

		startBlink();
	}

	/** 初始使化抽奖次数 */
	// private void initLottrey() {
	// if (Status == 1) {
	//
	// if (haveNumber == 0) {
	// if (alsoNumber == 0) {
	// toLottery_tv.setBackgroundColor(getResources().getColor(
	// R.color.gray));
	// toLottery_tv.setEnabled(false);
	// toLottery_tv.setText("抽奖结束");
	// } else {
	// toLottery_tv.setEnabled(true);
	// toLottery_tv.setText("立即抽奖");
	// }
	// lottery_hit_amount.setText(alsoNumber + "");
	// lottery_hit.setText("剩余");
	// } else {
	// toLottery_tv.setEnabled(true);
	// toLottery_tv.setText("立即抽奖");
	// lottery_hit.setText("您已有");
	// lottery_hit_amount.setText(haveNumber + "");
	// }
	//
	// }else if (Status == 0){
	//
	// if (haveNumber == 0) {
	//
	// lottery_hit_amount.setText(alsoNumber + "");
	// lottery_hit.setText("剩余");
	// } else {
	// lottery_hit.setText("您已有");
	// lottery_hit_amount.setText(haveNumber + "");
	// }
	//
	// }
	//
	// }

	/** 初始化圆点布局 */
	private void intcircle() {
		LinearLayoutaddView(linear_circle_top_hor_l, n + 1, 1);
		LinearLayoutaddView(linear_circle_top_hor, n, nWView);
		verLinearLayoutaddView(linear_circle_left_ver, n, nHView);
		if (nWView % 2 == 1) {
			LinearLayoutaddView(linear_circle_top_hor_r, n + 1, 1);
			verLinearLayoutaddView(linear_circle_right_ver, n, nHView);

		} else {
			LinearLayoutaddView(linear_circle_top_hor_r, n, 1);
			verLinearLayoutaddView(linear_circle_right_ver, n + 1, nHView);

		}
		if (nHView % 2 == 1) {
			LinearLayoutaddView(linear_circle_bottom_hor_l, n + 1, 1);
			LinearLayoutaddView(linear_circle_bottom_hor_r, n, 1);
			LinearLayoutaddView(linear_circle_bottom_hor, n, nWView);
		} else {
			LinearLayoutaddView(linear_circle_bottom_hor_l, n, 1);
			LinearLayoutaddView(linear_circle_bottom_hor_r, n + 1, 1);
			LinearLayoutaddView(linear_circle_bottom_hor, n + 1, nWView);
		}
	}

	/** 圆点闪烁 */
	private void startBlink() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				while (isBlink) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mHandler.sendEmptyMessage(200);
				}

			}
		}).start();
	}

	/** 增加下划线 */
	public void addUnderlinedText() {
		TextView textView;
		for (int i = 0; i < text_reg.length; i++) {
			textView = (TextView) findViewById(text_reg[i]);
			textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
			textView.getPaint().setAntiAlias(true);// 抗锯齿
		}
	}

	/**
	 * 为水平分布的线性布局增加圆点
	 * 
	 * @param linearLayout
	 *            线性布局的名称
	 * @param n
	 *            取0和1,0：以黄色的圆点开始增加，1：以白色的圆点开始增加
	 * @param sum
	 *            增加圆点的总个数
	 */
	private void LinearLayoutaddView(LinearLayout linearLayout, int n, int sum) {

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.leftMargin = Margin;
		linearLayout.removeAllViews();
		for (int i = 0 + n; i < sum + n; i++) {
			// if (i == n) {
			// params.leftMargin = 3;
			// } else {
			// params.leftMargin = Margin;
			// }
			if (sum != 1) {

				if (i % 2 == 0) {
					linearLayout.addView(getyellowImageView(), params);
				} else {
					linearLayout.addView(getwhiteImageView(), params);

				}
			} else {
				if (i % 2 == 0) {
					linearLayout.addView(getyellowImageView());
				} else {
					linearLayout.addView(getwhiteImageView());

				}
			}
		}
	}

	/**
	 * 为垂直分布的线性布局增加圆点
	 * 
	 * @param linearLayout
	 *            线性布局的名称
	 * @param n
	 *            取0和1,0：以黄色的圆点开始增加，1：以白色的圆点开始增加
	 * @param sum
	 *            增加圆点的总个数
	 */
	private void verLinearLayoutaddView(LinearLayout linearLayout, int n,
			int sum) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.topMargin = Margin;
		linearLayout.removeAllViews();
		for (int i = 0 + n; i < sum + n; i++) {
			if (i % 2 == 0) {
				linearLayout.addView(getyellowImageView(), params);
			} else {
				linearLayout.addView(getwhiteImageView(), params);

			}
		}
	}

	/** 黄色的圆点 */
	private ImageView getyellowImageView() {
		ImageView yellowcircle = new ImageView(this);
		yellowcircle.setImageResource(R.drawable.lottery_yellow_iv);
		return yellowcircle;
	}

	/** 白色的圆点 */
	private ImageView getwhiteImageView() {
		ImageView whitecircle = new ImageView(this);
		whitecircle.setImageResource(R.drawable.lottery_white_iv);
		return whitecircle;
	}

	/*
	 * 控件点击事件
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.toLottery_tv:// 抽奖按钮
			// if (Constants.GetResult_AuthToken(this).isEmpty()) {
			// // 没有登录，弹出注册提示
			// showPopupWindow("亲，您还未注册！", "注册登录，iphone6s、钻石、",
			// "金镶玉吊坠、红包...就是你的啦！", "立即注册");
			// } else if (haveNumber > 0) {// 可以抽奖
			// // lotteryManager.MemberLotteryActivity("", "1");
			// } else if (alsoNumber == 2) {// 弹出充值提示窗口
			// //showIntvSPopupWindow();
			// showPopupWindow("", "绑定银行卡，充值成功，", "抽奖再来一次！", "立即充值");
			// } else if (alsoNumber == 1) {// 弹出投资提示窗口
			// showIntvSPopupWindow();
			// }

			if (haveNumber == 0 && alsoNumber > 0) {
				showIntvSPopupWindow();
			}
			if (haveNumber > 0) {
				lotteryManager.GetNetInfo(2);
				toLottery_tv.setEnabled(false);
			}

			// starttoLottery();
			break;
		case R.id.lay_back_investment:// 返回按钮
			this.finish();
			overridePendingTransition(R.anim.trans_lift_in,
					R.anim.trans_right_out);
			break;
		case R.id.myawards:// 查看我的奖品按钮
			if (Constants.GetResult_AuthToken(this).isEmpty()) {
				startActivity(new Intent(this,
						LotteryNoLoginObtainedActivity.class));
				overridePendingTransition(R.anim.trans_right_in,
						R.anim.trans_lift_out);
			} else {
				Intent obtained = new Intent(this,
						LotteryObtainedActivity.class);
				// obtained.putExtra("MemberId", MemberId);
				startActivity(obtained);
				overridePendingTransition(R.anim.trans_right_in,
						R.anim.trans_lift_out);
			}
			break;
		// case R.id.lottery_rules:// 查看活动规则按钮
		// startActivity(new Intent(this, LotteryRulesActivity.class));
		// overridePendingTransition(R.anim.trans_right_in,
		// R.anim.trans_lift_out);
		// break;
		// case R.id.lottery_strategy:// 查看抽奖攻略按钮
		// startActivity(new Intent(this, LotteryStrategyActivity.class));
		// overridePendingTransition(R.anim.trans_right_in,
		// R.anim.trans_lift_out);
		// break;
		// case R.id.lottery_records:// 查看抽奖攻略按钮
		// startActivity(new Intent(this, LotteryRecordsActivity.class));
		// overridePendingTransition(R.anim.trans_right_in,
		// R.anim.trans_lift_out);
		// break;
		case R.id.popu_text_btn:// PopupWindow上的点击注册按钮
			if (alsoNumber == 2) {
				startActivity(new Intent(this, RechargeActivity.class));
			} else {
				startActivity(new Intent(this, LoginActivity.class));
			}
			mpWindow.dismiss();
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_lift_out);
			break;
		case R.id.to_top:// 回到顶部按钮
			scrollView.scrollTo(0, 0);
			break;
		case R.id.award_text_btn1:// 弹窗继续抽奖按钮
			// initLottrey();
			mpWindow.dismiss();
			break;
		case R.id.award_text_btn2:// 弹窗查看奖品按钮
			Intent obtained = new Intent(this, LotteryObtainedActivity.class);
			obtained.putExtra("MemberId", MemberId);
			startActivity(obtained);
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_lift_out);
			// startActivity(new Intent(this, LotteryObtainedActivity.class));
			// overridePendingTransition(R.anim.trans_right_in,
			// R.anim.trans_lift_out);
			mpWindow.dismiss();
			break;
		case R.id.popu_text_btn3:// 弹窗立即投资按钮
			mpWindow.dismiss();
		case R.id.tv_btn:// 立即投资按钮
//			Intent investment = new Intent(this, InvestmentActivity.class);
//			investment.putExtra("DingtoubaoProjects", false);
//			investment.putExtra("LatestProjects", false);
			Intent investment = new Intent(this, HomeFragmentActivity.class);
			investment.putExtra("tab", 1);
			startActivity(investment);
			overTransition(2);
			break;
		default:
			break;
		}
	}

	/** 提示注册时弹出的窗口 */
	public void showPopupWindow(String title, String content1, String content2,
			String btnstr) {

		mpWindow = new LotteryPopupWindow(this, this, qwidthWindow - 60, title,
				content1, content2, btnstr);
		// mpWindow = new LotteryPopupWindow(this, this,qwidthWindow-60);
		// 显示窗口 设置layout在PopupWindow中显示的位置
		mpWindow.showAtLocation(this.findViewById(R.id.lotterymain),
				Gravity.CENTER, 0, 0);
		// mpWindow.showAtLocation(this.findViewById(R.id.lotterymain),
		// Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	/** 提示投资时弹出的窗口 */
	public void showIntvSPopupWindow() {

		mpWindow = new LotteryPopupWindow(this, this, qwidthWindow - 60);
		// mpWindow = new LotteryPopupWindow(this, this,qwidthWindow-60);
		// 显示窗口 设置layout在PopupWindow中显示的位置
		mpWindow.showAtLocation(this.findViewById(R.id.lotterymain),
				Gravity.CENTER, 0, 0);
		// mpWindow.showAtLocation(this.findViewById(R.id.lotterymain),
		// Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	/** 中奖时弹出的窗口 */
	public void showwinPopupWindow() {
		Bitmap awards_win_bitmap;
		String redAmount = null;
		String msg = "";

		if (PrizeList.get(lastNumber - 1).type == 0) {

			awards_win_bitmap = BitmapFactory.decodeResource(
					getResources(),
					awards_nameTobitmapId.get(
							PrizeList.get(lastNumber - 1).PrizeName)
							.getMbitmapId());
		} else {
			awards_win_bitmap = BitmapFactory.decodeResource(getResources(),
					awards_nameTobitmapId.get("现金红包").getMbitmapId());
			redAmount = (int) PrizeList.get(lastNumber - 1).RedEnvelopeAmount +"";
			msg = "恭喜您！抽中" + redAmount +"元" ;
		}
		// awards_wins_indx = awards_wins_indx % awards_win_Ids.length;
		// Bitmap awards_win_bitmap =
		// BitmapFactory.decodeResource(getResources(),
		// awards_nameTobitmapId.get(lotteryActivityMessage.Message).getMbitmapId());
		
		mpWindow = new LotteryPopupWindow(this, this, qwidthWindow - 60,
				awards_win_bitmap, msg, redAmount);
		// 显示窗口 设置layout在PopupWindow中显示的位置
		mpWindow.showAtLocation(this.findViewById(R.id.lotterymain),
				Gravity.CENTER, 0,
				(int) ((double) qheightWindow * 0.6 * 0.9 * 0.15));
		// mpWindow.showAtLocation(this.findViewById(R.id.lotterymain),
		// Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		awards_wins_indx++;
	}

	/** 返回键监听 */
	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
	}

	/** 开始抽奖 */
	private void starttoLottery() {
		// 随机获取一个数值，设置从哪个奖品开始抽奖
		Random random = new Random();
		number = random.nextInt((12)) + 1;
		// 抽奖线程开始
		new Thread(new Runnable() {
			@Override
			public void run() {
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				// Random random = new Random();
				// int num = random.nextInt((12));
				while (number < 36 + lastNumber) {
					Message message = new Message();
					message.what = 1;
					message.obj = number % 12;
					number++;
					mHandler.sendMessage(message);
					if (number < 24) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else if (number >= 24 && number < 36) {
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

				mHandler.sendEmptyMessage(30);

			}
		}).start();
	}

	/**
	 * 设置抽奖开始，抽奖按钮不可用
	 */
	public void setToLotteryInValid() {
		toLottery_tv.setBackgroundResource(R.drawable.bg_ra_gray);
		toLottery_tv.setText("正在抽奖");
		toLottery_tv.setEnabled(false);
	}

	/**
	 * 保证只有一个奖品处于选中状态，当前选中的为一种颜色，其他均为另外的颜色
	 */
	public void setCurrentAwardColor(int i) {
		flls[i].setBackgroundColor(getResources().getColor(R.color.lottery_yes));
		awards_textView[i].setTextColor(getResources().getColor(R.color.white));
		// 上一次的还原
		if (i == 0) {
			flls[11].setBackgroundColor(getResources().getColor(
					R.color.lottery_no));
			awards_textView[11].setTextColor(getResources().getColor(
					R.color.red));
		} else {

			flls[i - 1].setBackgroundColor(getResources().getColor(
					R.color.lottery_no));
			awards_textView[i - 1].setTextColor(getResources().getColor(
					R.color.red));
		}
	}

	/** 把奖品与图片相对于 */
	private void inntAwardInfo() {

		// awards_nameTobitmapId.put("华为mate8", new AwardInfo(
		// R.drawable.lottery_award_iphon_iv,
		// R.drawable.lottery_award_iphon_iv_m));
		awards_nameTobitmapId.put("现金红包", new AwardInfo(
				R.drawable.lottery_award_redbag_iv,
				R.drawable.lottery_award_redbag_iv_m));
		// awards_nameTobitmapId.put("限量版同号人民币", new AwardInfo(
		// R.drawable.lottery_award_mony_iv,
		// R.drawable.lottery_award_mony_iv_m));
		// awards_nameTobitmapId.put("金镶玉（叶）", new AwardInfo(
		// R.drawable.lottery_award_gold1_iv,
		// R.drawable.lottery_award_gold1_iv_m));
		// awards_nameTobitmapId.put("金镶玉（佛）", new AwardInfo(
		// R.drawable.lottery_award_gold2_iv,
		// R.drawable.lottery_award_gold2_iv_m));
		// awards_nameTobitmapId.put("3D苹果", new AwardInfo(
		// R.drawable.lottery_award_glodapple_iv,
		// R.drawable.lottery_award_glodapple_iv_m));
	}

	public void setTextViewType() {
		// String str = getResources().getString(R.string.lottery_rules);
		// SpannableStringBuilder style = new SpannableStringBuilder(str);
		// CharacterStyle span=new UnderlineSpan();
		// int start = str.indexOf("需体验");
		// int end = str.indexOf("。") + 1;
		// style.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// style.setSpan(new StyleSpan(Typeface.BOLD),start,
		// end,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		// style.setSpan(new
		// ForegroundColorSpan(Color.parseColor("#324650")),start,
		// end,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		// int start1 = str.indexOf("长沙、");
		// int end1 = str.indexOf("常德") + 2;
		// style.setSpan(new StyleSpan(Typeface.BOLD),start1,
		// end1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		// style.setSpan(new
		// ForegroundColorSpan(Color.parseColor("#324650")),start1,
		// end1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		// int start2 = str.indexOf("指定");
		// int end2 = str.indexOf("指定") + 2;
		// style.setSpan(new StyleSpan(Typeface.BOLD),start2,
		// end2,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		// style.setSpan(new
		// ForegroundColorSpan(Color.parseColor("#324650")),start2,
		// end2,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		// int start3 = str.indexOf("门店");
		// int end3 = str.indexOf("门店") + 2;
		// style.setSpan(new StyleSpan(Typeface.BOLD),start3,
		// end3,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		// style.setSpan(new
		// ForegroundColorSpan(Color.parseColor("#324650")),start3,
		// end3,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		String str = getResources().getString(R.string.lottery_rules2);
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		int start = str.indexOf("单笔≥");
		int end = str.indexOf("上") + 1;
		style.setSpan(new ForegroundColorSpan(Color.parseColor("#CE4F5A")),
				start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		start = str.indexOf("可参与");
		end = str.indexOf("与") + 1;
		style.setSpan(new ForegroundColorSpan(Color.parseColor("#CE4F5A")),
				start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		start = str.indexOf("抽奖活动");
		style.setSpan(new ForegroundColorSpan(Color.parseColor("#CE4F5A")),
				start, start + 4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		// lottery_rules_text.setText(style);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isBlink = false;
		awards_nameTobitmapId.clear();
		awards_nameTobitmapId.clear();
	}

}
