package com.md.hjyg.activities;

import java.util.ArrayList;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.MyInvestmentAdapter;
import com.md.hjyg.entity.FinancialDetails;
import com.md.hjyg.entity.ListModel;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.LoadMoreListView;
import com.md.hjyg.utility.LoadMoreListView.OnLoadMoreListener;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 我的投资-持有中
 */
public class HoldInActivity extends BaseListActivity implements
		OnClickListener, AdapterView.OnItemClickListener, OnLoadMoreListener {
	// UI variables
	// TextView
	// private TextView tv_title;
	// Linear Layout
	// private LinearLayout lay_back_investment;

	// private ProgressDialog progressDialog;

	private DataFetchService dft;
	private FinancialDetails financial_details;
	private MyInvestmentAdapter my_investment_adapter;
	// , AuthToken
	private String Loadmore = "";
	// public String Method_name = "InvestApi/GetRepaymentListReceivedByPage";
	// private static final String TAG = "HoldInActivity";
	private Context mcontext;
	private int page = 1;
	// private boolean isLoading = false;
	private ArrayList<ListModel> load_MoreList = new ArrayList<ListModel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hold_in);
		AppController.listActivity.add(this);
		mcontext = getBaseContext();
		findViews();
		init();
		try {
			CallMyInvestmentWebservice("", "" + page);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// AuthToken = Constants.GetResult_AuthToken(mcontext);
	}

	private void init() {

		dft = new DataFetchService(this);
		// progressDialog = Constants.getProgressDialog(this);
		financial_details = new FinancialDetails();
	}

	private void findViews() {
		this.getListView().setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {

	}

	private void CallMyInvestmentWebservice(String ftype, String page) {

		dft.getHoldInDetails(ftype, page,
				Constants.GetRepaymentListReceivedByPage_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						financial_details = (FinancialDetails) dft
								.GetResponseObject(response,
										FinancialDetails.class);

						SetUIData("");
						// homeApi = (HomeApiModel)
						// dft.GetResponseObject(response,HomeApiModel.class);

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
			// Set adaptor

			if (financial_details.List.size() < 10) {

				((LoadMoreListView) getListView()).onLoadMoreComplete();
				if (financial_details.List.size() == 0) {
					((LoadMoreListView) getListView()).noDataLoad("暂无数据");
				}
			}
			load_MoreList = financial_details.List;
			// Toast.makeText(this, load_MoreList.size() + "", 1).show();
			my_investment_adapter = new MyInvestmentAdapter(mcontext,
					load_MoreList, 0);
			setListAdapter(my_investment_adapter);
		} else {
			if (!financial_details.List.isEmpty()) {
				load_MoreList.addAll(financial_details.List);
				my_investment_adapter.notifyDataSetChanged();
				((LoadMoreListView) getListView()).onLoadMoreComplete();
				// isLoading = false;
			} else {
				((LoadMoreListView) getListView()).onLoadMoreComplete();
			}
		}
		// ((LoadMoreListView) getListView())
		// .setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
		// public void onLoadMore() {
		// // Do the work to load more items at the end of list
		// // here
		// page++;
		// CallMyInvestmentWebservice_Loadmore("", "" + page);
		// }
		// });

		if ((financial_details.List.size() <= Constants.PAGESIZE)) {
			((LoadMoreListView) getListView()).setOnLoadMoreListener(null);
			((LoadMoreListView) getListView()).onLoadMoreComplete();
		} else {
			((LoadMoreListView) getListView()).setOnLoadMoreListener(this);
		}

	}

	public void CallMyInvestmentWebservice_Loadmore(String ftype, String page) {

		dft.getHoldInDetails(ftype, page,
				Constants.GetRepaymentListReceivedByPage_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						financial_details = (FinancialDetails) dft
								.GetResponseObject(response,
										FinancialDetails.class);
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	public void onLoadMore() {
		page++;
		CallMyInvestmentWebservice_Loadmore("", "" + page);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppController.listActivity.remove(this);
	}
}
