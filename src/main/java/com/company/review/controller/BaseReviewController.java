/**
 * 
 */
package com.company.review.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.review.constants.ReviewConst;
import com.company.review.domain.ReviewRequest;
import com.company.review.service.BaseReviewService;
import com.company.review.utils.ReviewUtils;
import com.xinwei.nnl.common.domain.ProcessResult;

/**
 * @notes 
 * 
 * @author wangjiamin
 * 
 * @version 2018年7月7日 下午5:39:07
 */
@RestController
@RequestMapping("/review")
public class BaseReviewController {

	@Resource
	private BaseReviewService baseReviewService;

	@PostMapping(value = "/{category}/{dbId}/submit")
	public ProcessResult performReview(@RequestBody ReviewRequest reviewRequest) {
		ProcessResult processResult = null;
		try {
			processResult = baseReviewService.performReview(reviewRequest);
		} catch (Exception e) {
			processResult = ReviewUtils.getProcessResult(ReviewConst.RET_ERROR, "system error");
			e.printStackTrace();
		}
		return processResult;
	}
}
