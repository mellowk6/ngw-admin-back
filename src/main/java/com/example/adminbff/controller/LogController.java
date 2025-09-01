package com.example.adminbff.controller;

import com.example.adminbff.dto.LogEntryDto;
import com.example.adminbff.helper.LogEntrySpecs;
import com.example.adminbff.repository.LogEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {
    private final LogEntryRepository repo;

    @GetMapping
    public Page<LogEntryDto> list(
            @RequestParam(required=false) String guid,
            @RequestParam(required=false) List<String> logger,
            @PageableDefault(size=10, sort="time", direction = Sort.Direction.DESC) Pageable pageable) {
        var spec = Specification.where(LogEntrySpecs.guidLike(guid))
                .and(LogEntrySpecs.loggerIn(logger));
        return repo.findAll(spec, pageable).map(LogEntryDto::from);
    }

    @GetMapping("/loggers")
    public List<String> loggers() { return repo.findDistinctLoggerNames(); }

    @GetMapping("/guids")
    public List<String> guids(@RequestParam(defaultValue="20") int limit) {
        return repo.findDistinctGuids(PageRequest.of(0, Math.max(1, Math.min(limit, 100))));
    }
}