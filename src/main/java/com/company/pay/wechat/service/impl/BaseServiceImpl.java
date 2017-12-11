package com.company.pay.wechat.service.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.company.pay.wechat.domain.WeChatPayConst;
import com.company.pay.wechat.sdk.WXPayConstants;
import com.xinwei.nnl.common.domain.ProcessResult;

public class BaseServiceImpl {
	protected ProcessResult getFromResponse(Map<String, String> respData,ProcessResult processResult)
	{
		if(processResult==null)
		{
			processResult = new ProcessResult();
		}
		processResult.setRetCode(WeChatPayConst.RESULT_Error_Fail);
		 String RETURN_CODE_KEY = "result_code";
		 String ERROR_CODE = "err_code";
		 String code_url  ="code_url";
				 
		 String result_code="";
		 if (respData.containsKey(RETURN_CODE_KEY)) {
			 result_code = respData.get(RETURN_CODE_KEY);
	            //StringEscapeUtils.unescapeJava(return_code);
	            //respData.put(RETURN_CODE, StringEscapeUtils.unescapeJava(return_code));
	     }
		 if (result_code.equals(WXPayConstants.SUCCESS))
		 {
			 processResult.setRetCode(WeChatPayConst.RESULT_Success);
			 if(respData.containsKey(code_url))
			 {
				 respData.put(code_url, StringEscapeUtils.unescapeJava(respData.get(code_url)));
				    		 
			 }
		 }
		 else
		 {
			 if (respData.containsKey(ERROR_CODE)) {
				 processResult.setRetMsg(respData.get(ERROR_CODE));
		     }
			
		 }
		 processResult.setResponseInfo(respData);
		 return processResult;
	}

	protected ProcessResult getFromResponse(Exception e,ProcessResult processResult)
	{
		if(processResult==null)
		{
			processResult = new ProcessResult();
		}
		processResult.setRetCode(WeChatPayConst.RESULT_Error_Fail);
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

}
