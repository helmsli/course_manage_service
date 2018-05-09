package com.company.courseManager.controller.rest;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.courseManager.Const.CoursemanagerConst;
import com.company.courseManager.teacher.service.TeacherCourseManager;
import com.company.coursestudent.domain.StudentBuyOrder;
import com.company.coursestudent.domain.StudentConst;
import com.company.coursestudent.domain.UserInfo;
import com.company.coursestudent.service.CourseStudentService;
import com.company.pay.wechat.domain.WeChatScanPayRequest;
import com.company.platform.controller.rest.ControllerUtils;
import com.xinwei.nnl.common.domain.JsonRequest;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.orderDb.domain.OrderMain;

@RestController
@RequestMapping("/studentCourse")
public class CStudentCourseController {
	 private Logger log = LoggerFactory.getLogger(getClass());
		
	@Resource(name="teacherCourseManager")
	private TeacherCourseManager teacherCourseManager;
	
	@Resource(name="courseStudentService")
	private CourseStudentService courseStudentService;
	
	
	
	@RequestMapping(method = RequestMethod.GET,value = "/{courseId}/queryCourse")
	public  ProcessResult queryCourse(@PathVariable String courseId) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			processResult = teacherCourseManager.getCourse(courseId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return processResult;
	}
	
	/**
	 * 查询我的全部课程，如果购买状态为255，其余状态为正常状态
	 * @param courseId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value = "/{courseId}/queryMyCourse")
	public  ProcessResult queryMyCourse(@PathVariable String courseId,@RequestBody UserInfo userInfo) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			processResult = teacherCourseManager.getMyCourse(courseId,userInfo.getUserId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return processResult;
	}
	
	@RequestMapping(method = RequestMethod.GET,value = "/{courseId}/{classId}/queryOneClass")
	public  ProcessResult queryOneClass(@PathVariable String courseId,@PathVariable String classId) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			processResult = teacherCourseManager.getCourseClass(courseId, classId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return processResult;
	}
	@RequestMapping(method = RequestMethod.GET,value = "/{courseId}/queryAllClass")
	public  ProcessResult queryAllClass(@PathVariable String courseId) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			processResult = teacherCourseManager.getCourseAllClass(courseId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, processResult);
			
			
		}
		return processResult;
	}
	@RequestMapping(method = RequestMethod.POST,value = "/{userid}/{orderId}/submitBuyOrder")
	public ProcessResult submitBuyOrder(@PathVariable String userid,@PathVariable String orderId,@RequestBody StudentBuyOrder studentBuyOrder)
	{
		
		try {
			return courseStudentService.submitBuyOrder(orderId, studentBuyOrder);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
		}
		
	}
	
	@PostMapping(value = "/{userid}/{orderId}/getOrderPayInfo")
	public ProcessResult getOrderPayInfo(@PathVariable String userid,@PathVariable String orderId,@RequestBody WeChatScanPayRequest weChatScanPayRequest)
	{
		
		try {
			ProcessResult processResult= courseStudentService.getPayOrderInfo(orderId,weChatScanPayRequest.getTrade_type());
			if(processResult.getRetCode()==StudentConst.RESULT_Success)
			{
				 Map<String, String> responseData = (Map<String, String>)processResult.getResponseInfo();
				// Map<String, String> retMap = new HashMap<String,String>();
				// retMap.put(StudentConst.PAY_QRCODEURL_KEY, responseData.get(StudentConst.PAY_QRCODEURL_KEY));
				// retMap.
				 processResult.setResponseInfo(responseData);
			}
			return processResult;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
		}
		
	}
	
	@RequestMapping(method = RequestMethod.POST,value = "/{category}/{dbid}/{orderId}/notifyPaySuccess")
	public String weixinNotify(@PathVariable String category,@PathVariable String dbid,@PathVariable String orderId,HttpServletRequest request,
			HttpServletResponse response)
	{
		InputStream is =null;
		BufferedReader br=null;
		String xmlString = "";
		try {
			 is = request.getInputStream();
			 br = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));
			// 微信返回的xml字符流
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			xmlString = sb.toString();
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			try
			{
				is.close();
				br.close();
			}
			catch(Exception e) {}
		}
		try {
			
			ProcessResult processResult = courseStudentService.processWeChatSuccessPay(orderId,xmlString);
			String responseXml="";
			if(processResult.getRetCode()==0)
			{
				responseXml = "<xml>"
						+ "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
			}
			else
			{
				log.error(processResult.toString());
				responseXml = "<xml>"
						+ "<return_code><![CDATA[FAIL]]></return_code>"
						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
			}
			BufferedOutputStream out = null;
			try {
				 out = new BufferedOutputStream(
						response.getOutputStream());
				out.write(responseXml.getBytes());
				out.flush();
				
			}
			catch(Exception e)
			{
				
			}
			finally {
				if(out!=null)
				{
					out.close();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
	/*
	@RequestMapping(value = "/wx_notify",method=RequestMethod.POST)
	public String weiChatNotify(HttpServletRequest request,
			HttpServletResponse response) {
		try
		{
			throw new Exception("receive wechat notify");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}
	*/
	
}
