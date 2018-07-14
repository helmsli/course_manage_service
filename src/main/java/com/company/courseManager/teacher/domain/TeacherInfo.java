package com.company.courseManager.teacher.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TeacherInfo implements Serializable {
	private String userId;
	
	
	/**
	 * 教师照
	 */
	private String photo;
	/**
	 *简介 
	 */
	private String resume;
	/**
	 * 特长
	 */
	private String specialties;
	/**
	 * 等级
	 */
	private String level;
	/**
	 * 职称
	 */
	private String title;
	/**
	 * 证书，，分割多个证书
	 */
	private String certificate;
	
	/**
	 * 就业公司
	 */
	private String company;
	
	
	/**
	 * 个人成就
	 */
	private String accomplishment;


	public String getuserId() {
		return userId;
	}


	public void setuserId(String userId) {
		this.userId = userId;
	}


	public String getPhoto() {
		return photo;
	}


	public void setPhoto(String photo) {
		this.photo = photo;
	}


	public String getResume() {
		return resume;
	}


	public void setResume(String resume) {
		this.resume = resume;
	}


	public String getSpecialties() {
		return specialties;
	}


	public void setSpecialties(String specialties) {
		this.specialties = specialties;
	}


	public String getLevel() {
		return level;
	}


	public void setLevel(String level) {
		this.level = level;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getCertificate() {
		return certificate;
	}


	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}


	public String getCompany() {
		return company;
	}


	public void setCompany(String company) {
		this.company = company;
	}


	public String getAccomplishment() {
		return accomplishment;
	}


	public void setAccomplishment(String accomplishment) {
		this.accomplishment = accomplishment;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
