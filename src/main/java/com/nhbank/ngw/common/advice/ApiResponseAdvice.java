package com.nhbank.ngw.common.advice;

import com.nhbank.ngw.common.dto.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // ApiResponse 만 후처리
        return ApiResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType contentType,
                                  Class<? extends HttpMessageConverter<?>> converterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof ApiResponse<?> r) {
            // path 또는 timestamp가 비어 있으면 채워줌
            if (r.getTimestamp() == null) r.setTimestamp(OffsetDateTime.now());
            if (r.getPath() == null) {
                String path = (request instanceof ServletServerHttpRequest s)
                        ? s.getServletRequest().getRequestURI()
                        : request.getURI().getPath();
                r.setPath(path);
            }
        }
        return body;
    }
}