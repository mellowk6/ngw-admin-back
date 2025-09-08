package com.nhbank.ngw.domain.log.repository;

import java.util.List;

public interface LogEntryRepository {

    List<String> findDistinctLoggerNames();

    List<String> findRecentGuids(int limit);
}