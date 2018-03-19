package com.louisz.zflow.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HIKARI数据库连接池
 * 
 * @author zhanghb
 * @description
 * @time 2018年1月17日
 */
@Configuration
public class DatabaseConfig {
	Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

	@Bean(name = "dataSource")
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public DataSource dataSource() {
		logger.info("Configuring dataSource...");
		return DataSourceBuilder.create().build();
	}
}