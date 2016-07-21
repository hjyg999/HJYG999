package com.md.hjyg.activities;

import java.util.ArrayList;
import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.adapter.DialogListAdapter;
import com.md.hjyg.entity.GoldTypeChoice;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ClassName: GoldBaoExtractExpressActivity date: 2016-1-21 下午5:26:09 remark:
 * 黄金提取-快递接收
 * 
 * @author pyc
 */
public class GoldBaoExtractExpressActivity extends BaseActivity implements
		OnClickListener {

	private Activity mActivity;
	HeaderControler header;
	private View header_bottom_line;
	private Intent intent;
	/** 返回键 */
	private LinearLayout lay_back_investment;
	/**增加地址*/
	private RelativeLayout rel_addAddress;
	/**选择收货地址*/
	private RelativeLayout rel_choiceAddress;
	/**是否保价按钮*/
	private RelativeLayout rel_protect;
	private boolean isProtect;
	private ImageView img_protect;
	private TextView tv_protectfeel;
	/**选择金条类型*/
	private LinearLayout lin_goldtype;
	private TextView ed_goldtype;
	/** 黄金类型dialog */
	private Dialog dialog;
	/** 关闭dialog */
	private ImageView close_dialog;
	private List<GoldTypeChoice> lists;
	private static final int CODE = 1011;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_goldbaoextractexpress_activity);
		mActivity = this;
		findViewandInit();
	}

	private void findViewandInit() {
		// 标题栏
		header = new HeaderControler(this, true, false, "快递接收",
				Constants.CheckAuthtoken(getBaseContext()));
		header_bottom_line = findViewById(R.id.header_bottom_line);
		header_bottom_line.setBackgroundColor(getResources().getColor(
				R.color.gray_line));
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		lay_back_investment.setOnClickListener(this);
		//地址
		rel_addAddress = (RelativeLayout) findViewById(R.id.rel_addAddress);
		rel_addAddress.setOnClickListener(this);
		rel_choiceAddress = (RelativeLayout) findViewById(R.id.rel_choiceAddress);
		rel_choiceAddress.setOnClickListener(this);
		//保价
		rel_protect = (RelativeLayout) findViewById(R.id.rel_protect);
		rel_protect.setOnClickListener(this);
		img_protect = (ImageView) findViewById(R.id.img_protect);
		tv_protectfeel = (TextView) findViewById(R.id.tv_protectfeel);
		setProtectUI();
		//选择金条类型
		lin_goldtype = (LinearLayout) findViewById(R.id.lin_goldtype);
		lin_goldtype.setOnClickListener(this);
		ed_goldtype = (TextView) findViewById(R.id.ed_goldtype);
		lists = new ArrayList<GoldTypeChoice>();
		lists.add(new GoldTypeChoice("AU20克", false));
		lists.add(new GoldTypeChoice("AU20克", false));
		lists.add(new GoldTypeChoice("AU20克", false));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_back_investment://返回
			mActivity.finish();
			overTransition(1);
			break;
		case R.id.rel_addAddress://增加地址
			intent = new Intent(mActivity, GoldBaoAddAddressActivity.class);
			intent.putExtra("type", 0);
			startActivityForResult(intent, CODE);
			overTransition(2);
			break;
		case R.id.rel_choiceAddress://选择地址
			intent = new Intent(mActivity, GoldBaoReceiptAddressActivity.class);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.lin_goldtype://选择金条类型
			choiceGoldTypeDilog();
			break;
		case R.id.close_dialog://关闭dialog
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			break;
		case R.id.rel_protect://是否保价
			if (isProtect) {
				isProtect = false;
			}else {
				isProtect = true;
			}
			setProtectUI();
			break;

		default:
			break;
		}
	}
	
	/**设置与保价相关的显示状态*/
	private void setProtectUI(){
		if (isProtect) {
			img_protect.setImageResource(R.drawable.gold_ckeckbox_yes);
			tv_protectfeel.setVisibility(View.VISIBLE);
		}else {
			img_protect.setImageResource(R.drawable.gold_ckeckbox_no);
			tv_protectfeel.setVisibility(View.GONE);
		}
	}
	
	/**选择黄金类型弹窗*/
	@SuppressLint("InflateParams")
	private void choiceGoldTypeDilog(){
		
		dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setWindowAnimations(R.style.Animcardtype);
		dialog.setCanceledOnTouchOutside(true);
		View view = LayoutInflater.from(this).inflate(
				R.layout.dialog_havelist_layout, null);
		close_dialog = (ImageView) view.findViewById(R.id.close_dialog);
		close_dialog.setOnClickListener(this);
		ListView mListView = (ListView) view.findViewById(R.id.mListView);
		final DialogListAdapter adapter = new DialogListAdapter(this, lists);
		mListView.setAdapter(adapter);
		dialog.setContentView(view);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (lists.get(position).isChoice()) {
					lists.get(position).setChoice(false);
					ed_goldtype.setText("");
					adapter.notifyDataSetChanged();
					
				}else {
					for (int i = 0; i < lists.size(); i++) {
						lists.get(i).setChoice(false);
					}
					lists.get(position).setChoice(true);
					ed_goldtype.setText(lists.get(position).getName());
					adapter.notifyDataSetChanged();
					dialog.dismiss();
				}
			}
		});
		dialog.show();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CODE && data != null ) {
			rel_addAddress.setVisibility(View.GONE);
		}
	}

	@Override
	public void onBackPressed() {
		mActivity.finish();
		overTransition(1);
	}

}
