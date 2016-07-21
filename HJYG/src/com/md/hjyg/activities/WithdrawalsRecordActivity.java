 package com.md.hjyg.activities;

import java.util.ArrayList;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.WithdrawalsFinancialDetailsAdapter;
import com.md.hjyg.entity.WithdrawalsList;
import com.md.hjyg.entity.WithdrawalsRecodes;
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
 * 提现记录
 */
public class WithdrawalsRecordActivity extends BaseListActivity implements
		OnClickListener, AdapterView.OnItemClickListener ,OnLoadMoreListener{

	// UI variables
	//
//	private TextView tv_title;
//	private LinearLayout lay_back_investment;
	// ProgressDialog
//	private ProgressDialog pDialog;

	private Context mcontext;
	private DataFetchService dft;
	private WithdrawalsRecodes withdrawalsDetails;
	// Adapter
	private WithdrawalsFinancialDetailsAdapter financial_details_adapter;

//	private boolean isLoading = false;
//	private boolean noload = false;
	private int page = 1;
//	private String AuthToken = "d9bd67b2-b925-43a2-bf2d-b1c450fae10b";
	private String Loadmore = "";
//	private String Method_name = "MemberServiceApi/WithdrawLogsLogs";
	
//	private static final String TAG = "WithdrawActivity";

	private ArrayList<WithdrawalsList> load_MoreList = new ArrayList<WithdrawalsList>();

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

	public void init() {
//		pDialog = new ProgressDialog(this);
//		pDialog.setMessage("Loading...");
//		pDialog.setCancelable(false);
		dft = new DataFetchService(this);
		withdrawalsDetails = new WithdrawalsRecodes();
	}

	public void findViews() {
		this.getListView().setOnItemClickListener(this);

	}

	public void CallFinancialDetailsWebservice(String ftype, String start,
			String end, String page) {

		dft.getWithdrawalRecordDetails(ftype, start, end, page, Constants.WithdrawLogsLogs_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						withdrawalsDetails = (WithdrawalsRecodes) dft
								.GetResponseObject(response,
										WithdrawalsRecodes.class);

						SetUIData("");
						// homeApi = (HomeApiModel)
						// dft.GetResponseObject(response,HomeApiModel.class);
//						Log.d(TAG, "----" + response.toString());

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

			if (withdrawalsDetails.WithdrawLogsList.size() < 10 ) {
				((LoadMoreListView) getListView()).onLoadMoreComplete();
				if(withdrawalsDetails.WithdrawLogsList.size()== 0)
				{
					((LoadMoreListView) getListView()).noDataLoad("暂无数据");
				}
			}
			// Set adaptor
			load_MoreList = withdrawalsDetails.WithdrawLogsList;
			financial_details_adapter = new WithdrawalsFinancialDetailsAdapter(mcontext,
					load_MoreList);
			// list_projects.setAdapter(investment_projects_adapter);
			setListAdapter(financial_details_adapter);
		} else {

			if (!withdrawalsDetails.WithdrawLogsList.isEmpty()) {
				load_MoreList.addAll(withdrawalsDetails.WithdrawLogsList);
				financial_details_adapter.notifyDataSetChanged();
				((LoadMoreListView) getListView()).onLoadMoreComplete();
//				isLoading = false;
			} else {
				((LoadMoreListView) getListView()).onLoadMoreComplete();
			}

		}
		//一次性加载完成了，不需要在加载更多
		((LoadMoreListView) getListView()).onLoadMoreComplete();
//		if((withdrawalsDetails.WithdrawLogsList.size() <= Constants.PAGESIZE))
//        {	
//			((LoadMoreListView) getListView()).setOnLoadMoreListener(null);
//			((LoadMoreListView) getListView()).onLoadMoreComplete();
//        }else {
//        	((LoadMoreListView) getListView()).setOnLoadMoreListener(this);
//		}
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
			String start, String end, String pageIndex) {

		// init_footer(getBaseContext());
		dft.getWithdrawalRecordDetails(ftype, start, end, pageIndex,
				Constants.WithdrawLogsLogs_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						withdrawalsDetails = (WithdrawalsRecodes) dft
								.GetResponseObject(response,
										WithdrawalsRecodes.class);
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

//	private void hideProgressDialog() {
//		if (pDialog.isShowing())
//			pDialog.hide();
//	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
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
