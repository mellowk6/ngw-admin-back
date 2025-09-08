package com.nhbank.ngw.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.nhbank.ngw.infra") // domain 하위의 @Mapper 자동 스캔
public class MyBatisConfig { }