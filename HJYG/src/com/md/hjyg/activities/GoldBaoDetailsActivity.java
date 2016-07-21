package com.md.hjyg.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.md.hjyg.R;
import com.md.hjyg.adapter.GoldExpandableAdapter;
import com.md.hjyg.entity.ChildItem;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.utility.Save;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ClassName: GoldBaoDetailsActivity date: 2016-1-20 上午11:50:59 remark:黄金宝详情界面
 * 
 * @author pyc
 */
public class GoldBaoDetailsActivity extends BaseActivity implements
		OnClickListener {
	private ExpandableListView expandableListView;
	private GoldExpandableAdapter adapter;
	private List<String> mapKey;
	private Map<String, List<ChildItem>> map;
	private Activity maActivity;
	HeaderControler header;
	private View header_bottom_line;
	private Intent intent;
	/** 返回键 */
	private LinearLayout lay_back_investment;
	/** 详情说明 */
	private TextView tv_explain;
	/** 顶部布局图片 */
	private ImageView img_top;
	private RelativeLayout rel_top;
	/**购买*/
	private Button btn_buy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_goldbaodetails_activity);
		maActivity = this;
		findViewandInit();
		setData();
	}

	private void findViewandInit() {
		// 标题栏
		header = new HeaderControler(this, true, false, "黄金宝",
				Constants.CheckAuthtoken(getBaseContext()));
		header_bottom_line = findViewById(R.id.header_bottom_line);
		header_bottom_line.setVisibility(View.GONE);
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		lay_back_investment.setOnClickListener(this);
		btn_buy = (Button) findViewById(R.id.btn_buy);
		btn_buy.setOnClickListener(this);

		tv_explain = (TextView) findViewById(R.id.tv_explain);
		img_top = (ImageView) findViewById(R.id.img_top);
		Bitmap bm = BitmapFactory.decodeResource(getResources(),
				R.drawable.gold_details_top);
		bm = Save.ScaleBitmap(bm, maActivity);
		rel_top = (RelativeLayout) findViewById(R.id.rel_top);
		LinearLayout.LayoutParams params = (LayoutParams) rel_top
				.getLayoutParams();
		params.height = bm.getHeight();
		rel_top.setLayoutParams(params);
		img_top.setImageBitmap(bm);

		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

	}

	private void setData() {
		tv_explain
				.setText("1、预计计息日为买入后的第二天；\n2、每人每日累计可买入500克， 卖出200克；\n3、买入无任何手续费， 卖出手续费3‰；\n4、上海黄金交易所开盘日09:00-24:00。");
		setScrollViewHightForGroup();
	}

	/** 设置expandableListView,更新ScrollView的高度 */
	private void setScrollViewHightForGroup() {
		// 取消系统左侧的图标
		expandableListView.setGroupIndicator(null);
		// 子条目展开时重新计算expandableListView的高度，用于刷新ScrollView的高度
		expandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {
						Constants
								.setListViewHeightBasedOnChildren(expandableListView);
						// setExpandableListViewHeightBasedOnChildren(expandableListView);
					}
				});
		// 子条目收缩时重新计算expandableListView的高度，用于刷新ScrollView的高度
		expandableListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int groupPosition) {
						Constants
								.setListViewHeightBasedOnChildren(expandableListView);
						// setExpandableListViewHeightBasedOnChildren(expandableListView);
					}
				});

		mapKey = new ArrayList<String>();
		map = new HashMap<String, List<ChildItem>>();
		mapKey.add("项目详情");
		mapKey.add("为什么要买黄金宝");
		List<ChildItem> item1 = new ArrayList<ChildItem>();
		item1.add(new ChildItem("| 黄金投资，毫克起买", "1毫克起售，按金额购买不足1毫克的尾差将退还您的现金账户;"));
		item1.add(new ChildItem("| 价格透明，超低手续费", "买入价格同步上海黄金交易所及国际金价，买入无手续费;"));
		item1.add(new ChildItem("| 稳定收金豆，年化2.0%-2.8%收益",
				"目前信投宝黄金的年收益率保持在2.0%-2.8%之间，将根据市场 情况调整;"));
		item1.add(new ChildItem("| 提取实物金条，珠宝门店线下取货",
				"顺丰速递实物金条送货到家，线下珠宝门店提供黄金饰品取金 换金。"));
		map.put(mapKey.get(0), item1);
		List<ChildItem> item2 = new ArrayList<ChildItem>();
		item2.add(new ChildItem(
				"| 全球经济 危机四伏",
				"欧元危机，卢布危机，第三世界货币大跌，中国经济下行……是否新一轮金融危机？\n当年东南亚金融危机，韩元，越南盾等变成废纸，只能用黄金买东西的境况历历在目！为保障个人财产安全，?现在就买黄金！"));
		item2.add(new ChildItem(
				"| 人民币贬值 黄金保值",
				"17年前你有10万元就是富豪，如果10万元存银行，你现在还是10万多元而已，在当今只能算屌丝。\n但如果你买了黄金宝，金价涨了34倍多，黄金宝克数涨了5倍多，现在就是有200多万的百万富翁。"));
		item2.add(new ChildItem(
				"| 我们只选择最稳健的投资",
				"黄金宝活期的20%资金，均通过上海黄金交易所预订黄金现货，确保享受金价上涨红利。 \n用户购买黄金，提供给需要用金的企业（如芯片、首饰生产和珠宝销售企业），使他们可以向信投宝的黄金持有者借金还金，黄金收益就在这一借一还中稳定产生。"));
		map.put(mapKey.get(1), item2);

		adapter = new GoldExpandableAdapter(maActivity, map, mapKey);
		expandableListView.setAdapter(adapter);
		Constants.setListViewHeightBasedOnChildren(expandableListView);
		// setExpandableListViewHeightBasedOnChildren(expandableListView);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_back_investment:
			maActivity.finish();
			overTransition(1);
			break;
		case R.id.btn_buy:
			intent = new Intent(maActivity, GoldBaoBuyActivity.class);
			startActivity(intent);
			overTransition(2);
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		maActivity.finish();
		overTransition(1);
	}

}
