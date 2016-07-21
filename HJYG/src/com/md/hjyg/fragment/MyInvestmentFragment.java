package com.md.hjyg.fragment;

import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.activities.BaseFragmentActivity;
import com.md.hjyg.adapter.MyInvestListAdapter;
import com.md.hjyg.adapter.MyInvestListAdapter.OnisExpandListener;
import com.md.hjyg.adapter.OvertimeListAdapter;
import com.md.hjyg.entity.GetInvestmentListModel;
import com.md.hjyg.entity.MyInvestmentIncomeModel;
import com.md.hjyg.entity.MyInvestmentModel;
import com.md.hjyg.entity.OverTimeFinLogDetailListModel;
import com.md.hjyg.entity.OverTimeFinLogDetailListModel.OverTimeFinLogDetailModel;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.LoadMoreListView;
import com.md.hjyg.utility.LoadMoreListView.OnLoadMoreListener;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * ClassName: MyInvestmentFragment date: 2016-4-20 上午10:30:11 remark:我的投资
 * 
 * @author pyc
 */
public class MyInvestmentFragment extends BaseFragment implements
		OnLoadMoreListener, DftErrorListener, OnisExpandListener {

	private LoadMoreListView mLoadMoreListView;
	private LinearLayout lin_nodata;
	private final static int startPage = 0;
	private int pageIndex = startPage;
	private DataFetchService dft;
	private MyInvestListAdapter adapter;
	private List<GetInvestmentListModel> lists;
	private int tab;
	private String method_names = Constants.GetNewRepaymentListReceivedByPage_URL;
	private DialogProgressManager dialog;
	private int ftype;
	private int lastPosition,firstPosition,listViewHight;
	private int  top = 0,bottom=0,tolHight = 0;

//	public MyInvestmentFragment() {
//		this(0);
//	}

	public MyInvestmentFragment(int tab) {
		this.tab = tab;
		this.ftype = tab;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_myinvestment_layout,
				container, false);
		findViewandInit(v);
		dialog.initDialog();
		getWebData(ftype, pageIndex);
		return v;
	}

	private void findViewandInit(View v) {
		dft = ((BaseFragmentActivity) getActivity()).getDft();
		dialog = new DialogProgressManager(getActivity(), "努力加载中...");
		dft.setOnDftErrorListener(this);

		lin_nodata = (LinearLayout) v.findViewById(R.id.lin_nodata);
		mLoadMoreListView = (LoadMoreListView) v.findViewById(R.id.mListView);
	}

	/**
	 * 获取网络数据
	 * 
	 * @param pageIndex
	 */
	private void getWebData(int ftype, final int pageIndex) {
		dft.getMyInvestmentInfo(ftype, pageIndex, method_names,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						dialog.dismiss();

						MyInvestmentModel myInvestmentModel = (MyInvestmentModel) dft
								.GetResponseObject(response,
										MyInvestmentModel.class);
						if (pageIndex == startPage) {
							setData(false, myInvestmentModel.List);
						} else {
							setData(true, myInvestmentModel.List);
						}

					}

				}, null);

	}

	private void setData(boolean isLoadmore, List<GetInvestmentListModel> lists) {
		if (!isLoadmore) {
			if (lists == null || lists.size() == 0) {
				lin_nodata.setVisibility(View.VISIBLE);
				mLoadMoreListView.setVisibility(View.GONE);
			} else {
				lin_nodata.setVisibility(View.GONE);
				mLoadMoreListView.setVisibility(View.VISIBLE);
				this.lists = lists;
				adapter = new MyInvestListAdapter(lists, getActivity(), tab);
				adapter.setOnisExpandListener(this);
				mLoadMoreListView.setAdapter(adapter);
			}
		} else {
			if (lists != null && lists.size() > 0) {
				this.lists.addAll(lists);
				adapter.notifyDataSetChanged();
			}
		}

		if (lists == null || (lists.size() <= Constants.PAGESIZE)) {
			mLoadMoreListView.setOnLoadMoreListener(null);
		} else {
			mLoadMoreListView.setOnLoadMoreListener(this);
		}
		mLoadMoreListView.onLoadMoreComplete();
	}

	@Override
	public void onLoadMore() {
		pageIndex++;
		getWebData(ftype, pageIndex);
	}

	@Override
	public void webLoadError() {
		dialog.dismiss();
	}

	@Override
	public void getMemberInvestDetail(final int p) {
		lastPosition = mLoadMoreListView.getLastVisiblePosition();
		firstPosition = mLoadMoreListView.getFirstVisiblePosition();
		listViewHight = mLoadMoreListView.getHeight();
		tolHight = 0;
		View v;
		int mtop = 0;
		bottom=0;
		for (int i = 0; i < mLoadMoreListView.getChildCount(); i++) {
			v = mLoadMoreListView.getChildAt(i);
			if (i== 0) {
				mtop = (v == null) ? 0 : v.getTop();
				top = mtop;
			}else {
				mtop = 0;
			}
			if (i== mLoadMoreListView.getChildCount() - 1) {
				bottom = (v == null) ? 0 : v.getBottom();
			}
			tolHight = v.getHeight() + mtop + tolHight;
		}
		if (tolHight > listViewHight) {
			bottom = tolHight - listViewHight;
			tolHight = listViewHight;
		}
		
		GetInvestmentListModel listModel = lists.get(p);
		if (listModel.getListDetail() != null
				&& listModel.getListDetail().size() > 0) {
			lists.get(p).setExpand(!lists.get(p).isExpand());
			adapter.notifyDataSetChanged();
			return;
		}
		dialog.initDialog();
		/**
		 * 获取还款详情信息
		 */
		dft.GetMemberInvestDetail(lists.get(p).Id,
				Constants.GetMemberInvestDetail_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						MyInvestmentIncomeModel detailModel = (MyInvestmentIncomeModel) dft
								.GetResponseObject(response,
										MyInvestmentIncomeModel.class);
						lists.get(p).setListDetail(detailModel.List);
						lists.get(p).setExpand(!lists.get(p).isExpand());
						adapter.notifyDataSetChanged();
						dialog.dismiss();

					}
				}, null);

	}
	
	@Override
	public void listViewScrollBy(int p, final int y) {
		if( lastPosition == p){
			
			if (tolHight + y > listViewHight) {
				mLoadMoreListView.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						mLoadMoreListView.setSelectionFromTop(firstPosition,top-y-bottom);
						
					}
				});
			}
			
		}else if (lastPosition- 1 == p) {
			mLoadMoreListView.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mLoadMoreListView.setSelectionFromTop(firstPosition,top-y);
					
				}
			});
		}
	}

	@Override
	public void showDialg(int InvestID) {
		dft.GetMemberInvestDetail(InvestID,
				Constants.GetMemberInvestDetail_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						OverTimeFinLogDetailListModel OverTimeModel = (OverTimeFinLogDetailListModel) dft
								.GetResponseObject(response,
										OverTimeFinLogDetailListModel.class);
						showListDialog(OverTimeModel.List);
					}
				}, null);
	}

	@SuppressLint("InflateParams")
	private void showListDialog(List<OverTimeFinLogDetailModel> list) {
		Dialog mDialog = new Dialog(getActivity(), R.style.m_dialogstyle);
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.dialog_myinvestment, null);

		ListView mListView = (ListView) view.findViewById(R.id.mListView);
		OvertimeListAdapter adapter = new OvertimeListAdapter(list,
				getActivity());
		mListView.setAdapter(adapter);

		mDialog.setContentView(view);

		Window dialogWindow = mDialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = (int) (ScreenUtils.getScreenWidth(getActivity()) * 0.8);
		dialogWindow.setAttributes(lp);
		if (!getActivity().isFinishing() && mDialog != null) {
			try {// 抓捕异常，防止程序崩溃
				mDialog.show();
			} catch (Exception e) {
			}
		}
	}
	
}
