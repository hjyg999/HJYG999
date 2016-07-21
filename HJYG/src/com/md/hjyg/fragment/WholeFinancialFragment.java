package com.md.hjyg.fragment;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.md.hjyg.adapter.FinancialAdaper;
import com.md.hjyg.adapter.WholeFinancialAdaper;
import com.md.hjyg.entity.FinancialDetails;
import com.md.hjyg.entity.ListModel;
import com.md.hjyg.xinTouConstant.Constants;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * ClassName: WholeFinancialFragment date: 2016-3-23 上午11:39:49 remark:资金记录-全部
 * 
 * @author pyc
 */
public class WholeFinancialFragment extends FinancialFragment<ListModel> {
	private int sort;
	private String[] URLs;
	
	public WholeFinancialFragment(){
		
	}
	public WholeFinancialFragment(int sort){
		this.sort = sort;
		URLs = new String[3];
		URLs[0] = Constants.GetFinanceLogs_URL;
		URLs[1] = Constants.GetRechargeLogs_URL;
		URLs[2] = Constants.GetFinanceLogs_URL;
		if (sort == WholeFinancialAdaper.Income) {
			this.ftype = - 1 + "";
		}
	}

	@Override
	public void getFinancialDetailsWebservice(String ftype,final int pageIndex) {

		dft.getWholeListDetails(ftype, "", "", pageIndex + "",
				URLs[sort], Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						FinancialDetails financialDetails = (FinancialDetails) dft
								.GetResponseObject(response,
										FinancialDetails.class);
						if (pageIndex == 1) {
							SetUIData("", financialDetails.List);
						} else {
							SetUIData("loadmore", financialDetails.List);

						}
					}
				}, null);

	}

	@Override
	public FinancialAdaper<ListModel> getAdaper(List<String> mapKey,
			Map<String, List<ListModel>> map) {
		return new WholeFinancialAdaper(getActivity(), map, mapKey,sort);
	}

	@Override
	public String getYearTime(ListModel t) {
		return t.Time;
	}

	

}
