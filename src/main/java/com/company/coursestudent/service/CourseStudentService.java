package com.company.coursestudent.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.company.courseManager.teacher.service.TeacherCourseManager;
import com.company.coursestudent.domain.Classbuyerorder;
import com.company.coursestudent.domain.StudentBuyOrder;
import com.company.coursestudent.domain.StudentConst;
import com.company.pay.wechat.domain.WeChatScanPayRequest;
import com.company.pay.wechat.sdk.WXPayConstants;
import com.company.pay.wechat.service.WeChatScanPayService;
import com.company.platform.controller.rest.ControllerUtils;
import com.company.platform.order.OrderClientService;
import com.company.platform.order.OrderPayResult;
import com.company.platform.order.OrderPayResultInfo;
import com.company.platform.order.OrderWillPayRequest;
import com.company.userOrder.domain.UserOrder;
import com.company.videodb.domain.CourseClass;
import com.company.videodb.domain.Courses;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;
import com.xinwei.orderDb.domain.OrderMainContext;
@Service("courseStudentService")
public class CourseStudentService extends OrderClientService{
	 private Logger log = LoggerFactory.getLogger(getClass());
	 
	@Value("${student.userDbWriteUrl}")
	private String studentUserDbWriteUrl;
	
	@Resource(name="teacherCourseManager")
	private TeacherCourseManager teacherCourseManager;
	
	@Resource(name="weChatScanPayService")
	private WeChatScanPayService weChatScanPayService;
	
