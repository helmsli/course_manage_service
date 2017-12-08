package com.company.coursestudent.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.company.coursestudent.domain.StudentBuyOrder;
import com.company.coursestudent.domain.StudentConst;
import com.company.platform.order.OrderClientService;
import com.company.userOrder.domain.UserOrder;
import com.company.videodb.domain.Courses;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;
import com.xinwei.orderDb.domain.OrderMainContext;
@Service("courseStudentService")
public class CourseStudentService extends OrderClientService{

	@Value("${student.userDbWriteUrl}")
	private String studentUserDbWriteUrl;
	public ProcessResult submitBuyOrder(String orderId,StudentBuyOrder studentBuyOrder) 
	{
		OrderMainContext orderMainContext = new OrderMainContext();
		orderMainContext.setCatetory(StudentConst.ORDER_BUYER_CATEGORY);
		orderMainContext.setOrderId(orderId);
		orderMainContext.setOwnerKey(studentBuyOrder.getUserid());
		Map<String,String> contextMap = new HashMap<String,String>();
		contextMap.put(StudentConst.ORDERKEY_ORDER,JsonUtil.toJson(studentBuyOrder));
		orderMainContext.setContextDatas(contextMap);
		ProcessResult processResult= createOrder(orderMainContext); 
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
