package com.md.hjyg.activities;

import java.util.ArrayList;
import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.adapter.RedpacketListAdapter;
import com.md.hjyg.entity.RedPackageObtainLogsModel.MemberShareRedEnvelopeLog;
import com.md.hjyg.entity.ShareRedPackageInfoModel;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.layoutEntities.SelectPopupWindow;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utility.UmengShareManager;
import com.md.hjyg.utility.WeixinRedPacketManager;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 分享微信红包界面
 */
public class ShareWeixinRedPacketActivity extends BaseActivity implements
		OnClickListener {
	/** 微信红包分享按钮 */
	private TextView redpacket_share_btn, redpacket_remark, twodim_remark,
			redpacket_total,redpacket_total_left,redpacket_total_right;

	private LinearLayout linear1, redpacket_Amount, linear2;
	private ImageView img_nodate, redpacket_twodim;

	private SelectPopupWindow menuWindow;

	HeaderControler header;
	/** 返回按钮 */
	private LinearLayout lay_back_investment;
	private String ShareTitle = "信投宝携手玛丽莱珠宝给您送红包了，祝您生活美满！";
	private String ShareContent = "100起投，年化利率6%-15%，100%本金收益保障";
	private WeixinRedPacketManager manager;
	private UmengShareManager umengShareManager;
	private ShareRedPackageInfoModel shareRedPackageInfoModel;
	private Bitmap sharebitmap;
	private int page = 1;
	private List<MemberShareRedEnvelopeLog> lists;
	private ListView mListView;
	private RedpacketListAdapter adapter;
	private ScrollView mScrollView;
	private boolean isLoadingMore = false;
	private boolean haveNoData = false;
	private int index = 0;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				shareRedPackageInfoModel = manager
						.getShareRedPackageInfoModel();
				setUIData();
				break;
			case 1:
				if (manager.getLists() != null && manager.getLists().size() > 0) {
					if (manager.getLists().size() < 10) {
						haveNoData = true;
					}
					lists.addAll(manager.getLists());
					if (page == 1) {
						linear2.setVisibility(View.VISIBLE);
						adapter = new RedpacketListAdapter(
								ShareWeixinRedPacketActivity.this, lists);
						mListView.setAdapter(adapter);
					} else {
						adapter.notifyDataSetChanged();
					}
					Constants.setListViewHeightBasedOnChildren(mListView);
				} else {
					if (page == 1) {
						img_nodate.setVisibility(View.VISIBLE);
					}
					haveNoData = true;
				}
				isLoadingMore = false;
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_share_weixin_redpacket);

		findViewAndInit();

		manager = new WeixinRedPacketManager(this, handler,dft);
		manager.getShareRedPackageInfo();

	}

	private void findViewAndInit() {
		header = new HeaderControler(this, true, false, "分享红包",
				Constants.CheckAuthtoken(getBaseContext()));

		// share_rest = (TextView) findViewById(R.id.share_rest);
		// share_amount = (TextView) findViewById(R.id.share_amount);
		// share_usedamount = (TextView) findViewById(R.id.share_usedamount);
		// share_pepole = (TextView) findViewById(R.id.share_pepole);
		// share_btn = (TextView) findViewById(R.id.share_btn);
		// share_list = (RelativeLayout) findViewById(R.id.share_list);
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		redpacket_share_btn = (TextView) findViewById(R.id.redpacket_share_btn);
		redpacket_share_btn.setEnabled(false);
		redpacket_remark = (TextView) findViewById(R.id.redpacket_remark);
		twodim_remark = (TextView) findViewById(R.id.twodim_remark);
		redpacket_total = (TextView) findViewById(R.id.redpacket_total);
		redpacket_total_left = (TextView) findViewById(R.id.redpacket_total_left);
		redpacket_total_right = (TextView) findViewById(R.id.redpacket_total_right);
		mListView = (ListView) findViewById(R.id.mListView);

		// share_btn.setOnClickListener(this);
		// share_list.setOnClickListener(this);
		lay_back_investment.setOnClickListener(this);
		redpacket_share_btn.setOnClickListener(this);

		redpacket_Amount = (LinearLayout) findViewById(R.id.redpacket_Amount);
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		ScreenUtils.setLayoutHeight(this, linear1, 0.92);// 0.92
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		linear2.setVisibility(View.GONE);

		redpacket_twodim = (ImageView) findViewById(R.id.redpacket_twodim);
		img_nodate = (ImageView) findViewById(R.id.img_nodate);
		Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),
				R.drawable.redpacket_nodata);
		Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.redpacket_bg);
		img_nodate.setImageBitmap(Save.ScaleBitmap(bitmap1, this, bitmap2));

		lists = new ArrayList<MemberShareRedEnvelopeLog>();
		mScrollView = (ScrollView) findViewById(R.id.mScrollView);
		// 对ScrollView就行滑动监听，滑到最底部的时候加载数据
		mScrollView.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					index++;
					break;
				default:
					break;
				}

				if (event.getAction() == MotionEvent.ACTION_UP && index > 0) {
					index = 0;
					if (!isLoadingMore && !haveNoData) {
						// getScrollY()表示ScrollView滑动的距离
						// getHeight()获取ScrollView在屏幕显示的高度
						// getScrollY()表示ScrollView滑动的距离
						View view = ((ScrollView) v).getChildAt(0);
						if (view.getMeasuredHeight() <= v.getScrollY()
								+ v.getHeight()) {
							if (manager != null) {
								page++;
								manager.getRedPackageObtainLogs(page);
								isLoadingMore = true;
							}
						}
					}
				}
				return false;
			}
		});
	}

	private void setUIData() {
		// 设置二维码
		byte[] bytes = Base64.decode(shareRedPackageInfoModel.QrCode,
				Base64.DEFAULT);
		sharebitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		Bitmap twodimensioncode_bitmap = Save.ScaleBitmap(sharebitmap, this,
				0.37);
		redpacket_twodim.setImageBitmap(twodimensioncode_bitmap);
		
		if (shareRedPackageInfoModel.IsShare && !shareRedPackageInfoModel.IsOver
				&& shareRedPackageInfoModel.shareAmount >0) {//有红包可以分享二维码显示状态
			twodim_remark.setText("让好友扫描上方二维码，领取您的红包。");
			redpacket_remark.setText("快去发红包吧！");
			
			redpacket_share_btn.setEnabled(true);
		}else {
			twodim_remark.setText("让好友扫描上方二维码，邀请好友注册信投宝。");
			redpacket_share_btn.setBackgroundResource(R.drawable.bg_ra_white);
			redpacket_share_btn.setTextColor(getResources().getColor(
					R.color.gray_q));
			redpacket_share_btn.setEnabled(false);
			
		}

		umengShareManager = new UmengShareManager(this);
		umengShareManager.setShareInit(shareRedPackageInfoModel.link,
				sharebitmap, ShareContent, ShareTitle);
		
		if (!shareRedPackageInfoModel.IsShare) {//是否有红包
			redpacket_Amount.setVisibility(View.GONE);
			redpacket_remark.setText("抱歉，您暂无红包可分享，\n敬请期待下一次活动！");

			
			img_nodate.setVisibility(View.VISIBLE);
		}else {
			if (shareRedPackageInfoModel.IsOver) {//是否过期
				
				redpacket_Amount.setVisibility(View.VISIBLE);
				redpacket_total.setText(Constants
						.StringToCurrency(shareRedPackageInfoModel.amount
								+ ""));
				redpacket_total_left.setText("抱歉，您的");
				redpacket_total_right.setText("元现金红包已过期，");
				redpacket_remark.setText("敬请期待下一次活动！");
				
			}else {
				if (shareRedPackageInfoModel.shareAmount >0) {//可分享的红包金额是否大于0
					redpacket_Amount.setVisibility(View.VISIBLE);
					redpacket_total.setText(Constants
							.StringToCurrency(shareRedPackageInfoModel.shareAmount
									+ ""));
					redpacket_total_left.setText("您有");
					redpacket_total_right.setText("元现金红包可发放，");
					redpacket_remark.setText("快去发红包吧！");
				}else {
					redpacket_remark.setText("抱歉，您的红包以派发完毕，\n敬请期待下一次活动！");
					redpacket_Amount.setVisibility(View.GONE);
					
					
				}
			}
		}

//		// 没有分享红包活动
//		if (shareRedPackageInfoModel.code == null) {
//			redpacket_Amount.setVisibility(View.GONE);
//			redpacket_remark.setText("抱歉，您暂无红包可分享，\n敬请期待下一次活动！");
//
//			twodim_remark.setText("让好友扫描上方二维码，邀请好友注册信投宝。");
//			redpacket_share_btn.setBackgroundResource(R.drawable.bg_ra_white);
//			redpacket_share_btn.setTextColor(getResources().getColor(
//					R.color.gray_q));
//			redpacket_share_btn.setEnabled(false);
//			img_nodate.setVisibility(View.VISIBLE);
//		} else {// 有分享红包活动
//			if (shareRedPackageInfoModel.code != null
//					&& shareRedPackageInfoModel.shareAmount == 0) { // 有分享红包活动，但是红包已经分享完毕
//				redpacket_remark.setText("抱歉，您的红包以派发完毕，\n敬请期待下一次活动！");
//				redpacket_Amount.setVisibility(View.GONE);
//				twodim_remark.setText("让好友扫描上方二维码，邀请好友注册信投宝。");
//				redpacket_share_btn
//						.setBackgroundResource(R.drawable.bg_ra_white);
//				redpacket_share_btn.setEnabled(false);
//				redpacket_share_btn.setTextColor(getResources().getColor(
//						R.color.gray_q));
//			} else {
//				redpacket_Amount.setVisibility(View.VISIBLE);
//				redpacket_total.setText(Constants
//						.StringToCurrency(shareRedPackageInfoModel.shareAmount
//								+ ""));
//				twodim_remark.setText("让好友扫描上方二维码，领取您的红包。");
//				redpacket_remark.setText("快去发红包吧！");
//			}
//
//			manager.getRedPackageObtainLogs(page);
//			isLoadingMore = true;
//		}
		
		manager.getRedPackageObtainLogs(page);
		isLoadingMore = true;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.lay_back_investment:
			this.finish();
			overTransition(1);
			break;
		case R.id.redpacket_share_btn:
			shareMenuWindow();
			break;
		case R.id.weixin:
			umengShareManager.ShareWenXin();
			break;
		case R.id.weixin_frends:
			umengShareManager.ShareWenXinfrends();
			break;
		case R.id.qq:
			umengShareManager.ShareQQ();
			break;

		default:
			break;
		}
	}

	/** 弹出分享界面及相关操作 */
	private void shareMenuWindow() {
		// 实例化SelectPopupWindow
		menuWindow = new SelectPopupWindow(this, this);
		// 显示窗口 设置layout在PopupWindow中显示的位置
		menuWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		ScreenUtils.backgroundAlpha(this, 0.3f);
		// 分享窗口消失后，恢复背景颜色
		menuWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				ScreenUtils.backgroundAlpha(ShareWeixinRedPacketActivity.this,
						1.0f);

			}
		});
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}

}
