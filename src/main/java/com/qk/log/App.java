package com.qk.log;

import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @ClassName: App.java
 * @Description: 启动类
 *
 * @version: v1.0.0
 * @author: AN
 * @date: 2019年3月6日 下午12:57:20 
 * 
 */
@SpringBootApplication
@Configuration
// @EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class}) 
// 2.x以后@SpringBootApplication注解集成了@EnableAutoConfiguration
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class);
	}
}
