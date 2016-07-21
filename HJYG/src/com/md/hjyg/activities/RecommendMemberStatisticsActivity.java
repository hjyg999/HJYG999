package com.md.hjyg.activities;

import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.RecMemberStatisticsAdapter;
import com.md.hjyg.entity.FinancialPlannerModel;
import com.md.hjyg.entity.OneLevelRecRewModel;
import com.md.hjyg.entity.RecRewOneLevelListModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/**
 * ClassName: RecommendMemberStatisticsActivity date: 2015-11-27 上午9:03:42
 * remark:推荐人会员统计tab
 * 
 * @author pyc
 */
public class RecommendMemberStatisticsActivity extends BaseActivity implements OnClickListener{
	
	/**我推荐的注册人数*/
	private TextView m_register_number;
	/**我推荐的投资人数*/
	private TextView m_invest_number;
	/**我奖金收益*/
	private TextView m_income;
	/**我的朋友推荐的注册人数*/
	private TextView f_register_number;
	/**我的朋友推荐的投资人数*/
	private TextView f_invest_number;
	/**我朋友的奖金收益*/
	private TextView f_income;
	
	/**查看我推荐的好友按钮*/
//	private LinearLayout myRecommend_friend;
	/**查看我好友推荐的好友按钮*/
//	private LinearLayout myfriendRecommend_friend;
	
	/**我推荐的好友信息列表*/
	private ListView mListView;
	
	private FinancialPlannerModel model;
	private RecMemberStatisticsAdapter adapter;
	private LinearLayout lin_nodata;
	private String searchCondition = "";
	private List<OneLevelRecRewModel> lists;
	private int page = 1;
	private ScrollView mScrollView;
	private int index;
	private boolean isLoadingMore ,haveNoData;
	private LinearLayout line_loading;
	private AutoCompleteTextView autotv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_memberstatistics_activity);
		findView();
		getFinancialPlannerServices();
		getRewardOnelevelServices(page,searchCondition);
	}
	
	private void findView() {
		HeaderViewControler.setHeaderView(this, "会员统计", this);
		
		m_register_number = (TextView) findViewById(R.id.m_register_number);
		m_invest_number = (TextView) findViewById(R.id.m_invest_number);
		m_income = (TextView) findViewById(R.id.m_income);
		f_register_number = (TextView) findViewById(R.id.f_register_number);
		f_invest_number = (TextView) findViewById(R.id.f_invest_number);
		f_income = (TextView) findViewById(R.id.f_income);
		
//		myRecommend_friend = (LinearLayout) findViewById(R.id.myRecommend_friend);
//		myfriendRecommend_friend = (LinearLayout) findViewById(R.id.myfriendRecommend_friend);
//		myRecommend_friend.setOnClickListener(this);
//		myfriendRecommend_friend.setOnClickListener(this);
		
		mListView = (ListView) findViewById(R.id.mListView);
		lin_nodata = (LinearLayout) findViewById(R.id.lin_nodata);
		
		mScrollView = (ScrollView) findViewById(R.id.mScrollView);
		setmScrollViewOnTouchListener();
		line_loading = (LinearLayout) findViewById(R.id.line_loading);
		autotv = (AutoCompleteTextView) findViewById(R.id.autotv);
		autotv.addTextChangedListener(watcher);
		
	}
	
	/**获取用户统计数据*/
	private void getFinancialPlannerServices() {
		dft.getNetInfoById(Constants.FinancialPlanner_URL, Request.Method.GET, 
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						 model = (FinancialPlannerModel) dft.GetResponseObject(
								jsonObject, FinancialPlannerModel.class);
						if (model != null ) {
							setDate();
						}
						
					}
				});
	}
	
	/**设置数据*/
	private void setDate() {
		m_register_number.setText(model.OneLevelRegisterTotal +"");
		m_invest_number.setText(model.OneLevelRechargeTotal +"");
		m_income.setText(Constants.StringToCurrency(model.OneLevelInvestTotal+"").replace(".00", "") );
		f_register_number.setText(model.TwoLevelRegisterTotal +"");
		f_invest_number.setText(model.TwoLevelRechargeTotal +"");
		f_income.setText(Constants.StringToCurrency( model.TwoLevelInvestTotal +"").replace(".00", ""));
	}
	
	/**获取用户统计数据*/
	private void getRewardOnelevelServices(int page,String searchCondition) {
		isLoadingMore = true;
		haveNoData = false;
		line_loading.setVisibility(View.VISIBLE);
		dft.postRewardOnelevel(page, searchCondition, Constants.GetOneLevelRecommendRewardList_URL, Request.Method.POST, 
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						RecRewOneLevelListModel Listmodel = (RecRewOneLevelListModel) dft.GetResponseObject(
								jsonObject, RecRewOneLevelListModel.class);
						isLoadingMore = false;
						line_loading.setVisibility(View.GONE);
						setListData(Listmodel);
					}
				});
	}
	
	private void setListData(RecRewOneLevelListModel Listmodel){
		if (page == 1) {
			if (Listmodel == null || Listmodel.list.size() == 0) {
				haveNoData = true;
				lin_nodata.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
				return;
			} else {
				lin_nodata.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
			}
			lists = Listmodel.list;
			adapter = new RecMemberStatisticsAdapter(lists, this);
			mListView.setAdapter(adapter);
			Constants.setListViewHeightBasedOnChildren(mListView);

		} else {
			if (Listmodel.list.size() > 0) {
				lists.addAll(Listmodel.list);
				adapter.notifyDataSetChanged();
				Constants.setListViewHeightBasedOnChildren(mListView);
			}else {
				haveNoData = true;
			}
		}
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
							page++;
							getRewardOnelevelServices(page,searchCondition);
						}
					}
				}
				return false;
			}
		});
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
			String sc = autotv.getText().toString().trim();
			if (!sc.equals(searchCondition)) {
				searchCondition = sc;
				if (lists != null) {
					lists.clear();
				}
				if (!isLoadingMore) {
					page = 1;
					adapter.notifyDataSetChanged();
					getRewardOnelevelServices(page, searchCondition);
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.myRecommend_friend://进入我推荐的好友界面
//			if (model == null) {
//				Toast.makeText(this, "数据正在加载，请稍后", Toast.LENGTH_SHORT).show();
//				return;
//			}
//			Intent intent = new Intent(this, RecommendedLevelOneActivity.class);
//			intent.putExtra("OneLevelRegisterTotal", model.OneLevelRegisterTotal);
//			intent.putExtra("OneLevelRechargeTotal", model.OneLevelRechargeTotal);
//			intent.putExtra("OneLevelRewardTotal", model.OneLevelRewardTotal);
//			
//			startActivity(intent);
//			overridePendingTransition(R.anim.trans_right_in, R.anim.trans_lift_out);
//			break;
//		case R.id.myfriendRecommend_friend://进入我好友推荐的好友界面
//			if (model == null) {
//				Toast.makeText(this, "数据正在加载，请稍后", Toast.LENGTH_SHORT).show();
//				return;
//			}
//			Intent i = new Intent(this, RecommendedLevelTwoActivity.class);
//			i.putExtra("TwoLevelRegisterTotal", model.TwoLevelRegisterTotal);
//			i.putExtra("TwoLevelRechargeTotal", model.TwoLevelRechargeTotal);
//			i.putExtra("TwoLevelRewardTotal", model.TwoLevelRewardTotal);
//			
//			startActivity(i);
//			overridePendingTransition(R.anim.trans_right_in, R.anim.trans_lift_out);
//			
//			break;
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

}
