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
import android.widget.TextView;

/**
 * ClassName: GoldBaoExtractMyselfActivity date: 2016-1-22 下午4:03:30 remark:
 * 金店自取
 * 
 * @author pyc
 */
public class GoldBaoExtractMyselfActivity extends BaseActivity implements
		OnClickListener {

	private Activity mActivity;
	HeaderControler header;
	private View header_bottom_line;
	/** 返回键 */
	private LinearLayout lay_back_investment;

	/** 选择金条类型 */
	private LinearLayout lin_goldtype;
	private TextView ed_goldtype;
	/** 黄金类型dialog */
	private Dialog dialog;
	/** 关闭dialog */
	private ImageView close_dialog;
	private List<GoldTypeChoice> lists;
	private LinearLayout lin_choieshop;
	private TextView  ed_choieshop;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goldbaoextractmyself_activity);
		findViewandInit();
		mActivity = this;
	}

	private void findViewandInit() {
		// 标题栏
		header = new HeaderControler(this, true, false, "金店自取",
				Constants.CheckAuthtoken(getBaseContext()));
		header_bottom_line = findViewById(R.id.header_bottom_line);
		header_bottom_line.setBackgroundColor(getResources().getColor(
				R.color.gray_line));
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		lay_back_investment.setOnClickListener(this);

		// 选择金条类型
		lin_goldtype = (LinearLayout) findViewById(R.id.lin_goldtype);
		lin_goldtype.setOnClickListener(this);
		ed_goldtype = (TextView) findViewById(R.id.ed_goldtype);
		lists = new ArrayList<GoldTypeChoice>();
		lists.add(new GoldTypeChoice("AU20克", false));
		lists.add(new GoldTypeChoice("AU20克", false));
		lists.add(new GoldTypeChoice("AU20克", false));
		
		lin_choieshop = (LinearLayout) findViewById(R.id.lin_choieshop);
		lin_choieshop.setOnClickListener(this);
		ed_choieshop = (TextView) findViewById(R.id.tv_choieshop);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_back_investment:
			mActivity.finish();
			overTransition(1);
			break;
		case R.id.lin_goldtype://选择金条类型
			choiceGoldTypeDilog();
			break;
		case R.id.lin_choieshop://选择金店
			intent = new Intent(mActivity, GoldBaoChoiceShopActivity.class);
			startActivityForResult(intent, 1);
			overTransition(2);
			break;
		case R.id.close_dialog://关闭dialog
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && data != null) {
			ed_choieshop.setText(data.getStringExtra("shop"));
		}
	}

	/** 选择黄金类型弹窗 */
	@SuppressLint("InflateParams")
	private void choiceGoldTypeDilog() {

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

				} else {
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
	public void onBackPressed() {
		mActivity.finish();
		overTransition(1);
	}

}
