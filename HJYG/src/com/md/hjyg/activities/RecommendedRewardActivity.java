package com.md.hjyg.activities;

import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.RecommendedViewPagerAdapter;
import com.md.hjyg.entity.StatsMonthlyRewardListsModel;
import com.md.hjyg.entity.StatsMonthlyRewardListsModel.StatsMonthlyReward;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.utility.LoadMoreListView;
import com.md.hjyg.utility.Save;
import com.md.hjyg.validators.RecommendedPageTransformer;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/**
 * ClassName: RecommendedRewardActivity date: 2015-11-25 下午4:20:36
 * remark:推荐奖励页面-包括已收奖励，待收奖励
 * 
 * @author rw
 */
public class RecommendedRewardActivity extends BaseActivity implements
		OnClickListener {

	private ViewPager viewPager;
	private RecommendedViewPagerAdapter adapter;
	// private TextView
	// one_level_f,two_level_f,one_level_f_botline,two_level_f_botline;
	@SuppressWarnings("unused")
	private LoadMoreListView mListView;
	// private RecommendedRewardAdapter adapter = null;
	private List<StatsMonthlyReward> lists;
	private Intent intent;
	private int awardType = 0;
	private int page = 1;
	private StatsMonthlyRewardListsModel model;
	private boolean haveMore;
	private LinearLayout nodate;
	private ImageView bg_img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_recommendedreward_activity);
		findViews();
		init();
		getRewardByMonthServices(page, awardType);
	}

	public void findViews() {
		nodate = (LinearLayout) findViewById(R.id.nodate);
		bg_img = (ImageView) findViewById(R.id.bg_img);
	}

	public void init() {
		intent = getIntent();
		awardType = intent.getIntExtra("awardType", 0);
		if (awardType == 1) {
			HeaderViewControler.setHeaderView(this, "已收奖励", this);
			bg_img.setImageBitmap(Save.ScaleBitmap(
					BitmapFactory.decodeResource(getResources(),
							R.drawable.rec_pig_money_bg), this));
		} else if (awardType == 2) {
			HeaderViewControler.setHeaderView(this, "待收奖励", this);
			bg_img.setImageBitmap(Save.ScaleBitmap(BitmapFactory
					.decodeResource(getResources(), R.drawable.rec_pig_bg),
					this));
		}

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
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

	@Override
	protected void onResume() {
		super.onResume();
	}

	/** 获取用户统计数据 */
	private void getRewardByMonthServices(final int page, int type) {
		dft.postRewardByMonth(page, type, Constants.GetRewardByMonth_URL,
				Request.Method.POST, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						model = (StatsMonthlyRewardListsModel) dft
								.GetResponseObject(jsonObject,
										StatsMonthlyRewardListsModel.class);
						if (model != null) {
							if (page == 1) {
								setUIData("");
							} else {
								setUIData("Loadmore");
							}
						}

					}
				});
	}

	private void setUIData(String Loadmore) {
		if (Loadmore.length() == 0) {
			viewPager = (ViewPager) findViewById(R.id.view_pager);
			viewPager
					.setPageTransformer(true, new RecommendedPageTransformer());
			lists = model.list;
			adapter = new RecommendedViewPagerAdapter(this, lists, awardType,
					mHandler);
			viewPager.setAdapter(adapter);
			if (model.list.size() == 0) {
				nodate.setVisibility(View.VISIBLE);
				viewPager.setVisibility(View.GONE);
				bg_img.setVisibility(View.GONE);
			} else {
				if (model.list.size() == 10) {
					haveMore = true;
				}
				nodate.setVisibility(View.GONE);
				viewPager.setVisibility(View.VISIBLE);
				bg_img.setVisibility(View.VISIBLE);
			}
		} else {
			if (model.list.size() > 0) {
				lists.addAll(model.list);
				adapter.notifyDataSetChanged();
				if (model.list.size() == 10) {
					haveMore = true;
				}
			}
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (haveMore) {
					page++;
					haveMore = false;
					getRewardByMonthServices(page, awardType);
				}
				break;

			default:
				break;
			}
		};
	};

}
