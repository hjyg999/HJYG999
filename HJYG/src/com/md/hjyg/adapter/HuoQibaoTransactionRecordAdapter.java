package com.md.hjyg.adapter;

import java.util.ArrayList;
import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.SingleLoanInvestModel;
import com.md.hjyg.layoutEntities.CircularImage;
import com.md.hjyg.utils.DateUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/** 
 * ClassName: HuoQibaoTransactionRecordAdapter 
 * date: 2015-11-13 上午9:12:48 
 * remark:活期宝用户交易记录适配器
 * @author pyc
 */
public class HuoQibaoTransactionRecordAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater layoutInflater;
	private List<SingleLoanInvestModel> lists = new ArrayList<SingleLoanInvestModel>();
	private String mobilePhone, amount, createtime;
	String Crrency_symbol = "¥";

	public HuoQibaoTransactionRecordAdapter(Context context,
			List<SingleLoanInvestModel> lists) {
		this.lists = lists;
		this.context = context;
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {

		return lists.size();
	}

	@Override
	public Object getItem(int position) {

		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(
					R.layout.template_transaction_record_item, null);
			holder.tv_encrypted_name = (TextView) convertView
					.findViewById(R.id.tv_encrypted_name);
			holder.tv_date_of_investment = (TextView) convertView
					.findViewById(R.id.tv_date_of_investment);
			holder.tv_amount_invested = (TextView) convertView
					.findViewById(R.id.tv_amount_invested);
			holder.tv_amount_invested.setTextColor(context.getResources().getColor(R.color.gray));
			holder.img_experiencegold = (ImageView) convertView
					.findViewById(R.id.img_experiencegold);
			holder.head_sex = (CircularImage) convertView
					.findViewById(R.id.head_sex);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		SingleLoanInvestModel model = lists.get(position);
		mobilePhone = model.MemberPhone;
		if (mobilePhone != null && !mobilePhone.equals("")){
			holder.tv_encrypted_name.setText(mobilePhone);
		}
		
		amount = model.Amount.toString();
		holder.tv_amount_invested.setText(amount);
		createtime = model.CreateTime.toString();
		holder.tv_date_of_investment.setText(DateUtil.removeYS(createtime));
		
		//体验金图标是否显示
		if (model.Type ==0 ) {
			holder.img_experiencegold.setVisibility(View.GONE);
		}else {
			holder.img_experiencegold.setVisibility(View.VISIBLE);
		}
		//头像
		if (model.Photo == null || model.Photo.length() == 0) {
			holder.head_sex.setImageResource(R.drawable.headimg);
		}else {
			try {
				Picasso.with(context).load(model.Photo).error(R.drawable.headimg).into(holder.head_sex);
			} catch (Exception e) {
				holder.head_sex.setImageResource(R.drawable.headimg);
			}
		}

		return convertView;
	}

	 class ViewHolder {
		TextView tv_encrypted_name, tv_date_of_investment, tv_amount_invested;
		ImageView img_experiencegold;
		CircularImage head_sex;
	}

}
