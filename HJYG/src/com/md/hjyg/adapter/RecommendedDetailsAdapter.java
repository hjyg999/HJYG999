package com.md.hjyg.adapter;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

import com.md.hjyg.R;
import com.md.hjyg.entity.modelList;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 推荐人收益详情
 */
public class RecommendedDetailsAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    String[] array;
    String Crrency_symbol ="¥";
    public ArrayList<modelList> modelList = new ArrayList<modelList>();

    int month,year,first_count;
    double unpaid_rewards_count,paid_rewards_count,first_income,first_reward;
    int unpaid_rewards,paid_rewards,recommended_count,list_count,friends_have_been_incorporated_count;
    String mon,Month;



	public RecommendedDetailsAdapter(Context context,
			ArrayList<modelList> modelList) {
        this.context = context;
        this.modelList = modelList;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View showLeaveStatus = convertView;

        if (showLeaveStatus == null)
        {
            holder = new ViewHolder();
            showLeaveStatus = layoutInflater.inflate(R.layout.recommended_list_item, null);
            holder.tv_date = (TextView) showLeaveStatus.findViewById(R.id.tv_date);
            holder.tv_recommended_no_value = (TextView) showLeaveStatus.findViewById(R.id.tv_recommended_no_value);
            holder.tv_newlisting_value = (TextView) showLeaveStatus.findViewById(R.id.tv_newlisting_value);
            holder.tv_friends_have_been_incorporated_count = (TextView) showLeaveStatus.findViewById(R.id.tv_friends_have_been_incorporated_count);
            holder.tv_rewards_paid_count = (TextView) showLeaveStatus.findViewById(R.id.tv_rewards_paid_count);
            holder.tv_unpaid_rewards_count = (TextView) showLeaveStatus.findViewById(R.id.tv_unpaid_rewards_count);

        } else
        {
            holder = (ViewHolder) showLeaveStatus.getTag();
        }
        showLeaveStatus.setTag(holder);


        year = modelList.get(position).Year;
        month = modelList.get(position).Month;
        mon = getMonth(month);
        Month = mon.substring(0, Math.min(mon.length(), 3));

        unpaid_rewards_count = modelList.get(position).FirstUnfinishedInterestReward;
        unpaid_rewards = (int) unpaid_rewards_count;
        paid_rewards_count = modelList.get(position).FirstReward;
        paid_rewards = (int) paid_rewards_count;
        recommended_count = modelList.get(position).RecommendCount;
        list_count = modelList.get(position).NewCount;
        first_count = modelList.get(position).FirstCount;
        first_income = modelList.get(position).FirstInCome;
        first_reward = modelList.get(position).Total;
        friends_have_been_incorporated_count = modelList.get(position).FirstCount;

        holder.tv_recommended_no_value.setText(String.valueOf(recommended_count));
        holder.tv_newlisting_value.setText(String.valueOf(first_count));
        holder.tv_friends_have_been_incorporated_count.setText(Crrency_symbol + Constants.StringToCurrency(first_income +""));
        holder.tv_rewards_paid_count.setText(Crrency_symbol + Constants.StringToCurrency(first_reward+""));
        holder.tv_unpaid_rewards_count.setText(Crrency_symbol + Constants.StringToCurrency(unpaid_rewards_count+""));
       
        holder.tv_date.setText(year +"年 " + Month.replace("月", "") + "月");

        return showLeaveStatus;
    }

    public static class ViewHolder
    {
        TextView tv_date,tv_recommended_no_value,tv_newlisting_value,tv_friends_have_been_incorporated_count,tv_rewards_paid_count,tv_unpaid_rewards_count;

    }
    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }
    }

