package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.activities.GoldBaoAddAddressActivity;
import com.md.hjyg.entity.GoldAddressModel;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * ClassName: GoldAddressListAdapter 
 * date: 2016-1-28 上午10:53:57 
 * remark: 黄金宝地址列表适配器
 * @author pyc
 */
public class GoldAddressListAdapter extends BaseAdapter{
	
	private List<GoldAddressModel> lists;
	private Activity mActivity;
	private LayoutInflater layoutInflater;
	private int state;
	
	public GoldAddressListAdapter(Activity mActivity,List<GoldAddressModel> lists){
		this.mActivity = mActivity;
		this.lists = lists;
		layoutInflater = LayoutInflater.from(mActivity);
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public GoldAddressModel getItem(int position) {
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
			convertView = layoutInflater.inflate(R.layout.edit_addresslist_adapter, null);
			holder = new ViewHolder(convertView);
			
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		GoldAddressModel model = lists.get(position);
		holder.tv_name.setText(model.getName());
		holder.tv_phone.setText(model.getPhone());
		holder.tv_address.setText(model.getAddress());
		if (model.isIsdefault()) {
			holder.tv_isdefault.setVisibility(View.VISIBLE);
		}else {
			holder.tv_isdefault.setVisibility(View.GONE);
		}
		
		holder.img_right.setTag(position);
		holder.img_left.setTag(position);
		
		if (state == 0) {
			holder.img_left.setVisibility(View.GONE);
			holder.img_right.setImageResource(R.drawable.gold_yes);
			if (model.isIsdefault()) {
				holder.img_right.setVisibility(View.VISIBLE);
			}else {
				holder.img_right.setVisibility(View.GONE);
			}
			
		}else {//==1
			holder.img_left.setVisibility(View.VISIBLE);
			if (model.isChecked()) {
				holder.img_left.setImageResource(R.drawable.gold_ckeckbox_yes);
			}else {
				holder.img_left.setImageResource(R.drawable.gold_ckeckbox_no);
			}
			holder.img_right.setImageResource(R.drawable.gold_address_edit);
			holder.img_right.setVisibility(View.VISIBLE);
			
		}
		return convertView;
	}
	
	 class ViewHolder {
		private TextView tv_name, tv_phone, tv_isdefault,tv_address;
		private ImageView img_left,img_right;
		
		public ViewHolder(View convertView){
			img_left = (ImageView) convertView.findViewById(R.id.img_left);
			tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
			tv_isdefault = (TextView) convertView.findViewById(R.id.tv_isdefault);
			tv_address = (TextView) convertView.findViewById(R.id.tv_address);
			img_right = (ImageView) convertView.findViewById(R.id.img_right);
			img_left.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int position = (Integer) v.getTag();
					if (lists.get(position).isChecked()) {
						lists.get(position).setChecked(false);
						((ImageView)v).setImageResource(R.drawable.gold_ckeckbox_no);
					}else {
						lists.get(position).setChecked(true);
						((ImageView)v).setImageResource(R.drawable.gold_ckeckbox_yes);
						
					}
				}
			});
			
			//修改时跳转
			img_right.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int position = (Integer) v.getTag();
					if (state == 1) {
						Intent intent = new Intent(mActivity, GoldBaoAddAddressActivity.class);
						intent.putExtra("mode", lists.get(position));
						intent.putExtra("type", 1);
						mActivity.startActivity(intent);
						mActivity.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_lift_out);
					}
				}
			});
		}
	}
	
	public void setState(int state){
		this.state = state;
	}
	
	public int getState(){
		return state;
	}

}
