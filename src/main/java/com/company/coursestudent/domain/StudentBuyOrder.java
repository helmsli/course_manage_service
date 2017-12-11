package com.company.coursestudent.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.reflect.TypeToken;
import com.xinwei.nnl.common.util.JsonUtil;


public class StudentBuyOrder extends Coursebuyerorder {
	/**
	 * 如果list为空，就是购买全部课程
	 */
	private List<Classbuyerorder> courseClassList=null;

	private  String courseClasses;

	
	public List<Classbuyerorder> getCourseClasses() {
		return courseClassList;
	}

	public void setCourseClasses(List<Classbuyerorder> courseClasses) {
		this.courseClassList = courseClasses;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public void setCourseClasses(String courseClasses) {
		this.courseClasses = courseClasses;		
	}
	
	
	public void createCourseClassList()
	{
		try {
			if(courseClasses!=null&&courseClasses.length()>10)
			{
				courseClassList = JsonUtil.fromJson(courseClasses, new TypeToken<ArrayList<Classbuyerorder>>() {}.getType());
			}
			courseClasses=null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			courseClasses=null;
		}
	}
}
