package com.md.hjyg.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.HqbTyjListAdapter;
import com.md.hjyg.entity.SingleLoanInterestListModel;
import com.md.hjyg.entity.SingleLoanInterestModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.LoadMoreListView;
import com.md.hjyg.utility.LoadMoreListView.OnLoadMoreListener;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * ClassName: HuoQibaoExperiencePurchaseActivity date: 2015-12-18 下午3:40:00
 * remark:活期宝体验金-收益记录
 * 
 * @author pyc
 */
public class HuoQibaoExperiencePurchaseActivity extends BaseActivity implements
		OnClickListener, OnLoadMoreListener {
	private Activity mActivity;
	private LoadMoreListView mListView;
	private HqbTyjListAdapter adapter;
	private int page = 1;
	private SingleLoanInterestListModel Model;
	private List<SingleLoanInterestModel> lists = new ArrayList<SingleLoanInterestModel>();
	private int status;
	private String msg;
	private RelativeLayout details;
	private DialogManager mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_huoqibaoexperiencepurchase_activity);

		findViewAndInit();
		getWebservice(page);
	}

	private void findViewAndInit() {
		mActivity = this;
		HeaderViewControler.setHeaderView(this, "体验金收益记录", this);
		mDialog = new DialogManager(this);
		details = (RelativeLayout) findViewById(R.id.details);

		mListView = (LoadMoreListView) findViewById(R.id.mListView);

	}

	/** 获取活期宝体验金资金记录详情 */
	private void getWebservice(final int page) {

		dft.postSingleLoanSelfInfoList(page,
				Constants.MemberExperienceInterestList_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Model = (SingleLoanInterestListModel) dft
								.GetResponseObject(response,
										SingleLoanInterestListModel.class);
						status = Model.notification.ProcessResult;
						msg = Model.notification.ProcessMessage;
						if (status == 1) {
							if (page == 1) {
								setData("");
							} else {
								setData("Loadmore");
							}
						} else {
							mDialog.initOneBtnDialog(false, msg, null);
						}
					}

				});

	}

	private void setData(String Loadmore) {

		if (Loadmore.isEmpty()) {

			if (Model.list.size() < 10) {
				mListView.onLoadMoreComplete();
				if (Model.list.size() == 0) {
					mListView.noDataLoad("暂无数据");
					details.setVisibility(View.GONE);
				}
			}
			lists = Model.list;
			adapter = new HqbTyjListAdapter( lists,this);
			mListView.setAdapter(adapter);

		} else {
			if (!Model.list.isEmpty()) {
				lists.addAll(Model.list);
				adapter.notifyDataSetChanged();
				mListView.onLoadMoreComplete();
			} else {
				mListView.onLoadMoreComplete();
			}
		}
		if ((Model.list.size() <= Constants.PAGESIZE)) {
			mListView.setOnLoadMoreListener(null);
			mListView.onLoadMoreComplete();
		} else {
			mListView.setOnLoadMoreListener(this);
		}

	}

	@Override
	public void onLoadMore() {

		page++;
		getWebservice(page);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			mActivity.finish();
			overTransition(1);
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		mActivity.finish();
		overTransition(1);
	}

}
