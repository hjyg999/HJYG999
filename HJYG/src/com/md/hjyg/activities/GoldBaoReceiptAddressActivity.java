package com.md.hjyg.activities;

import java.util.ArrayList;
import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.adapter.GoldAddressListAdapter;
import com.md.hjyg.entity.GoldAddressModel;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * ClassName: GoldBaoReceiptAddressActivity date: 2016-1-25 下午3:15:58 remark:
 * 收货地址-编辑-修改-删除等
 * 
 * @author pyc
 */
public class GoldBaoReceiptAddressActivity extends BaseActivity implements
		OnClickListener {

	private Activity mActivity;
	HeaderControler header;
	private View header_bottom_line;
	private Intent intent;
	/** 返回键 */
	private LinearLayout lay_back_investment;
	/***
	 * 状态0.取消编辑状态(正常状态) 1 编辑状态
	 */
	private int state = 0;
	private TextView tv_edit;
	private Button btn_confirm;
	private ListView mListView;
	private GoldAddressListAdapter adapter;
	private List<GoldAddressModel> lists;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goldbaoreceiptaddress_activity);
		mActivity = this;
		findViewandInit();
		setStateUI();
	}
	
	private void findViewandInit() {
		// 标题栏

		header = new HeaderControler(this, true, false, "收货地址",
				Constants.CheckAuthtoken(getBaseContext()));

		header_bottom_line = findViewById(R.id.header_bottom_line);
		header_bottom_line.setBackgroundColor(getResources().getColor(
				R.color.gray_line));
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		lay_back_investment.setOnClickListener(this);
		
		tv_edit = (TextView) findViewById(R.id.tv_edit);
		tv_edit.setOnClickListener(this);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);
		
		mListView = (ListView) findViewById(R.id.mListView);
		lists = new ArrayList<GoldAddressModel>();
		lists.add(new GoldAddressModel("张三", "151****0516", "湖南长沙开福区玛丽莱大厦6楼",true));
		lists.add(new GoldAddressModel("张三", "151****0516", "湖南长沙开福区玛丽莱大厦6楼",false));
		lists.add(new GoldAddressModel("张三", "151****0516", "湖南长沙开福区玛丽莱大厦6楼",false));
		adapter = new GoldAddressListAdapter(mActivity, lists);
		mListView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_back_investment:// 返回
			mActivity.finish();
			overTransition(1);
			break;
		case R.id.tv_edit:// 编辑、取消编辑
			if (state == 0) {
				state = 1;
			}else {
				state = 0;
			}
			setStateUI();
			break;
		case R.id.btn_confirm:// 确认
			if (state == 0) {//新增收货地址
				intent = new Intent(mActivity, GoldBaoAddAddressActivity.class);
				startActivity(intent);
				overTransition(2);
			}else {//删除选中地址
			}
			break;
		default:
			break;
		}
	}

	/***
	 * 根据选择的状态设置显示的UI
	 */
	private void setStateUI(){
		if (state == 0) {
			tv_edit.setText("编辑");
			btn_confirm.setText("新增收货地址");
		}else {
			tv_edit.setText("取消");
			btn_confirm.setText("删除");
		}
		
		if (adapter != null) {
			adapter.setState(state);
			adapter.notifyDataSetChanged();
		}
	}
	

	@Override
	public void onBackPressed() {
		mActivity.finish();
		overTransition(1);
	}
}
