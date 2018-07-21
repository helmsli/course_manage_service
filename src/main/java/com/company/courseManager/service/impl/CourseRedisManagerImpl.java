package com.company.courseManager.service.impl;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.company.courseManager.domain.CourseTeacher;
import com.company.videodb.domain.CourseClass;
import com.company.videodb.domain.Courses;
import com.xinwei.nnl.common.util.JsonUtil;

@Service("courseRedisManager")
public class CourseRedisManagerImpl {
	@Resource (name = "redisTemplate")
	protected RedisTemplate<Object, Object> redisTemplate;
	
	@Value("${course.cache.expireHours:72}")
	private int courseCacheExpireHours;
	
	
	public String getCourseKey(String courseId)
	{
		return "courseId:" + courseId; 
	}
	
	public String getCourseClassMapKey(String courseId)
	{
		return "classes:" + courseId; 
	}
	public String getClassKey(String courseId,String classId)
	{
		return ":"+classId; 
	}
	
	public void putCourseToCache(Courses courses)
	{
		try {
			String key = this.getCourseKey(courses.getCourseId());
			redisTemplate.opsForValue().set(key, JsonUtil.toJson(courses), courseCacheExpireHours, TimeUnit.HOURS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void delCourse(String courseId)
	{
		try {
			String key = this.getCourseKey(courseId);
			String classKey = this.getCourseClassMapKey(courseId);
			redisTemplate.delete(key);
			redisTemplate.delete(classKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public CourseTeacher getCourseFromCache(String courseId)
	{
		try {
			String key = this.getCourseKey(courseId);
			String lsStr = (String)redisTemplate.opsForValue().get(key);
			return (CourseTeacher)JsonUtil.fromJson(lsStr, CourseTeacher.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void putClassToCache(CourseClass courseClass)
	{
		try {
			String key = this.getCourseClassMapKey(courseClass.getCourseId());
			String classkey = this.getClassKey(courseClass.getCourseId(), courseClass.getClassId());
			
			redisTemplate.opsForHash().put(key,classkey, courseClass);
			redisTemplate.expire(key, courseCacheExpireHours, TimeUnit.HOURS);
			//redisTemplate.opsForValue().set(classkey, courseClass, courseCacheExpireHours, TimeUnit.HOURS);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public CourseClass getClassFromCache(String courseId,String classId)
	{
		try {
			String key = this.getCourseClassMapKey(courseId);
			String classkey = this.getClassKey(courseId,classId);
			
			//String key = this.getClassKey(courseId,classId);
			
			return (CourseClass)redisTemplate.opsForHash().get(key, classkey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param courseId
	 * @param classId
	 * @return
	 */
	public Map<Object,Object> getAllClassFromCache(String courseId)
	{
		try {
			String key = this.getCourseClassMapKey(courseId);
			
			//String key = this.getClassKey(courseId,classId);
			Map<Object, Object> maps = redisTemplate.opsForHash().entries(key);
			return maps;
			//		return (CourseClass)redisTemplate.boundHashOps(key).get(classkey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
