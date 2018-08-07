package com.company.courseManager.question.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.courseManager.courseevaluation.domain.CourseEvaluation;
import com.company.courseManager.question.domain.Note;
import com.company.courseManager.question.domain.QueryNote;
import com.company.courseManager.question.service.NoteService;
import com.company.coursestudent.domain.StudentConst;
import com.company.platform.controller.rest.ControllerUtils;
import com.xinwei.nnl.common.domain.ProcessResult;

@RestController
@RequestMapping("/courseNote")
public class NoteController {
	@Autowired
	private NoteService noteService;
	@PostMapping(value = "/{userId}/addNodes")
	public ProcessResult configureEvaluation(@PathVariable String userId, @RequestBody Note note) {
		try {
			return noteService.addNote(note);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
			
		}
	}
	@PostMapping(value = "/{userId}/queryNodesList")
	public ProcessResult queryMyNote(@PathVariable String userId, @RequestBody QueryNote queryNote) {
		try {
			return noteService.queryAllMyNote(queryNote);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
			
		}
	}
	@PostMapping(value = "/{userId}/queryNoteChapter")
	public ProcessResult queryNoteChapter(@PathVariable String userId, @RequestBody QueryNote queryNote) {
		try {
			return noteService.queryCourseChapter(queryNote);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
			
		}
	}
	@PostMapping(value = "/{userId}/queryNoteByCourseId")
	public ProcessResult queryNoteByCourseId(@PathVariable String userId, @RequestBody QueryNote queryNote) {
		try {
			return noteService.queryNodesWithClassId(queryNote);
			//return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
			
		}
	}
	
	@PostMapping(value = "/{userId}/queryNoteByClassId")
	public ProcessResult queryNodesWithClassId(@PathVariable String userId, @RequestBody QueryNote queryNote) {
		try {
			return noteService.queryNodesWithClassId(queryNote);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
			
		}
	}
	
}
