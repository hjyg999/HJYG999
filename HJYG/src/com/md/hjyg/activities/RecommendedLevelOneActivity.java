package com.md.hjyg.activities;

import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.RecRewOneLevelAdapter;
import com.md.hjyg.entity.OneLevelRecRewModel;
import com.md.hjyg.entity.RecRewOneLevelListModel;
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
 * ClassName: RecommendedLevelOneActivity date: 2015-11-25 下午4:34:37
 * remark:我的推荐-一级信息
 * 
 * @author rw
 */
public class RecommendedLevelOneActivity extends BaseActivity implements
		OnClickListener, OnLoadMoreListener {

	/** 我推荐的注册人数 */
	// private TextView m_register_number;
	/** 我推荐的投资人数 */
	// private TextView m_invest_number;
	/** 我奖金收益 */
	// private TextView m_income;
	/** 我推荐的好友信息列表 */
	private LoadMoreListView mListView;
	/** 自动完成输入搜索框 */
	private AutoCompleteTextView onelevel_autotv;
	private RecRewOneLevelAdapter adapter;
	private RecRewOneLevelListModel Listmodel;
	private List<OneLevelRecRewModel> lists;
	private int page = 1;
	private String searchCondition = "";
	private boolean isloading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_recommendedlevelone_activity);
		findViewandInit();
		getRewardOnelevelServices(page, searchCondition);

	}

	private void findViewandInit() {
		HeaderViewControler.setHeaderView(this, "我推荐的好友", this);


		// m_register_number = (TextView) findViewById(R.id.m_register_number);
		// m_invest_number = (TextView) findViewById(R.id.m_invest_number);
		// m_income = (TextView) findViewById(R.id.m_income);
		onelevel_autotv = (AutoCompleteTextView) findViewById(R.id.onelevel_autotv);
		onelevel_autotv.addTextChangedListener(watcher);
		mListView = (LoadMoreListView) findViewById(R.id.mListView);

//		Intent intent = getIntent();
		// m_register_number.setText(intent.getIntExtra("OneLevelRegisterTotal",
		// 0) +"");
		// m_invest_number.setText(intent.getIntExtra("OneLevelRechargeTotal",
		// 0) +"");
		// m_income.setText(Constants.StringToCurrency(intent.getStringExtra("OneLevelRewardTotal")));

	}

	/** 获取用户统计数据 */
	private void getRewardOnelevelServices(final int page,
			String searchCondition) {
		mListView.haveDataLoad();
		isloading = true;
		dft.postRewardOnelevel(page, searchCondition,
				Constants.GetOneLevelRecommendRewardList_URL,
				Request.Method.POST, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						Listmodel = (RecRewOneLevelListModel) dft
								.GetResponseObject(jsonObject,
										RecRewOneLevelListModel.class);
						if (Listmodel != null) {
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
		mListView.onLoadMoreComplete();
		if (Loadmore.isEmpty()) {

			if (Listmodel.list.size() == 0) {
				mListView.noDataLoad("暂无数据");
			}
			lists = Listmodel.list;
			adapter = new RecRewOneLevelAdapter(this, lists);
			mListView.setAdapter(adapter);
		} else {
			if (Listmodel.list.size() > 0) {
				// lists = Listmodel.list;
				lists.addAll(Listmodel.list);
				adapter.notifyDataSetChanged();
			}
		}

		if ((Listmodel.list.size() <= Constants.PAGESIZE)) {
			mListView.setOnLoadMoreListener(null);
		} else {
			mListView.setOnLoadMoreListener(this);
		}
		
		isloading = false;

	}

	@Override
	public void onLoadMore() {
		page++;
		mListView.onLoadMoreing("加载中...");
		getRewardOnelevelServices(page, searchCondition);

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
			String sc = onelevel_autotv.getText().toString().trim();
			if (!sc.equals(searchCondition)) {
				searchCondition = sc;
				if (lists != null) {
					lists.clear();
				}
				if (!isloading) {
					adapter.notifyDataSetChanged();
					mListView.onLoadMoreing("正在搜索...");
					getRewardOnelevelServices(page, searchCondition);
				}
			}
		}
	};

	@Override
	public void onBackPressed() {
		RecommendedLevelOneActivity.this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
	}

}
