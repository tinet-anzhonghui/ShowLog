package com.qk.log;

import com.qk.log.util.AddConfigUtil;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

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

	/**
	 * @Description: 加载额外的配置文件
	 * @return:
	 * @Author: huihui
	 * @CreateDate: 2019/9/7 16:42
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
		System.out.println(AddConfigUtil.addConfigPath);
		yaml.setResources(new FileSystemResource(AddConfigUtil.addConfigPath));
		configurer.setProperties(yaml.getObject());
		return configurer;
	}


	public static void main(String[] args) {
		SpringApplication.run(App.class);
	}
}
