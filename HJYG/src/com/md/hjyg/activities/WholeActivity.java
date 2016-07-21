package com.md.hjyg.activities;

import java.util.ArrayList;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.FinancialDetailsAdapter;
import com.md.hjyg.entity.FinancialDetails;
import com.md.hjyg.entity.ListModel;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.LoadMoreListView;
import com.md.hjyg.utility.LoadMoreListView.OnLoadMoreListener;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 全部资金记录
 */
public class WholeActivity extends BaseListActivity implements OnClickListener,
		OnItemClickListener, OnLoadMoreListener {
	private ProgressDialog pDialog;

	private Context mcontext;
	private DataFetchService dft;
	private FinancialDetails financialDetails;
	// Adapter
	private FinancialDetailsAdapter financial_details_adapter;

	// private boolean isLoading = false;
	// private boolean noload = false;
	private int page = 1;
	// private String AuthToken /* = "8fde6b9f-d4d6-415c-b270-db99400d7971" */;
	private String Loadmore = "";
	// private String Method_name = "MemberServiceApi/GetFinanceLogs";
	// private static final String TAG = "WholeActivity";

	private ArrayList<ListModel> load_MoreList = new ArrayList<ListModel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_whole_records);
		// setContentView(R.layout.layout_xintou_projects_list);
		AppController.listActivity.add(this);
		mcontext = getBaseContext();
		init();
		findViews();
		CallFinancialDetailsWebservice("", "", "", "" + page);

		// AuthToken = Constants.GetResult_AuthToken(mcontext);

	}

	// private void showProgressDialog() {
	// if (!pDialog.isShowing())
	// pDialog.show();
	// }

	private void init() {
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);

		dft = new DataFetchService(this);
		financialDetails = new FinancialDetails();

	}

	private void findViews() {
		this.getListView().setOnItemClickListener(this);

	}

	public void CallFinancialDetailsWebservice(String ftype, String start,
			String end, String page) {

		dft.getWholeListDetails(ftype, start, end, page,
				Constants.GetFinanceLogs_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						financialDetails = (FinancialDetails) dft
								.GetResponseObject(response,
										FinancialDetails.class);

						SetUIData("");
						// homeApi = (HomeApiModel)
						// dft.GetResponseObject(response,HomeApiModel.class);
						// Log.d(TAG, "---WHOLE-" + response.toString());
						// Log.d(TAG, "----" +
						// financialDetails.List.get(0).Amount);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Show error or whatever...
					}
				});

	}

	public void CallFinancialDetailsWebservice_Loadmore(String ftype,
			String start, String end, String pageIndex) {

		dft.getWholeListDetails(ftype, start, end, pageIndex,
				Constants.GetFinanceLogs_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						financialDetails = (FinancialDetails) dft
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

	public void SetUIData(String Loadmore) {

		if (Loadmore.isEmpty()) {

			if (financialDetails.List.size() < 10) {
				((LoadMoreListView) getListView()).onLoadMoreComplete();
				if (financialDetails.List.size() == 0) {
					((LoadMoreListView) getListView()).noDataLoad("暂无数据");
				}
			}
			// Set adaptor
			load_MoreList = financialDetails.List;
			financial_details_adapter = new FinancialDetailsAdapter(mcontext,
					load_MoreList, 1);
			// list_projects.setAdapter(investment_projects_adapter);
			setListAdapter(financial_details_adapter);
		} else {
			if (!financialDetails.List.isEmpty()) {
				load_MoreList.addAll(financialDetails.List);
				financial_details_adapter.notifyDataSetChanged();
				((LoadMoreListView) getListView()).onLoadMoreComplete();
				// isLoading = false;
			} else {
				((LoadMoreListView) getListView()).onLoadMoreComplete();
			}
		}

		if ((financialDetails.List.size() <= Constants.PAGESIZE)) {
			((LoadMoreListView) getListView()).setOnLoadMoreListener(null);
			((LoadMoreListView) getListView()).onLoadMoreComplete();
		} else {
			((LoadMoreListView) getListView()).setOnLoadMoreListener(this);
		}
	}

	// private void hideProgressDialog() {
	// if (pDialog.isShowing())
	// pDialog.hide();
	// }

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	public void onLoadMore() {
		page++;
		CallFinancialDetailsWebservice_Loadmore("", "", "", "" + page);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AppController.listActivity.remove(this);
	}
}
