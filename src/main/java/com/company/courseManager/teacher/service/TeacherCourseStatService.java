package com.company.courseManager.teacher.service;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.company.courseManager.Const.CourseCounterConst;
import com.company.courseManager.Const.CoursemanagerConst;
import com.company.courseManager.teacher.domain.TeacherCounter;
import com.company.platform.controller.rest.ControllerUtils;
import com.company.platform.idgenerator.RedisCommonServiceImpl;
import com.company.platform.order.StatCounter;
import com.company.platform.order.StatCounterDbService;
import com.xinwei.nnl.common.domain.ProcessResult;

@Service("teacherCourseStatService")
public class TeacherCourseStatService extends RedisCommonServiceImpl {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name = "statCounterDbService")
	private StatCounterDbService statCounterDbService;

	/**
	 * 返回教师发布的课程数目的锁
	 * 
	 * @return
	 */
	protected String getLockForTeacherCourse(String userId) {
		return "lock:t:" + userId ;
	}

	/**
	 * 返回redis中存储的教师发布课程的数目的key
	 * 
	 * @param userId
	 * @param courseId
	 * @return
	 */
	protected String getRedisKeyTeacherCourse(String userId) {
		return "counter:t:" + userId;
	}
	/**
	 * 获取老师总的评价分数
	 * @param userId
	 * @return
	 */
	protected String getRedisKeyTeacherTotalScore(String userId) {
		return "counter:tScore:" + userId;
	}

	/**
	 * 教师发布课程后增加教师发布的课程数目
	 * 
	 * @param userId
	 * @param courseId
	 * @param amount
	 * @return
	 */
	@Async
	public ProcessResult plusTeacherCourseAmountOne(String userId, String courseId) {
		// 增加redis内存，
		String lockKey = getLockForTeacherCourse(userId);
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
			statCounter.setAmountId(CourseCounterConst.getTeacherCourseCourseId());
			statCounter.setOwnerKey(teacherCourseKey);
			ProcessResult ret = this.statCounterDbService.plusOne(CourseCounterConst.Category_Teacher_course,
					statCounter);
			if (ret.getRetCode() == 0) {

				String redisKey = getRedisKeyTeacherCourse(userId);
				this.redisTemplate.delete(redisKey);
			}
			else if(ret.getRetCode() == 255)
			{
				ret.setRetCode(0);;
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
	public ProcessResult getTeacherCourseCounter(String userId) {
		String lockKey = getLockForTeacherCourse(userId);
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

			// 加锁后需要在判断redis中是否有数据，避免脏数据；
			counterValue = this.redisTemplate.opsForValue().get(redisKey);
			if (counterValue != null) {
				ProcessResult ret = ControllerUtils.getSuccessResponse(null);
				ret.setResponseInfo(counterValue);
				return ret;
			}

			
			StatCounter statCounter = new StatCounter();
			statCounter.setUserId(CourseCounterConst.getTeacherCourseUserId(userId));
			statCounter.setAmountId(CourseCounterConst.getTeacherCourseCourseId());
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
	protected String getLockStudentAmount(String userId) {
		return "lock:ts:" + userId ;
	}

	/**
	 * 返回redis中保存教师学生数目的key
	 * 
	 * @param userId
	 * @return
	 */
	protected String getRedisKeyStudentAmount(String userId) {
		return "counter:ts:" + userId;
	}
	@Async 
	public ProcessResult plusTeacherStudentAmountOne(String teacherUserId, String userId) {
		// 增加redis内存，
		String lockKey = getLockStudentAmount(teacherUserId);
		long lockTime = 0;
		try {
			lockTime = getCommonLock(lockKey, 30);
			if (lockTime == 0) {
				return ControllerUtils.getErrorResponse(CoursemanagerConst.RESULT_FAILURE_COURSTeacherSTAT,
						CoursemanagerConst.RESULT_FAILURE_STRING_TeacherSTAT);
			}
			String teacherStudentKey = CourseCounterConst.getTeacherStudentCounterKey(teacherUserId, userId);
			StatCounter statCounter = new StatCounter();
			statCounter.setUserId(CourseCounterConst.getTeacherStudentUserId(teacherUserId));
			statCounter.setAmountId(CourseCounterConst.getTeacherStudentAmountId());
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
	public ProcessResult getTeacherStudentCounter(String teacherUserId) {
		String lockKey = getLockStudentAmount(teacherUserId);
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
			statCounter.setUserId(CourseCounterConst.getTeacherStudentUserId(teacherUserId));
			statCounter.setAmountId(CourseCounterConst.getTeacherStudentAmountId());
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
	 * 返回课程学员数目的锁
	 * 
	 * @return
	 */
	protected String getLockCourseAmount(String courseId) {
		return "lock:course:"  + courseId;
	}

	/**
	 * 返回redis中保存教师学生数目的key
	 * 
	 * @param userId
	 * @return
	 */
	protected String getRedisKeyCourseAmount(String courseId) {
		return "counter:course:" + courseId;
	}

	/**
	 * 计算课程学员的数量
	 * @param teacherUserId
	 * @param userId
	 * @param courseId
	 * @return
	 */
	@Async
	public ProcessResult plusCourseStudentAmountOne(String userId, String courseId) {
		// 增加redis内存，
		String lockKey = getLockCourseAmount(courseId);
		long lockTime = 0;
		try {
			lockTime = getCommonLock(lockKey, 30);
			if (lockTime == 0) {
				return ControllerUtils.getErrorResponse(CoursemanagerConst.RESULT_FAILURE_COURSTeacherSTAT,
						CoursemanagerConst.RESULT_FAILURE_STRING_TeacherSTAT);
			}
			String courseStudentKey = CourseCounterConst.getTeacherCourseCounterKey(userId, courseId);
			StatCounter statCounter = new StatCounter();
			statCounter.setUserId(CourseCounterConst.getCourseStudentUserId(courseId));
			statCounter.setAmountId(CourseCounterConst.getCourseStudentAmountId());
			statCounter.setOwnerKey(courseStudentKey);
			ProcessResult ret = this.statCounterDbService.plusOne(CourseCounterConst.Category_Teacher_course,
					statCounter);
			if (ret.getRetCode() == 0) {
				String redisKey = getRedisKeyStudentAmount(courseId);
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
	public ProcessResult getCourseStudentCounter(String courseId) {
		String lockKey = getLockCourseAmount(courseId);
		long lockTime = 0;
		try {
			String redisKey = getRedisKeyStudentAmount(courseId);
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
			statCounter.setUserId(CourseCounterConst.getCourseStudentUserId(courseId));
			statCounter.setAmountId(CourseCounterConst.getCourseStudentAmountId());
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
	 * 返回课程评价和评分的锁
	 * 
	 * @return
	 */
	protected String getLockCourseScore(String courseId) {
		return "lock:courseScore:"  + courseId;
	}

	
	/**
	 * 返回课程评价和评分的锁
	 * 
	 * @return
	 */
	protected String getLockCourseStartCounter(String courseId) {
		return "lock:courseStarCounter:"  + courseId;
	}
	
	/**
	 * 
	 * @param courseId
	 * @return
	 */
	protected String getLockTeacherScore(String teacherId) {
		return "lock:teacherScore:"  + teacherId;
	}
	
	
	/**
	 * 返回redis中保存教师学生数目的key
	 * 
	 * @param userId
	 * @return
	 */
	protected String getRedisKeyCourseScore(String courseId) {
		return "score:courseScore:" + courseId;
	}
	/**
	 * 评价的人数保存在redis中
	 * @param courseId
	 * @return
	 */
	protected String getRedisKeyCourseScorePerson(String courseId) {
		return "score:courseamount:" + courseId;
	}
	
	/**
	 * 课程回复和评论的个数的rediskey
	 * @param courseId
	 * @return
	 */
	protected String getRedisKeyCourseStarPerson(String courseId) {
		return "coursestar:" + courseId;
	}
	
	/**
	 * 老师的总的评价数目
	 * @param courseId
	 * @return
	 */
	protected String getRedisKeyTeacherScorePerson(String teacherUid) {
		return "score:teacheramount:" + teacherUid;
	}
	
	
	/**
	 * 获取老师的所有课程的评价总人数
	 * @param courseId
	 * @return
	 */
	public long getTeacherStartPerson(String teacherUid,LoadCacheCallback loadToredisCallback) {
		try {
			
			String redisKey = getRedisKeyTeacherScorePerson(teacherUid);
			Object counterValue = this.redisTemplate.opsForValue().get(redisKey);
			if (counterValue != null) {
				Long amountValue = (Long)counterValue;
				return amountValue.longValue();
			}
			StatCounter statCounter = new StatCounter();
			statCounter.setUserId(CourseCounterConst.getTeacherScoreUserId(teacherUid));
			statCounter.setAmountId(CourseCounterConst.getTeacherStarAmountId());
			ProcessResult ret = this.statCounterDbService.getAmount(CourseCounterConst.Category_Teacher_course,
					statCounter);
			if (ret.getRetCode() == 0) {
				long amount = Long.parseLong(ret.getResponseInfo().toString());
				Long amountValue = new Long(amount);
				if(loadToredisCallback==null)
				{
					loadToredisCallback=new LoadCourseScoreTomemroy();
				}
				loadToredisCallback.loadToCache(redisKey, amountValue);
				
				return amountValue.longValue();
			}
			return 0;
			
			// 增加数据库的计数器
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} finally {
			
		}
	}

	/**
	 * 获取老师的总的评价
	 * @param coureseId
	 * @return
	 */
	public double getTeacherScore(String coureseId)
	{
		long courseScore=0;
		try {
			courseScore = getInitCourseScore(coureseId,null);
			if(courseScore<=0)
			{
				return 100;
			}
			return courseScore/(41850) * 100;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 100;
		}
		
		
	}


	/**
	 * 计算老师的评分
	 * @param userId
	 * @param courseId
	 * @param star
	 * @return
	 */
	@Async
	public ProcessResult plusTeacherScore(String teacherId,int star,String starUserId,String starCourseId) {
		// 增加redis内存，
		String lockKey = getLockTeacherScore(teacherId);
		long lockTime = 0;
		try {
			lockTime = getCommonLock(lockKey, 30);
			if (lockTime == 0) {
				return ControllerUtils.getErrorResponse(CoursemanagerConst.RESULT_FAILURE_COURSTeacherSTAT,
						CoursemanagerConst.RESULT_FAILURE_STRING_TeacherSTAT);
			}
			
			//获取当前的评价人数和评价分数
			long currentPerson = getTeacherStartPerson(teacherId,new NoLoadCourseScoreTomemroy());
			long currentScore = getInitTeacherScore(teacherId,new NoLoadCourseScoreTomemroy()); 
			long starScore = caculateScoreFromStar(star);
			long newScore = caculateCourseScore(currentPerson,currentScore,1,starScore);
			/**
			 * 增加老师评价计数器的总分数
			 */
			String courseScoreKey = CourseCounterConst.getCourseScoreCounterKey(starUserId, starCourseId);
			StatCounter statCounter = new StatCounter();
			statCounter.setUserId(CourseCounterConst.getTeacherScoreUserId(teacherId));
			statCounter.setAmountId(CourseCounterConst.getTeacherScoreAmountId());
			statCounter.setOwnerKey(courseScoreKey);
			statCounter.setAmount((int)(newScore-currentScore));
			ProcessResult ret = this.statCounterDbService.plusGreaterOne(CourseCounterConst.Category_Teacher_course,
					statCounter);
			
			if(ret.getRetCode()!=0)
			{
				return ret;
			}			
			statCounter.setUserId(CourseCounterConst.getTeacherScoreUserId(teacherId));
			statCounter.setAmountId(CourseCounterConst.getTeacherStarAmountId());
			statCounter.setOwnerKey(courseScoreKey);
			//增加老师评价的总人数计数
			ret = this.statCounterDbService.plusOne(CourseCounterConst.Category_Teacher_course,
					statCounter);
			if (ret.getRetCode() == 0) {
				String redisKey = getRedisKeyTeacherScorePerson(teacherId);
				
				this.redisTemplate.opsForValue().set(redisKey, new Long(currentPerson+1), 7, TimeUnit.DAYS);
		
				redisKey = this.getRedisKeyTeacherTotalScore(teacherId);
				
				this.redisTemplate.opsForValue().set(redisKey, new Long(newScore), 7, TimeUnit.DAYS);
		
			}
			return ret;
			// 增加数据库的计数器
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("error", e);
			return ControllerUtils.getFromResponse(e, CoursemanagerConst.RESULT_FAILURE, null);
		} finally {
			this.releaseCommonLock(lockKey, lockTime);
		}
	}

	/**
	 * 获取一个课程总的评价数和评论数目
	 * @param courseId
	 * @return
	 */
	public int getCourseStarCounter(String courseId) {
		try {
			String redisKey = getRedisKeyCourseStarPerson(courseId);			
			Long value = (Long)redisTemplate.opsForValue().get(redisKey);
			if(value==null)
			{
				//
				StatCounter statCounter = new StatCounter();
				statCounter.setUserId(CourseCounterConst.getCourseScoreUserId(courseId));
				statCounter.setAmountId(CourseCounterConst.getCourseStartPersonAmountId());
				ProcessResult ret = this.statCounterDbService.getAmount(CourseCounterConst.Category_Teacher_course,
						statCounter);
				if (ret.getRetCode() == 0) {
					long amount = Long.parseLong(ret.getResponseInfo().toString());
					Long amountValue = new Long(amount);
					String lockKey = getLockCourseStartCounter(courseId);
					long lockTime = 0;
					try {
						lockTime = getCommonLock(lockKey, 30);
						if (lockTime == 0) {
							return 0;
						}
						redisTemplate.opsForValue().set(redisKey, amountValue, 7, TimeUnit.DAYS);
					}
					finally
					{
						this.releaseCommonLock(lockKey, lockTime);
					}
				}
				else
				{
					return 0;
				}
			
				
			}
			return value.intValue();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
		
	
	/**
	 * 对课程的评论和回复进行计数，单纯记录对课程的评论和回复的个数
	 * @param userId
	 * @param courseId
	 * @param star
	 * @return
	 */
	@Async
	public ProcessResult plusCourseStarCounter(String userId, String courseId) {
		// 增加redis内存，
		
		String lockKey = getLockCourseStartCounter(courseId);
		long lockTime = 0;
		try {
			lockTime = getCommonLock(lockKey, 30);
			if (lockTime == 0) {
				return ControllerUtils.getErrorResponse(CoursemanagerConst.RESULT_FAILURE_COURSTeacherSTAT,
						CoursemanagerConst.RESULT_FAILURE_STRING_TeacherSTAT);
			}
			
			//获取当前的评价人数和评价分数
			StatCounter statCounter = new StatCounter();
			statCounter.setUserId(CourseCounterConst.getCourseScoreUserId(courseId));
			statCounter.setAmountId(CourseCounterConst.getCourseStarAmountId());
			statCounter.setOwnerKey(String.valueOf(userId) + ":" + String.valueOf(System.currentTimeMillis()/(300000)));
			
			ProcessResult ret = this.statCounterDbService.plusOne(CourseCounterConst.Category_Teacher_course,
					statCounter);
			
			if(ret.getRetCode()!=0)
			{
				return ret;
			}			
			if (ret.getRetCode() == 0) {
				String redisKey = getRedisKeyCourseStarPerson(courseId);			
				this.redisTemplate.delete(redisKey);
			}
			return ret;
			// 增加数据库的计数器
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("error", e);
			return ControllerUtils.getFromResponse(e, CoursemanagerConst.RESULT_FAILURE, null);
		} finally {
			this.releaseCommonLock(lockKey, lockTime);
		}
	}
	
	
	
	/**
	 * 计算课程的评价分数
	 * @param teacherUserId
	 * @param userId
	 * @param courseId
	 * @return
	 */
	@Async
	public ProcessResult plusCourseScore(String userId, String courseId,int star) {
		// 增加redis内存，
		String lockKey = getLockCourseScore(courseId);
		long lockTime = 0;
		try {
			lockTime = getCommonLock(lockKey, 30);
			if (lockTime == 0) {
				return ControllerUtils.getErrorResponse(CoursemanagerConst.RESULT_FAILURE_COURSTeacherSTAT,
						CoursemanagerConst.RESULT_FAILURE_STRING_TeacherSTAT);
			}
			
			//获取当前的评价人数和评价分数
			long currentPerson = getCourseStartPerson(courseId,new NoLoadCourseScoreTomemroy());
			long currentScore = getInitCourseScore(courseId,new NoLoadCourseScoreTomemroy()); 
			long starScore = caculateScoreFromStar(star);
			long newScore = caculateCourseScore(currentPerson,currentScore,1,starScore);
			
			String courseScoreKey = CourseCounterConst.getCourseScoreCounterKey(userId, courseId);
			StatCounter statCounter = new StatCounter();
			statCounter.setUserId(CourseCounterConst.getCourseScoreUserId(courseId));
			statCounter.setAmountId(CourseCounterConst.getCourseScoreAmountId());
			statCounter.setOwnerKey(courseScoreKey);
			statCounter.setAmount((int)(newScore-currentScore));
			ProcessResult ret = this.statCounterDbService.plusGreaterOne(CourseCounterConst.Category_Teacher_course,
					statCounter);
			
			if(ret.getRetCode()!=0)
			{
				return ret;
			}			
			statCounter.setUserId(CourseCounterConst.getCourseScoreUserId(courseId));
			statCounter.setAmountId(CourseCounterConst.getCourseStartPersonAmountId());
			statCounter.setOwnerKey(courseScoreKey);
			ret = this.statCounterDbService.plusOne(CourseCounterConst.Category_Teacher_course,
					statCounter);
			if (ret.getRetCode() == 0) {
				String redisKey = getRedisKeyCourseScorePerson(courseId);
				this.redisTemplate.opsForValue().set(redisKey, new Long(currentPerson+1), 7, TimeUnit.DAYS);
		
			    redisKey = getRedisKeyCourseScore(courseId);
				this.redisTemplate.opsForValue().set(redisKey, new Long(newScore), 7, TimeUnit.DAYS);
		
			}
			return ret;
			// 增加数据库的计数器
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("error", e);
			return ControllerUtils.getFromResponse(e, CoursemanagerConst.RESULT_FAILURE, null);
		} finally {
			this.releaseCommonLock(lockKey, lockTime);
		}
	}

	/**
	 * 更具老的评分计算新课程的评分
	 * @param currentPerson
	 * @param currentScore
	 * @param newPerson
	 * @param newScore
	 * @return
	 */
	public long caculateCourseScore(long currentPerson,long currentScore,long newPerson,long newScore)
	{
		if(newPerson >0 && currentScore>=0 && currentPerson>=0 && newPerson>0)
		{
			//假设最初有10个人给了五星，避免刚开始分数过低的问题
			if(currentPerson<10)
			{
				return (8370*50 + currentScore * currentPerson+newScore) /(10+currentPerson+newPerson);
						
			}
			else
			{
				return (currentScore * currentPerson+newScore) /(currentPerson+newPerson);
			}
		}
		return 0;
	}
	
	
	/**
	 * 根据星级计算评分
	 * @param currentPerson
	 * @param currentScore
	 * @param newPerson
	 * @param newScore
	 * @return
	 */
	public long caculateScoreFromStar(int star)
	{
		if(star>5)
		{
			star=5;
		}
		if(star<=0)
		{
			star=1;
		}
		if(star==5)
		{
			return 8370*5;
		}
		if(star==4)
		{
			return 4*1130;
		}
		if(star==3)
		{
			return 3*278;
		}
		if(star==2)
		{
			return 2*185;
		}
		if(star==1)
		{
			return 1*37;
		}
		return 0;
	}
	
	/**
	 * 获取当前课程的评价人数
	 * @param courseId
	 * @return
	 */
	public long getCourseStartPerson(String courseId,LoadCacheCallback loadToredisCallback) {
		try {
			
			String redisKey = getRedisKeyCourseScorePerson(courseId);
			Object counterValue = this.redisTemplate.opsForValue().get(redisKey);
			if (counterValue != null) {
				Long amountValue = (Long)counterValue;
				return amountValue.longValue();
			}
			StatCounter statCounter = new StatCounter();
			statCounter.setUserId(CourseCounterConst.getCourseScoreUserId(courseId));
			statCounter.setAmountId(CourseCounterConst.getCourseStartPersonAmountId());
			ProcessResult ret = this.statCounterDbService.getAmount(CourseCounterConst.Category_Teacher_course,
					statCounter);
			if (ret.getRetCode() == 0) {
				long amount = Long.parseLong(ret.getResponseInfo().toString());
				Long amountValue = new Long(amount);
				if(loadToredisCallback==null)
				{
					loadToredisCallback=new LoadCourseScoreTomemroy();
				}
				loadToredisCallback.loadToCache(redisKey, amountValue);
				
				return amountValue.longValue();
			}
			return 0;
			
			// 增加数据库的计数器
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} finally {
			
		}
	}
	
	
	public double getCourseScore(String coureseId)
	{
		long courseScore=0;
		try {
			courseScore = getInitCourseScore(coureseId,null);
			if(courseScore<=0)
			{
				return 100;
			}
			return courseScore/(41850) * 100;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 100;
		}
		
		
	}

	/**
	 * 获取老师的评分的原始评分
	 * @param teacherUid
	 * @param loadToCachecallbcak
	 * @return
	 */
	public long getInitTeacherScore(String teacherUid,LoadCacheCallback loadToCachecallbcak) {
		try {
			
			String redisKey = this.getRedisKeyTeacherTotalScore(teacherUid);
			Object counterValue = this.redisTemplate.opsForValue().get(redisKey);
			if (counterValue != null) {
				Long amountValue = (Long)counterValue;
				return amountValue.longValue();
			}			
			StatCounter statCounter = new StatCounter();
			statCounter.setUserId(CourseCounterConst.getTeacherScoreUserId(teacherUid));
			statCounter.setAmountId(CourseCounterConst.getTeacherScoreAmountId());
			ProcessResult ret = this.statCounterDbService.getAmount(CourseCounterConst.Category_Teacher_course,
					statCounter);
			if (ret.getRetCode() == 0) {
				long amount = Long.parseLong(ret.getResponseInfo().toString());
				Long amountValue = new Long(amount);
				if(loadToCachecallbcak!=null)
				{
				  loadToCachecallbcak.loadToCache(redisKey,amountValue);
				}
				else
				{
					loadToCachecallbcak=new LoadCourseScoreTomemroy();
					loadToCachecallbcak.loadToCache(redisKey, amountValue);
			
				}
				return amountValue.longValue();
			}
			return 0;
			
			// 增加数据库的计数器
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} finally {
			
		}
	}

	
	/**
	 * 获取当前课程的评价分数,原始的评价分数，没有加工过直接用于新的评价的计算。
	 * @param courseId
	 * @return
	 */
	public long getInitCourseScore(String courseId,LoadCacheCallback loadToCachecallbcak) {
		try {
			
			String redisKey = getRedisKeyCourseScore(courseId);
			Object counterValue = this.redisTemplate.opsForValue().get(redisKey);
			if (counterValue != null) {
				Long amountValue = (Long)counterValue;
				return amountValue.longValue();
			}			
			StatCounter statCounter = new StatCounter();
			statCounter.setUserId(CourseCounterConst.getCourseScoreUserId(courseId));
			statCounter.setAmountId(CourseCounterConst.getCourseScoreAmountId());
			ProcessResult ret = this.statCounterDbService.getAmount(CourseCounterConst.Category_Teacher_course,
					statCounter);
			if (ret.getRetCode() == 0) {
				long amount = Long.parseLong(ret.getResponseInfo().toString());
				Long amountValue = new Long(amount);
				if(loadToCachecallbcak!=null)
				{
				  loadToCachecallbcak.loadToCache(redisKey,amountValue);
				}
				else
				{
					loadToCachecallbcak=new LoadCourseScoreTomemroy();
					loadToCachecallbcak.loadToCache(redisKey, amountValue);
			
				}
				return amountValue.longValue();
			}
			return 0;
			
			// 增加数据库的计数器
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} finally {
			
		}
	}

	
	private interface LoadCacheCallback {  
	    public void loadToCache(String key,Object object);  
	}  
	
	private class LoadCourseScoreTomemroy implements LoadCacheCallback
	{

		@Override
		public void loadToCache(String key, Object object) {
			// TODO Auto-generated method stub
			String lockKey = key;
			long lockTime = 0;
			try {
				lockTime = getCommonLock(lockKey, 30);
				if (lockTime == 0) {
					return ;
				}
				redisTemplate.opsForValue().set(lockKey, object, 7, TimeUnit.DAYS);
				
			}
			catch(Exception e)
			{
				
			}
			finally
			{
				releaseCommonLock(key,lockTime);
			}
		}
		
	}
	private class NoLoadCourseScoreTomemroy implements LoadCacheCallback
	{

		@Override
		public void loadToCache(String key, Object object) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	public TeacherCounter getTeacherCounter(String teacherUserId,TeacherCounter teacherCounter)
	{
		if(teacherCounter==null)
		{
			teacherCounter = new TeacherCounter();
		}
		try {
			ProcessResult ret = 
					getTeacherStudentCounter(teacherUserId);

			if (ret.getRetCode() == 0) {
				Long amountValue = (Long) ret.getResponseInfo();
				teacherCounter.setStudentAmount((int) (amountValue.longValue()));
			}

			ret = getTeacherCourseCounter(teacherUserId);

			if (ret.getRetCode() == 0) {
				Long amountValue = (Long) ret.getResponseInfo();
				teacherCounter.setCourseAmount((int) amountValue.longValue());
			}

			double teacherScore = getTeacherScore(teacherUserId);
			DecimalFormat df = new DecimalFormat("#.##");
			teacherCounter.setScore(df.format(teacherScore));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return teacherCounter;
	}
}
