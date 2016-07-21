package com.md.hjyg.fragment;

import java.util.ArrayList;
import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.activities.BaseFragmentActivity;
import com.md.hjyg.activities.ConsumptionFragmentActivity;
import com.md.hjyg.adapter.ConsumptionGridViewAdapter;
import com.md.hjyg.entity.ConsumptionInfoModel;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/** 
 * ClassName: ConsumptionFragment 
 * date: 2016-6-28 上午10:36:15 
 * remark:消费标分类fragment-九宫格
 * @author pyc
 */
public class ConsumptionFragment extends Fragment{
	
//	private static final int 新手标 = 0;
	private static final int 旅游标 = 1;
	private static final int 婚庆标 = 2;
	private static final int 健身标 = 3;
	private static final int 美容标 = 4;
	private static final int 教育标 = 5;
	private static final int 地产标 = 6;
	
	private GridView mGridView;
	private ConsumptionGridViewAdapter adapter;
	private List<ConsumptionInfoModel> lists;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_consumption_layout, container,
				false);
		findViewandInit(v);
		return v;
	}
	
	private void findViewandInit(View v ){
		mGridView = (GridView) v.findViewById(R.id.mGridView);
		mGridView.setVerticalSpacing(0);
		mGridView.setHorizontalSpacing(0);
		lists = new ArrayList<ConsumptionInfoModel>();
		lists.add(new ConsumptionInfoModel(R.drawable.gift_ly, "旅游", "世界那么大  免费去看看", 旅游标));
		lists.add(new ConsumptionInfoModel(R.drawable.gift_fq, "婚庆", "定制礼服", 婚庆标));
		lists.add(new ConsumptionInfoModel(R.drawable.gift_js, "健身", "运动健身", 健身标));
		lists.add(new ConsumptionInfoModel(R.drawable.gift_mr, "美容", "美容美体", 美容标));
		lists.add(new ConsumptionInfoModel(R.drawable.gift_dc, "地产", "别墅买卖", 地产标));
		adapter = new ConsumptionGridViewAdapter(lists, getActivity());
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), ConsumptionFragmentActivity.class);
				intent.putExtra(ConsumptionFragmentActivity.TYPE, lists.get(position).getTitle());
				intent.putExtra(ConsumptionFragmentActivity.ID, lists.get(position).getId());
				startActivity(intent);
				((BaseFragmentActivity)getActivity()).overTransition(2);
			}
		});
	}
	
	

}
