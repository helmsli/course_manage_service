package com.company.courseManager.teacher.service;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.company.courseManager.Const.CourseCounterConst;
import com.company.courseManager.Const.CoursemanagerConst;
import com.company.platform.controller.rest.ControllerUtils;
import com.company.platform.idgenerator.RedisCommonServiceImpl;
import com.company.platform.order.StatCounter;
import com.company.platform.order.StatCounterDbService;
import com.xinwei.nnl.common.domain.ProcessResult;

@Service("teacherCourseStatService")
public class TeacherCourseStatService extends RedisCommonServiceImpl {

	@Resource(name = "statCounterDbService")
	private StatCounterDbService statCounterDbService;

	/**
	 * 返回教师发布的课程数目的锁
	 * 
	 * @return
	 */
	protected String getLockForTeacherCourse(String userId, String courseId) {
		return "lock:t:" + userId + ":" + courseId;
	}
	/**
	 * 返回redis中存储的教师发布课程的数目的key
	 * @param userId
	 * @param courseId
	 * @return
	 */
	protected String getRedisKeyTeacherCourse(String userId) {
		return "counter:t:" + userId;
	}

	/**
	 * 教师发布课程后增加教师发布的课程数目
	 * 
	 * @param userId
	 * @param courseId
	 * @param amount
	 * @return
	 */
	public ProcessResult plusTeacherCourseAmountOne(String userId, String courseId) {
		// 增加redis内存，
		String lockKey = getLockForTeacherCourse(userId, courseId);
		long lockTime = 0;
		try {
			lockTime = getCommonLock(lockKey, 30);
			if (lockTime == 0) {
				return ControllerUtils.getErrorResponse(CoursemanagerConst.RESULT_FAILURE_COURSTeacherSTAT,
						CoursemanagerConst.RESULT_FAILURE_STRING_TeacherSTAT);
			}
			String teacherCourseKey = CourseCounterConst.getTeacherCourseCounterKey(userId, courseId);
			StatCounter statCounter = new StatCounter();
			statCounter.setUserId(CourseCounterConst.getTeacherCourseUserId(userId));
			statCounter.setAmountId(CourseCounterConst.getTeacherCourseCourseId(courseId));
			statCounter.setOwnerKey(teacherCourseKey);
			ProcessResult ret = this.statCounterDbService.plusOne(CourseCounterConst.Category_Teacher_course,
					statCounter);
			if (ret.getRetCode() == 0) {
				
				String redisKey = getRedisKeyTeacherCourse(userId);
				this.redisTemplate.delete(redisKey);
			}
			return ret;
			// 增加数据库的计数器
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, CoursemanagerConst.RESULT_FAILURE, null);
		} finally {
			this.releaseCommonLock(lockKey, lockTime);
		}
	}

	/**
	 * 返回
	 * 
	 * @param userId
	 * @param courseId
	 * @return if success responseInfo = new Long(amount)
	 */
	public ProcessResult getTeacherCourseCounter(String userId, String courseId) {
		String lockKey = getLockForTeacherCourse(userId, courseId);
		long lockTime = 0;
		try {
			String redisKey = getRedisKeyTeacherCourse(userId);
			Object counterValue = this.redisTemplate.opsForValue().get(redisKey);
			if (counterValue != null) {
				ProcessResult ret = ControllerUtils.getSuccessResponse(null);
				ret.setResponseInfo(counterValue);
				return ret;
			}
			lockTime = getCommonLock(lockKey, 30);
			if (lockTime == 0) {
				return ControllerUtils.getErrorResponse(CoursemanagerConst.RESULT_FAILURE_COURSTeacherSTAT,
						CoursemanagerConst.RESULT_FAILURE_STRING_TeacherSTAT);
			}

			//加锁后需要在判断redis中是否有数据，避免脏数据；
			counterValue = this.redisTemplate.opsForValue().get(redisKey);
			if (counterValue != null) {
				ProcessResult ret = ControllerUtils.getSuccessResponse(null);
				ret.setResponseInfo(counterValue);
				return ret;
			}
			
			String teacherCourseKey = CourseCounterConst.getTeacherCourseCounterKey(userId, courseId);
			
			StatCounter statCounter = new StatCounter();
			statCounter.setUserId(CourseCounterConst.getTeacherCourseUserId(userId));
			statCounter.setAmountId(CourseCounterConst.getTeacherCourseCourseId(courseId));
			ProcessResult ret = this.statCounterDbService.getAmount(CourseCounterConst.Category_Teacher_course,
					statCounter);
			if (ret.getRetCode() == 0) {
				long amount = Long.parseLong(ret.getResponseInfo().toString());
				Long amountValue = new Long(amount);
				this.redisTemplate.opsForValue().set(redisKey, amountValue, 7, TimeUnit.DAYS);
				ret.setResponseInfo(amountValue);
			}
			return ret;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, CoursemanagerConst.RESULT_FAILURE, null);
		} finally {
			this.releaseCommonLock(lockKey, lockTime);
		}
	}

	/**
	 * 返回教师学生数目的锁
	 * 
	 * @return
	 */
	protected String getLockStudentAmount(String userId, String courseId) {
		return "lock:ts:" + userId + ":" + courseId;
	}
