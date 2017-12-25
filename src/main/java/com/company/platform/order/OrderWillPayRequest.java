package com.company.platform.order;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class OrderWillPayRequest implements Serializable{
		private String userid="";
		private Date createTime;
		private Date expireTime;
	//付款商家信息title
		private String title_desc_seller;
		//付款商品信息title
		private String title_goods;
		
		//支付金额类型
		private String fee_type="CNY";
		//向支付渠道发送请求的支付的总金额
		private String total_fee;
		//原始总价格
		private double originalTotalFee;
		/**
		 * 付款方式，支付宝，微信，货到付款或者其他，保存json字符串
		 */
		private List<String> payments;
		
		private String crcKey;
		/**
		 *支付方式
		 */
		private String paymentType="0";
		/**
		 * 为别的订单付款，用于拆单的业务需求
		 */
		private String payChildOrders = "";
		
		public String getTitle_desc_seller() {
			return title_desc_seller;
		}

		public void setTitle_desc_seller(String title_desc_seller) {
			this.title_desc_seller = title_desc_seller;
		}

		public String getTitle_goods() {
			return title_goods;
		}

		public void setTitle_goods(String title_goods) {
			this.title_goods = title_goods;
		}

		public String getFee_type() {
			return fee_type;
		}

		public void setFee_type(String fee_type) {
			this.fee_type = fee_type;
		}

		public String getTotal_fee() {
			return total_fee;
		}

		public void setTotal_fee(String total_fee) {
			this.total_fee = total_fee;
		}

		public List<String> getPayments() {
			return payments;
		}

		public void setPayments(List<String> payments) {
			this.payments = payments;
		}

		public String getCrcKey() {
			return crcKey;
		}

		public void setCrcKey(String crcKey) {
			this.crcKey = crcKey;
		}
		
		
		
		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = formatDate(createTime);
		}
		protected Date formatDate(Date sourceDate) {
			if (sourceDate != null) {
				Calendar calenCreate = Calendar.getInstance();
				calenCreate.setTime(sourceDate);
				calenCreate.set(Calendar.MILLISECOND, 0);
				return calenCreate.getTime();
			}
			return sourceDate;
		}

		public Date getExpireTime() {
			return expireTime;
		}

		public void setExpireTime(Date expireTime) {
			this.expireTime = expireTime;
		}

		public String getPaymentType() {
			return paymentType;
		}
		
		

		public double getOriginalTotalFee() {
			return originalTotalFee;
		}

		public void setOriginalTotalFee(double originalTotalFee) {
			this.originalTotalFee = originalTotalFee;
		}

		public void setPaymentType(String paymentType) {
			this.paymentType = paymentType;
		}

		
		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

		public String getPayChildOrders() {
			return payChildOrders;
		}

		public void setPayChildOrders(String payChildOrders) {
			this.payChildOrders = payChildOrders;
		}

		public boolean crcOk(String crcResult)
		{
			if(this.crcKey.compareToIgnoreCase(crcResult)==0)
				return true;
			return false;
		}
		public String createCrcKey(String crckey)  {
		    try {
				StringBuilder str = new StringBuilder();
				str.append(this.getUserid());
				str.append(this.getFee_type());
				str.append(this.getTotal_fee());
				str.append(this.paymentType);
				str.append(this.payChildOrders);
				if(crckey==null||crckey=="")
				{
					str.append("lihairong");	
				}
				else
				{
					str.append(crckey);
				}
				return MD5(str.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace()
				;
			}
		    return null;
            
		}
		
	
		
		public  String MD5(String data) throws Exception {
	        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] array = md.digest(data.getBytes("UTF-8"));
	        StringBuilder sb = new StringBuilder();
	        for (byte item : array) {
	            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
	        }
	        return sb.toString().toUpperCase();
	    }
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
		
}
