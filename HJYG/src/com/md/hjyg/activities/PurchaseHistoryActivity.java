package com.md.hjyg.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.PurchaseHistoryListAdapter;
import com.md.hjyg.entity.SingleLoanInterestListModel;
import com.md.hjyg.entity.SingleLoanInterestModel;
import com.md.hjyg.utility.LoadMoreListView;
import com.md.hjyg.utility.LoadMoreListView.OnLoadMoreListener;
import com.md.hjyg.xinTouConstant.Constants;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * ClassName: PurchaseHistoryActivity date: 2015-11-11 上午10:57:26 remark: 收益记录
 * 
 * @author pyc
 */
public class PurchaseHistoryActivity extends BaseActivity implements
		OnLoadMoreListener {

	// private TextView tv_titleremark, tv_titleamount, tv_titledate;
	private LinearLayout details;
	private PurchaseHistoryListAdapter maAdapter;
	// private Context mcontext;
	// ListView
	private LoadMoreListView list_transactions;
	private int status;
	private String msg;
	private int page = 1;

	// private boolean isLoading = false;
	private SingleLoanInterestListModel singleLoanInterestListModel;
	private List<SingleLoanInterestModel> lists = new ArrayList<SingleLoanInterestModel>();

	// private String MethodName = "SingleLoanApi/MemberInterestList";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_purchasehistory_tab);
		// mcontext = getBaseContext();
		findViews();
		GetWebserviceTransactionDetailsAPI(1);

	}

	private void findViews() {

		list_transactions = (LoadMoreListView) findViewById(R.id.list_transactions);

		// tv_titleremark = (TextView) findViewById(R.id.tv_titleremark);
		// tv_titleamount = (TextView) findViewById(R.id.tv_titleamount);
		// tv_titledate = (TextView) findViewById(R.id.tv_titledate);
		details = (LinearLayout) findViewById(R.id.details);

	}

	/** 获取用户本身的交易信息列表 */
	public void GetWebserviceTransactionDetailsAPI(final int page) {

		dft.postSingleLoanSelfInfoList(page, Constants.MemberInterestList_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						singleLoanInterestListModel = (SingleLoanInterestListModel) dft
								.GetResponseObject(response,
										SingleLoanInterestListModel.class);
						status = singleLoanInterestListModel.notification.ProcessResult;
						msg = singleLoanInterestListModel.notification.ProcessMessage;
						if (status == 1) {
							if (page == 1) {
								setData("");
							} else {
								setData("Loadmore");
							}
						} else {
							Constants.showOkPopup(PurchaseHistoryActivity.this,
									msg);
							// Constants.showOkPopup(PurchaseHistoryActivity.this,
							// msg, new DialogInterface.OnClickListener() {
							//
							// @Override
							// public void onClick(DialogInterface dialog, int
							// which) {
							// dialog.dismiss();
							// }
							// });
						}
					}

				});
	}

	public void setData(String Loadmore) {

		if (Loadmore.isEmpty()) {

			if (singleLoanInterestListModel.list.size() < 10) {
				list_transactions.onLoadMoreComplete();
				if (singleLoanInterestListModel.list.size() == 0) {
					list_transactions.noDataLoad("暂无数据");
					details.setVisibility(View.GONE);
				}
			}
			// Set adaptor
			lists = singleLoanInterestListModel.list;
			maAdapter = new PurchaseHistoryListAdapter(this, lists);
			list_transactions.setAdapter(maAdapter);

		} else {
			if (!singleLoanInterestListModel.list.isEmpty()) {
				lists.addAll(singleLoanInterestListModel.list);
				maAdapter.notifyDataSetChanged();
				list_transactions.onLoadMoreComplete();
				// isLoading = false;
			} else {
				list_transactions.onLoadMoreComplete();
			}
		}
		if ((singleLoanInterestListModel.list.size() <= Constants.PAGESIZE)) {
			list_transactions.setOnLoadMoreListener(null);
			list_transactions.onLoadMoreComplete();
		} else {
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
		GetWebserviceTransactionDetailsAPI(page);

	}
}
