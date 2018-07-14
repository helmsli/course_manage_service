/**
 * 
 */
package com.company.review.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.company.platform.order.OrderClientService;
import com.company.review.constants.ReviewConst;
import com.company.review.domain.ReviewRequest;
import com.company.review.utils.RedisUtils;
import com.company.review.utils.ReviewUtils;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;
import com.xinwei.orderDb.domain.OrderMain;
import com.xinwei.orderDb.domain.OrderMainContext;

/**
 * @notes
 * 
 * @author wangjiamin
 * 
 * @version 2018年7月6日 上午11:01:38
 * 
 */
@Service
public class BaseReviewService {

	private static final Logger logger = LoggerFactory.getLogger(BaseReviewService.class);

	// 存储当前步骤taskId的key
	private static final String REVIEW_CURRENT_TASK_ID = "current_task_id";

	// redis 锁时间 秒
	@Value("${redis-lock.review.lock-seconds:10}")
	private int lockSeconds;

	@Resource
	private OrderClientService orderClientService;

	@Autowired
	private RedisUtils redisUtils;

	/**
	 * 订单审核
	 * 
	 * @param reviewRequest
	 * @return
	 */
	public ProcessResult performReview(ReviewRequest reviewRequest) {
		long requestTime = 0;
		ProcessResult result;
		// 获取redis锁的key
		String redisKey = getRedisKey(reviewRequest.getOrderId());
		try {
			/** 0. 申请 redis 锁 */
			requestTime = redisUtils.getCommonLock(redisKey, lockSeconds);
			// 获取redis锁失败
			if (requestTime == 0) {
				logger.error("get redis-lock error...");
				return ReviewUtils.getProcessResult(ReviewConst.RET_ERROR, "get redis-lock error...");
			}
			// 当前步骤正确的taskId
			result = getCurrentTaskId(reviewRequest);
			if (result.getRetCode() != ReviewConst.RET_SUCCESS) {
				logger.error("get currentTaskId error...result=" + result);
				return result;
			}
			String taskId = (String) result.getResponseInfo();
			/** 1. 校验 taskId */
			if (!taskId.equals(reviewRequest.getTaskId())) {
				logger.error("taskId error...taskId=" + reviewRequest.getTaskId() + ", currentTaskId=" + taskId);
				return ReviewUtils.getProcessResult(ReviewConst.RET_ERROR, "teaskId is error...");
			}
			/** 2. 保存 task 上下文数据 */
			result = saveReviewRequest(reviewRequest);
			if (result.getRetCode() != ReviewConst.RET_SUCCESS) {
				logger.error("saveReviewRequest error...result= " + result);
				return result;
			}
			/** 3. 跳转订单 */
			
			result = orderClientService.manualJumpToNextStep(reviewRequest.getCategory(), reviewRequest.getOrderId(),
					String.valueOf(reviewRequest.getReviewResult()));
			if (result.getRetCode() != ReviewConst.RET_SUCCESS) {
				logger.error("manualJumpToNextStep error...result= " + result);
				return result;
			}
			/** 4. 更新 currentTaskId */
			result = updateCurrentTaskId(reviewRequest);
			if (result.getRetCode() != ReviewConst.RET_SUCCESS) {
				logger.error("updateCurrentTaskId error...result= " + result);
				return result;
			}
			/** 5. 释放 redis 锁 */
		} finally {
			redisUtils.releaseCommonLock(redisKey, requestTime);
		}
		// 返回给页面操作结果
		return result;
	}

	/**
	 * 获取redis锁的key
	 * 
	 * @param orderId
	 * @return
	 */
	private String getRedisKey(String orderId) {
		return "jisu_review_" + orderId;
	}

	/**
	 * 获取当前的taskId
	 * 
	 * @param reviewRequest
	 * @return
	 */
	private ProcessResult getCurrentTaskId(ReviewRequest reviewRequest) {
		String dbId = OrderMainContext.getDbId(reviewRequest.getOrderId());
		ProcessResult result =  orderClientService.getContextData(reviewRequest.getCategory(), dbId, reviewRequest.getOrderId(), Arrays.asList(REVIEW_CURRENT_TASK_ID));
		if (result != null && result.getRetCode() == 0) {
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>) result.getResponseInfo();
			String taskId = map.get(REVIEW_CURRENT_TASK_ID);
			result.setResponseInfo(taskId);
		}
		return result;
	}

	/**
	 * 生成 获取 currentTaskId 的 key
	 * 
	 * @param taskId
	 * @return
	 */
	private String getTaskDataKey(String taskId) {
		return "__Step:" + taskId;
	}

	/**
	 * 审核数据保存到上下文
	 * 
	 * @param reviewRequest
	 * @return
	 */
	private ProcessResult saveReviewRequest(ReviewRequest reviewRequest) {
		Map<String, String> reviewData = new HashMap<>();
		if (reviewRequest.getReviewDate() == null) {
			reviewRequest.setReviewDate(new Date());
		}
		// key为 __Step: + taskId
		reviewData.put(getTaskDataKey(reviewRequest.getTaskId()), JsonUtil.toJson(reviewRequest));
		String dbId = OrderMainContext.getDbId(reviewRequest.getOrderId());
		ProcessResult result = orderClientService.putContextData(reviewRequest.getCategory(),dbId,
				reviewRequest.getOrderId(), reviewData);
		return result;
	}

	/**
	 * 更新 currentTaskId
	 * 
	 * @param reviewRequest
	 * @return
	 */
	private ProcessResult updateCurrentTaskId(ReviewRequest reviewRequest) {
		Map<String, String> currentTaskId = new HashMap<>();
		// 步骤+1
		long newTaskId = Long.parseLong(reviewRequest.getTaskId()) + 1;
		currentTaskId.put(REVIEW_CURRENT_TASK_ID, String.valueOf(newTaskId));
		String dbId = OrderMainContext.getDbId(reviewRequest.getOrderId());
		
		ProcessResult result = orderClientService.putContextData(reviewRequest.getCategory(),dbId,
				reviewRequest.getOrderId(), currentTaskId);
		return result;
	}
}