	public ProcessResult isCheckSubmitBuyOrder(StudentBuyOrder studentBuyOrder)
	{
		//check price
		ProcessResult processResult = null;
		
		//check 是否已经购买
		try {
			StudentBuyOrder oldStudentBuyOrder = getByerStudentBuyOrder(studentBuyOrder.getUserId(),studentBuyOrder.getCourseId());
			//有数据
			if(oldStudentBuyOrder!=null)
			{
				//如果以前全部购买
				if(oldStudentBuyOrder.getCourseClasses()==null ||oldStudentBuyOrder.getCourseClasses().size()==0)
				{
					return ControllerUtils.getErrorResponse(StudentConst.RESULT_Error_HAVEPAID, "all have paid;");
					
				}
				//以前部分购买
				else
				{
					//现在全部购买
					if(studentBuyOrder.getCourseClasses()==null||studentBuyOrder.getCourseClasses().size()==0)
					{
						return ControllerUtils.getErrorResponse(StudentConst.RESULT_Error_HAVEPAID, oldStudentBuyOrder.getCourseClasses().get(0).getClassId());
							
					}
					//判断是否有重复购买的
					Map<String,String> classes = new HashMap<String,String>();
					for(int i=0;i<oldStudentBuyOrder.getCourseClasses().size();i++)
					{
						classes.put(oldStudentBuyOrder.getCourseClasses().get(i).getClassId(), "1");
					}
					
					for(int i=0;i<studentBuyOrder.getCourseClasses().size();i++)
					{
						if(classes.containsKey(studentBuyOrder.getCourseClasses().get(i).getClassId()))
						{
							return ControllerUtils.getErrorResponse(StudentConst.RESULT_Error_HAVEPAID, studentBuyOrder.getCourseClasses().get(i).getClassId() + " have paid.");
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, processResult);
			
		}
		
		
		
		processResult = teacherCourseManager.getCourse(studentBuyOrder.getCourseId());
		
		Courses courses= (Courses)processResult.getResponseInfo();
		studentBuyOrder.setTitle(courses.getTitle());
		if(StringUtils.isEmpty(studentBuyOrder.getTitle()))
		{
			studentBuyOrder.setTitle("课程");
		}
		
		List<Classbuyerorder> classList= studentBuyOrder.getCourseClasses();
		List<CourseClass> courseClassList=new ArrayList<CourseClass>();
		if(classList ==null)
		{
			 processResult = teacherCourseManager.getAllClass(studentBuyOrder.getCourseId());
			 if(processResult.getRetCode()!=StudentConst.RESULT_Success)
			 {
				 return processResult;
			 }
			 courseClassList = (List<CourseClass>)processResult.getResponseInfo();
			 if(courses.getRealPrice()!=studentBuyOrder.getTotalRealPrice())
			 {
					log.error("now Money:" + studentBuyOrder.getTotalRealPrice() + " need money:" + courses.getRealPrice());
					return ControllerUtils.getErrorResponse(StudentConst.RESULT_Error_money, "course real money error:need money "+courses.getRealPrice());
			}
		}
		else
		{
			for(int i=0;i<classList.size();i++)
			{
				Classbuyerorder classbuyerorder= classList.get(i);
				processResult=teacherCourseManager.getCourseClass(studentBuyOrder.getCourseId(), classbuyerorder.getClassId());
				if(processResult.getRetCode()!=StudentConst.RESULT_Success)
				 {
					 return processResult;
				 }
				courseClassList.add((CourseClass)processResult.getResponseInfo());
			}
			//校验余额
			double totalMoney = 0;
			for(int i=0;i<courseClassList.size();i++)
			{
				totalMoney += courseClassList.get(i).getRealPrice();
			}
			if(totalMoney!=studentBuyOrder.getTotalRealPrice())
			{
				log.error("now Money:" + studentBuyOrder.getTotalRealPrice() + " need money:" + totalMoney);
				return ControllerUtils.getErrorResponse(StudentConst.RESULT_Error_money, "course real money error:need money "+totalMoney);
			}	
		}
				
		return ControllerUtils.getSuccessResponse(processResult);
	}
	
	
	
	
	protected OrderWillPayRequest getWillPayRequest(StudentBuyOrder studentBuyOrder)
	{
		OrderWillPayRequest orderWillPayRequest = new OrderWillPayRequest();
		 Calendar nowCalendar = Calendar.getInstance();
		 nowCalendar.set(Calendar.MILLISECOND, 0);
		orderWillPayRequest.setCreateTime(nowCalendar.getTime());
		orderWillPayRequest.setUserid(studentBuyOrder.getUserId());
		orderWillPayRequest.setFee_type(studentBuyOrder.getFeeType());
		orderWillPayRequest.setTitle_desc_seller(studentBuyOrder.getTitle());
		if(studentBuyOrder.getCourseClasses()!=null && studentBuyOrder.getCourseClasses().size()>0)
		{
			orderWillPayRequest.setTitle_goods(studentBuyOrder.getCourseClasses().get(0).getClassTitle());
		}
		orderWillPayRequest.setTotal_fee(String.valueOf(1));
		orderWillPayRequest.setCrcKey(orderWillPayRequest.createCrcKey(null));
		return orderWillPayRequest;
	}
	
	protected ProcessResult getStudentBuyOrder(String orderId)
	{
		String dbid = OrderMainContext.getDbId(orderId);
		String orderKey = StudentConst.ORDERKEY_ORDER; 
		List<String> keys = new ArrayList<String>();
		keys.add(orderKey);
		
		//获取订单的key
		ProcessResult processResult = getContextData(StudentConst.ORDER_BUYER_CATEGORY,dbid,orderId,keys);
		if(processResult.getRetCode()!=StudentConst.RESULT_Success)
		{
			return processResult;
		}
		else
		{
			Map<String,String> maps = (Map<String,String>)processResult.getResponseInfo();
			if(maps.containsKey(orderKey))
			{
				processResult.setResponseInfo(JsonUtil.fromJson(maps.get(orderKey), StudentBuyOrder.class));
			}
		}
		return processResult;
	}
	
	
	public ProcessResult processWeChatSuccessPay(String orderId,String xmlString) throws Exception
	{
		 Map<String, String> responseMap = this.weChatScanPayService.processResponseXml(xmlString);
		 log.error(JsonUtil.toJson(responseMap));
		 if(responseMap.containsKey("return_code"))
		 {
			String return_code= responseMap.get("return_code");
			if(return_code.compareToIgnoreCase(WXPayConstants.SUCCESS)!=0)
			{
				 throw new Exception("err return_code not success:" + orderId + ":" + return_code);
			}
		 }
		 else
		 {
			 throw new Exception("err get return_code:" + orderId);
					 
		 }
		 
		 String out_trade_no="";
		 if(!responseMap.containsKey("out_trade_no"))
		 {
			 throw new Exception("err get out_trade_no:" + orderId);
		 }
		 out_trade_no = responseMap.get("out_trade_no");
		 //进行交易查询您
		 if(orderId.compareToIgnoreCase(out_trade_no)!=0)
		 {
			 throw new Exception("error out_trade_no not equal orderId:" + out_trade_no + ":" + orderId);
		 }
		 
		ProcessResult processResult = this.weChatScanPayService.doPayQuery(orderId);
		if(processResult.getRetCode()!=StudentConst.RESULT_Success)
		{
			throw new Exception("not find success pay order:" +orderId);
			
		}
		//查询订单中付款信息
		 Map<String, String> responseData =(Map<String, String>)processResult.getResponseInfo();
		String totalFee = responseData.get(StudentConst.PAY_TOTAL_FEE_KEY);
		if(StringUtils.isEmpty(totalFee))
		{
			throw new Exception("error pay money order:" +orderId);
		}
		
		String dbid = OrderMainContext.getDbId(orderId);
		String paymentKey = getOrderWillPayKey(); 
		List<String> keys = new ArrayList<String>();
		keys.add(paymentKey);
		keys.add(StudentConst.ORDERKEY_ORDER);
		processResult = getContextData(StudentConst.ORDER_BUYER_CATEGORY,dbid,orderId,keys);
		if(processResult.getRetCode()!=StudentConst.RESULT_Success)
		{
			throw new Exception("error get will pay information: " + orderId);
		}
		Map<String,String> maps = (Map<String,String>)processResult.getResponseInfo();
		OrderWillPayRequest orderWillPayRequest=null;
		if(maps.containsKey(paymentKey))
		{
			orderWillPayRequest = JsonUtil.fromJson(maps.get(paymentKey), OrderWillPayRequest.class);
		}
		else
		{
			throw new Exception("error get will payment info  order:" +orderId);
		}
		//校验CRC信息
		String crcResult = orderWillPayRequest.createCrcKey(null);
		if(!orderWillPayRequest.crcOk(crcResult))
		{
			throw new Exception("error will payment ,crc error." + orderWillPayRequest.toString() + ":" + crcResult);
		}
		//判断是否已经全部付款
		int payResult = OrderPayResult.PAY_RESULT_FAIL;
		if(totalFee.compareToIgnoreCase(orderWillPayRequest.getTotal_fee())!=0)
		{
			payResult=OrderPayResult.PAY_RESULT_ALLSUCCESS;
		}
		else
		{
			payResult = OrderPayResult.PAY_RESULT_ALLSUCCESS;
		}
		//构造付款信息，保存到订单
		OrderPayResult orderPayResult = new OrderPayResult();
		orderPayResult.setFee_type(responseData.get(StudentConst.PAY_FEE_TYPE_KEY));
		orderPayResult.setPayResult(payResult);
		orderPayResult.setPaymentType(OrderPayResult.PTYPE_WECHAT);
		orderPayResult.setPayOrder(orderId);
		orderPayResult.setPayTime(Calendar.getInstance().getTime());
		orderPayResult.setTotal_fee(totalFee);
		orderPayResult.setCrcKey(orderPayResult.createCrcKey(null));
		OrderPayResultInfo orderPayResultInfo = new OrderPayResultInfo();
		responseData.put(StudentConst.PAYRESULT_KEY_payOrder, orderId);
		orderPayResultInfo.getPayInfo().add(JsonUtil.toJson(responseData));
		Map<String,String> contextdata = new HashMap<String,String>();
		
		contextdata.put(getOrderSuccessPayKey(), JsonUtil.toJson(orderPayResult));
		contextdata.put(getOrderPayInfoKey(), JsonUtil.toJson(orderPayResultInfo));
		processResult = putContextData(StudentConst.ORDER_BUYER_CATEGORY,dbid,orderId,contextdata);
		if(processResult.getRetCode()!=StudentConst.RESULT_Success)
		{
			throw new Exception("error put success pay information: " + orderId);
		}
		//更新用戶的订单状态,将原来用户订购的信息查询出来，增加新的订购信息进入列表
		UserOrder userOrder= new UserOrder();
		userOrder.setCategory(StudentConst.ORDER_BUYER_CATEGORY);
		userOrder.setCreateTime(orderWillPayRequest.getCreateTime());
		userOrder.setOrderId(orderId);
		userOrder.setStatus(UserOrder.STATUS_FinishOrder);
		userOrder.setUserId(orderWillPayRequest.getUserid());
		processResult= updateUserOrderStatus(studentUserDbWriteUrl,userOrder);
		if(processResult.getRetCode()!=StudentConst.RESULT_Success)
		{
			return processResult;
		}
		//更新用户的购买信息的状态
		StudentBuyOrder newStudentBuyOrder = null;
		
		newStudentBuyOrder=	JsonUtil.fromJson(maps.get(StudentConst.ORDERKEY_ORDER), StudentBuyOrder.class);
		
		UserOrder userBuyCourse= new UserOrder();
		userBuyCourse.setCategory(StudentConst.USER_BUYER_CATEGORY);
		userBuyCourse.setConstCreateTime();
		userBuyCourse.setOrderId(newStudentBuyOrder.getCourseId());		
		userBuyCourse.setUserId(orderWillPayRequest.getUserid());
		
		processResult=queryOneOrder(studentUserDbWriteUrl,userBuyCourse);
		//数据不存在
		if(processResult.getRetCode()==10002)
		{
			
			
		}
		else if(processResult.getRetCode()!=StudentConst.RESULT_Success)
		{
			return processResult;
		}
		else
		{
			newStudentBuyOrder=	JsonUtil.fromJson(maps.get(StudentConst.ORDERKEY_ORDER), StudentBuyOrder.class);
			userBuyCourse  = (UserOrder)processResult.getResponseInfo();
			
			StudentBuyOrder oldStudentBuyOrder = JsonUtil.fromJson(userBuyCourse.getOrderData(),StudentBuyOrder.class);
			float totalRealPrice = oldStudentBuyOrder.getTotalRealPrice() + newStudentBuyOrder.getTotalRealPrice();
			float totalOriginalPrice  = oldStudentBuyOrder.getTotalOriginalPrice() + newStudentBuyOrder.getTotalOriginalPrice();
			oldStudentBuyOrder.setTotalRealPrice(totalRealPrice);
			oldStudentBuyOrder.setTotalOriginalPrice(totalOriginalPrice);
			oldStudentBuyOrder.getCourseClasses().addAll(newStudentBuyOrder.getCourseClasses());
			newStudentBuyOrder = oldStudentBuyOrder;
		}
		userBuyCourse.setUpdateTime(Calendar.getInstance().getTime());
		userBuyCourse.setOrderData(JsonUtil.toJson(newStudentBuyOrder));
		processResult = this.saveUserOrder(studentUserDbWriteUrl, userBuyCourse);
		return processResult;
	}
	/**
	 * 查询用户已经购买的信息
	 * @param userid
	 * @return
	 */
	public StudentBuyOrder getByerStudentBuyOrder(String userid,String courseId) throws Exception
	{
		UserOrder userBuyCourse= new UserOrder();
		userBuyCourse.setCategory(StudentConst.USER_BUYER_CATEGORY);
		userBuyCourse.setConstCreateTime();
		userBuyCourse.setOrderId(courseId);		
		userBuyCourse.setUserId(userid);
		ProcessResult processResult=queryOneOrder(studentUserDbWriteUrl,userBuyCourse);
		if(processResult.getRetCode()==10002)
		{
			return null;
		}
		else if(processResult.getRetCode()!=StudentConst.RESULT_Success)
		{
			throw new Exception(processResult.getRetMsg());
		}
		userBuyCourse  = (UserOrder)processResult.getResponseInfo();
		StudentBuyOrder oldStudentBuyOrder = JsonUtil.fromJson(userBuyCourse.getOrderData(),StudentBuyOrder.class);
		return oldStudentBuyOrder;
	}
	
	
	public ProcessResult getPayOrderInfo(String orderId)
	{
		String dbid = OrderMainContext.getDbId(orderId);
		String paymentKey = getOrderWillPayKey(); 
		List<String> keys = new ArrayList<String>();
		keys.add(paymentKey);
		
		//获取订单的key
		ProcessResult processResult = getContextData(StudentConst.ORDER_BUYER_CATEGORY,dbid,orderId,keys);
		if(processResult.getRetCode()!=StudentConst.RESULT_Success)
		{
			return processResult;
		}
		Map<String,String> maps = (Map<String,String>)processResult.getResponseInfo();
		OrderWillPayRequest orderWillPayRequest=null;
		//获取orderWillPayRequest
		if(maps.containsKey(paymentKey))
		{
			orderWillPayRequest = JsonUtil.fromJson(maps.get(paymentKey), OrderWillPayRequest.class);
		}
		else
		{
			return ControllerUtils.getErrorResponse(StudentConst.RESULT_Error_Fail, "no payment info");
		}
		//获取微信支付信息；
		WeChatScanPayRequest weChatScanPayRequest = new WeChatScanPayRequest();
		if(!StringUtils.isEmpty(orderWillPayRequest.getTitle_desc_seller()))
		{
			weChatScanPayRequest.setTitle_desc_seller(orderWillPayRequest.getTitle_desc_seller());
		}
		else
		{
			weChatScanPayRequest.setTitle_desc_seller("购买课程");
				
		}
		if(!StringUtils.isEmpty(orderWillPayRequest.getTitle_goods()))
		{
			
			weChatScanPayRequest.setTitle_goods(orderWillPayRequest.getTitle_goods());
		}
		else
		{
			weChatScanPayRequest.setTitle_goods("");
		}
		weChatScanPayRequest.setOut_trade_no(orderId);
		weChatScanPayRequest.setTotal_fee(orderWillPayRequest.getTotal_fee());
		weChatScanPayRequest.setSpbill_create_ip("127.0.0.1");
		weChatScanPayRequest.setNotify_action(StudentConst.ORDER_BUYER_CATEGORY + "/" + dbid + "/" + orderId + "/notifyPaySuccess");
		weChatScanPayRequest.setProduct_id(orderId);
		
		
		
		
		processResult = weChatScanPayService.doUnifiedPay(weChatScanPayRequest);
		
		return processResult;
	}
	
	public ProcessResult submitBuyOrder(String orderId,StudentBuyOrder studentBuyOrder)
	{
		
		studentBuyOrder.createCourseClassList();
		ProcessResult processResult = isCheckSubmitBuyOrder(studentBuyOrder);
		if(processResult.getRetCode()!=StudentConst.RESULT_Success)
		{
			return processResult;
		}
		
		OrderMainContext orderMainContext = new OrderMainContext();
		orderMainContext.setCatetory(StudentConst.ORDER_BUYER_CATEGORY);
		orderMainContext.setOrderId(orderId);
		orderMainContext.setOwnerKey(studentBuyOrder.getUserId());
		
		OrderWillPayRequest orderWillPayRequest = getWillPayRequest(studentBuyOrder);
		Map<String,String> contextMap = new HashMap<String,String>();
		contextMap.put(StudentConst.ORDERKEY_ORDER,JsonUtil.toJson(studentBuyOrder));
		contextMap.put(getOrderWillPayKey(),JsonUtil.toJson(orderWillPayRequest));
		orderMainContext.setContextDatas(contextMap);
		processResult= createOrder(orderMainContext); 
		if(processResult.getRetCode()!=StudentConst.RESULT_Success)
		{
			return processResult;
		}
		//查询用户订购信息
		
		UserOrder userOrder= new UserOrder();
		userOrder.setCategory(StudentConst.ORDER_BUYER_CATEGORY);
		//userOrder.setConstCreateTime();
		userOrder.setCreateTime(orderWillPayRequest.getCreateTime());
		userOrder.setOrderId(orderId);
		userOrder.setStatus(StudentConst.ORDER_BUYER_STATUS_NEEDPAY);
		userOrder.setUserId(studentBuyOrder.getUserId());
		processResult= saveUserOrder(studentUserDbWriteUrl,userOrder);
		if(processResult.getRetCode()!=StudentConst.RESULT_Success)
		{
			return processResult;
		}
		
		//保存支付信息；
		return this.startOrder(StudentConst.ORDER_BUYER_CATEGORY,orderId);
		
	}
}
