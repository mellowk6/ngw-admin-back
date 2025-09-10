package com.nhbank.ngw.common.exception;

public class DuplicateIdException extends RuntimeException {
    public DuplicateIdException(String id) {
        super("이미 사용 중인 아이디입니다: " + id);
    }
}
