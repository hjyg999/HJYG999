package com.md.hjyg.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.WithdrawHistoryListAdapter;
import com.md.hjyg.entity.SingleLoanWithdrawListModel;
import com.md.hjyg.entity.SingleLoanWithdrawModel;
import com.md.hjyg.utility.LoadMoreListView;
import com.md.hjyg.utility.LoadMoreListView.OnLoadMoreListener;
import com.md.hjyg.xinTouConstant.Constants;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;

/** 
 * ClassName: WithdrawHistoryActivity 
 * date: 2015-11-14 上午11:00:09 
 * remark:活期宝提取记录
 * @author pyc
 */
public class WithdrawHistoryActivity extends BaseActivity implements OnLoadMoreListener{
	
//	private TextView tv_titleremark, tv_titleamount, tv_titledate;
	private LinearLayout details;
	private WithdrawHistoryListAdapter maAdapter;
//	private Context mcontext;
	// ListView
	private LoadMoreListView list_transactions;
	private int status;
	private String msg;
	private int page = 1;

//	private boolean isLoading = false;
	private SingleLoanWithdrawListModel singleLoanWithdrawListModel;
	private List<SingleLoanWithdrawModel> lists = new ArrayList<SingleLoanWithdrawModel>();
//	private String MethodName = "SingleLoanApi/MemberWithdrawList";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_purchasehistory_tab);
//		mcontext = getBaseContext();
		findViews();
		GetWebserviceTransactionDetailsAPI(1);
	}

	private void findViews() {

		list_transactions = (LoadMoreListView) findViewById(R.id.list_transactions);

//		tv_titleremark = (TextView) findViewById(R.id.tv_titleremark);
//		tv_titleamount = (TextView) findViewById(R.id.tv_titleamount);
//		tv_titledate = (TextView) findViewById(R.id.tv_titledate);
		details = (LinearLayout) findViewById(R.id.details);

	}

	/**获取用户本身的交易信息列表*/
	public void GetWebserviceTransactionDetailsAPI( final int page) {

		dft.postSingleLoanSelfInfoList(page, Constants.HuoQiBaoMemberWithdrawList_URL, Request.Method.POST, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				singleLoanWithdrawListModel = (SingleLoanWithdrawListModel) dft
						.GetResponseObject(response,
								SingleLoanWithdrawListModel.class);
				status = singleLoanWithdrawListModel.notification.ProcessResult;
				msg = singleLoanWithdrawListModel.notification.ProcessMessage;
				if (status == 1) {
					if (page == 1) {
						setData("");
					}else {
						setData("Loadmore");
					}
				}else {
					Constants.showOkPopup(WithdrawHistoryActivity.this, msg);
//					Constants.showOkPopup(WithdrawHistoryActivity.this, msg, new DialogInterface.OnClickListener() {
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

			if (singleLoanWithdrawListModel.list.size() < 10) {
				list_transactions.onLoadMoreComplete();
				if(singleLoanWithdrawListModel.list.size()== 0)
				{
					list_transactions.noDataLoad("暂无数据");
					details.setVisibility(View.GONE);
				}
			}
			// Set adaptor
			lists = singleLoanWithdrawListModel.list;
			maAdapter = new WithdrawHistoryListAdapter(this, lists);
			list_transactions.setAdapter(maAdapter);
			
		} else {
			if (!singleLoanWithdrawListModel.list.isEmpty()) {
				lists.addAll(singleLoanWithdrawListModel.list);
				maAdapter.notifyDataSetChanged();
				list_transactions.onLoadMoreComplete();
//				isLoading = false;
			} else {
				list_transactions.onLoadMoreComplete();
			}
		}
		if((singleLoanWithdrawListModel.list.size() <= Constants.PAGESIZE))
        {	
			list_transactions.setOnLoadMoreListener(null);
			list_transactions.onLoadMoreComplete();
        }else {
        	list_transactions.setOnLoadMoreListener(this);
		}

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
