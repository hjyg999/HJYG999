package com.md.hjyg.adapter;

import java.util.ArrayList;

import com.md.hjyg.R;
import com.md.hjyg.entity.TransactionList;
import com.md.hjyg.layoutEntities.CircularImage;
import com.md.hjyg.utils.DateUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * 交易记录列表适配器
 */
public class TransactionRecordAdapter extends BaseAdapter {

	Context context;
	LayoutInflater layoutInflater;
	public ArrayList<TransactionList> gettransaction_data = new ArrayList<TransactionList>();
	String member_name, mobilePhone, amount, createtime;
//	String Crrency_symbol = "¥";

	public TransactionRecordAdapter(Context c,
			ArrayList<TransactionList> transaction_list_data) {
		gettransaction_data = transaction_list_data;
		context = c;
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {

		return gettransaction_data.size();
	}

	@Override
	public Object getItem(int position) {

		return gettransaction_data.get(position);
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
			holder.head_sex = (CircularImage) convertView
					.findViewById(R.id.head_sex);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setTag(holder);
		TransactionList model = gettransaction_data.get(position);
		member_name = model.MemberName.toString();
		mobilePhone = model.MobilePhone.toString();
		if (mobilePhone != null && !mobilePhone.equals(""))
			holder.tv_encrypted_name.setText(mobilePhone);
		else
			holder.tv_encrypted_name.setText(member_name);
		amount = model.Amount.toString();
		holder.tv_amount_invested.setText("" + amount);
		createtime = model.CreateTime.toString();
		holder.tv_date_of_investment.setText(DateUtil.removeYS(createtime));
		
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

	public class ViewHolder {
		TextView tv_encrypted_name, tv_date_of_investment, tv_amount_invested;
		CircularImage head_sex;
	}

}
