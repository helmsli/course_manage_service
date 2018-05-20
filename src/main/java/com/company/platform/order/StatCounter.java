package com.company.platform.order;

import java.io.Serializable;

public class StatCounter  implements Serializable,Cloneable {
	private String userId;
	private String amountId;
	private String ownerKey;
	private int    amount;
	private int    maxAmount;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAmountId() {
		return amountId;
	}
	public void setAmountId(String amountId) {
		this.amountId = amountId;
	}
	public String getOwnerKey() {
		return ownerKey;
	}
	public void setOwnerKey(String ownerKey) {
		this.ownerKey = ownerKey;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(int maxAmount) {
		this.maxAmount = maxAmount;
	}
	
}
