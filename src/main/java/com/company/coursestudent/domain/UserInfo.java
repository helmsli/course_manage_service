package com.company.coursestudent.domain;

import java.io.Serializable;

public class UserInfo implements Serializable{
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
