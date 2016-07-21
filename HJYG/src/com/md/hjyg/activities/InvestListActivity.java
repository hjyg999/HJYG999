package com.md.hjyg.activities;

import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.InvestmentProjectsListAdapter;
import com.md.hjyg.entity.LoanApiModel;
import com.md.hjyg.entity.LoanModel;
import com.md.hjyg.layoutEntities.HeaderView;
import com.md.hjyg.layoutEntities.RefreshableView;
import com.md.hjyg.layoutEntities.RefreshableView.PullToRefreshListener;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.LoadMoreListView;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/**
 * ClassName: InvestListActivity date: 2016-3-21 上午11:46:26 remark:理财列表-新版的
 * 
 * @author pyc
 */
public class InvestListActivity extends BaseActivity implements OnClickListener {

	private HeaderView mheadView;
	private DialogProgressManager myProgressDialog;
	private Context context;

	private LoanApiModel loanApiModel;
	private RefreshableView refreshableView;

	private LoadMoreListView mLoadMoreListView;
	private InvestmentProjectsListAdapter adapter;
	private List<LoanModel> lists;
	private int pageIndex = 1;
	private int pageSize = 10;
	private int type = 0;
	private boolean isPull = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_investlist_layout);
		context = getBaseContext();
		findView();
		myProgressDialog = new DialogProgressManager(this, "努力加载中...");
		myProgressDialog.initDialog();
		CallLoanListWebservice(type, pageIndex, pageSize);
	}

	private void findView() {
		mheadView = (HeaderView) findViewById(R.id.mheadView);
		mheadView.setUIData("定投宝", this);

		mLoadMoreListView = (LoadMoreListView) findViewById(R.id.mLoadMoreListView);
		mLoadMoreListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					// Method name --"LoanApi/Details/EncrytedID!"
					Intent investmentdetails = new Intent(context,
							InvestmentDetailsNewActivity.class);
					investmentdetails.putExtra("EncrytedID",
							lists.get(position).EncryptedId.toString());
					investmentdetails.putExtra("LoanTitle",
							lists.get(position).Title.toString());
					startActivity(investmentdetails);
					InvestListActivity.this.overTransition(2);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {

			@Override
			public void onRefresh() {
				pageIndex = 2;
				isPull = true;
				CallLoanListWebservice(type, pageIndex, pageSize);
			}
		}, 1);
	}

	/***
	 * 获取项目列表
	 * 
	 * @param pageIndex
	 * @param pageSize
	 */
	private void CallLoanListWebservice(int type, final int pageIndex,
			int pageSize) {
		dft.GetIndexOfPage(type, pageIndex, pageSize,
				Constants.GetIndexOfPage_URL, Request.Method.POST,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						myProgressDialog.dismiss();

						loanApiModel = (LoanApiModel) dft.GetResponseObject(
								response, LoanApiModel.class);
						if (loanApiModel == null || loanApiModel.LoanList == null) {

							Toast.makeText(context, "服务器繁忙，请稍后再试!",
									Toast.LENGTH_LONG).show();
						} else {

							if (pageIndex == 1) {

								SetUIData("");
							} else {
								SetUIData("loadmore");
							}

						}
						try {

						} catch (Exception e) {
							Toast.makeText(context,
									getString(R.string.data_error),
									Toast.LENGTH_SHORT).show();
						}

					}
				});
	}

	private void SetUIData(String Loadmore) {

		if (Loadmore.isEmpty()) {
			// Set adaptor
			lists = loanApiModel.LoanList;
			// timeisStaet();
			adapter = new InvestmentProjectsListAdapter(lists, context);
			mLoadMoreListView.setAdapter(adapter);

			
		} else {
			if (!loanApiModel.LoanList.isEmpty()) {
				lists.addAll(loanApiModel.LoanList);
				// timeisStaet();
				adapter.notifyDataSetChanged();
				mLoadMoreListView.onLoadMoreComplete();
			} else {
				mLoadMoreListView.onLoadMoreComplete();
			}
		}
		if (isPull) {
			refreshableView.finishRefreshing();
			isPull = false;
		}

		mLoadMoreListView
				.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
					public void onLoadMore() {
						// Do the work to load more items at the end of list
						// here
						pageIndex++;
						if (pageIndex < 6) {
							CallLoanListWebservice(type, pageIndex, pageSize);
						} else {
							mLoadMoreListView.onLoadMoreComplete();
						}
					}
				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_top_left:
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
