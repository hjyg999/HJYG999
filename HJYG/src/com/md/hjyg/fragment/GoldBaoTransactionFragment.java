package com.md.hjyg.fragment;

import java.util.ArrayList;
import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.activities.GoldBaoExtractDetailsActivity;
import com.md.hjyg.adapter.GoldBaoTransactionAdapter;
import com.md.hjyg.entity.GoldBaoTransactionModel;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * ClassName: GoldBaoTransactionFragment date: 2016-2-1 上午10:59:35 remark:
 * 黄金宝交易记录
 * 
 * @author pyc
 */
public class GoldBaoTransactionFragment extends Fragment {

	private ListView mListView;
	private GoldBaoTransactionAdapter adapter;
	private List<GoldBaoTransactionModel> lists;
	private int type;

	public GoldBaoTransactionFragment(int type) {
		super();
		this.type = type;
		lists = new ArrayList<GoldBaoTransactionModel>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.goldbaotransaction_fragment,
				container, false);
		mListView = (ListView) view.findViewById(R.id.mListView);
		if (type == 1) {
			lists.add(new GoldBaoTransactionModel(type, "08/26 10:20", "买入成功",
					"1,261.21"));
			lists.add(new GoldBaoTransactionModel(type, "08/27 10:20", "买入成功",
					"1,265.21"));
			lists.add(new GoldBaoTransactionModel(type, "08/28 10:20", "买入成功",
					"1,260.21"));
		} else if (type == 2) {
			lists.add(new GoldBaoTransactionModel(type, "08/26 10:20", "卖出成功",
					"1,261.21"));
			lists.add(new GoldBaoTransactionModel(type, "08/27 10:20", "卖出成功",
					"1,265.21"));
			lists.add(new GoldBaoTransactionModel(type, "08/28 10:20", "卖出成功",
					"1,260.21"));
		} else if (type == 3) {
			lists.add(new GoldBaoTransactionModel(type, "08/26 10:20", "快递接收",
					"1,261.21"));
			lists.add(new GoldBaoTransactionModel(type, "08/27 10:20", "卖出成功",
					"1,265.21"));
			lists.add(new GoldBaoTransactionModel(type, "08/28 10:20", "快递接收",
					"1,260.21"));
		}

		adapter = new GoldBaoTransactionAdapter(lists, getActivity());
		mListView.setAdapter(adapter);
		if (type == 3) {
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					startActivity( new Intent(getActivity(), GoldBaoExtractDetailsActivity.class));
					getActivity(). overridePendingTransition(R.anim.trans_right_in, R.anim.trans_lift_out);
				}
			});
		}
		return view;
	}

}
