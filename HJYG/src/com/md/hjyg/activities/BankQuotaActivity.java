package com.md.hjyg.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.BankQuotaListAdapter;
import com.md.hjyg.entity.YintongPayQuotaListModel;
import com.md.hjyg.entity.YintongPayQuotaModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.xinTouConstant.Constants;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;

/** 
 * ClassName: BankQuotaActivity 
 * date: 2015-11-5 下午2:14:28 
 * remark:快捷支付银行限额表
 * @author pyc
 */
public class BankQuotaActivity extends BaseActivity implements OnClickListener{
	
	private ListView mListView;
//	private String Method_name = "RechargeApi/BankRestraint";
	private YintongPayQuotaListModel yintongPayQuotaListModel;
	private List<YintongPayQuotaModel> lists = new ArrayList<YintongPayQuotaModel>();
	private BankQuotaListAdapter mAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_bankquota_activity);
		
		findViewAndInit();
		getWebserviceBankRestraint();
	}
	
	private void findViewAndInit() {
		HeaderViewControler.setHeaderView(this, "快捷支付银行列表", this);
		mListView = (ListView) findViewById(R.id.mListView);
	}
	
	/**从后台获取银行限额列表*/
	private void getWebserviceBankRestraint() {
		
		dft.getBankRestraint(Constants.BankRestraint_URL, Request.Method.GET, 
				new  Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				yintongPayQuotaListModel = (YintongPayQuotaListModel) dft.GetResponseObject(response,
								YintongPayQuotaListModel.class);
				if (yintongPayQuotaListModel.notification.ProcessResult == 1) {
					InitListView();
				}else {
					Toast.makeText(BankQuotaActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
				}
			}
		}, null);
		
	}
	
	private void InitListView() {
		if (yintongPayQuotaListModel.list != null) {
			lists = yintongPayQuotaListModel.list;
			mAdapter = new BankQuotaListAdapter(this, lists);
			mListView.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();
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
