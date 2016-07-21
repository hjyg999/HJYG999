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
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 充值记录
 */
public class RechargeRecordActivity extends BaseListActivity implements
		View.OnClickListener, AdapterView.OnItemClickListener ,OnLoadMoreListener{

	// UI variables
//	private TextView tv_title;
//	private LinearLayout lay_back_investment;

	// ProgressDialog
	private ProgressDialog pDialog;

	private Context mcontext;
	private DataFetchService dft;
	private FinancialDetails financialDetails;
	// Adapter
	private FinancialDetailsAdapter financial_details_adapter;
	//RechargeFinancialDetailsAdapter rechargeFinancialDetailsAdapter;

//	private boolean isLoading = false;
//	private boolean noload = false;
	//, value = 0
	private int page = 1;
//	private String AuthToken = "d9bd67b2-b925-43a2-bf2d-b1c450fae10b";
	private String Loadmore = "";
//	public String Method_name = "MemberServiceApi/GetFinanceLogs";
//	private String Method_name = "MemberServiceApi/GetRechargeLogs";
//	private static final String TAG = "WholeActivity";

	private ArrayList<ListModel> load_MoreList = new ArrayList<ListModel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_whole_records);
		AppController.listActivity.add(this);
		mcontext = getBaseContext();
		findViews();
		init();
		CallFinancialDetailsWebservice("", "", "", "" + page);
	}

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

//	private void showProgressDialog() {
//		if (!pDialog.isShowing())
//			pDialog.show();
//	}

	public void CallFinancialDetailsWebservice(String ftype, String start,
			String end, String page) {

		dft.getRechargeRecordDetails(ftype, start, end, page, Constants.GetRechargeLogs_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						financialDetails = (FinancialDetails) dft
								.GetResponseObject(response,
										FinancialDetails.class);

						SetUIData("");

					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Show error or whatever...
					}
				});

	}

	public void hideProgressDialog() {
		if (pDialog.isShowing())
			pDialog.hide();
	}

	public void SetUIData(String Loadmore) {

		if (Loadmore.isEmpty()) {

			if (financialDetails.List.size() < 10) {
				((LoadMoreListView) getListView()).onLoadMoreComplete();
				if(financialDetails.List.size()== 0)
				{
					((LoadMoreListView) getListView()).noDataLoad("暂无数据");
				}
			}
			// Set adaptor
			load_MoreList = financialDetails.List;
			financial_details_adapter = new FinancialDetailsAdapter(
					mcontext, load_MoreList,2);
			setListAdapter(financial_details_adapter);
		} else {
			if (!financialDetails.List.isEmpty()) {
				load_MoreList.addAll(financialDetails.List);
				financial_details_adapter.notifyDataSetChanged();
				((LoadMoreListView) getListView()).onLoadMoreComplete();
//				isLoading = false;
			} else {
				((LoadMoreListView) getListView()).onLoadMoreComplete();
			}
		}
		
		if((financialDetails.List.size()  <= Constants.PAGESIZE))
        {	
			((LoadMoreListView) getListView()).setOnLoadMoreListener(null);
			((LoadMoreListView) getListView()).onLoadMoreComplete();
        }else {
        	((LoadMoreListView) getListView()).setOnLoadMoreListener(this);
		}

//		((LoadMoreListView) getListView())
//				.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
//					public void onLoadMore() {
//						// Do the work to load more items at the end of list
//						// here
//						page++;
//						Log.e(TAG, "page Index--" + page);
//						CallFinancialDetailsWebservice_Loadmore("", "", "", ""
//								+ page);
//					}
//				});

	}

	public void CallFinancialDetailsWebservice_Loadmore(String ftype,
			String start, String end, String page) {

		dft.getRechargeRecordDetails(ftype, start, end, page, Constants.GetRechargeLogs_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						financialDetails = (FinancialDetails) dft
								.GetResponseObject(response,
										FinancialDetails.class);
						Loadmore = "loadmore";
						SetUIData(Loadmore);
						// hideProgressDialog();
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
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AppController.listActivity.remove(this);
	}

	@Override
	public void onLoadMore() {
		page++;
		
		CallFinancialDetailsWebservice_Loadmore("", "", "", ""
				+ page);
		
	}
}
