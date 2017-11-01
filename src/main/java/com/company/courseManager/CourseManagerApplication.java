package com.company.courseManager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
//@EnableRedisHttpSession
//@EnableRedisHttpSession 
@ComponentScan ("com.company.courseManager")
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400*30)
public class CourseManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourseManagerApplication.class, args);
		
		try {
			
		}
		catch(Throwable e)
		{
		   e.printStackTrace();	
		}
	}
}
