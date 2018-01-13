package com.company.courseManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
//@EnableRedisHttpSession
//@EnableRedisHttpSession 
@ComponentScan ("com.company.elasticsearch.repositories,com.company.courseManager,com.company.fileManager.fastDfs,com.company.pay,com.company.coursestudent,com.company.websocket,com.company.videoPlay.service,com.company.elasticsearch")
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400*30)
public class CourseManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourseManagerApplication.class, args);
		
		
	}
}
