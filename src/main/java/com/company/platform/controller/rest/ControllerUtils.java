package com.company.platform.controller.rest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;

public class ControllerUtils {
	
	public static ProcessResult getFromResponse(Exception e,int errorCode,ProcessResult processResult)
	{
		if(processResult==null)
		{
			processResult = new ProcessResult();
		}
		processResult.setRetCode(errorCode);
		if(StringUtils.isEmpty(e.getMessage()))
		{
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			String errorStr = errors.toString();
			if(!StringUtils.isEmpty(errorStr))
			{
				processResult.setRetMsg(errorStr.substring(0,1000));
			}
		}
		else
		{
		
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			String errorStr = errors.toString();
			if(!StringUtils.isEmpty(errorStr))
			{
				processResult.setRetMsg(errorStr.substring(0,1000));
			}
		}
	    return processResult;
	}
	
	public static ProcessResult getErrorResponse(int errorCode,String errorMsg)
	{
		
		ProcessResult processResult = new ProcessResult();
		
		processResult.setRetCode(errorCode);
		if(StringUtils.isEmpty(errorMsg))
		{
			processResult.setRetMsg("init default error");
		}
		else
		{
			processResult.setRetMsg(errorMsg);
		}
		return processResult;
	}
	
	public static ProcessResult toJsonSimpleProcessResult(ProcessResult processResult) {
		Object object = processResult.getResponseInfo();
		if (object != null) {
			String jsonStr = JsonUtil.toJson(object);
			processResult.setResponseInfo(jsonStr);
		}
		return processResult;
	}

}
