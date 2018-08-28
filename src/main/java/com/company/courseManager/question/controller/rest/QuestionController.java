package com.company.courseManager.question.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.courseManager.question.domain.Note;
import com.company.courseManager.question.domain.QueryNote;
import com.company.courseManager.question.domain.QueryQuestion;
import com.company.courseManager.question.domain.Question;
import com.company.courseManager.question.domain.QuestionEval;
import com.company.courseManager.question.service.NoteService;
import com.company.courseManager.question.service.QuestionService;
import com.company.coursestudent.domain.StudentConst;
import com.company.platform.controller.rest.ControllerUtils;
import com.xinwei.nnl.common.domain.ProcessResult;
@RestController
@RequestMapping("/courseQuestion")
public class QuestionController {
	@Autowired
	private QuestionService questionService;
	@PostMapping(value = "/{userId}/addQuestion")
	public ProcessResult configureQuestion(@PathVariable String userId, @RequestBody Question question) {
		try {
			return questionService.addQuestion(question);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
			
		}
	}
	
	@PostMapping(value = "/{userId}/queryNeedAnswerList")
	public ProcessResult queryNeedAnswer(@PathVariable String userId, @RequestBody QueryQuestion queryQuestion) {
		try {
			return questionService.queryNeedAnswer(queryQuestion);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
			
		}
	}
	
	@PostMapping(value = "/{userId}/queryQuestionsList")
	public ProcessResult queryMyQuestion(@PathVariable String userId, @RequestBody QueryQuestion queryQuestion) {
		try {
			return questionService.queryAllMyQuestion(queryQuestion);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
			
		}
	}
	
	@PostMapping(value = "/{userId}/answerQuestion")
	public ProcessResult answerQuestion(@PathVariable String userId, @RequestBody Question question) {
		try {
			return questionService.answerQuestion(question);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
			
		}
	}
	
	@PostMapping(value = "/{userId}/waitAnswerquestion")
	public ProcessResult waitAnswerquestion(@PathVariable String userId, @RequestBody Question question) {
		try {
			return questionService.waitAnswerquestion(question);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
			
		}
	}
	
	
	@PostMapping(value = "/{userId}/closedAnswerquestion")
	public ProcessResult closedAnswerquestion(@PathVariable String userId, @RequestBody Question question) {
		try {
			return questionService.endAnswerquestion(question);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
			
		}
	}
	
	@PostMapping(value = "/{userId}/ignoredAnswerquestion")
	public ProcessResult ignoredAnswerquestion(@PathVariable String userId, @RequestBody Question question) {
		try {
			return questionService.ignoreAnswerquestion(question);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
			
		}
	}
	
	@PostMapping(value = "/{userId}/evalAnswerquestion")
	public ProcessResult evalAnswerquestion(@PathVariable String userId, @RequestBody QuestionEval questionEval) {
		try {
			return questionService.evaluationAnswerquestion(questionEval);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
			
		}
	}
	
	@PostMapping(value = "/{userId}/queryQuestionChapter")
	public ProcessResult queryQuestionChapter(@PathVariable String userId, @RequestBody QueryQuestion queryQuestion) {
		try {
			return questionService.queryCourseChapter(queryQuestion);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
			
		}
	}
	
	
	@PostMapping(value = "/{userId}/queryQuestionDetail")
	public ProcessResult queryQuestionAndAnswer(@PathVariable String userId, @RequestBody QueryQuestion queryQuestion) {
		try {
			return questionService.queryQuestionAndAnswer(queryQuestion);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
			
		}
	}
	@PostMapping(value = "/{userId}/queryQuesitonByCourseId")
	public ProcessResult queryQuestionByCourseId(@PathVariable String userId, @RequestBody QueryQuestion queryQuestion) {
		try {
			return questionService.queryQuestionWithClassId(queryQuestion);
			//return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
			
		}
	}
	
	@PostMapping(value = "/{userId}/queryQuestionByClassId")
	public ProcessResult queryQuestionWithClassId(@PathVariable String userId, @RequestBody QueryQuestion queryQuestion) {
		try {
			return questionService.queryQuestionWithClassId(queryQuestion);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
			
		}
	}
	
}
