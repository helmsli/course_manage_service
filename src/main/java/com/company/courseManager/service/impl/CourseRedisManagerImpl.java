package com.company.courseManager.service.impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.company.videodb.domain.CourseClass;
import com.company.videodb.domain.Courses;

@Service("courseRedisManager")
public class CourseRedisManagerImpl {
	@Resource (name = "redisTemplate")
	protected RedisTemplate<Object, Object> redisTemplate;
	
	@Value("${course.cache.expireHours:120}")
	private int courseCacheExpireHours;
	
	
	public String getCourseKey(String courseId)
	{
		return "courseId:" + courseId; 
	}
	
	
	public String getClassKey(String courseId,String classId)
	{
		return "classId:" + courseId + ":" + classId; 
	}
	
	public void putCourseToCache(Courses courses)
	{
		String key = this.getCourseKey(courses.getCourseId());
		redisTemplate.opsForValue().set(key, courses, courseCacheExpireHours, TimeUnit.HOURS);
	}
	
	public void delCourse(String courseId)
	{
		try {
			String key = this.getCourseKey(courseId);
			redisTemplate.delete(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Courses getCourseFromCache(String courseId)
	{
		try {
			String key = this.getCourseKey(courseId);
			return (Courses)redisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void putClassToCache(CourseClass courseClass)
	{
		try {
			String key = this.getClassKey(courseClass.getCourseId(), courseClass.getClassId());
			redisTemplate.opsForValue().set(key, courseClass, courseCacheExpireHours, TimeUnit.HOURS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public CourseClass getClassFromCache(String courseId,String classId)
	{
		try {
			String key = this.getClassKey(courseId,classId);
			
			return (CourseClass)redisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
