package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.GetInvestmentListModel;
import com.md.hjyg.entity.InvestmentListDetailModel;
import com.md.hjyg.utility.Save;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ClassName: MyInvestListAdapter date: 2016-4-22 下午1:25:39 remark: 我的投资列表适配器
 * 
 * @author pyc
 */
public class MyInvestListAdapter extends MyBaseAdapter<GetInvestmentListModel> {
	private Bitmap[] bitmaps;
	private int tab;
	private Context context;
	private OnisExpandListener onisExpandListener;
	private int p = -1;

	public MyInvestListAdapter(List<GetInvestmentListModel> lists,
			Context context, int tab) {
		super(lists, context);
		this.tab = tab;
		this.context = context;
		if (tab != 0) {
			initBitmap();
		}
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HoldView holdView = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.myinvest_item_layout, null);
			holdView = new HoldView(convertView);
			convertView.setTag(holdView);
		} else {
			holdView = (HoldView) convertView.getTag();
		}
		if (position == lists.size() - 1) {
			holdView.rel_main.setPadding(20, 10, 20, 20);
		} else if (position == 0) {
			holdView.rel_main.setPadding(20, 20, 20, 10);
		} else {
			holdView.rel_main.setPadding(20, 10, 20, 10);

		}

		GetInvestmentListModel model = lists.get(position);
		switch (tab) {
		case 0:// 投标中
			holdView.mListView.setVisibility(View.GONE);
			holdView.rel_expand.setVisibility(View.GONE);
			holdView.rel_income.setVisibility(View.GONE);
			holdView.tv_time.setText("投资日期："
					+ model.CreateTime.replaceAll("-", "/"));
			holdView.tv_type.setText("状态：" + model.Type);
			break;
		case 1:// 持有中
		case 2:// 已还款
			if (tab ==1 ) {
				holdView.tv_income_hit.setText("应收本息(元)");
			}else if (tab ==2) {
				holdView.tv_income_hit.setText("已收本息(元)");
			}
			holdView.tv_time.setText("结标日期："
					+ model.FullTime.replaceAll("-", "/"));
			holdView.rel_expand.setTag(position);
			holdView.tv_sum.setText(model.RepaymentCount + "/"
					+ model.RepaymentTerms);
			holdView.rel_expand.setVisibility(View.VISIBLE);
			// 应收本息
			holdView.tv_income.setText(model.PrincipalInterest);
			holdView.rel_income.setVisibility(View.VISIBLE);
			// 还款方式
			holdView.tv_type.setText("还款方式：" + model.RepaymentWay);
			if (model.isExpand()) {
				holdView.mListView.setVisibility(View.VISIBLE);
				holdView.mListView.setTag(position);
				holdView.img_state.setImageBitmap(bitmaps[2]);

				MyInvestExpandListAdapter adapter = new MyInvestExpandListAdapter(
						model.getListDetail(), context, bitmaps);
				holdView.mListView.setAdapter(adapter);
				int h = Constants.getListViewHeight(holdView.mListView);
				lists.get(position).setH(h);
				if (position == p && onisExpandListener != null) {
					onisExpandListener.listViewScrollBy(p, h);
					p = -1;
				}
				holdView.mListView
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								if (parent != null) {
									int p = (Integer) parent.getTag();
									InvestmentListDetailModel detailModel = lists.get(p).getListDetail().get(position);
									if (onisExpandListener != null
											&& detailModel.isCanClick()) {
										onisExpandListener.showDialg(detailModel.RepaymentID);
									}
								}
							}
						});
			} else {
				holdView.mListView.setVisibility(View.GONE);
				holdView.img_state.setImageBitmap(bitmaps[3]);
				if (position == lists.size() - 1 && onisExpandListener != null && p != -1) {
					onisExpandListener.listViewScrollBy(p, -lists.get(position).getH());
					p = -1;
				}
			}

			break;
		case 4:// 日历
			holdView.mListView.setVisibility(View.GONE);
			holdView.rel_expand.setVisibility(View.GONE);
			holdView.rel_income.setVisibility(View.VISIBLE);
			holdView.tv_time.setText("收款日期："
					+ model.RepaymentDate.replaceAll("-", "/"));
			holdView.tv_type.setVisibility(View.GONE);
			holdView.tv_income.setText(model.PrincipalInterest);
			if (model.Principal >0) {
				holdView.tv_income_hit.setText("应收本息(元)");
			}else {
				holdView.tv_income_hit.setText("应收利息(元)");
			}
			holdView.line_hstate.setVisibility(View.VISIBLE);
			if (model.Status == 2) {//已还
				holdView.img_hstate.setImageResource(R.drawable.right_28x28);
				holdView.tv_hstate.setTextColor(context.getResources().getColor(R.color.green_99CC33));
				holdView.tv_income.setTextColor(context.getResources().getColor(R.color.green_99CC33));
				holdView.tv_hstate.setText("已收");
			}else {
				holdView.img_hstate.setImageResource(R.drawable.time_28x28);
				holdView.tv_hstate.setTextColor(context.getResources().getColor(R.color.blue_ht));
				holdView.tv_income.setTextColor(context.getResources().getColor(R.color.blue_ht));
				holdView.tv_hstate.setText("待收");
			}
			
			break;

		default:
			break;
		}

		holdView.tv_title.setText(model.Title);
		holdView.tv_account.setText(model.Amount);
		holdView.tv_rate.setText("年利率：" + model.LoanRate + "%");
		holdView.tv_term.setText("项目期限：" + model.LoanTerm + model.LoanDateType);
		return convertView;
	}

	class HoldView {
		TextView tv_title, tv_time, tv_account, tv_income, tv_rate, tv_term,
				tv_type, tv_sum,tv_income_hit,tv_hstate;
		/**
		 * 应收本息
		 */
		RelativeLayout rel_income, rel_expand;
		LinearLayout rel_main,line_hstate;
		ImageView img_state,img_hstate;
		ListView mListView;

		public HoldView(View v) {
			tv_title = (TextView) v.findViewById(R.id.tv_title);
			tv_time = (TextView) v.findViewById(R.id.tv_time);
			tv_account = (TextView) v.findViewById(R.id.tv_account);
			tv_income = (TextView) v.findViewById(R.id.tv_income);
			tv_rate = (TextView) v.findViewById(R.id.tv_rate);
			tv_term = (TextView) v.findViewById(R.id.tv_term);
			tv_type = (TextView) v.findViewById(R.id.tv_type);
			tv_sum = (TextView) v.findViewById(R.id.tv_sum);
			rel_income = (RelativeLayout) v.findViewById(R.id.rel_income);
			rel_expand = (RelativeLayout) v.findViewById(R.id.rel_expand);
			rel_main = (LinearLayout) v.findViewById(R.id.rel_main);
			img_state = (ImageView) v.findViewById(R.id.img_state);
			mListView = (ListView) v.findViewById(R.id.mListView);
			tv_income_hit = (TextView) v.findViewById(R.id.tv_income_hit);
			
			line_hstate = (LinearLayout) v.findViewById(R.id.line_hstate);
			img_hstate = (ImageView) v.findViewById(R.id.img_hstate);
			tv_hstate = (TextView) v.findViewById(R.id.tv_hstate);
			if (tab != 0) {
				rel_expand.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int p = (Integer) v.getTag();

						if (onisExpandListener != null) {
							onisExpandListener.getMemberInvestDetail(p);
							MyInvestListAdapter.this.p = p;
						}

					}
				});
			}

		}
	}

	private void initBitmap() {
		Bitmap reimg = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.novice_red_bg);
		bitmaps = new Bitmap[4];
		bitmaps[0] = Save.ScaleBitmap(BitmapFactory.decodeResource(
				context.getResources(), R.drawable.fq), context, reimg);
		bitmaps[1] = Save.ScaleBitmap(BitmapFactory.decodeResource(
				context.getResources(), R.drawable.arrow_red_r_36x36), context,
				reimg);
		bitmaps[2] = Save.ScaleBitmap(BitmapFactory.decodeResource(
				context.getResources(), R.drawable.arrow_red_t_36x36), context,
				reimg);
		bitmaps[3] = Save.ScaleBitmap(BitmapFactory.decodeResource(
				context.getResources(), R.drawable.arrow_red_b_36x36), context,
				reimg);
	}

	public void setOnisExpandListener(OnisExpandListener onisExpandListener) {
		this.onisExpandListener = onisExpandListener;
	}

	public interface OnisExpandListener {
		/**
		 * 获取点击标的还款详情还款详情
		 * 
		 * @param p
		 */
		public void getMemberInvestDetail(int p);

		/**
		 * @param InvestID
		 *            项目逾期id *逾期列表详细信息
		 */
		public void showDialg(int InvestID);
		/**
		 * 
		 * @param p
		 * @param y
		 */
		public void listViewScrollBy(int p,int y);
	}

}
