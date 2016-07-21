package com.md.hjyg.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.BuyHistoryListAdapter;
import com.md.hjyg.entity.SingleLoanSelfInvestListModel;
import com.md.hjyg.entity.SingleLoanSelfInvestModel;
import com.md.hjyg.utility.LoadMoreListView;
import com.md.hjyg.utility.LoadMoreListView.OnLoadMoreListener;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

/** 
 * ClassName: BuyHistoryActivity 
 * date: 2015-11-14 上午9:02:45 
 * remark:购买记录
 * @author pyc
 */
public class BuyHistoryActivity extends BaseActivity implements OnLoadMoreListener{
	
	TextView tv_titleremark, tv_titleamount, tv_titledate;
	LinearLayout details;
	BuyHistoryListAdapter maAdapter;
	Context mcontext;

	// ListView
	LoadMoreListView list_transactions;


	public ProgressDialog pDialog;
    int status;
    String msg;
	int page = 1;

	boolean isLoading = false;
	SingleLoanSelfInvestListModel singleLoanSelfInvestListModel ;
	public List<SingleLoanSelfInvestModel> lists = new ArrayList<SingleLoanSelfInvestModel>();
//	public String MethodName = "SingleLoanApi/MemberPurchaseList";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_purchasehistory_tab);
		mcontext = getBaseContext();
		findViews();
		init();
		GetWebserviceTransactionDetailsAPI(page);

	}

	private void init() {

		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);

	}

	private void findViews() {

		list_transactions = (LoadMoreListView) findViewById(R.id.list_transactions);

		tv_titleremark = (TextView) findViewById(R.id.tv_titleremark);
		tv_titleamount = (TextView) findViewById(R.id.tv_titleamount);
		tv_titledate = (TextView) findViewById(R.id.tv_titledate);
		details = (LinearLayout) findViewById(R.id.details);

	}

	/**获取用户本身的交易信息列表*/
	public void GetWebserviceTransactionDetailsAPI( final int page) {

		dft.postSingleLoanSelfInfoList(page, Constants.MemberPurchaseList_URL, Request.Method.POST, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				singleLoanSelfInvestListModel = (SingleLoanSelfInvestListModel) dft
						.GetResponseObject(response,
								SingleLoanSelfInvestListModel.class);
				status = singleLoanSelfInvestListModel.notification.ProcessResult;
				msg = singleLoanSelfInvestListModel.notification.ProcessMessage;
				if (status == 1) {
					if (page == 1) {
						setData("");
					}else {
						setData("Loadmore");
					}
				}else {
					Constants.showOkPopup(BuyHistoryActivity.this, msg);
//					Constants.showOkPopup(BuyHistoryActivity.this, msg, new DialogInterface.OnClickListener() {
//						
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
//						}
//					});
				}
			}

		});
	}

	public void setData(String Loadmore) {

		if (Loadmore.isEmpty()) {

			if (singleLoanSelfInvestListModel.list.size() < 10) {
				list_transactions.onLoadMoreComplete();
				if(singleLoanSelfInvestListModel.list.size()== 0)
				{
					list_transactions.noDataLoad("暂无数据");
					details.setVisibility(View.GONE);
				}
			}
			// Set adaptor
			lists = singleLoanSelfInvestListModel.list;
			maAdapter = new BuyHistoryListAdapter(this, lists);
			list_transactions.setAdapter(maAdapter);
			
		} else {
			if (!singleLoanSelfInvestListModel.list.isEmpty()) {
				lists.addAll(singleLoanSelfInvestListModel.list);
				maAdapter.notifyDataSetChanged();
				list_transactions.onLoadMoreComplete();
				isLoading = false;
			} else {
				list_transactions.onLoadMoreComplete();
			}
		}
		if((singleLoanSelfInvestListModel.list.size() <= Constants.PAGESIZE))
        {	
			list_transactions.setOnLoadMoreListener(null);
			list_transactions.onLoadMoreComplete();
        }else {
        	list_transactions.setOnLoadMoreListener(this);
		}

	}

	public void showProgressDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	public void onLoadMore() {
		
		page++;
		GetWebserviceTransactionDetailsAPI( page);
		
	}

}
