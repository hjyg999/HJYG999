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
import com.md.hjyg.xinTouConstant.Constants;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 已还款
 */
public class RepaidActivity extends BaseListActivity implements
		View.OnClickListener, AdapterView.OnItemClickListener {

	ProgressDialog progressDialog;

	DataFetchService dft;
	FinancialDetails financial_details;
	MyInvestmentAdapter my_investment_adapter;

	boolean isLoading = false;
	boolean noload = false;
	int page = 1;
	String AuthToken /* = "8fde6b9f-d4d6-415c-b270-db99400d7971" */;
	public String Loadmore = "";
	// public String Method_name = "InvestApi/GetRepaymentListReceivedByPage";
	private static final String TAG = "RepaidActivity";
	Context mcontext;

	public ArrayList<ListModel> load_MoreList = new ArrayList<ListModel>();

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
		AuthToken = Constants.GetResult_AuthToken(mcontext);

	}

	private void init() {
		dft = new DataFetchService(this);
		financial_details = new FinancialDetails();
		progressDialog = Constants.getProgressDialog(this);
	}

	private void findViews() {
		this.getListView().setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
	}

	public void CallMyInvestmentWebservice(String ftype, String page) {

		dft.getRepaidDetails(ftype, page,
				Constants.GetRepaymentListReceivedByPage_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						financial_details = (FinancialDetails) dft
								.GetResponseObject(response,
										FinancialDetails.class);
						SetUIData("");
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});

	}

	public void SetUIData(String Loadmore) {
		if (Loadmore.isEmpty()) {
			if (financial_details.List.size() < 10) {
				((LoadMoreListView) getListView()).onLoadMoreComplete();
				if (financial_details.List.size() == 0) {
					((LoadMoreListView) getListView()).noDataLoad("暂无数据");
				}
			}
			load_MoreList = financial_details.List;
			my_investment_adapter = new MyInvestmentAdapter(mcontext,
					load_MoreList, 2);
			setListAdapter(my_investment_adapter);
		} else {
			if (!financial_details.List.isEmpty()) {
				load_MoreList.addAll(financial_details.List);
				my_investment_adapter.notifyDataSetChanged();
				((LoadMoreListView) getListView()).onLoadMoreComplete();
				isLoading = false;
			} else {
				((LoadMoreListView) getListView()).onLoadMoreComplete();
			}
		}
		if ((financial_details.List.size() <= Constants.PAGESIZE)) {
			((LoadMoreListView) getListView()).setOnLoadMoreListener(null);
			((LoadMoreListView) getListView()).onLoadMoreComplete();
		} else {
			((LoadMoreListView) getListView())
					.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
						public void onLoadMore() {
							// Do the work to load more items at the end of list
							// here
							page++;
							Log.e(TAG, "page Index--" + page);
							CallMyInvestmentWebservice_Loadmore("", "" + page);
						}
					});
		}

	}

	public void CallMyInvestmentWebservice_Loadmore(String ftype, String page) {

		dft.getRepaidDetails(ftype, page,
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AppController.listActivity.remove(this);
	}
}
