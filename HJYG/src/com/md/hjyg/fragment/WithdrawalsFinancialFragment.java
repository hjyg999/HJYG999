package com.md.hjyg.fragment;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.md.hjyg.adapter.FinancialAdaper;
import com.md.hjyg.adapter.WithdrawalsFinancialAdapter;
import com.md.hjyg.entity.WithdrawalsList;
import com.md.hjyg.entity.WithdrawalsRecodes;
import com.md.hjyg.xinTouConstant.Constants;

import com.android.volley.Request;
import com.android.volley.Response;

/** 
 * ClassName: WithdrawalsFinancialFragment 
 * date: 2016-4-9 下午4:32:07 
 * remark:提现记录
 * @author pyc
 */
public class WithdrawalsFinancialFragment extends FinancialFragment<WithdrawalsList>{
	
	public WithdrawalsFinancialFragment(){
		ftype = 5 + "";
	}

	@Override
	public void getFinancialDetailsWebservice(String ftype,final int pageIndex) {
		dft.getWithdrawalRecordDetails(ftype, "", "", pageIndex +"",
				Constants.WithdrawLogsLogs_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						WithdrawalsRecodes withdrawalsDetails = (WithdrawalsRecodes) dft
								.GetResponseObject(response,
										WithdrawalsRecodes.class);
						if (pageIndex == 1) {
							SetUIData("", withdrawalsDetails.WithdrawLogsList);
						} else {
							//提现一次性加载完了，没做分页
							SetUIData("loadmore", null);

						}

					}
				},null);
	}

	@Override
	public FinancialAdaper<WithdrawalsList> getAdaper(List<String> mapKey,
			Map<String, List<WithdrawalsList>> map) {
		return new WithdrawalsFinancialAdapter(getActivity(), map, mapKey);
	}

	@Override
	public String getYearTime(WithdrawalsList t) {
		return t.CreateTime;
	}

	

}
