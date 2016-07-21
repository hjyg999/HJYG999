package com.md.hjyg.entity;

/** 
 * ClassName: YintongPayQuotaModel 
 * date: 2015-11-5 下午3:45:37 
 * remark:单个银行限额详情
 * @author pyc
 */
public class YintongPayQuotaModel {
	
	   public int Id ;       

       /**银行名 */
       public String BankName ;

               
       /**单笔限额  */
       public String SingleQuota ;

               
       /**单日限额  */
       public String DayQuota ;

                
       /**单月限额  */
       public String MonthQuota ;

               
       /**备注  */
       public String Remark ;

             
       /**删除否  */
       public Boolean IsDelete ;

              
       /**创建时间   */
       public String CreateTime;


}
