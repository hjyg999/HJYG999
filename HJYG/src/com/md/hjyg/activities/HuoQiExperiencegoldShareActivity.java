package com.md.hjyg.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.adapter.RedpacketListAdapter;
import com.md.hjyg.entity.RedPackageObtainLogsModel.MemberShareRedEnvelopeLog;
import com.md.hjyg.entity.ShareRedPackageInfoModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.layoutEntities.SelectPopupWindow;
import com.md.hjyg.layoutEntities.wheel.ChangeBirthDialog;
import com.md.hjyg.layoutEntities.wheel.ChangeBirthDialog.OnBirthListener;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utility.UmengShareManager;
import com.md.hjyg.utility.WeixinRedPacketManager;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * ClassName: HuoQiExperiencegoldShareActivity date: 2016-1-9 上午11:56:12 remark:
 * 分享体验金界面
 * 
 * @author pyc
 */
public class HuoQiExperiencegoldShareActivity extends BaseActivity implements
		OnClickListener {

	private TextView tv_search, tv_searchAll, tv_amount,tv_amount_s, tv_amount1,
			tv_amount2, tv_noShare, tv_twoHit;
	/** 分享按钮 */
	private TextView tv_shareBtn;
	private LinearLayout line_loading,line_total;
	private ImageView top_img, img_nodate, img_noShare;
	/** 二维码 */
	private ImageView redpacket_twodim;
	private Bitmap topBitmap, nodateBitmap, onTopBitmap;
	private Activity mActivity;
	/** 现在日期Dialog */
	private ChangeBirthDialog myDialog;

	private String ShareTitle = "信投宝携手玛丽莱珠宝给您送红包了，祝您生活美满！";
	private String ShareContent = "100起投，年化利率6%-15%，100%本金收益保障";
	/** 分享链接: */
	private String link;
	/** 用于加解密的code: */
//	private String code;
	private WeixinRedPacketManager manager;
	private ShareRedPackageInfoModel model;
	private Bitmap sharebitmap;
	private LinearLayout lin_canShare, lin_noShare;
	private int page = 1;
	private String yearmonth = "";
	private ScrollView mScrollView;
	private boolean isLoadingMore = false;
	private boolean haveNoData = false;
	private List<MemberShareRedEnvelopeLog> lists;
	private ListView mListView;
	private RedpacketListAdapter adapter;
	private int index = 0;
	private SelectPopupWindow menuWindow;
	private UmengShareManager umengShareManager;
	private View view_line;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_share_experiencegold);

		mActivity = this;
		findViewandInit();
		manager = new WeixinRedPacketManager(mActivity, handler,dft);
		manager.getShareExpInfo();
		manager.getShareExpLogs(page, yearmonth);
		line_loading.setVisibility(View.VISIBLE);
	}

	private void findViewandInit() {

		// 标题栏设置
		HeaderViewControler.setHeaderView(this, "分享体验金", this);

		lin_canShare = (LinearLayout) findViewById(R.id.lin_canShare);
		lin_noShare = (LinearLayout) findViewById(R.id.lin_noShare);
		line_total = (LinearLayout) findViewById(R.id.line_total);
		line_total.setVisibility(View.GONE);
		// ImageView
		top_img = (ImageView) findViewById(R.id.top_img);
		img_nodate = (ImageView) findViewById(R.id.img_nodate);
		img_noShare = (ImageView) findViewById(R.id.img_noShare);
		redpacket_twodim = (ImageView) findViewById(R.id.redpacket_twodim);
		LinearLayout.LayoutParams params = (LayoutParams) redpacket_twodim.getLayoutParams();
		params.height = (int) ((ScreenUtils.getScreenWidth(mActivity))*0.5);
		params.width = (int) ((ScreenUtils.getScreenWidth(mActivity))*0.5);
		redpacket_twodim.setLayoutParams(params);

		line_loading = (LinearLayout) findViewById(R.id.line_loading);
		view_line =  findViewById(R.id.view_line);
		tv_amount = (TextView) findViewById(R.id.tv_amount);
		tv_amount_s = (TextView) findViewById(R.id.tv_amount_s);
		tv_amount1 = (TextView) findViewById(R.id.tv_amount1);
		tv_amount2 = (TextView) findViewById(R.id.tv_amount2);
		tv_noShare = (TextView) findViewById(R.id.tv_noShare);
		tv_twoHit = (TextView) findViewById(R.id.tv_twoHit);
		tv_shareBtn = (TextView) findViewById(R.id.tv_shareBtn);
		tv_search = (TextView) findViewById(R.id.tv_search);
		tv_searchAll = (TextView) findViewById(R.id.tv_searchAll);
		tv_search.setOnClickListener(this);
		tv_searchAll.setOnClickListener(this);

		mScrollView = (ScrollView) findViewById(R.id.mScrollView);
		mListView = (ListView) findViewById(R.id.mListView);
		setmScrollViewOnTouchListener();

		new MyAsyncTask().execute(R.drawable.share_top_bg,
				R.drawable.share_nodate, R.drawable.share_notop);

		lists = new ArrayList<MemberShareRedEnvelopeLog>();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				model = manager.getShareRedPackageInfoModel();
				setUIData();
				break;
			case 1:
				//隐藏加载框
				line_loading.setVisibility(View.GONE);
				if (manager.getLists() != null && manager.getLists().size() > 0) {
					if (manager.getLists().size() < 10) {
						haveNoData = true;
					}else {
						haveNoData = false;
					}
					lists.addAll(manager.getLists());
					if (page == 1) {
						mListView.setVisibility(View.VISIBLE);
						view_line.setVisibility(View.VISIBLE);
						img_nodate.setVisibility(View.GONE);
						adapter = new RedpacketListAdapter(mActivity, lists,1);
						mListView.setAdapter(adapter);
					} else {
						adapter.notifyDataSetChanged();
					}
					Constants.setListViewHeightBasedOnChildren(mListView);
				} else {
					if (page == 1) {
						img_nodate.setVisibility(View.VISIBLE);
						view_line.setVisibility(View.GONE);
						mListView.setVisibility(View.GONE);
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

	private void setUIData() {
		// 设置二维码
		byte[] bytes = Base64.decode(model.QrCode, Base64.DEFAULT);
		sharebitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

		redpacket_twodim.setImageBitmap(sharebitmap);

		tv_twoHit.setText(model.QRDes);
		ShareTitle = model.ShareTittle;
		ShareContent = model.ShareContent;
		link = model.link;
//		code = model.code;
		umengShareManager = new UmengShareManager(this);
		Bitmap bitmap = sharebitmap;
		try {
			bitmap = BitmapFactory.decodeStream(mActivity.getResources().getAssets().open("applogo.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		umengShareManager.setShareInit(link,bitmap, ShareContent, ShareTitle);
		if (model.IsShare) {// 可以分享
			tv_shareBtn.setEnabled(true);
			tv_shareBtn.setOnClickListener(this);
			tv_shareBtn.setTextColor(getResources().getColor(R.color.red));
			tv_shareBtn.setBackgroundResource(R.drawable.bg_red_strok_selector);
			lin_canShare.setVisibility(View.VISIBLE);
			lin_noShare.setVisibility(View.GONE);
			// 头部文字
			String[] strs = model.ShareDes.split("-");
//			String str = strs[0] + strs[1];
//			tv_amount.setText(TextUtil.getRelativeSize(str, str.length() - 1,
//					str.length(), 0.5f));
			
			tv_amount.setText(strs[0]);
			tv_amount_s.setText(strs[1]);
			tv_amount1.setText(strs[2]);
			tv_amount2.setText(strs[3]);
		} else {
			lin_canShare.setVisibility(View.GONE);
			lin_noShare.setVisibility(View.VISIBLE);
			tv_noShare.setText(model.ShareDes);
			tv_shareBtn.setEnabled(false);
			tv_shareBtn.setTextColor(getResources().getColor(R.color.gray_sq));
			tv_shareBtn.setBackgroundResource(R.drawable.bg_mra_grayq);
		}
		
		line_total.setVisibility(View.VISIBLE);

	}

	/**
	 * 对ScrollView就行滑动监听，滑到最底部的时候加载数据
	 */
	private void setmScrollViewOnTouchListener() {
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
								manager.getShareExpLogs(page, yearmonth);
								line_loading.setVisibility(View.VISIBLE);
								isLoadingMore = true;
							}
						}
					}
				}
				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.tv_search:
			myDialog = new ChangeBirthDialog(this);
			myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题
			Window dialogWindow = myDialog.getWindow();
			dialogWindow.setWindowAnimations(R.style.Animcardtype);
			myDialog.setCanceledOnTouchOutside(false);
			myDialog.setStyle(true, true, false);
//			myDialog.setDate(2016, 01, 29);
			myDialog.setDate(0, 0, 0);
			myDialog.show();
			myDialog.setBirthdayListener(new OnBirthListener() {

				@Override
				public void onClick(String year, String month,String day) {
					tv_search.setText(year + "年" + month + "月");
					tv_search.setBackgroundResource(R.drawable.bg_grad_red_strok);
					settv_searchAll(R.drawable.bg_grad_strok, R.color.gray, R.drawable.share_found_gray);
					yearmonth = year + "/" + month;
					
					inintDate();
				}
			});
			break;
		case R.id.tv_searchAll:
			settv_searchAll(R.drawable.bg_mra_redq_strok, R.color.red, R.drawable.share_found_red);
			yearmonth = "";
			inintDate();
			tv_search.setText("");
			tv_search.setBackgroundResource(R.drawable.bg_grad_strok);
			break;
		case R.id.tv_shareBtn:
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
	
	private void inintDate(){
		page = 1;
		lists.removeAll(lists);
		if (adapter != null) {
			adapter.notifyDataSetChanged();
			Constants.setListViewHeightBasedOnChildren(mListView);
		}
		manager.getShareExpLogs(page, yearmonth);
		line_loading.setVisibility(View.VISIBLE);
		img_nodate.setVisibility(View.GONE);
	}
	
	/**
	 * 设置tv_searchAll
	 * @param bgRid 背景图片Id
	 * @param textColorId  字体颜色Id
	 * @param img_lefDrawableRid 左边的图片Id
	 */
	private void settv_searchAll(int bgRid,int textColorId,int img_lefDrawableRid){
		tv_searchAll.setBackgroundResource(bgRid);
		tv_searchAll.setTextColor(getResources().getColor(textColorId));
		Drawable img_lefDrawable = getResources().getDrawable(img_lefDrawableRid);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		img_lefDrawable.setBounds(0, 0, img_lefDrawable.getMinimumWidth(), img_lefDrawable.getMinimumHeight());
		tv_searchAll.setCompoundDrawables(img_lefDrawable, null, null, null);
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
				ScreenUtils.backgroundAlpha(mActivity,1.0f);
			}
		});
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}

	private class MyAsyncTask extends AsyncTask<Integer, Void, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			Bitmap top = BitmapFactory.decodeResource(mActivity.getResources(),
					params[0]);
			topBitmap = Save.ScaleBitmap(top, mActivity);
			Bitmap nodate = BitmapFactory.decodeResource(
					mActivity.getResources(), params[1]);
			nodateBitmap = Save.ScaleBitmap(nodate, mActivity, top);
			Bitmap noTop = BitmapFactory.decodeResource(
					mActivity.getResources(), params[2]);
			onTopBitmap = Save.ScaleBitmap(noTop, mActivity, top);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (topBitmap != null) {
				top_img.setImageBitmap(topBitmap);
			}
			if (nodateBitmap != null) {
				img_nodate.setImageBitmap(nodateBitmap);
			}
			if (onTopBitmap != null) {
				img_noShare.setImageBitmap(onTopBitmap);
			}
		}

	}

}
