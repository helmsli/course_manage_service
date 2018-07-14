/**
 * 
 */
package com.company.review.utils;

import java.beans.PropertyDescriptor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.xinwei.coojisureview.constants.ReviewConst;
import com.xinwei.nnl.common.domain.ProcessResult;

/**
 * @notes
 * 
 * @author wangboc
 * 
 * @version 2018年6月30日 下午2:30:15
 */
public class ReviewUtils {

	/**
	 * 获取自定义封装结果
	 * 
	 * @param retCode
	 *            返回码
	 * @return
	 */
	public static ProcessResult getProcessResult(int retCode) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(retCode);
		return processResult;
	}

	/**
	 * 获取自定义封装结果
	 * 
	 * @param retCode
	 *            返回码
	 * @param responseInfo
	 *            返回数据
	 * @return
	 */
	public static ProcessResult getProcessResult(int retCode, Object responseInfo) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(retCode);
		processResult.setResponseInfo(responseInfo);
		return processResult;
	}

	/**
	 * 获取自定义封装结果
	 * 
	 * @param retCode
	 *            返回码
	 * @param retMsg
	 *            返回信息
	 * @return
	 */
	public static ProcessResult getProcessResult(int retCode, String retMsg) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(retCode);
		processResult.setRetMsg(retMsg);
		return processResult;
	}

	/**
	 * 获取自定义封装结果
	 * 
	 * @param retCode
	 *            返回码
	 * @param retMsg
	 *            返回信息
	 * @param responseInfo
	 *            返回数据
	 * @return
	 */
	public static ProcessResult getProcessResult(int retCode, String retMsg, Object responseInfo) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(retCode);
		processResult.setRetMsg(retMsg);
		processResult.setResponseInfo(responseInfo);
		return processResult;
	}

	/**
	 * 包装异常信息到processResult
	 * 
	 * @param processResult
	 * @param e
	 */
	public static void saveExceptionToResult(ProcessResult processResult, Exception e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		String errorStr = errors.toString();
		processResult.setRetCode(ReviewConst.RET_ERROR);
		if (StringUtils.isNotBlank(errorStr)) {
			processResult.setRetMsg(errorStr.substring(0, 1000));
		}
	}
	
	/**
	 * 获取源对象的所有null属性
	 * 
	 * @param obj
	 * @return
	 */
	public static String[] getNullPropNames(Object obj) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(obj);
		PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors();
		Set<String> nullPropNames = new HashSet<>();
		for (PropertyDescriptor pd : pds) {
			Object srcObjValue = beanWrapper.getPropertyValue(pd.getName());
			if (srcObjValue == null)
				nullPropNames.add(pd.getName());
		}
		String[] result = new String[nullPropNames.size()];
		return nullPropNames.toArray(result);
	}

}
