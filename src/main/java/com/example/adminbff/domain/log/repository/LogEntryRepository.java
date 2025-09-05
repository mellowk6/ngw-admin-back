package com.example.adminbff.domain.log.repository;

import com.example.adminbff.domain.log.mapper.LogEntryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LogEntryRepository {
    private final LogEntryMapper mapper;

    public List<String> findDistinctLoggerNames() {
        return mapper.selectDistinctLoggerNames();
    }

    public List<String> findRecentGuids(int limit) {
        return mapper.selectRecentGuids(limit);
    }
}