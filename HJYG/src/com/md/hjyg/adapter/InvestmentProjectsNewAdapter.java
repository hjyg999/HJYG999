package com.md.hjyg.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import xintou.com.xintou.com.todddavies.components.progressbar.ProgressWheel;
import com.md.hjyg.R;
import com.md.hjyg.entity.LoanModel;
import com.md.hjyg.utility.CustomDigitalClock;
import com.md.hjyg.utility.CustomDigitalClock.ClockListener;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 理财频道项目列表适配器
 */
public class InvestmentProjectsNewAdapter extends BaseAdapter {

	LayoutInflater layoutInflater;
	Context context;
	String[] array;
	String[] Status_Text;
	String Crrency_symbol = "¥";
	public ArrayList<LoanModel> LoanList = new ArrayList<LoanModel>();

	public InvestmentProjectsNewAdapter(Context context,
			ArrayList<LoanModel> LoanList) {
		this.context = context;
		this.LoanList = LoanList;
		layoutInflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		Status_Text = new String[]{"审核中","初审中", "初审通过", "竞标中", "核保审批中","平台终（复）审",
				"还款中","审核不通过","流标" ,"已还款"};
	}

	@Override
	public int getCount() {
		return LoanList.size();
	}

	@Override
	public Object getItem(int position) {
		return LoanList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		View showLeaveStatus = convertView;

		TextView tv_loan_title, tv_rate, tv_rate_value, tv_terms, tv_term_value, tv_remaining_amount, tv_amout_value, booking_percentage_text;

		if (showLeaveStatus == null) {
			holder = new ViewHolder();
			showLeaveStatus = layoutInflater.inflate(
					R.layout.template_investment_list_item_new, null);
			holder.investment_progress = (ProgressWheel) showLeaveStatus
					.findViewById(R.id.progressBar_booking);
			holder.tv_loan_title = (TextView) showLeaveStatus
					.findViewById(R.id.tv_loan_title);
			/*holder.tv_rate = (TextView) showLeaveStatus
					.findViewById(R.id.tv_rate);*/
			holder.tv_rate_value = (TextView) showLeaveStatus
					.findViewById(R.id.tv_rate_value);
			/*holder.tv_terms = (TextView) showLeaveStatus
					.findViewById(R.id.tv_terms);*/
			holder.tv_term_value = (TextView) showLeaveStatus
					.findViewById(R.id.tv_term_value);
			/*holder.tv_remaining_amount = (TextView) showLeaveStatus
					.findViewById(R.id.tv_remaining_amount);*/
			holder.tv_amout_value = (TextView) showLeaveStatus
					.findViewById(R.id.tv_amout_value);
			holder.booking_percentage_text = (TextView) showLeaveStatus
					.findViewById(R.id.booking_percentage_text);
			holder.cdc_hall_time = (CustomDigitalClock) showLeaveStatus.findViewById(R.id.cdc_hall_time);
			holder.cdc_hall_time_rel = (RelativeLayout) showLeaveStatus.findViewById(R.id.cdc_hall_time_rel);
			holder.tv_rate_add =  (TextView) showLeaveStatus.findViewById(R.id.tv_rate_add);
			holder.booking_percentage_text_fh = (TextView) showLeaveStatus.findViewById(R.id.booking_percentage_text_fh);
			holder.tv_term_days = (TextView) showLeaveStatus.findViewById(R.id.tv_term_days);
			holder.tv_amout_text = (TextView) showLeaveStatus.findViewById(R.id.tv_amout_text);

		} else {
			holder = (ViewHolder) showLeaveStatus.getTag();
		}
		showLeaveStatus.setTag(holder);
		holder.investment_progress.setVisibility(View.VISIBLE);
		holder.booking_percentage_text_fh.setVisibility(View.GONE);
		holder.tv_loan_title.setText(LoanList.get(position).Title);
		holder.tv_rate_add.setVisibility(View.GONE);
		//holder.booking_percentage_state.setVisibility(View.GONE);
		if(LoanList.get(position).ActivitiesRate > 0)
		{
			holder.tv_rate_add.setVisibility(View.VISIBLE);
			holder.tv_rate_add.setText(" +" + (LoanList.get(position).ActivitiesRate+"").replace(".0", "") + "% ");
			holder.tv_rate_value.setText(Constants.StringToCurrency((LoanList.get(position).LoanRate - LoanList.get(position).ActivitiesRate) +""));
		}else {
			
			holder.tv_rate_value.setText(Constants.StringToCurrency(LoanList.get(position).LoanRate + ""));
		}
		
		holder.tv_term_value.setText(LoanList.get(position).LoanTerm +"");
		if(LoanList.get(position).LoanDateType == 0)
		{
			holder.tv_term_days.setText("个月");
		}else if(LoanList.get(position).LoanDateType == 2){
			holder.tv_term_days.setText("天");	
		}else if(LoanList.get(position).LoanDateType == 4){
			holder.tv_term_days.setText("周");
		}
		
		/*if(LoanList.get(position).LoanTerm == 7 || LoanList.get(position).LoanTerm == 30)
		{
			holder.tv_term_value.setText(LoanList.get(position).LoanTerm + "天");	
		}else {
			holder.tv_term_value.setText(LoanList.get(position).LoanTerm + "个月");	
		}*/
		
		if((Constants.StringToCurrency(LoanList.get(position).Amount)).indexOf("0,000.00") != -1)
		{
//			while (value.lastIndexOf(",") != -1) {
//				value = value.replace(",", "");
//			}
			String value = (LoanList.get(position).Amount).replace("0000", "");
			holder.tv_amout_value.setText(value);
			holder.tv_amout_text.setText("万元");
		}else {
			holder.tv_amout_value.setText((Constants.StringToCurrency(LoanList.get(position).Amount)).replace(".00", ""));
			holder.tv_amout_text.setText("元");
			
		}
		
		int Status = LoanList.get(position).Status;
		holder.cdc_hall_time_rel.setVisibility(View.GONE);
		holder.booking_percentage_text.setVisibility(View.VISIBLE);

//		if (LoanList.get(position).InvestProcess >= 100){
//			holder.booking_percentage_text.setText("100");
//		} else {
//			holder.booking_percentage_text.setText(Constants.StringToCurrency( LoanList.get(position).InvestProcess +""));
//		}
//		
		switch (Status) {
		case 7: //还款中 深灰色显示
			holder.booking_percentage_text.setText("还款中");
			//holder.booking_percentage_state.setTextColor(context.getResources().getColor(R.color.gray_s));
			holder.booking_percentage_text.setTextColor(context.getResources().getColor(R.color.gray_s));
			holder.investment_progress.progress = ((int) (3.6 * 100));
			holder.investment_progress.setBarColor(context.getResources().getColor(R.color.gray_s));
			break;
		case 4: //竞标中  
			if(!isStart(LoanList.get(position).BiddingStratTime))
			{//已经发布，红色显示
				//holder.cdc_hall_time.setVisibility(View.GONE);
				//holder.booking_percentage_text.setVisibility(View.VISIBLE);
				//holder.booking_percentage_state.setText("竞标中");
				//holder.booking_percentage_state.setTextColor(context.getResources().getColor(R.color.red));
				holder.booking_percentage_text.setTextColor(context.getResources().getColor(R.color.red));
				//传换成两位小数
				//holder.booking_percentage_text.setText(Constants.StringToCurrency( LoanList.get(position).InvestProcess +""));
				holder.booking_percentage_text.setText( LoanList.get(position).InvestProcess +"");
				holder.investment_progress.progress = ((int) (3.6 * LoanList
						.get(position).InvestProcess));
				holder.booking_percentage_text_fh.setVisibility(View.VISIBLE);
				/*holder.booking_percentage_text
				.setText("竞标中\r\n" +Constants.StringToCurrency( LoanList.get(position).InvestProcess +"")+ "%");*/
				holder.investment_progress.setBarColor(context.getResources().getColor(R.color.red));
				
			}
			else 
			{//即将发布、黄色显示
				holder.cdc_hall_time_rel.setVisibility(View.VISIBLE);
				holder.cdc_hall_time.setEndTime(getLongTime(LoanList.get(position).BiddingStratTime));
				holder.cdc_hall_time.setTextColor(context.getResources().getColor(R.color.red));
				holder.booking_percentage_text.setVisibility(View.GONE);
				holder.cdc_hall_time.setClockListener(new ClockListener() {
					
					@Override
					public void timeEnd() {
						InvestmentProjectsNewAdapter.this.notifyDataSetChanged();
					}
					
					@Override
					public void remainFiveMinutes() {	
					}
				});
				//holder.booking_percentage_state.setVisibility(View.GONE);
				//holder.booking_percentage_text.setText("即将发布");
				//holder.booking_percentage_text.setTextColor(context.getResources().getColor(R.color.yellow));
				//holder.investment_progress.progress = ((int) (3.6 * LoanList
						//.get(position).InvestProcess));
				//holder.booking_percentage_text.setText("即将开始\r\n" + getLongTime(LoanList.get(position).BiddingStratTime));
				//holder.investment_progress.setBarColor(context.getResources().getColor(R.color.red));
				//holder.investment_progress.setBarColor(context.getResources().getColor(R.color.yellow));

				holder.investment_progress.setVisibility(View.GONE);
			}
			break;
		case 5: //核保审批中  灰色显示
			holder.booking_percentage_text.setText("审批中");
			//holder.booking_percentage_state.setText("核保审批中");
			//holder.booking_percentage_state.setTextColor(context.getResources().getColor(R.color.gray));
			holder.booking_percentage_text.setTextColor(context.getResources().getColor(R.color.gray));
			holder.investment_progress.progress = ((int) (3.6 * 100));
			holder.investment_progress.setBarColor(context.getResources().getColor(R.color.gray));
			break;
		//case 10: //还款完成 浅灰色显示
			//holder.booking_percentage_text.setText("还款完成");
		default:
			//holder.booking_percentage_state.setText(Status_Text[Status - 1]);
			//holder.booking_percentage_state.setTextColor(context.getResources().getColor(R.color.gray_q));
			holder.booking_percentage_text.setText(Status_Text[Status - 1]);
			holder.booking_percentage_text.setTextColor(context.getResources().getColor(R.color.gray_q));
			holder.investment_progress.progress = ((int) (3.6 * 100));
			holder.investment_progress.setBarColor(context.getResources().getColor(R.color.gray_q));
			break;
		}

//		if (LoanList.get(position).InvestProcess == 100) {
//			//holder.booking_percentage_text.setText("SOLD");
//			//增加颜色区分
//			holder.booking_percentage_text.setText("还款中");
//			holder.booking_percentage_text.setTextColor(context.getResources().getColor(R.color.green));
//			holder.investment_progress.progress = ((int) (3.6 * 100));
//			
//			holder.investment_progress.setBarColor(context.getResources().getColor(R.color.green));
//			//Log.d("vivi", position + "还款中" + LoanList.get(position).InvestProcess+holder.investment_progress.getBarColor());
//		} else {
//			//竞标显示红色
//			holder.booking_percentage_text.setTextColor(context.getResources().getColor(R.color.red));
//			holder.investment_progress.progress = ((int) (3.6 * LoanList
//					.get(position).InvestProcess));
//			holder.booking_percentage_text
//					.setText(LoanList.get(position).InvestProcess + "%");
//			holder.investment_progress.setBarColor(context.getResources().getColor(R.color.red));
//			//Log.d("vivi", position +  "竞标-->" + LoanList.get(position).InvestProcess+holder.investment_progress.getBarColor());
//		}

		return showLeaveStatus;
	}

	public static class ViewHolder {
		TextView tv_loan_title, tv_rate, tv_rate_value, tv_terms,
				tv_term_value, tv_remaining_amount, tv_amout_value,
				booking_percentage_text ,tv_rate_add ,tv_term_days ,booking_percentage_text_fh,tv_amout_text;
		CustomDigitalClock cdc_hall_time;
		// Progress wheel to indicate..
		ProgressWheel investment_progress;
		RelativeLayout cdc_hall_time_rel;
	}
	
	private boolean isStart(String str)
	{
		//2015-08-20T13:38:04
		str = str.replace("T", " ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date newTime = new Date();
		Date date =null;
		try {
			date = sdf.parse(str);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date.after(newTime);
	}
	
	private long getLongTime(String str)
	{
		str = str.replace("T", " ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date newTime = new Date();
		long time = 0;
		String stingTime = null;
		Date date =null;
		try {
			date = sdf.parse(str);
			time = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return time;
	}
}

