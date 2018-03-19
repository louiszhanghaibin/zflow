package com.louisz.zflow.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * configuration for RestTemplate
 * 
 * @author zhang
 * @description
 * @time 2018年3月15日
 */
@Configuration
public class RestTemplateConfig {
	Logger logger = LoggerFactory.getLogger(RestTemplateConfig.class);

	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		logger.info("Configuring RestTemplate...");
		return new RestTemplate();
	}

}
