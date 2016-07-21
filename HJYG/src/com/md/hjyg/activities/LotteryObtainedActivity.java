package com.md.hjyg.activities;

import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.LotteryObtainedAdapter;
import com.md.hjyg.entity.AnnualMeetingLotteryInfoModel.MemberLotteryLogModel;
import com.md.hjyg.entity.MyRewardinfoModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.utility.LoadMoreListView;
import com.md.hjyg.utility.LoadMoreListView.OnLoadMoreListener;
import com.md.hjyg.xinTouConstant.Constants;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/**
 * ClassName: LotteryRecordsActivity <br/>
 * date: 2015-10-19 下午4:38:58 <br/>
 * remark:已获取奖品页面
 * 
 * @author rw
 */
public class LotteryObtainedActivity extends BaseActivity implements
		OnClickListener ,OnLoadMoreListener{
	
	private LinearLayout nodata;
	private LoadMoreListView mLoadMoreListView;
	private final static int startPage = 1;
	private int page = startPage;
	private List<MemberLotteryLogModel> lists;
	private LotteryObtainedAdapter adapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_lottery_obtained_activity);
		findViews();
		CallLoanListWebservice(page);

	}

	public void findViews() {
		HeaderViewControler.setHeaderView(this, "我的奖品", this);
		nodata = (LinearLayout) findViewById(R.id.nodata);
		mLoadMoreListView = (LoadMoreListView) findViewById(R.id.mLoadMoreListView);
	}
	
	private void CallLoanListWebservice(final int page){
		dft.postMyRewardInfo(page, Constants.GetMyRewardinfo_URL, Request.Method.POST, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject josnbject) {
				MyRewardinfoModel model = (MyRewardinfoModel) dft
						.GetResponseObject(josnbject,
								MyRewardinfoModel.class);
				if (page == startPage) {
					setData(false, model.MemberLotteryLogList);
				}else {
					setData(true, model.MemberLotteryLogList);
				}
			}
		});
	}
	
	private void setData(boolean isLoadmore, List<MemberLotteryLogModel> lists) {
		if (!isLoadmore) {
			if (lists == null || lists.size() == 0) {
				nodata.setVisibility(View.VISIBLE);
				mLoadMoreListView.setVisibility(View.GONE);
			} else {
				nodata.setVisibility(View.GONE);
				mLoadMoreListView.setVisibility(View.VISIBLE);
				this.lists = lists;
				adapter = new LotteryObtainedAdapter(this.lists, this);
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
	public void onClick(View view) {
		switch (view.getId()) {
		case HeaderViewControler.ID:
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

	@Override
	public void onLoadMore() {
		page ++;
		CallLoanListWebservice(page);
	}


}
