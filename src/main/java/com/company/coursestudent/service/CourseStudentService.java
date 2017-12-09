package com.company.coursestudent.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.company.courseManager.teacher.service.TeacherCourseManager;
import com.company.coursestudent.domain.Classbuyerorder;
import com.company.coursestudent.domain.StudentBuyOrder;
import com.company.coursestudent.domain.StudentConst;
import com.company.platform.controller.rest.ControllerUtils;
import com.company.platform.order.OrderClientService;
import com.company.userOrder.domain.UserOrder;
import com.company.videodb.domain.CourseClass;
import com.company.videodb.domain.Courses;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;
import com.xinwei.orderDb.domain.OrderMainContext;
@Service("courseStudentService")
public class CourseStudentService extends OrderClientService{

	@Value("${student.userDbWriteUrl}")
	private String studentUserDbWriteUrl;
	
	@Resource(name="teacherCourseManager")
	private TeacherCourseManager teacherCourseManager;
	
	
	protected ProcessResult isCheckSubmitBuyOrder(StudentBuyOrder studentBuyOrder)
	{
		//check price
		ProcessResult processResult = null;
		List<Classbuyerorder> classList= studentBuyOrder.getCourseClasses();
		List<CourseClass> courseClassList=new ArrayList<CourseClass>();
		if(classList ==null)
		{
			 processResult = teacherCourseManager.getCourseAllClass(studentBuyOrder.getCourseid());
			 if(processResult.getRetCode()!=StudentConst.RESULT_Success)
			 {
				 return processResult;
			 }
			 courseClassList = (List<CourseClass>)processResult.getResponseInfo();
		}
		else
		{
			for(int i=0;i<classList.size();i++)
			{
				Classbuyerorder classbuyerorder= classList.get(i);
				processResult=teacherCourseManager.getCourseClass(studentBuyOrder.getCourseid(), classbuyerorder.getClassid());
				if(processResult.getRetCode()!=StudentConst.RESULT_Success)
				 {
					 return processResult;
				 }
				courseClassList.add((CourseClass)processResult.getResponseInfo());
			}
			
		}
		//校验余额
		double totalMoney = 0;
		for(int i=0;i<courseClassList.size();i++)
		{
			totalMoney += courseClassList.get(i).getRealPrice();
		}
		if(totalMoney!=studentBuyOrder.getRealprice())
		{
			return ControllerUtils.getErrorResponse(StudentConst.RESULT_Error_money, "course real money error");
		}		
		return ControllerUtils.getSuccessResponse(processResult);
	}
	
	public ProcessResult submitBuyOrder(String orderId,StudentBuyOrder studentBuyOrder) 
	{
		
		
		ProcessResult processResult = isCheckSubmitBuyOrder(studentBuyOrder);
		if(processResult.getRetCode()!=StudentConst.RESULT_Success)
		{
			return processResult;
		}
		OrderMainContext orderMainContext = new OrderMainContext();
		orderMainContext.setCatetory(StudentConst.ORDER_BUYER_CATEGORY);
		orderMainContext.setOrderId(orderId);
		orderMainContext.setOwnerKey(studentBuyOrder.getUserid());
		Map<String,String> contextMap = new HashMap<String,String>();
		contextMap.put(StudentConst.ORDERKEY_ORDER,JsonUtil.toJson(studentBuyOrder));
		orderMainContext.setContextDatas(contextMap);
		processResult= createOrder(orderMainContext); 
		if(processResult.getRetCode()!=StudentConst.RESULT_Success)
		{
			return processResult;
		}
		UserOrder userOrder= new UserOrder();
		userOrder.setCategory(StudentConst.ORDER_BUYER_CATEGORY);
		userOrder.setConstCreateTime();
		userOrder.setOrderId(orderId);
		userOrder.setStatus(StudentConst.ORDER_BUYER_STATUS_NEEDPAY);
		userOrder.setUserId(studentBuyOrder.getUserid());
		processResult= saveUserOrder(studentUserDbWriteUrl,userOrder);
		if(processResult.getRetCode()!=StudentConst.RESULT_Success)
		{
			return processResult;
		}
		return this.startOrder(StudentConst.ORDER_BUYER_CATEGORY,orderId);
		
	}
}
