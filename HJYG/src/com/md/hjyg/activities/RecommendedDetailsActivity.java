package com.md.hjyg.activities;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.RecommendedDetailsAdapter;
import com.md.hjyg.entity.RecommendedDetails;
import com.md.hjyg.entity.modelList;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.LoadMoreListView;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 推荐人详情
 */
public class RecommendedDetailsActivity extends BaseListActivity implements
		View.OnClickListener, AdapterView.OnItemClickListener {

	// UI variable
//	private ProgressDialog progressDialog;
	// TextView
	private TextView tv_no_of_referrals_count, tv_gross_income_value,
			tv_recommeded_reward_value, tv_unpaid_referral_reward_value;
//, AuthToken
	private String Loadmore = "";
//	private String Method_name = "MemberServiceApi/RecommendReward";
//	private static final String TAG = "RecommendedDetailsActivity";
	private Context mcontext;
	//page = 1,
	private int  no_of_referrals;
	//, paidRewards
	private String grossIncome, unpaidRewards;
//	private boolean isLoading = false;
	private ArrayList<modelList> modelList = new ArrayList<modelList>();

	private DataFetchService dft;
	private RecommendedDetails recommended_details;
	private RecommendedDetailsAdapter recommended_details_adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_recommended_details);
		AppController.listActivity.add(this);
		mcontext = getBaseContext();
		findViews();
		init();
		CallRecommendedDetailstWebservice("");

	}

	public void init() {
		dft = new DataFetchService(this);
		recommended_details = new RecommendedDetails();
//		progressDialog = Constants.getProgressDialog(this);
	}

	private void findViews() {
		this.getListView().setOnItemClickListener(this);
		tv_unpaid_referral_reward_value = (TextView) findViewById(R.id.tv_unpaid_referral_reward_value);
		tv_gross_income_value = (TextView) findViewById(R.id.tv_gross_income_value);
		tv_no_of_referrals_count = (TextView) findViewById(R.id.tv_no_of_referrals_count);
		tv_recommeded_reward_value = (TextView) findViewById(R.id.tv_recommeded_reward_value);

	}

	public void CallRecommendedDetailstWebservice(String year) {

		dft.getRecommendedDetails(year, Constants.RecommendReward_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						recommended_details = (RecommendedDetails) dft
								.GetResponseObject(response,
										RecommendedDetails.class);
						no_of_referrals = recommended_details.RecommendCount;
						grossIncome = recommended_details.FriendGrossIncome.toString();
						unpaidRewards = recommended_details.UnpaidReferralRewards.toString();
//						paidRewards = recommended_details.Awardpaid.toString();
						tv_no_of_referrals_count.setText(String.valueOf(no_of_referrals));
						tv_gross_income_value.setText(grossIncome + "元");
						tv_recommeded_reward_value.setText(unpaidRewards + "元");
						// 计算拟待收推荐奖励的总和
						double FirstUnfinishedInterestRewardSum = 0.0;
						ArrayList<modelList> mList = recommended_details.modelList;
						if (mList != null && mList.size() > 0) {
							double FirstUnfinishedInterestReward;
							for (modelList modelList : mList) {
								FirstUnfinishedInterestReward =  modelList.FirstUnfinishedInterestReward;
								FirstUnfinishedInterestRewardSum += FirstUnfinishedInterestReward;
							}
						}
						tv_unpaid_referral_reward_value.setText(toTwoPoint(FirstUnfinishedInterestRewardSum) + "元");
						SetUIData("");
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Show error or whatever...

					}
				});

	}

	public void SetUIData(String Loadmore) {

		if (Loadmore.isEmpty()) {
			
			if (recommended_details.modelList.size() < 10) {
				((LoadMoreListView) getListView()).onLoadMoreComplete();
				if(recommended_details.modelList.size() == 0)
				{
					((LoadMoreListView) getListView()).noDataLoad("暂无数据");
				}
			}
			// Set adaptor
			modelList = recommended_details.modelList;
			recommended_details_adapter = new RecommendedDetailsAdapter(
					mcontext, modelList);
			setListAdapter(recommended_details_adapter);
			((LoadMoreListView) getListView()).onLoadMoreComplete();
		} else {
			if (!recommended_details.modelList.isEmpty()) {
				modelList.addAll(recommended_details.modelList);
				recommended_details_adapter.notifyDataSetChanged();
				((LoadMoreListView) getListView()).onLoadMoreComplete();
//				isLoading = false;
			} else {
				((LoadMoreListView) getListView()).onLoadMoreComplete();
			}
		}
		//一次性加载完成，不需要在加载更多
		((LoadMoreListView) getListView()).onLoadMoreComplete();
//		if(recommended_details.modelList.size() <= Constants.PAGESIZE)
//		{
//			((LoadMoreListView) getListView()).setOnLoadMoreListener(null);
//			((LoadMoreListView) getListView()).onLoadMoreComplete();
//			
//		}else {
//			
//			((LoadMoreListView) getListView())
//			.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
//				public void onLoadMore() {
//					// Do the work to load more items at the end of list
//					// here
//					page++;
//					CallMyInvestmentWebservice_Loadmore("", "" + page);
//				}
//			});
//		}

	}

	public void CallMyInvestmentWebservice_Loadmore(String ftype, String page) {

		dft.getRecommendedDetails(page, Constants.RecommendReward_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						recommended_details = (RecommendedDetails) dft
								.GetResponseObject(response,
										RecommendedDetails.class);
						Loadmore = "loadmore";
						SetUIData(Loadmore);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Show error or whatever...
					}
				});
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}
	
	/**
	 * 取double类型两位小数
	 * @param dSum
	 * @return
	 */
	public double toTwoPoint(double dSum) {
		BigDecimal bg = new BigDecimal(dSum);  
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
        return f1;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AppController.listActivity.remove(this);
	}
}
