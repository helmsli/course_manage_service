package com.company.pay.wechat;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:pay.properties"},encoding="utf-8")  
@ConditionalOnProperty(name = "pay.webChat.enable")
@ConfigurationProperties(prefix = "pay.webChat")
public class WeChatConfig {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1743061893127547008L;
	/**
	 * 商户信息配置
	 */
	// 微信开发平台应用id
	private String APP_ID = "wx3ccbf4ac04ebd137";

	// 应用对应凭证
	private String APP_SECRET = "ii";

	// 商家号
	private String MCH_ID = "1480876362";

	// api key 校验用，重要
	private String API_KEY = "xinwei2400weixinsaomazhifu000000";

	/**
	 * url配置
	 */
	// 微信预订单统一下单url,固定地址
	private String UFDODER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	// 微信订单关闭地址
	private String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";

	// 微信订单状态查询地址
	private String QUERY_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

	// 微信订单退款地址
	private String REFUND_ORDER_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	// 微信退单状态查询地址
	private String QUERY_REFUND_ORDER_URL = "https://api.mch.weixin.qq.com/pay/refundquery";

	// 账单下载地址
	private String CHECK_BILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";

	// 微信支付成功回调url,配置商户公网url
	private String NOTIFY_URL = "https://3af79d0b.ngrok.io/weichat_pay/wx_notify";

	
	
	
	public String getAPP_ID() {
		return APP_ID;
	}

	public void setAPP_ID(String aPP_ID) {
		APP_ID = aPP_ID;
	}

	public String getAPP_SECRET() {
		return APP_SECRET;
	}

	public void setAPP_SECRET(String aPP_SECRET) {
		APP_SECRET = aPP_SECRET;
	}

	public String getMCH_ID() {
		return MCH_ID;
	}

	public void setMCH_ID(String mCH_ID) {
		MCH_ID = mCH_ID;
	}

	public String getAPI_KEY() {
		return API_KEY;
	}

	public void setAPI_KEY(String aPI_KEY) {
		API_KEY = aPI_KEY;
	}

	public String getUFDODER_URL() {
		return UFDODER_URL;
	}

	public void setUFDODER_URL(String uFDODER_URL) {
		UFDODER_URL = uFDODER_URL;
	}

	public String getCLOSE_ORDER_URL() {
		return CLOSE_ORDER_URL;
	}

	public void setCLOSE_ORDER_URL(String cLOSE_ORDER_URL) {
		CLOSE_ORDER_URL = cLOSE_ORDER_URL;
	}

	public String getQUERY_ORDER_URL() {
		return QUERY_ORDER_URL;
	}

	public void setQUERY_ORDER_URL(String qUERY_ORDER_URL) {
		QUERY_ORDER_URL = qUERY_ORDER_URL;
	}

	public String getREFUND_ORDER_URL() {
		return REFUND_ORDER_URL;
	}

	public void setREFUND_ORDER_URL(String rEFUND_ORDER_URL) {
		REFUND_ORDER_URL = rEFUND_ORDER_URL;
	}

	public String getQUERY_REFUND_ORDER_URL() {
		return QUERY_REFUND_ORDER_URL;
	}

	public void setQUERY_REFUND_ORDER_URL(String qUERY_REFUND_ORDER_URL) {
		QUERY_REFUND_ORDER_URL = qUERY_REFUND_ORDER_URL;
	}

	public String getNOTIFY_URL() {
		return NOTIFY_URL;
	}

	public void setNOTIFY_URL(String nOTIFY_URL) {
		NOTIFY_URL = nOTIFY_URL;
	}

	public String getCHECK_BILL_URL() {
		return CHECK_BILL_URL;
	}

	public void setCHECK_BILL_URL(String cHECK_BILL_URL) {
		CHECK_BILL_URL = cHECK_BILL_URL;
	}

	@Override
	public String toString() {
		return "WeiChatConfig [APP_ID=" + APP_ID + ", APP_SECRET=" + APP_SECRET
				+ ", MCH_ID=" + MCH_ID + ", API_KEY=" + API_KEY
				+ ", UFDODER_URL=" + UFDODER_URL + ", CLOSE_ORDER_URL="
				+ CLOSE_ORDER_URL + ", QUERY_ORDER_URL=" + QUERY_ORDER_URL
				+ ", REFUND_ORDER_URL=" + REFUND_ORDER_URL
				+ ", QUERY_REFUND_ORDER_URL=" + QUERY_REFUND_ORDER_URL
				+ ", CHECK_BILL_URL=" + CHECK_BILL_URL + ", NOTIFY_URL="
				+ NOTIFY_URL + "]";
	}

}
