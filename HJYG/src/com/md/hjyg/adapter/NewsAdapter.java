package com.md.hjyg.adapter;

import java.util.ArrayList;

import com.md.hjyg.R;
import com.md.hjyg.entity.MessageList;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 用户系统信息适配器
 */
public class NewsAdapter extends BaseAdapter implements
		OnClickListener {
	LayoutInflater layoutInflater;
	Context context;
	String[] array;
	String news_title, id, news_time;
	int news_id;
	boolean isRead, isUnread, isShow, isChecked;
	public ArrayList<MessageList> messageList = new ArrayList<MessageList>();

	CheckboxIscheckedListener listener;

	public NewsAdapter(Context context, ArrayList<MessageList> messageList) {
		this.context = context;
		this.messageList = messageList;
		layoutInflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return messageList.size();
	}

	@Override
	public MessageList getItem(int position) {
		return messageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		View showLeaveStatus = convertView;
		if (showLeaveStatus == null) {
			holder = new ViewHolder();
			showLeaveStatus = layoutInflater.inflate(R.layout.news_item, null);
			holder.tv_news_title = (TextView) showLeaveStatus
					.findViewById(R.id.tv_news_title);
			holder.checkbox_news = (ImageView) showLeaveStatus
					.findViewById(R.id.checkbox_news);
			holder.tv_news_time = (TextView) showLeaveStatus
					.findViewById(R.id.tv_news_time);
			holder.read_status_icon = (ImageView) showLeaveStatus
					.findViewById(R.id.read_status_icon);

		} else {
			holder = (ViewHolder) showLeaveStatus.getTag();
		}
		showLeaveStatus.setTag(holder);
		holder.checkbox_news.setTag(position);
		news_title = messageList.get(position).Title.toString();
		news_id = messageList.get(position).Id;
		holder.tv_news_title.setText(news_title);
		isRead = messageList.get(position).IsRead;

		news_time = messageList.get(position).CreateTime.toString();
		holder.tv_news_time.setText(DateUtil.removeYS(news_time));

		if (isRead == true) {
			holder.tv_news_title.setTextColor(context.getResources().getColor(
					R.color.gray));
			holder.read_status_icon.setBackground(context.getResources().getDrawable(R.drawable.news_read));
		} else {
			holder.tv_news_title.setTextColor(context.getResources().getColor(
					R.color.gray_gold));
			holder.read_status_icon.setBackground(context.getResources().getDrawable(R.drawable.news_unread));
		}
		if (isShow) {
			holder.checkbox_news.setVisibility(View.VISIBLE);
		} else {
			holder.checkbox_news.setVisibility(View.GONE);
		}
		holder.checkbox_news.setBackground(context.getResources().getDrawable(R.drawable.news_default_img));
		boolean isChecked = messageList.get(position).isChecked;
		if(isChecked){
			holder.checkbox_news.setBackground(context.getResources().getDrawable(R.drawable.news_click_img));
		} else {
			holder.checkbox_news.setBackground(context.getResources().getDrawable(R.drawable.news_default_img));
		}
		// 重新加载时更新全选框
		if (position == 0 && !messageList.get(position).isChecked) {
			listener.CheckboxIschecked(false);
		}
		holder.checkbox_news.setOnClickListener(this);

		return showLeaveStatus;
	}

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		news_id = messageList.get(position).Id;
		id = String.valueOf(news_id);
		Constants.NewsID = id;

		if(messageList.get(position).isChecked){
			v.setBackground(context.getResources().getDrawable(R.drawable.news_default_img));
			messageList.get(position).isChecked = false;
		} else {
			v.setBackground(context.getResources().getDrawable(R.drawable.news_click_img));
			messageList.get(position).isChecked = true;
		}
		if (listener != null) {
			listener.CheckboxIschecked(itemIsCheckedAll());
		}
	}

	public static class ViewHolder {
		TextView tv_news_title, tv_news_time;
		ImageView checkbox_news;
		ImageView read_status_icon;
	}

	/** 判断条目是不是被全选 */
	protected boolean itemIsCheckedAll() {
		for (MessageList message : messageList) {
			if (!message.isChecked) {
				return false;
			}
		}
		return true;
	}

	/** 定义一个接口，用于判断是否还处于全选状态 */
	public interface CheckboxIscheckedListener {
		public void CheckboxIschecked(Boolean isAll);
	}

	/** 设置接口监听 */
	public void SetCheckboxIscheckedListener(
			CheckboxIscheckedListener checkboxIscheckedListener) {
		this.listener = checkboxIscheckedListener;
	}
	
	/** 控制选择框是否显示 */
	public void setIsShowCheckBox(boolean isShow){
		this.isShow = isShow;
	}
}
