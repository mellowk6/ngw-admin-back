package com.example.adminbff.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.adminbff.domain") // domain 하위의 @Mapper 자동 스캔
public class MyBatisConfig { }