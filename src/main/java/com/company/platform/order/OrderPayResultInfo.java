package com.company.platform.order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderPayResultInfo implements Serializable{
	
	private List<String> payInfo=new ArrayList<String>();
	
	
	public List<String> getPayInfo() {
		return payInfo;
	}
	public void setPayInfo(List<String> payInfo) {
		this.payInfo = payInfo;
	}
	
	
}
