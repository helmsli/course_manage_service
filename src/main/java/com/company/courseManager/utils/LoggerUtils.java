package com.company.courseManager.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtils {
	public static void debugger(Logger logger,Exception e)
	{
		if(e!=null)
		{
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			String errorStr = errors.toString();
			if(!StringUtils.isEmpty(errorStr))
			{
				logger.error(errorStr);
			}
		}
	}
}
