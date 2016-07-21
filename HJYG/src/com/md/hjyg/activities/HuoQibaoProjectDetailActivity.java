package com.md.hjyg.activities;

import java.util.ArrayList;

import com.md.hjyg.R;
import com.md.hjyg.adapter.ProjectDetailsAdapter;
import com.md.hjyg.entity.AssureList;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

/** 
 * ClassName: HuoQibaoProjectDetailActivity 
 * date: 2015-11-11 下午3:06:43 
 * remark: 活期宝项目详情列表
 * @author pyc
 */
public class HuoQibaoProjectDetailActivity extends BaseActivity implements OnClickListener{
	
//	/**购买限额*/
//	double PurchaseLimit;
//	/**Rate:利率
//	 * */
//	double Rate ;
//	/**起投金额*/
//	double StartInvestAmount;
//	/**活期宝总额*/
//	double Total;
	
//	TextView tv_Rate,tv_StartInvestAmount,tv_Total,tv_PurchaseLimit,buy_rule;
	/*** 活期宝项目详情*/
	private ArrayList<AssureList> HQBDetailsList;
	/***  活期宝常见问题*/
	private ArrayList<AssureList> HQBQuestionList ;
	private ListView myListView;
	private ProjectDetailsAdapter adapter;
	private int type = -1 ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_huoqibaoprojectdetail_activity);
		findViewAndInit();
	}
	
	private void findViewAndInit() {
		
		Intent intent = getIntent();
		type = intent.getIntExtra("type", -1);
		if (type == 0) {
			HeaderViewControler.setHeaderView(this, "项目详情", this);
			HQBDetailsList = intent.getParcelableArrayListExtra("HQBDetailsList");
			adapter = new ProjectDetailsAdapter(this, HQBDetailsList);
		}else if (type == 1) {
			HeaderViewControler.setHeaderView(this, "常见问题", this);
			HQBQuestionList = intent.getParcelableArrayListExtra("HQBQuestionList");
			adapter = new ProjectDetailsAdapter(this, HQBQuestionList);
			
		}
		
		myListView = (ListView) findViewById(R.id.myListView);
		if (adapter != null) {
			myListView.setAdapter(adapter);
		}

	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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

}