/**
 * 返回redis中保存教师学生数目的key
 * @param userId
 * @return
 */
	protected String getRedisKeyStudentAmount(String userId) {
		return "counter:ts:" + userId ;
	}
	public ProcessResult plusTeacherStudentAmountOne(String teacherUserId,String userId, String courseId) {
		// 增加redis内存，
		String lockKey = getLockStudentAmount(teacherUserId, courseId);
		long lockTime = 0;
		try {
			lockTime = getCommonLock(lockKey, 30);
			if (lockTime == 0) {
				return ControllerUtils.getErrorResponse(CoursemanagerConst.RESULT_FAILURE_COURSTeacherSTAT,
						CoursemanagerConst.RESULT_FAILURE_STRING_TeacherSTAT);
			}
			String teacherStudentKey = CourseCounterConst.getTeacherStudentCounterKey(teacherUserId, userId, courseId);
			StatCounter statCounter = new StatCounter();
			statCounter.setUserId(CourseCounterConst.getTeacherStudentCourseId(courseId));
			statCounter.setAmountId(CourseCounterConst.getTeacherStudentCourseId(courseId));
			statCounter.setOwnerKey(teacherStudentKey);
			ProcessResult ret = this.statCounterDbService.plusOne(CourseCounterConst.Category_Teacher_course,
					statCounter);
			if (ret.getRetCode() == 0) {
				String redisKey = getRedisKeyStudentAmount(teacherUserId);
				this.redisTemplate.delete(redisKey);
			}
			return ret;
			// 增加数据库的计数器
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, CoursemanagerConst.RESULT_FAILURE, null);
		} finally {
			this.releaseCommonLock(lockKey, lockTime);
		}
	}

	/**
	 * 返回
	 * 
	 * @param userId
	 * @param courseId
	 * @return if success responseInfo = new Long(amount)
	 */
	public ProcessResult getTeacherStudentCounter(String teacherUserId, String courseId) {
		String lockKey = getLockStudentAmount(teacherUserId, courseId);
		long lockTime = 0;
		try {
			String redisKey = getRedisKeyStudentAmount(teacherUserId);
			Object counterValue = this.redisTemplate.opsForValue().get(redisKey);
			if (counterValue != null) {
				ProcessResult ret = ControllerUtils.getSuccessResponse(null);
				ret.setResponseInfo(counterValue);
				return ret;
			}
			lockTime = getCommonLock(lockKey, 30);
			if (lockTime == 0) {
				return ControllerUtils.getErrorResponse(CoursemanagerConst.RESULT_FAILURE_COURSTeacherSTAT,
						CoursemanagerConst.RESULT_FAILURE_STRING_TeacherSTAT);
			}
			counterValue = this.redisTemplate.opsForValue().get(redisKey);
			if (counterValue != null) {
				ProcessResult ret = ControllerUtils.getSuccessResponse(null);
				ret.setResponseInfo(counterValue);
				return ret;
			}
			
			StatCounter statCounter = new StatCounter();
			statCounter.setUserId(CourseCounterConst.getTeacherStudentCourseId(courseId));
			statCounter.setAmountId(CourseCounterConst.getTeacherStudentCourseId(courseId));
			ProcessResult ret = this.statCounterDbService.getAmount(CourseCounterConst.Category_Teacher_course,
					statCounter);
			if (ret.getRetCode() == 0) {
				long amount = Long.parseLong(ret.getResponseInfo().toString());
				Long amountValue = new Long(amount);
				this.redisTemplate.opsForValue().set(redisKey, amountValue, 7, TimeUnit.DAYS);
				ret.setResponseInfo(amountValue);
			}
			return ret;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, CoursemanagerConst.RESULT_FAILURE, null);
		} finally {
			this.releaseCommonLock(lockKey, lockTime);
		}
	}

}
