package com.company.coursestudent.domain;

public class StudentConst {
	
	
	public static final String ORDER_ID_NULL = "000000";	
	/**
	 * 订单需要付款
	 */
	public static final int ORDER_BUYER_STATUS_NEEDPAY=512;
	
	public static final int RESULT_Success = 0;	
	public static final int RESULT_Error_Fail = 12100;	
	
	public static final int RESULT_Error_money=RESULT_Error_Fail+1;
	
	public static final int RESULT_Error_HAVEPAID=RESULT_Error_Fail+2;
	
	public static final int RESULT_Error_ORDERID_NULL=RESULT_Error_Fail+3;
	
	public static final int RESULT_Error_MISSING_COURSE=RESULT_Error_Fail+4;
	
	
	public static final int RESULT_Error_MISSING_COURSEClass=RESULT_Error_Fail+5;
	
	public static final String RESULT_String_ORDERID_NULL="orderid is null";
	
	/**
	 * 用于在订购订单中记录原始的订单信息的KEY
	 */
	public static final String ORDERKEY_ORDER="order";
	

	
	//学生购买订单
	//对于用户信息的订单ID固定填写000000
	public static final String USER_BUYER_ORDERID="000000";
	public static final String USER_BUYER_CATEGORY="ubuyer";
	public static final String USER_DRAFT_CATEGORY="tcoursepub";
	
	public static final String USER_DRAFT_COURSEKey="cDraftKey";
	
	
	public static final String ORDER_BUYER_CATEGORY="coursebuyer";

	public static final String PAY_QRCODEURL_KEY="code_url";
	public static final String PAY_TOTAL_FEE_KEY="total_fee";
	public static final String PAY_FEE_TYPE_KEY="fee_type";
	/**
	 * 用于在payresultInfo中记录支付的订单ID
	 */
	public static final String PAYRESULT_KEY_payOrder="pay_order*#";
	
	
	//public static final String BUYER_CATEGORY="coursebuyer";

}
