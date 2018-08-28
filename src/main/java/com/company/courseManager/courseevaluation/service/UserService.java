package com.company.courseManager.courseevaluation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.company.courseManager.utils.JsonUtilSuper;
import com.company.security.domain.SecurityUser;
import com.xinwei.nnl.common.domain.ProcessResult;
@Service("userService")
public class UserService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Value("${security.userCenter}")
	private String securityUserCenter;

	@Autowired
	protected RestTemplate restTemplate;

	public SecurityUser getUserInfo(String userId) {
		// {routerId}/getUserInfoById
		ProcessResult result = null;
		SecurityUser securityUser = new SecurityUser();

		securityUser.setUserId(Long.parseLong(userId));
		logger.debug(this.securityUserCenter + "/" + userId + "/getUserInfoById");
		result = restTemplate.postForObject(this.securityUserCenter + "/" + userId + "/getUserInfoById", securityUser,
				ProcessResult.class);
		if (result.getRetCode() == 0) {
			String ls = JsonUtilSuper.toJson(result.getResponseInfo());
			return JsonUtilSuper.fromJson(ls, SecurityUser.class);
		}
		return null;
	}
	
	public ProcessResult modifyUserInfo(SecurityUser securityUser) {
		// {routerId}/getUserInfoById
		ProcessResult result = null;
		

	
		
		result = restTemplate.postForObject(this.securityUserCenter + "/010/modifyUserInfo", securityUser,
				ProcessResult.class);
		
		return result;
	}
}
