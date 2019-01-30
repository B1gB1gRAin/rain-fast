package com.ifast.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;

@Configuration
public class MyBatisConfiguration {

    /**
     * 分页插件
     * 
     * @return
     * @author zhongweiyuan
     * @date 2018年4月14日下午4:13:15
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }

    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
    	return new OptimisticLockerInterceptor();
    }
}
