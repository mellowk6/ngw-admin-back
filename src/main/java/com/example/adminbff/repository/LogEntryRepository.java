package com.example.adminbff.repository;

import com.example.adminbff.entity.LogEntry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long>, JpaSpecificationExecutor<LogEntry> {
    @Query("select distinct e.loggerName from LogEntry e order by e.loggerName")
    List<String> findDistinctLoggerNames();

    @Query("select distinct e.guid from LogEntry e where e.guid is not null order by e.guid desc")
    List<String> findDistinctGuids(Pageable pageable); // 최근 N개 제안
}