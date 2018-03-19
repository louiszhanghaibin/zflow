package com.louisz.zflow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhang
 * @description spring boot application
 * @time 2018年2月2日
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
@MapperScan({ "com.louisz.zflow.dao" })
public class ZflowApplication {
	public static void main(String[] args) {
		SpringApplication.run(ZflowApplication.class, args);
	}

	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
