package com.nhbank.ngw.infra.log.repository;

import com.nhbank.ngw.infra.log.mapper.LogEntryMapper;
import com.nhbank.ngw.domain.log.repository.LogEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LogEntryRepositoryImpl implements LogEntryRepository {
    private final LogEntryMapper mapper;

    public List<String> findDistinctLoggerNames() {
        return mapper.selectDistinctLoggerNames();
    }

    public List<String> findRecentGuids(int limit) {
        return mapper.selectRecentGuids(limit);
    }
}