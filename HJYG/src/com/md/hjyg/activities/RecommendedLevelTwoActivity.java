package com.md.hjyg.activities;

import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.RecRewTwoLevelAdapter;
import com.md.hjyg.entity.Notification;
import com.md.hjyg.entity.RecRewTwoLevelListModel;
import com.md.hjyg.entity.TwoLevelRecRewModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.utility.LoadMoreListView;
import com.md.hjyg.utility.LoadMoreListView.OnLoadMoreListener;
import com.md.hjyg.xinTouConstant.Constants;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/**
 * ClassName: RecommendedLevelTwoActivity date: 2015-11-25 下午4:37:46
 * remark:我的推荐-二级信息 我好友推荐的好友信息
 * 
 * @author rw
 */
public class RecommendedLevelTwoActivity extends BaseActivity implements
		OnClickListener, OnLoadMoreListener {

	/** 我推荐的注册人数 */
	// private TextView f_register_number;
	/** 我推荐的投资人数 */
	// private TextView f_invest_number;
	/** 我奖金收益 */
	// private TextView f_income;
	private AutoCompleteTextView twolevel_autotv;
	private RecRewTwoLevelAdapter adapter;
	/** 我推荐的好友信息列表 */
	private LoadMoreListView mListView;

	private RecRewTwoLevelListModel listModel;
	private int page = 1;
	private String searchCondition = "";
	private List<TwoLevelRecRewModel> lists;
	private boolean isLoading;
//	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_recommendedleveltwo_activity);

		findViewandInit();

	}

	private void findViewandInit() {
		HeaderViewControler.setHeaderView(this, "我好友推荐的好友", this);
		twolevel_autotv = (AutoCompleteTextView) findViewById(R.id.twolevel_autotv);
		twolevel_autotv.addTextChangedListener(watcher);
		// f_register_number = (TextView) findViewById(R.id.f_register_number);
		// f_invest_number = (TextView) findViewById(R.id.f_invest_number);
		// f_income = (TextView) findViewById(R.id.f_income);

		mListView = (LoadMoreListView) findViewById(R.id.mListView);
//		intent = getIntent();
		// f_register_number.setText(intent.getIntExtra("TwoLevelRegisterTotal",
		// 0) +"");
		// f_invest_number.setText(intent.getIntExtra("TwoLevelRechargeTotal",
		// 0)+ "");
		// f_income.setText(intent.getStringExtra("TwoLevelRewardTotal"));
		getRewardTwolevelServices(page, searchCondition);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
		RecommendedLevelTwoActivity.this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
	}

	/** 获取用户统计数据 */
	private void getRewardTwolevelServices(final int page,
			String searchCondition) {
		mListView.haveDataLoad();
		isLoading = true;
		dft.postRewardTwolevel(page, searchCondition,
				Constants.GetTwoLevelRecommendRewardList_URL,
				Request.Method.POST, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						listModel = (RecRewTwoLevelListModel) dft
								.GetResponseObject(jsonObject,
										RecRewTwoLevelListModel.class);
						Notification notification = listModel.notification;
						if (notification != null
								&& notification.ProcessResult == 1) {
							if (page == 1) {
								setUIData("");
							} else {
								setUIData("Loadmore");
							}
						} else {

						}
					}
				});
	}

	private void setUIData(String Loadmore) {

		mListView.onLoadMoreComplete();
		if (Loadmore.isEmpty()) {

			if (listModel.list.size() == 0) {
				mListView.noDataLoad("暂无数据");
			}
			lists = listModel.list;
			adapter = new RecRewTwoLevelAdapter(this, lists);
			mListView.setAdapter(adapter);
		} else {
			if (listModel.list.size() > 0) {
				lists.addAll(listModel.list);
				adapter.notifyDataSetChanged();
			} 
		}

		if ((listModel.list.size() <= Constants.PAGESIZE)) {
			mListView.setOnLoadMoreListener(null);
		} else {
			mListView.setOnLoadMoreListener(this);
		}
		
		isLoading = false;
	}

	@Override
	public void onLoadMore() {
		page++;
		mListView.onLoadMoreing("加载中...");
		getRewardTwolevelServices(page, searchCondition);

	}

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			page = 1;
		}

		@Override
		public void afterTextChanged(Editable s) {
			String sc = twolevel_autotv.getText().toString().trim();
			if ( ! sc.equals(searchCondition)) {
				searchCondition = sc;
				if (lists != null) {
					lists.clear();
				}
				if (!isLoading) {
					adapter.notifyDataSetChanged();
					mListView.onLoadMoreing("正在搜索...");
					getRewardTwolevelServices(page, searchCondition);
				}
			}
		}
	};

}
