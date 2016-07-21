package com.md.hjyg.utility;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.adapter.NoviceViewPagerAdapter;
import com.md.hjyg.entity.NoviceLoan;
import com.md.hjyg.entity.NoviceLoan.LoanPicture;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.validators.RecommendedPageTransformer;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ClassName: NoviceViewPagerManager date: 2016-3-11 上午9:50:09 remark: 首页新手专享管理类
 * 
 * @author pyc
 */
public class NoviceViewPagerManager implements OnClickListener {
	public final static int Handler_What = 101;
	private ViewPager viewpager_prize;
	private LinearLayout lin_smlimg;
	private ImageView novic_arrow_left, novic_arrow_right, novice_img_titile;
	private TextView tv_invest_vale, tv_invest_term, tv_invest_income,
			tv_imgTitle;
	private Activity context;

	private ImageView[] smallImageViews;
	private int size;
	private List<NoviceLoan> lists;
	private NoviceViewPagerAdapter adapter;
	private RelativeLayout rel_novic;
	private Bitmap[] bitmaps;
	private LruCacheWebBitmapManager lruCacheBitmap;


	public NoviceViewPagerManager(Activity context, View v) {
		this.context = context;
		lruCacheBitmap = LruCacheWebBitmapManager.getInstance();

		rel_novic = (RelativeLayout) v.findViewById(R.id.rel_novic);
		viewpager_prize = (ViewPager) v.findViewById(R.id.viewpager_prize);
		lin_smlimg = (LinearLayout) v.findViewById(R.id.lin_smlimg);

		novic_arrow_left = (ImageView) v.findViewById(R.id.novic_arrow_left);
		novic_arrow_right = (ImageView) v.findViewById(R.id.novic_arrow_right);
		novic_arrow_left.setOnClickListener(this);
		novic_arrow_right.setOnClickListener(this);
		novice_img_titile = (ImageView) v.findViewById(R.id.novice_img_titile);

		// 新手专享
		tv_invest_vale = (TextView) v.findViewById(R.id.tv_invest_vale);
		tv_invest_term = (TextView) v.findViewById(R.id.tv_invest_term);
		tv_invest_income = (TextView) v.findViewById(R.id.tv_invest_income);
		tv_imgTitle = (TextView) v.findViewById(R.id.tv_imgTitle);

		Save.loadingImg(handler, context, new int[] {
				R.drawable.novice_img_titile, R.drawable.novice_shouh_48 ,R.drawable.novice_shouh_120},
				Handler_What);

		// setImg();

	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Handler_What:
				bitmaps = (Bitmap[]) msg.obj;
				setNovice_img_titile();
				break;
			case LoanPicture.移动端产品图标:
				smallImageViews[msg.arg1].setImageBitmap((Bitmap)msg.obj);
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.novic_arrow_left://
			if (size != 0) {
				int p = viewpager_prize.getCurrentItem();
				viewpager_prize.setCurrentItem(p - 1);
			}
			break;
		case R.id.novic_arrow_right://
			if (size != 0) {
				int p = viewpager_prize.getCurrentItem();
				viewpager_prize.setCurrentItem(p + 1);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 设置数据
	 * 
	 * @param lists
	 */
	public void setNetDate(List<NoviceLoan> lists) {
		this.lists = lists;
		size = lists.size();
		setNovice();
	}

	/**
	 * 设置标题图片
	 */
	public void setNovice_img_titile() {
		novice_img_titile.setImageBitmap(bitmaps[0]);
		setNovice();
	}

	/**
	 * 设置控件的宽度
	 * 
	 * @param w
	 */
	public void setWidth(int w) {
		ViewParamsSetUtil.setViewHandW_lin(rel_novic, 0, w);
	}

	/**
	 * 初始化数据和UI
	 */
	public void setNovice() {
		if (lists != null && lists.size() > 0 && bitmaps != null) {

			// 新手标相关设置
			setNoviceDate(0);
			initSmallImg();
			initNoviceViewPager();
		}
	}

	private void setNoviceDate(int i) {
		// 投资金额
		String InvestAmount = Constants.StringToCurrency(
				lists.get(i).InvestAmount + "").replace(".00", "");
		tv_invest_vale.setText(TextUtil.getRelativeSize(InvestAmount + "元", 0,
				InvestAmount.length(), 1.35f));
		// 投资期限
		String LoanTerm = lists.get(i).LoanTerm + "";
		// 单位 ：月等
		int datatype = lists.get(i).LoanDateType;
		String type = "";
		if (datatype == 0) {
			type = "个月";
		} else if (datatype == 2) {
			type = "天";
		} else if (datatype == 4) {
			type = "周";
		}
		tv_invest_term.setText(TextUtil.getRelativeSize(LoanTerm + type, 0,
				LoanTerm.length(), 1.35f));
		// 预期收益
		String LoanInterest = Constants.StringToCurrency(
				lists.get(i).LoanInterest + "").replace(".00", "");
		tv_invest_income.setText(TextUtil.getRelativeSize(LoanInterest + "元",
				0, LoanInterest.length(), 1.35f));
		tv_imgTitle.setText(lists.get(i).Title);
	}

	private void initSmallImg() {
		if (bitmaps == null || bitmaps.length < 1 || bitmaps[1] == null
				|| lists == null) {
			return;
		}
		lin_smlimg.removeAllViews();
		smallImageViews = new ImageView[size];
		LinearLayout.LayoutParams params = new LayoutParams(
				bitmaps[1].getWidth(), bitmaps[1].getHeight());
		params.leftMargin = 5;
		for (int i = 0; i < size; i++) {
			smallImageViews[i] = new ImageView(context);

			smallImageViews[i].setLayoutParams(params);
			for (int j = 0; j < lists.get(i).loanPicture.size(); j++) {
				if (lists.get(i).loanPicture.get(j).Type == LoanPicture.移动端产品图标) {
					lruCacheBitmap.getBitmap(Constants.getDft(context),
							lists.get(i).loanPicture.get(j).URL, handler,
							LoanPicture.移动端产品图标, i);
				}
			}

			if (i == 0) {
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
	private void initNoviceViewPager() {
		LinearLayout.LayoutParams v_pParams = (LinearLayout.LayoutParams) viewpager_prize
				.getLayoutParams();
		v_pParams.height = bitmaps[2].getHeight();
		// v_pParams.width = award_imgs[0].getWidth();
		viewpager_prize.setLayoutParams(v_pParams);
		// 设置动画
		viewpager_prize.setPageTransformer(true,
				new RecommendedPageTransformer());
		adapter = new NoviceViewPagerAdapter(context, true, lists,lruCacheBitmap);
		viewpager_prize.setAdapter(adapter);
		viewpager_prize.setCurrentItem(size * 100);
		viewpager_prize.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				setNoviceDate(position % size);

				for (int i = 0; i < size; i++) {
					if (smallImageViews[i] != null) {
						if (position % size == i) {
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

}
