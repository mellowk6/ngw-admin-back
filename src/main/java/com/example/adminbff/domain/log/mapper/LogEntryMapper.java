package com.example.adminbff.domain.log.mapper;

import com.example.adminbff.domain.log.model.LogEntry;
import com.example.adminbff.domain.log.model.LogSearchCond;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LogEntryMapper {
    List<String> selectDistinctLoggerNames();
    List<String> selectRecentGuids(@Param("limit") int limit);

    List<LogEntry> selectLogs(LogSearchCond cond);
    long countLogs(LogSearchCond cond);
}