package com.company.pay.wechat.domain;

public class WeChatPayConst {
	/**
	 * [11201:11300]
	 */
	public static final int RESULT_Success = 0;	
	public static final int RESULT_Error_Fail = 11201;	
	public static final int RESULT_Error_NoPayInfo =RESULT_Error_Fail+1;
	public static final String RESULT_Error_NoPayInfo_MSG="Error_NoPayInfo";
	
	public static final String DEVICE_INFO_WEB="WEB";
	public static final String DEVICE_INFO_APP="APP";
	
	public static final String FEE_TYPE_CNY="CNY";

    public static final String TRADE_TYPE_SCAN="NATIVE";
    public static final String TRADE_TYPE_APP="APP";
    public static final String TRADE_TYPE_MICROPAY = "MICROPAY";
    
    
    public static final String ORDERKEY_PAYINFO="payinfo";

}
