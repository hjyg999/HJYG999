package com.md.hjyg.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.MyBaseAdapter;
import com.md.hjyg.entity.GetInvestmentListModel;
import com.md.hjyg.entity.MyInvestmentModel;
import com.md.hjyg.entity.RedEnvelopeItemModel;
import com.md.hjyg.entity.RedEnvelopeListModel;
import com.md.hjyg.entity.RedEnvelopeType;
import com.md.hjyg.utility.AddRateSelectDialog;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * ClassName: MyInvestmentingFragment date: 2016-7-6 上午10:24:52
 * remark:我的投资-投标中(增加加息券功能)
 * 
 * @author pyc
 */
public class MyInvestmentingFragment extends
		LoadMoreListFragment<GetInvestmentListModel> implements OnClickListener {

	private int[] add_imgHWs;
	private AddRateSelectDialog addRateSelectDialog;
	private int p;

	@Override
	protected void setInit() {
		super.setInit();
		startPage = 0;
		page = startPage;
		fra_main.setBackgroundColor(getResources().getColor(R.color.gray_e9));
		img_nodate.setImageResource(R.drawable.meeting_nodata190_240);
		dialog.initDialog();
	}

	@Override
	protected void getWebData(String ftype, final int page) {
		dft.getMyInvestmentInfo(0, page,
				Constants.GetNewRepaymentListReceivedByPage_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						dialog.dismiss();

						MyInvestmentModel myInvestmentModel = (MyInvestmentModel) dft
								.GetResponseObject(response,
										MyInvestmentModel.class);
						if (page == startPage) {
							setData(false, myInvestmentModel.List);
						} else {
							setData(true, myInvestmentModel.List);
						}

					}

				}, null);
	}

	@Override
	protected MyBaseAdapter<GetInvestmentListModel> getAdapter(
			List<GetInvestmentListModel> lists, Activity activity) {
		return new MyInvestmentingAdapter(lists, activity);
	}

	/**
	 * 选择红包加息券弹窗
	 */
	private void showSelectDialog(int p) {
		this.p = p;
		addRateSelectDialog = new AddRateSelectDialog(getActivity(),
				lists.get(p).RedEnvelopeList, this, false){
			@Override
			public void Refresh() {
				super.Refresh();
				page = 0;
				dialog.initDialog();
				getWebData("", page);
			}
		};
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case AddRateSelectDialog.BTN_ID:
			addRateSelectDialog.postAddRedEnvelopeLog(dft, dialog,
					lists.get(p).Id,
					lists.get(p).MemberInvestRedEnvelopeLogType, dialogManager,
					false);
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * ClassName: MyInvestmentingAdapter date: 2016-7-6 上午10:29:49
	 * remark:投标中(增加加息券功能)
	 * 
	 * @author pyc
	 */
	class MyInvestmentingAdapter extends MyBaseAdapter<GetInvestmentListModel> {

		private int[] arrow_imgHWs;

		public MyInvestmentingAdapter(List<GetInvestmentListModel> lists,
				Context context) {
			super(lists, context);
			add_imgHWs = Save.getScaleBitmapWangH(32, 32);
			arrow_imgHWs = Save.getScaleBitmapWangH(36, 20);
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HoldView holdView = null;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.myinvest_ingitem_layout, null);
				holdView = new HoldView(convertView);
				convertView.setTag(holdView);

			} else {
				holdView = (HoldView) convertView.getTag();
			}
			if (holdView == null) {
				return null;
			}
			if (position == lists.size() - 1) {
				holdView.rel_main.setPadding(20, 10, 20, 20);
			} else if (position == 0) {
				holdView.rel_main.setPadding(20, 20, 20, 10);
			} else {
				holdView.rel_main.setPadding(20, 10, 20, 10);

			}
			GetInvestmentListModel model = lists.get(position);
			holdView.tv_title.setText(model.Title);
			holdView.tv_time.setText(model.CreateTime);
			holdView.tv_account.setText(model.PrincipalInterest);
			holdView.tv_rate.setText("年利率：" + model.LoanRate + "%");
			holdView.tv_term.setText("项目期限：" + model.LoanTerm
					+ model.LoanDateType);
			holdView.tv_type.setText("状态：" + model.Type);
			if (model.IsRedEnvelope == -1) {// 已经选择红包加息券
				holdView.tv_selectBtn.setVisibility(View.GONE);
				holdView.img_state.setVisibility(View.VISIBLE);

					RedEnvelopeItemModel itemModel = model.RedEnvelopeItem;
					List<RedEnvelopeListModel> mList = new ArrayList<RedEnvelopeListModel>();
					if (itemModel.Type == RedEnvelopeType.加息券) {
						holdView.tv_add1.setVisibility(View.VISIBLE);
						holdView.img_add1.setVisibility(View.VISIBLE);
						holdView.tv_add2.setVisibility(View.GONE);
						holdView.img_add2.setVisibility(View.GONE);
						holdView.tv_add1
								.setText("+"
										+ Constants
												.StringToCurrency(itemModel.IncreaseRate
														+ "") + "%");
						holdView.tv_add1.setTextColor(getResources().getColor(
								R.color.yellow));
						holdView.img_add1
								.setImageResource(R.drawable.myinvest_jxj);
						holdView.tv_listtitle.setText("使用加息券");
						holdView.tv_listtitle.setTextColor(getResources()
								.getColor(R.color.yellow));
						mList.add(new RedEnvelopeListModel(
								R.drawable.myinvest_jxj, "加息券收益：", Constants
										.StringToCurrency(itemModel.Interest
												+ "")+ "元") );
						String lonanDateType = "";
						if (itemModel.DateType == 0) {
							lonanDateType = "个月";
						} else if (itemModel.DateType == 2) {
							lonanDateType = "天";

						} else if (itemModel.DateType == 4) {
							lonanDateType = "周";
						}
						mList.add(new RedEnvelopeListModel(
								R.drawable.myinvest_qx, "加息期限：",
								itemModel.DateTerm + lonanDateType));
					} else {
						holdView.tv_listtitle.setText("使用投资红包");
						holdView.tv_listtitle.setTextColor(getResources()
								.getColor(R.color.green_99CC33));
						if (itemModel.ExperienceAmount > 0) {// 体验金
							holdView.tv_add1.setVisibility(View.VISIBLE);
							holdView.img_add1.setVisibility(View.VISIBLE);
							holdView.tv_add1.setTextColor(getResources()
									.getColor(R.color.green_99CC33));
							holdView.tv_add1.setText(Constants
									.StringToCurrency(
											itemModel.ExperienceAmount + "")
									.replace(".00", "")
									+ "元");
							holdView.img_add1
									.setImageResource(R.drawable.myinvest_tyj);
							mList.add(new RedEnvelopeListModel(
									R.drawable.myinvest_tyj,
									"体验金：",
									Constants
											.StringToCurrency(itemModel.ExperienceAmount
													+ "")
											+ "元"));
						} else {
							holdView.tv_add1.setVisibility(View.GONE);
							holdView.img_add1.setVisibility(View.GONE);
						}
						if (itemModel.RedEnvelopeAmount > 0) {// 现金
							holdView.tv_add2.setVisibility(View.VISIBLE);
							holdView.img_add2.setVisibility(View.VISIBLE);
							holdView.tv_add2.setTextColor(getResources()
									.getColor(R.color.green_99CC33));
							holdView.tv_add2.setText("+" +Constants
									.StringToCurrency(
											itemModel.RedEnvelopeAmount + "")
									.replace(".00", "")
									+ "元");
							holdView.img_add2
									.setImageResource(R.drawable.myinvest_xj);
							mList.add(new RedEnvelopeListModel(
									R.drawable.myinvest_xj,
									"现金：",
									 Constants
											.StringToCurrency(itemModel.RedEnvelopeAmount
													+ "")
											+ "元"));

						} else {
							holdView.tv_add2.setVisibility(View.GONE);
							holdView.img_add2.setVisibility(View.GONE);
						}
					}

					mList.add(new RedEnvelopeListModel(R.drawable.myinvest_sj,
							"到期时间：", itemModel.endTimes));
					mList.add(new RedEnvelopeListModel(R.drawable.myinvest_tj,
							"使用条件：", itemModel.Remark));
					lists.get(position).RedEnvelopeItem.setmList(mList);

				if (model.isExpand()) {
					holdView.mListView.setVisibility(View.VISIBLE);
					holdView.tv_listtitle.setVisibility(View.VISIBLE);
					holdView.tv_term.setPadding(0, 5, 0, 0);
					holdView.img_state
							.setImageResource(R.drawable.arrow_grayt_36x20);
					ChildrenListAdapter adapter = new ChildrenListAdapter(
							model.RedEnvelopeItem.getmList(), getActivity());
					holdView.mListView.setAdapter(adapter);
					Constants
							.setListViewHeightBasedOnChildren(holdView.mListView);
				} else {
					holdView.mListView.setVisibility(View.GONE);
					holdView.tv_listtitle.setVisibility(View.GONE);
					holdView.tv_term.setPadding(0, 0, 0, 0);
					holdView.img_state
							.setImageResource(R.drawable.arrow_grayb_36x20);
				}

				holdView.rel_state.setTag(position);
				holdView.rel_state.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Integer p = (Integer) v.getTag();
						lists.get(p).setExpand(!lists.get(p).isExpand());
						notifyDataSetChanged();
					}
				});

			} else if (model.IsRedEnvelope == 1) {// 可以选择红包加息券
				holdView.tv_selectBtn.setVisibility(View.VISIBLE);
				holdView.tv_add1.setVisibility(View.GONE);
				holdView.tv_add2.setVisibility(View.GONE);
				holdView.img_add1.setVisibility(View.GONE);
				holdView.img_add2.setVisibility(View.GONE);
				holdView.img_state.setVisibility(View.GONE);
				holdView.tv_listtitle.setVisibility(View.GONE);
				holdView.mListView.setVisibility(View.GONE);
				holdView.tv_selectBtn.setTag(position);
				holdView.tv_selectBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Integer p = (Integer) v.getTag();
						showSelectDialog(p);
					}
				});
			} else if (model.IsRedEnvelope == 0) {// 没有红包加息券可选择
				holdView.tv_selectBtn.setVisibility(View.GONE);
				holdView.tv_add1.setVisibility(View.GONE);
				holdView.tv_add2.setVisibility(View.GONE);
				holdView.img_add1.setVisibility(View.GONE);
				holdView.img_add2.setVisibility(View.GONE);
				holdView.img_state.setVisibility(View.GONE);
				holdView.tv_listtitle.setVisibility(View.GONE);
				holdView.mListView.setVisibility(View.GONE);
			}

			return convertView;
		}

		class HoldView {
			TextView tv_title, tv_time, tv_account, tv_rate, tv_selectBtn;
			TextView tv_add1, tv_add2;
			ImageView img_add1, img_add2, img_state;
			TextView tv_listtitle;
			ListView mListView;
			TextView tv_term, tv_type;
			RelativeLayout rel_state;
			LinearLayout rel_main;

			public HoldView(View v) {
				rel_main = (LinearLayout) v.findViewById(R.id.rel_main);
				tv_title = (TextView) v.findViewById(R.id.tv_title);
				tv_time = (TextView) v.findViewById(R.id.tv_time);
				tv_account = (TextView) v.findViewById(R.id.tv_account);
				tv_rate = (TextView) v.findViewById(R.id.tv_rate);
				tv_selectBtn = (TextView) v.findViewById(R.id.tv_selectBtn);
				tv_add1 = (TextView) v.findViewById(R.id.tv_add1);
				tv_add2 = (TextView) v.findViewById(R.id.tv_add2);

				img_add1 = (ImageView) v.findViewById(R.id.img_add1);
				img_add2 = (ImageView) v.findViewById(R.id.img_add2);
				img_state = (ImageView) v.findViewById(R.id.img_state);

				tv_listtitle = (TextView) v.findViewById(R.id.tv_listtitle);

				mListView = (ListView) v.findViewById(R.id.mListView);

				tv_term = (TextView) v.findViewById(R.id.tv_term);
				tv_type = (TextView) v.findViewById(R.id.tv_type);
				rel_state = (RelativeLayout) v.findViewById(R.id.rel_state);

				ViewParamsSetUtil.setViewHandW_lin(img_add1, add_imgHWs[1],
						add_imgHWs[0]);
				ViewParamsSetUtil.setViewHandW_rel(img_state, arrow_imgHWs[1],
						arrow_imgHWs[0]);
			}

		}

	}

	/**
	 * ClassName: ChildrenListAdapter date: 2016-7-6 上午11:42:38 remark:加息券详情适配器
	 * 
	 * @author pyc
	 */
	class ChildrenListAdapter extends MyBaseAdapter<RedEnvelopeListModel> {

		public ChildrenListAdapter(List<RedEnvelopeListModel> lists,
				Context context) {
			super(lists, context);
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			HoldView holdView = null;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.myinvest_ingitem_children_layout, null);
				holdView = new HoldView(convertView);
				convertView.setTag(holdView);
			} else {
				holdView = (HoldView) convertView.getTag();
			}
			RedEnvelopeListModel model = lists.get(position);
			holdView.img.setImageResource(model.getImgId());
			holdView.tv_name.setText(model.getType());
			holdView.tv_right.setText(model.getAccount());

			return convertView;
		}

		class HoldView {
			ImageView img;
			TextView tv_name, tv_right;

			public HoldView(View v) {
				img = (ImageView) v.findViewById(R.id.img);
				tv_name = (TextView) v.findViewById(R.id.tv_name);
				tv_right = (TextView) v.findViewById(R.id.tv_right);

				ViewParamsSetUtil.setViewHandW_rel(img, add_imgHWs[1],
						add_imgHWs[0]);

			}
		}

	}

}
